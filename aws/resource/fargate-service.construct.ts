import { ISecurityGroup, IVpc, SecurityGroup, Vpc } from "aws-cdk-lib/aws-ec2";
import { Cluster, FargateService, FargateServiceProps, TaskDefinition } from "aws-cdk-lib/aws-ecs";
import { INamespace, PrivateDnsNamespace } from "aws-cdk-lib/aws-servicediscovery";
import { Construct } from "constructs";
import env, { isProd } from "../env";

type FargateServiceConstructProps = {
  vpcId: string;
  clusterArn: string;
  namespaceArn: string;
  taskDefinition: TaskDefinition;
};

const environment = env.environment;
const { id, name } = env.stack;
const { id: namespaceId, name: namespaceName } = env.namespace;

export class FargateServiceConstruct extends FargateService {
  constructor(scope: Construct, props: FargateServiceConstructProps) {
    const { vpcId, clusterArn, namespaceArn, taskDefinition } = props;
    const vpc = getVpc(scope, vpcId);
    const securityGroup = getSecurityGroup(scope, vpc);
    const namespace = getNamespace(scope, namespaceArn);
    const cluster = getCluster(scope, clusterArn, vpc, securityGroup, namespace);
    const config: FargateServiceProps = {
      cluster,
      serviceName: name,
      securityGroups: [securityGroup],
      taskDefinition,
      minHealthyPercent: 100,
      maxHealthyPercent: 200,
      desiredCount: 1,
      circuitBreaker: {
        rollback: true,
      },
      serviceConnectConfiguration: {
        services: [
          {
            portMappingName: name,
          },
        ],
      },
    };

    super(scope, `${id}Service-${environment}`, config);

    if (isProd) {
      this.setScaling();
    }
  }

  private setScaling() {
    const scalableTarget = this.autoScaleTaskCount({
      minCapacity: 1,
      maxCapacity: 20,
    });
    scalableTarget.scaleOnMemoryUtilization(`${id}ScaleByMemory-${environment}`, {
      policyName: "ScaleOn70PercentMemory",
      targetUtilizationPercent: 70,
    });
    scalableTarget.scaleOnCpuUtilization(`${id}ScaleByCpu-${environment}`, {
      policyName: "ScaleOn50PercentCpu",
      targetUtilizationPercent: 50,
    });
  }
}

const getVpc = (scope: Construct, vpcId: string) => {
  return Vpc.fromLookup(scope, `${id}Vpc-${environment}`, {
    vpcId,
  });
};

const getSecurityGroup = (scope: Construct, vpc: IVpc) => {
  return SecurityGroup.fromLookupByName(scope, `${id}SecurityGroup-${environment}`, `api-${environment}`, vpc);
};

const getNamespace = (scope: Construct, namespaceArn: string) => {
  return PrivateDnsNamespace.fromPrivateDnsNamespaceAttributes(scope, `${id}Namespace-${environment}`, {
    namespaceArn,
    namespaceId,
    namespaceName,
  });
};

const getCluster = (
  scope: Construct,
  clusterArn: string,
  vpc: IVpc,
  securityGroup: ISecurityGroup,
  namespace: INamespace
) => {
  return Cluster.fromClusterAttributes(scope, `${id}Cluster-${environment}`, {
    clusterName: `api-cluster-${environment}`,
    clusterArn,
    vpc,
    securityGroups: [securityGroup],
    defaultCloudMapNamespace: namespace,
  });
};
