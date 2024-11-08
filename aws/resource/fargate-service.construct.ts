import { Duration, RemovalPolicy } from "aws-cdk-lib";
import { ISecurityGroup } from "aws-cdk-lib/aws-ec2";
import { FargateService, FargateServiceProps, ICluster } from "aws-cdk-lib/aws-ecs";
import { Rule, Schedule } from "aws-cdk-lib/aws-events";
import { LambdaFunction } from "aws-cdk-lib/aws-events-targets";
import { Effect, PolicyStatement } from "aws-cdk-lib/aws-iam";
import { NodejsFunction } from "aws-cdk-lib/aws-lambda-nodejs";
import { LogGroup, RetentionDays } from "aws-cdk-lib/aws-logs";
import { Construct } from "constructs";
import env, { isProd } from "../env";
import { TaskDefinitionConstruct } from "./task-definition.construct";

export type FargateServiceConstructProps = {
  taskDefinition: TaskDefinitionConstruct;
  cluster: ICluster;
  securityGroup: ISecurityGroup;
};

const { environment, clusterName } = env;
const { id, name } = env.stack;

export class FargateServiceConstruct extends FargateService {
  constructor(scope: Construct, props: FargateServiceConstructProps) {
    const { taskDefinition, cluster, securityGroup } = props;
    const config: FargateServiceProps = {
      cluster,
      serviceName: name,
      securityGroups: [securityGroup],
      taskDefinition,
      minHealthyPercent: 100,
      maxHealthyPercent: 200,
      circuitBreaker: {
        rollback: true,
      },
      serviceConnectConfiguration: {
        services: [
          {
            portMappingName: name,
            perRequestTimeout: Duration.minutes(5),
          },
        ],
      },
    };

    super(scope, `${id}FargateService-${environment}`, config);

    if (isProd) {
      this.setScaling();
    } else {
      this.createStartSchedule();
      this.createStopSchedule();
    }
  }

  private setScaling() {
    const scalableTarget = this.autoScaleTaskCount({
      minCapacity: 1,
      maxCapacity: 20,
    });
    scalableTarget.scaleOnMemoryUtilization(`${id}ScaleByMemory-${environment}`, {
      policyName: "ScaleOnMemory70",
      targetUtilizationPercent: 70,
    });
    scalableTarget.scaleOnCpuUtilization(`${id}ScaleByCpu-${environment}`, {
      policyName: "ScaleOnCpu50",
      targetUtilizationPercent: 50,
    });
  }

  private createStartSchedule() {
    const policyStatement = new PolicyStatement({
      effect: Effect.ALLOW,
      actions: ["ecs:UpdateService"],
      resources: [this.serviceArn],
    });
    const lambda = new NodejsFunction(this, `EcsServiceStartFunction-${clusterName}-${name}`, {
      functionName: `${clusterName}-${name}-ecs-service-start`,
      entry: "function/ecs-service-start.ts",
      environment: {
        CLUSTER: clusterName,
        SERVICE: name,
      },
    });
    lambda.addToRolePolicy(policyStatement);

    new LogGroup(this, `EcsServiceStartFunctionLogGroup-${name}-${environment}`, {
      logGroupName: `/aws/lambda/${lambda.functionName}`,
      removalPolicy: RemovalPolicy.DESTROY,
      retention: RetentionDays.FIVE_DAYS,
    });

    const target = new LambdaFunction(lambda);

    new Rule(this, `EcsServiceStartFunctionScheduler-${clusterName}-${name}`, {
      ruleName: `${clusterName}-${name}-ecs-service-start-scheduler`,
      schedule: Schedule.cron({
        minute: "0",
        hour: "8",
      }),
      targets: [target],
    });
  }

  private createStopSchedule() {
    const policyStatement = new PolicyStatement({
      effect: Effect.ALLOW,
      actions: ["ecs:UpdateService"],
      resources: [this.serviceArn],
    });
    const lambda = new NodejsFunction(this, `EcsServiceStopFunction-${clusterName}-${name}`, {
      functionName: `${clusterName}-${name}-ecs-service-stop`,
      entry: "function/ecs-service-stop.ts",
      environment: {
        CLUSTER: clusterName,
        SERVICE: name,
      },
    });
    lambda.addToRolePolicy(policyStatement);

    new LogGroup(this, `EcsServiceStopFunctionLogGroup-${name}-${environment}`, {
      logGroupName: `/aws/lambda/${lambda.functionName}`,
      removalPolicy: RemovalPolicy.DESTROY,
      retention: RetentionDays.FIVE_DAYS,
    });

    const target = new LambdaFunction(lambda);

    new Rule(this, `EcsServiceStopFunctionScheduler-${clusterName}-${name}`, {
      ruleName: `${clusterName}-${name}-ecs-service-stop-scheduler`,
      schedule: Schedule.cron({
        minute: "0",
        hour: "18",
      }),
      targets: [target],
    });
  }
}
