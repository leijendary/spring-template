import { Duration, RemovalPolicy } from "aws-cdk-lib";
import { Distribution, IDistribution } from "aws-cdk-lib/aws-cloudfront";
import { IRepository, Repository } from "aws-cdk-lib/aws-ecr";
import {
  AppProtocol,
  Compatibility,
  ContainerImage,
  CpuArchitecture,
  LogDriver,
  OperatingSystemFamily,
  Secret,
  TaskDefinition,
  TaskDefinitionProps,
} from "aws-cdk-lib/aws-ecs";
import { PolicyDocument, PolicyStatement, Role, ServicePrincipal } from "aws-cdk-lib/aws-iam";
import { LogGroup, RetentionDays } from "aws-cdk-lib/aws-logs";
import { DatabaseSecret } from "aws-cdk-lib/aws-rds";
import { Bucket, IBucket } from "aws-cdk-lib/aws-s3";
import { Secret as SecretManager } from "aws-cdk-lib/aws-secretsmanager";
import { Construct } from "constructs";
import env, { isProd } from "../env";

type TaskDefinitionConstructProps = {
  repositoryArn: string;
};

const environment = env.environment;
const organization = env.organization;
const port = env.port;
const imageTag = env.imageTag;
const { id, name } = env.stack;
const { id: distributionId, domainName: distributionDomainName } = env.distribution;
const family = `${name}-${environment}`;
const assumedBy = new ServicePrincipal("ecs-tasks.amazonaws.com");
const logPrefix = "/ecs/fargate";

export class TaskDefinitionConstruct extends TaskDefinition {
  constructor(scope: Construct, props: TaskDefinitionConstructProps) {
    const { repositoryArn } = props;
    const memoryMiB = isProd() ? "2 GB" : "0.5 GB";
    const cpu = isProd() ? "1 vCPU" : "0.25 vCPU";
    const repository = getRepository(scope, repositoryArn);
    const image = getImage(repository);
    const bucket = getBucket(scope);
    const distribution = getDistribution(scope);
    const logGroup = createLogGroup(scope);
    const taskRole = createTaskRole(scope);
    const executionRole = createExecutionRole(scope, logGroup, repository);
    const config: TaskDefinitionProps = {
      family,
      compatibility: Compatibility.FARGATE,
      memoryMiB,
      cpu,
      runtimePlatform: {
        cpuArchitecture: CpuArchitecture.ARM64,
        operatingSystemFamily: OperatingSystemFamily.LINUX,
      },
      taskRole,
      executionRole,
    };

    super(scope, `${id}TaskDefinition-${environment}`, config);

    this.container(scope, image, logGroup);
    this.trustPolicy(taskRole, executionRole);
    this.grantBucketAccess(taskRole, bucket);
    this.grantDistribution(taskRole, distribution);
  }

  private container(scope: Construct, image: ContainerImage, logGroup: LogGroup) {
    const securityCredentials = getSecurityCredentials(scope);
    const auroraCredentials = getAuroraCredentials(scope);
    const dataStorageCredentials = getDataStorageCredentials(scope);

    this.addContainer(`${id}Container-${environment}`, {
      containerName: name,
      image,
      logging: LogDriver.awsLogs({
        streamPrefix: logPrefix,
        logGroup,
      }),
      portMappings: [
        {
          name,
          containerPort: port,
          hostPort: port,
          appProtocol: AppProtocol.http,
        },
      ],
      healthCheck: {
        command: ["CMD-SHELL", "wget -qO- http://localhost/actuator/health || exit 1"],
        startPeriod: Duration.seconds(isProd() ? 0 : 200),
      },
      environment: {
        SPRING_PROFILES_ACTIVE: environment,
        AWS_EC2_METADATA_DISABLED: "true",
      },
      secrets: {
        ENCRYPT_KEY: securityCredentials.encrypt.key,
        ENCRYPT_SALT: securityCredentials.encrypt.salt,
        SPRING_CLOUD_AWS_CLOUD_FRONT_PUBLIC_KEY_ID: securityCredentials.cloudFront.publicKeyId,
        SPRING_CLOUD_AWS_CLOUD_FRONT_PRIVATE_KEY: securityCredentials.cloudFront.privateKey,
        SPRING_DATA_REDIS_USERNAME: dataStorageCredentials.redis.username,
        SPRING_DATA_REDIS_PASSWORD: dataStorageCredentials.redis.password,
        SPRING_DATASOURCE_PRIMARY_USERNAME: auroraCredentials.username,
        SPRING_DATASOURCE_PRIMARY_PASSWORD: auroraCredentials.password,
        SPRING_DATASOURCE_READONLY_USERNAME: auroraCredentials.username,
        SPRING_DATASOURCE_READONLY_PASSWORD: auroraCredentials.password,
        SPRING_ELASTICSEARCH_USERNAME: dataStorageCredentials.elasticsearch.username,
        SPRING_ELASTICSEARCH_PASSWORD: dataStorageCredentials.elasticsearch.password,
        SPRING_KAFKA_JAAS_OPTIONS_USERNAME: dataStorageCredentials.kafka.username,
        SPRING_KAFKA_JAAS_OPTIONS_PASSWORD: dataStorageCredentials.kafka.password,
      },
    });
  }

  private trustPolicy(taskRole: Role, executionRole: Role) {
    const trustPolicy = new PolicyStatement({
      actions: ["sts:AssumeRole"],
      resources: [this.taskDefinitionArn],
    });

    taskRole.addToPolicy(trustPolicy);
    executionRole.addToPolicy(trustPolicy);
  }

  private grantBucketAccess(role: Role, bucket: IBucket) {
    bucket.grantReadWrite(role);
    bucket.grantPut(role);
    bucket.grantDelete(role);
  }

  private grantDistribution(role: Role, distribution: IDistribution) {
    distribution.grantCreateInvalidation(role);
  }
}

const createLogGroup = (scope: Construct) => {
  return new LogGroup(scope, `${id}LogGroup-${environment}`, {
    logGroupName: `${logPrefix}/${family}`,
    removalPolicy: RemovalPolicy.DESTROY,
    retention: RetentionDays.ONE_MONTH,
  });
};

const createTaskRole = (scope: Construct) => {
  return new Role(scope, `${id}TaskRole-${environment}`, {
    roleName: `${id}TaskRole-${environment}`,
    assumedBy,
  });
};

const createExecutionRole = (scope: Construct, logGroup: LogGroup, repository: IRepository) => {
  return new Role(scope, `${id}ExecutionRole-${environment}`, {
    roleName: `${id}ExecutionRole-${environment}`,
    assumedBy,
    inlinePolicies: {
      [`${id}ExecutionRolePolicy-${environment}`]: new PolicyDocument({
        statements: [
          new PolicyStatement({
            actions: [
              "ecr:BatchCheckLayerAvailability",
              "ecr:BatchGetImage",
              "ecr:GetAuthorizationToken",
              "ecr:GetDownloadUrlForLayer",
            ],
            resources: [repository.repositoryArn],
          }),
          new PolicyStatement({
            actions: ["logs:CreateLogStream", "logs:PutLogEvents"],
            resources: [logGroup.logGroupArn],
          }),
        ],
      }),
    },
  });
};

const getRepository = (scope: Construct, repositoryArn: string) => {
  return Repository.fromRepositoryArn(scope, `${id}Repository-${environment}`, repositoryArn);
};

const getImage = (repository: IRepository) => {
  return ContainerImage.fromEcrRepository(repository, imageTag);
};

const getBucket = (scope: Construct) => {
  return Bucket.fromBucketName(scope, `${id}Bucket-${environment}`, `${organization}-api-${environment}`);
};

const getDistribution = (scope: Construct) => {
  return Distribution.fromDistributionAttributes(scope, `${id}Distribution-${environment}`, {
    distributionId,
    domainName: distributionDomainName,
  });
};

const getSecurityCredentials = (scope: Construct) => {
  const credential = SecretManager.fromSecretNameV2(
    scope,
    `${id}SecuritySecret-${environment}`,
    `security-${environment}`
  );

  return {
    encrypt: {
      key: Secret.fromSecretsManager(credential, "encrypt.key"),
      salt: Secret.fromSecretsManager(credential, "encrypt.salt"),
    },
    cloudFront: {
      publicKeyId: Secret.fromSecretsManager(credential, "cloudFront.publicKeyId"),
      privateKey: Secret.fromSecretsManager(credential, "cloudFront.privateKey"),
    },
  };
};

const getAuroraCredentials = (scope: Construct) => {
  const credential = DatabaseSecret.fromSecretNameV2(
    scope,
    `${id}AuroraSecret-${environment}`,
    `api-aurora-${environment}`
  );

  return {
    username: Secret.fromSecretsManager(credential, "username"),
    password: Secret.fromSecretsManager(credential, "password"),
  };
};

const getDataStorageCredentials = (scope: Construct) => {
  const credential = SecretManager.fromSecretNameV2(
    scope,
    `${id}DataStorageSecret-${environment}`,
    `data-storage-${environment}`
  );

  return {
    elasticsearch: {
      username: Secret.fromSecretsManager(credential, "elasticsearch.username"),
      password: Secret.fromSecretsManager(credential, "elasticsearch.password"),
    },
    kafka: {
      username: Secret.fromSecretsManager(credential, "kafka.username"),
      password: Secret.fromSecretsManager(credential, "kafka.password"),
    },
    redis: {
      username: Secret.fromSecretsManager(credential, "redis.username"),
      password: Secret.fromSecretsManager(credential, "redis.password"),
    },
  };
};
