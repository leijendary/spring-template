import { ECSClient, UpdateServiceCommand, UpdateServiceRequest } from "@aws-sdk/client-ecs";
import { Handler } from "aws-lambda";

const client = new ECSClient();
const cluster = process.env.CLUSTER!!;
const service = process.env.SERVICE!!;

export const handler: Handler = async () => {
  console.log("Stopping", service);

  const input: UpdateServiceRequest = {
    cluster,
    service,
    desiredCount: 0,
    forceNewDeployment: true,
  };
  const command = new UpdateServiceCommand(input);
  const response = await client.send(command);

  console.log("Triggered stop command to", response.service?.serviceName);
};
