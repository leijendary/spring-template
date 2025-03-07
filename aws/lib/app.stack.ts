import { Stack, StackProps } from "aws-cdk-lib";
import { Distribution, DistributionAttributes } from "aws-cdk-lib/aws-cloudfront";
import { ISecurityGroup, IVpc, SecurityGroup, Vpc, VpcLookupOptions } from "aws-cdk-lib/aws-ec2";
import { IRepository, Repository } from "aws-cdk-lib/aws-ecr";
import { Cluster, ClusterAttributes, ContainerImage, Secret } from "aws-cdk-lib/aws-ecs";
import { LogGroup } from "aws-cdk-lib/aws-logs";
import { DatabaseSecret } from "aws-cdk-lib/aws-rds";
import { Bucket } from "aws-cdk-lib/aws-s3";
import { Secret as SecretManager } from "aws-cdk-lib/aws-secretsmanager";
import {
  IPrivateDnsNamespace,
  PrivateDnsNamespace,
  PrivateDnsNamespaceAttributes,
} from "aws-cdk-lib/aws-servicediscovery";
import { Construct } from "constructs";
import env from "../env";
import CloudWatchConstruct, { CloudWatchConstructProps } from "../resource/cloud-watch.construct";
import { FargateServiceConstruct, FargateServiceConstructProps } from "../resource/fargate-service.construct";
import { TaskDefinitionConstruct, TaskDefinitionConstructProps } from "../resource/task-definition.construct";

const { account, region, environment, organization, vpcId, imageTag, clusterName, buildAccount } = env;
const { id: distributionId, domainName } = env.distribution;
const { id: namespaceId, name: namespaceName } = env.namespace;
const { id, name } = env.stack;

export class ApplicationStack extends Stack {
  constructor(scope: Construct, props: StackProps) {
    super(scope, `${id}Application-${environment}`, props);

    const taskDefinition = this.createTaskDefinition();

    this.createCloudWatch(taskDefinition.logGroup);
    this.createFargateService(taskDefinition);
  }

  private createCloudWatch(logGroup: LogGroup) {
    const config: CloudWatchConstructProps = {
      logGroup,
    };

    new CloudWatchConstruct(this, config);
  }

  private createTaskDefinition() {
    const repository = this.getRepository();
    const image = this.getImage(repository);
    const bucket = this.getBucket();
    const distribution = this.getDistribution();
    const config: TaskDefinitionConstructProps = {
      repository,
      image,
      bucket,
      distribution,
      credentials: {
        security: this.getSecurityCredentials(),
        aurora: this.getAuroraCredentials(),
        dataStorage: this.getDataStorageCredentials(),
      },
    };

    return new TaskDefinitionConstruct(this, config);
  }

  private createFargateService(taskDefinition: TaskDefinitionConstruct) {
    const vpc = this.getVpc();
    const securityGroup = this.getSecurityGroup(vpc);
    const privateNamespace = this.getPrivateNamespace();
    const cluster = this.getCluster(vpc, securityGroup, privateNamespace);
    const config: FargateServiceConstructProps = {
      taskDefinition,
      securityGroup,
      cluster,
    };

    new FargateServiceConstruct(this, config);
  }

  private getRepository() {
    return Repository.fromRepositoryArn(
      this,
      `${id}Repository-${environment}`,
      `arn:aws:ecr:${region}:${buildAccount}:repository/${name}`
    );
  }

  private getImage(repository: IRepository) {
    return ContainerImage.fromEcrRepository(repository, imageTag);
  }

  private getBucket() {
    return Bucket.fromBucketName(this, `${id}Bucket-${environment}`, `${organization}-api-${environment}`);
  }

  private getDistribution() {
    const config: DistributionAttributes = {
      distributionId,
      domainName,
    };

    return Distribution.fromDistributionAttributes(this, `${id}Distribution-${environment}`, config);
  }

  private getVpc() {
    const config: VpcLookupOptions = {
      vpcId,
    };

    return Vpc.fromLookup(this, `${id}Vpc-${environment}`, config) as Vpc;
  }

  private getSecurityGroup(vpc: IVpc) {
    return SecurityGroup.fromLookupByName(this, `${id}SecurityGroup-${environment}`, `api-${environment}`, vpc);
  }

  private getPrivateNamespace() {
    const config: PrivateDnsNamespaceAttributes = {
      namespaceArn: `arn:aws:servicediscovery:${region}:${account}:namespace/${namespaceId}`,
      namespaceId,
      namespaceName,
    };

    return PrivateDnsNamespace.fromPrivateDnsNamespaceAttributes(this, `${id}Namespace-${environment}`, config);
  }

  private getCluster(vpc: IVpc, securityGroup: ISecurityGroup, privateNamespace: IPrivateDnsNamespace) {
    const config: ClusterAttributes = {
      vpc,
      clusterName,
      securityGroups: [securityGroup],
      defaultCloudMapNamespace: privateNamespace,
    };

    return Cluster.fromClusterAttributes(this, `${id}Cluster-${environment}`, config);
  }

  private getSecurityCredentials() {
    const secret = SecretManager.fromSecretNameV2(
      this,
      `${id}SecuritySecret-${environment}`,
      `${environment}/security`
    );

    return {
      encrypt: {
        key: Secret.fromSecretsManager(secret, "encrypt.key"),
        salt: Secret.fromSecretsManager(secret, "encrypt.salt"),
      },
      cloudFront: {
        privateKey: Secret.fromSecretsManager(secret, "cloudFront.privateKey"),
        publicKeyId: Secret.fromSecretsManager(secret, "cloudFront.publicKeyId"),
      },
    };
  }

  private getAuroraCredentials() {
    const secret = DatabaseSecret.fromSecretNameV2(
      this,
      `${id}AuroraSecret-${environment}`,
      `${environment}/aurora/api`
    );

    return {
      username: Secret.fromSecretsManager(secret, "username"),
      password: Secret.fromSecretsManager(secret, "password"),
    };
  }

  private getDataStorageCredentials() {
    const secret = SecretManager.fromSecretNameV2(
      this,
      `${id}DataStorageSecret-${environment}`,
      `${environment}/data-storage`
    );

    return {
      elasticsearch: {
        username: Secret.fromSecretsManager(secret, "elasticsearch.username"),
        password: Secret.fromSecretsManager(secret, "elasticsearch.password"),
      },
      kafka: {
        username: Secret.fromSecretsManager(secret, "kafka.username"),
        password: Secret.fromSecretsManager(secret, "kafka.password"),
      },
      redis: {
        username: Secret.fromSecretsManager(secret, "redis.username"),
        password: Secret.fromSecretsManager(secret, "redis.password"),
      },
    };
  }
}
