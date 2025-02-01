const environment = process.env.ENVIRONMENT!!;

export default {
  account: process.env.CDK_DEFAULT_ACCOUNT!!,
  region: process.env.CDK_DEFAULT_REGION!!,
  organization: process.env.ORGANIZATION!!,
  environment,
  port: 80,
  stack: {
    id: process.env.STACK_ID!!,
    name: process.env.STACK_NAME!!,
  },
  vpcId: process.env.VPC_ID!!,
  imageTag: process.env.IMAGE_TAG!!,
  namespace: {
    id: process.env.NAMESPACE_ID!!,
    name: process.env.NAMESPACE_NAME!!,
  },
  distribution: {
    id: process.env.DISTRIBUTION_ID!!,
    domainName: process.env.DISTRIBUTION_DOMAIN_NAME!!,
  },
  buildAccount: process.env.BUILD_ACCOUNT!!,
  isProd: environment === "prod",
  clusterName: `api-${environment}`
};
