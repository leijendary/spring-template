const environment = process.env.ENVIRONMENT!!;

export const isProd = () => environment === "prod";

export default {
  account: process.env.CDK_DEFAULT_ACCOUNT!!,
  region: process.env.CDK_DEFAULT_REGION!!,
  environment,
  stack: {
    id: process.env.STACK_ID!!,
    name: process.env.STACK_NAME!!,
  },
  vpcId: process.env.VPC_ID!!,
  imageTag: process.env.IMAGE_TAG!!,
  database: {
    name: "spring_template",
    clusterEndpoint: process.env.DATABASE_CLUSTER_ENDPOINT!!,
    readerEndpoint: process.env.DATABASE_READER_ENDPOINT!!,
  },
  namespace: {
    arn: process.env.NAMESPACE_ARN!!,
    id: process.env.NAMESPACE_ID!!,
    name: process.env.NAMESPACE_NAME!!,
  },
};
