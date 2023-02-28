import { Duration } from "aws-cdk-lib";
import { SecurityGroup, Vpc } from "aws-cdk-lib/aws-ec2";
import { Cluster, FargateService, FargateServiceProps, TaskDefinition } from "aws-cdk-lib/aws-ecs";
import { Construct } from "constructs";
import env from "../env";
import { isProd } from "./../env";

type FargateServiceConstructProps = {
  vpcId: string;
  clusterArn: string;
  taskDefinition: TaskDefinition;
};

const environment = env.environment;
const id = env.stackId;
const name = env.stackName;

export class FargateServiceConstruct extends FargateService {
  constructor(scope: Construct, props: FargateServiceConstructProps) {
    const { vpcId, clusterArn, taskDefinition, ...rest } = props;
    const vpc = Vpc.fromLookup(scope, `${id}Vpc-${environment}`, {
      vpcId,
    });
    const securityGroup = SecurityGroup.fromLookupByName(
      scope,
      `${id}SecurityGroup-${environment}`,
      `api-${environment}`,
      vpc
    );
    const cluster = Cluster.fromClusterAttributes(scope, `${id}Cluster-${environment}`, {
      clusterName: `api-cluster-${environment}`,
      clusterArn,
      vpc,
      securityGroups: [securityGroup],
    });
    const config: FargateServiceProps = {
      ...rest,
      cluster,
      serviceName: name,
      securityGroups: [securityGroup],
      taskDefinition,
      healthCheckGracePeriod: isProd() ? Duration.minutes(5) : undefined,
      minHealthyPercent: 100,
      maxHealthyPercent: 200,
      desiredCount: 1,
      circuitBreaker: {
        rollback: true,
      },
      cloudMapOptions: {
        name
      },
    };

    super(scope, `${id}Service-${environment}`, config);

    this.setScaling();
  }

  private setScaling() {
    const scalableTarget = this.autoScaleTaskCount({
      minCapacity: 1,
      maxCapacity: 10,
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
