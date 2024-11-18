import {
  DescribeServicesCommand,
  DescribeServicesCommandInput,
  ECSClient,
  UpdateServiceCommand,
  UpdateServiceRequest,
} from "@aws-sdk/client-ecs";
import { Handler } from "aws-lambda";

const client = new ECSClient();
const cluster = process.env.CLUSTER!!;
const service = process.env.SERVICE!!;

export const handler: Handler = async () => {
  console.log("Starting", service);

  const desiredCount = await getDesiredCount();

  if (desiredCount > 0) {
    console.log(`Current desired count is ${desiredCount}. Skipping.`);
    return;
  }

  const input: UpdateServiceRequest = {
    cluster,
    service,
    desiredCount: 1,
    forceNewDeployment: true,
  };
  const command = new UpdateServiceCommand(input);
  const response = await client.send(command);

  console.log("Triggered start command to", response.service?.serviceName);
};

async function getDesiredCount() {
  const input: DescribeServicesCommandInput = {
    cluster,
    services: [service],
  };
  const command = new DescribeServicesCommand(input);
  const response = await client.send(command);

  return response.services?.[0].desiredCount || 0;
}
