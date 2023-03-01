import { Secret } from "aws-cdk-lib/aws-ecs";
import { DatabaseSecret, ServerlessCluster } from "aws-cdk-lib/aws-rds";
import { Construct } from "constructs";
import env from "../env";

const environment = env.environment;
const { id } = env.stack;
const { name, clusterEndpoint, readerEndpoint } = env.database;

export class AuroraDatabase {
  primaryUrl: string;
  readonlyUrl: string;
  username: Secret;
  password: Secret;

  constructor(scope: Construct) {
    const cluster = ServerlessCluster.fromServerlessClusterAttributes(scope, `${id}AuroraCluster`, {
      clusterIdentifier: `api-${environment}`,
      clusterEndpointAddress: clusterEndpoint,
      readerEndpointAddress: readerEndpoint,
      port: 5432,
    });
    const credential = DatabaseSecret.fromSecretNameV2(
      scope,
      `${id}AuroraSecret-${environment}`,
      `api-aurora-${environment}`
    );

    const endpoint = cluster.clusterEndpoint;
    const readEndpoint = cluster.clusterReadEndpoint;

    this.primaryUrl = `jdbc:postgresql://${endpoint.socketAddress}/${name}`;
    this.readonlyUrl = `jdbc:postgresql://${readEndpoint.socketAddress}/${name}`;
    this.username = Secret.fromSecretsManager(credential, "username");
    this.password = Secret.fromSecretsManager(credential, "password");
  }
}
