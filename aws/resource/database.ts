import { Secret } from "aws-cdk-lib/aws-ecs";
import { DatabaseSecret, ServerlessCluster } from "aws-cdk-lib/aws-rds";
import { Construct } from "constructs";
import env from "../env";

const environment = env.environment;
const id = env.stackId;
const database = env.database;

export class AuroraDatabase {
  primaryUrl: string;
  readonlyUrl: string;
  username: Secret;
  password: Secret;

  constructor(scope: Construct) {
    const cluster = ServerlessCluster.fromServerlessClusterAttributes(scope, `${id}AuroraCluster`, {
      clusterIdentifier: `api-${environment}`,
    });
    const credential = DatabaseSecret.fromSecretNameV2(
      scope,
      `${id}AuroraSecret-${environment}`,
      `${environment}/aurora/api`
    );

    console.log("cluster", cluster);
    console.log("credential", credential);

    console.log("cluster.json", JSON.stringify(cluster));

    const endpoint = cluster.clusterEndpoint;
    const readEndpoint = cluster.clusterReadEndpoint;

    this.primaryUrl = `jdbc:postgresql://${endpoint.socketAddress}/${database}`;
    this.readonlyUrl = `jdbc:postgresql://${readEndpoint.socketAddress}/${database}`;
    this.username = Secret.fromSecretsManager(credential, "username");
    this.password = Secret.fromSecretsManager(credential, "password");
  }
}
