import { Stack, StackProps } from "aws-cdk-lib";
import { Distribution, DistributionAttributes } from "aws-cdk-lib/aws-cloudfront";
import { ISecurityGroup, IVpc, SecurityGroup, Vpc, VpcLookupOptions } from "aws-cdk-lib/aws-ec2";
import { IRepository } from "aws-cdk-lib/aws-ecr";
import { Cluster, ClusterAttributes, ContainerImage, Secret } from "aws-cdk-lib/aws-ecs";
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
import { FargateServiceConstruct, FargateServiceConstructProps } from "../resource/fargate-service.construct";
import { RepositoryConstruct } from "../resource/repository.construct";
import { TaskDefinitionConstruct, TaskDefinitionConstructProps } from "../resource/task-definition.construct";

const { account, region, environment, organization, vpcId, imageTag, clusterName } = env;
const { id: distributionId, domainName } = env.distribution;
const { id: namespaceId, name: namespaceName } = env.namespace;
const { id } = env.stack;

export class ApplicationStack extends Stack {
  constructor(scope: Construct, props: StackProps) {
    super(scope, `${id}Application-${environment}`, props);

    const taskDefinition = this.createTaskDefinition();

    this.createFargateService(taskDefinition);
  }

  private createTaskDefinition() {
    const repository = new RepositoryConstruct(this);
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
      clusterArn: `arn:aws:ecs:${region}:${account}:cluster/${clusterName}`,
      securityGroups: [securityGroup],
      defaultCloudMapNamespace: privateNamespace,
    };

    return Cluster.fromClusterAttributes(this, `${id}Cluster-${environment}`, config);
  }

  private getSecurityCredentials() {
    const credential = SecretManager.fromSecretNameV2(
      this,
      `${id}SecuritySecret-${environment}`,
      `${environment}/security`
    );

    return {
      encrypt: {
        key: Secret.fromSecretsManager(credential, "encrypt.key"),
        salt: Secret.fromSecretsManager(credential, "encrypt.salt"),
      },
      cloudFront: {
        privateKey: Secret.fromSecretsManager(credential, "cloudFront.privateKey"),
        publicKeyId: Secret.fromSecretsManager(credential, "cloudFront.publicKeyId"),
      },
    };
  }

  private getAuroraCredentials() {
    const credential = DatabaseSecret.fromSecretNameV2(
      this,
      `${id}AuroraSecret-${environment}`,
      `${environment}/aurora/api`
    );

    return {
      username: Secret.fromSecretsManager(credential, "username"),
      password: Secret.fromSecretsManager(credential, "password"),
    };
  }

  private getDataStorageCredentials() {
    const credential = SecretManager.fromSecretNameV2(
      this,
      `${id}DataStorageSecret-${environment}`,
      `${environment}/data-storage`
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
  }
}
