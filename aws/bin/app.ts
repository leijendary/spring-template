import env from "@/env";
import { ApplicationStack } from "@/lib/app.stack";
import { App } from "aws-cdk-lib";
import "source-map-support/register";

const { account, region } = env;
const app = new App();

new ApplicationStack(app, {
  env: {
    account,
    region,
  },
});

app.synth();
