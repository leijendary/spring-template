const environment = process.env.ENVIRONMENT!!;

export const isProd = () => environment === "prod";

export default {
  account: process.env.CDK_DEFAULT_ACCOUNT!!,
  region: process.env.CDK_DEFAULT_REGION!!,
  environment,
  stackId: process.env.STACK_ID!!,
  stackName: process.env.STACK_NAME!!,
  vpcId: process.env.VPC_ID!!,
  imageTag: process.env.IMAGE_TAG!!,
  database: "spring_template",
};
