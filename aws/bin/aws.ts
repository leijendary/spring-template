import { App } from "aws-cdk-lib";
import "source-map-support/register";
import env from "../env";
import { ApplicationStack } from "../lib/stack";

const app = new App();
const account = env.account;
const region = env.region;

new ApplicationStack(app, {
  env: {
    account,
    region,
  },
});

app.synth();
