import { Stack, StackProps } from "aws-cdk-lib";
import { Construct } from "constructs";
import env from "../env";
import { FargateServiceConstruct } from "./../resource/fargate-service.construct";
import { TaskDefinitionConstruct } from "./../resource/task-definition.construct";

const environment = env.environment;
const { id, name } = env.stack;
const vpcId = env.vpcId;
const account = env.account;
const region = env.region;
const { id: namespaceId } = env.namespace;

export class ApplicationStack extends Stack {
  constructor(scope: Construct, props: StackProps) {
    super(scope, `${id}Stack-${environment}`, props);

    const repositoryArn = `arn:aws:ecr:${region}:${account}:repository/${name}`;
    const clusterArn = `arn:aws:ecs:${region}:${account}:cluster/api-cluster-${environment}`;
    const namespaceArn = `arn:aws:servicediscovery:${region}:${account}:namespace/${namespaceId}`;
    const taskDefinition = new TaskDefinitionConstruct(this, {
      repositoryArn,
    });

    new FargateServiceConstruct(this, {
      vpcId,
      clusterArn,
      namespaceArn,
      taskDefinition,
    });
  }
}
