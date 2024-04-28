import { RemovalPolicy } from "aws-cdk-lib";
import { IDistribution } from "aws-cdk-lib/aws-cloudfront";
import { IRepository } from "aws-cdk-lib/aws-ecr";
import {
  AppProtocol,
  Compatibility,
  ContainerDefinitionOptions,
  ContainerImage,
  CpuArchitecture,
  EcrImage,
  LogDriver,
  OperatingSystemFamily,
  Secret,
  TaskDefinition,
  TaskDefinitionProps,
} from "aws-cdk-lib/aws-ecs";
import { PolicyDocument, PolicyStatement, Role, RoleProps, ServicePrincipal } from "aws-cdk-lib/aws-iam";
import { LogGroup, LogGroupProps, RetentionDays } from "aws-cdk-lib/aws-logs";
import { IBucket } from "aws-cdk-lib/aws-s3";
import { Construct } from "constructs";
import env, { isProd } from "../env";

export type TaskDefinitionConstructCredentialsProps = {
  security: {
    encrypt: {
      key: Secret;
      salt: Secret;
    };
    cloudFront: {
      privateKey: Secret;
      publicKeyId: Secret;
    };
  };
  aurora: {
    username: Secret;
    password: Secret;
  };
  dataStorage: {
    elasticsearch: {
      username: Secret;
      password: Secret;
    };
    kafka: {
      username: Secret;
      password: Secret;
    };
    redis: {
      username: Secret;
      password: Secret;
    };
  };
};

export type TaskDefinitionConstructProps = {
  repository: IRepository;
  image: EcrImage;
  bucket: IBucket;
  distribution: IDistribution;
  credentials: TaskDefinitionConstructCredentialsProps;
};

const { environment, port, clusterName } = env;
const { id, name } = env.stack;
const logPrefix = `/ecs/fargate/${clusterName}`;
const assumedBy = new ServicePrincipal("ecs-tasks.amazonaws.com");
const memory = isProd ? 1024 : 512;
const cpu = isProd ? 512 : 256;

export class TaskDefinitionConstruct extends TaskDefinition {
  constructor(scope: Construct, props: TaskDefinitionConstructProps) {
    const { repository, image, bucket, distribution, credentials } = props;
    const logGroup = createLogGroup(scope, logPrefix);
    const taskRole = createTaskRole(scope);
    const executionRole = createExecutionRole(scope, logGroup, repository);
    const config: TaskDefinitionProps = {
      family: `${name}-${environment}`,
      memoryMiB: `${memory}`,
      cpu: `${cpu}`,
      compatibility: Compatibility.FARGATE,
      runtimePlatform: {
        cpuArchitecture: CpuArchitecture.ARM64,
        operatingSystemFamily: OperatingSystemFamily.LINUX,
      },
      taskRole,
      executionRole,
    };

    super(scope, `${id}TaskDefinition-${environment}`, config);

    this.mainContainer(image, credentials, logGroup);
    this.trustPolicy(taskRole, executionRole);
    this.grantBucketAccess(taskRole, bucket);
    this.grantDistribution(taskRole, distribution);
  }

  private mainContainer(
    image: ContainerImage,
    credentials: TaskDefinitionConstructCredentialsProps,
    logGroup: LogGroup
  ) {
    const config: ContainerDefinitionOptions = {
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
      },
      secrets: {
        ENCRYPT_KEY: credentials.security.encrypt.key,
        ENCRYPT_SALT: credentials.security.encrypt.salt,
        SPRING_CLOUD_AWS_CLOUD_FRONT_PRIVATE_KEY: credentials.security.cloudFront.privateKey,
        SPRING_CLOUD_AWS_CLOUD_FRONT_PUBLIC_KEY_ID: credentials.security.cloudFront.publicKeyId,
        SPRING_DATA_REDIS_USERNAME: credentials.dataStorage.redis.username,
        SPRING_DATA_REDIS_PASSWORD: credentials.dataStorage.redis.password,
        SPRING_DATASOURCE_USERNAME: credentials.aurora.username,
        SPRING_DATASOURCE_PASSWORD: credentials.aurora.password,
        SPRING_ELASTICSEARCH_USERNAME: credentials.dataStorage.elasticsearch.username,
        SPRING_ELASTICSEARCH_PASSWORD: credentials.dataStorage.elasticsearch.password,
        SPRING_KAFKA_JAAS_OPTIONS_USERNAME: credentials.dataStorage.kafka.username,
        SPRING_KAFKA_JAAS_OPTIONS_PASSWORD: credentials.dataStorage.kafka.password,
      },
    };

    this.addContainer(`${id}Container-${environment}`, config);
  }

  private trustPolicy(taskRole: Role, executionRole: Role) {
    const policyStatement = new PolicyStatement({
      actions: ["sts:AssumeRole"],
      resources: [this.taskDefinitionArn],
    });

    taskRole.addToPolicy(policyStatement);
    executionRole.addToPolicy(policyStatement);
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

function createLogGroup(scope: Construct, logPrefix: string) {
  const config: LogGroupProps = {
    logGroupName: `${logPrefix}/${name}`,
    removalPolicy: RemovalPolicy.DESTROY,
    retention: isProd ? RetentionDays.THREE_MONTHS : RetentionDays.ONE_MONTH,
  };

  return new LogGroup(scope, `${id}LogGroup-${environment}`, config);
}

function createTaskRole(scope: Construct) {
  const config: RoleProps = {
    roleName: `${id}TaskRole-${environment}`,
    assumedBy,
  };

  return new Role(scope, `${id}TaskRole-${environment}`, config);
}

function createExecutionRole(scope: Construct, logGroup: LogGroup, repository: IRepository) {
  const config: RoleProps = {
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
  };

  return new Role(scope, `${id}ExecutionRole-${environment}`, config);
}
