import { Duration, RemovalPolicy } from "aws-cdk-lib";
import { Repository, RepositoryProps, TagStatus } from "aws-cdk-lib/aws-ecr";
import { Construct } from "constructs";
import env from "../env";

const { environment } = env;
const { id, name } = env.stack;

export class RepositoryConstruct extends Repository {
  constructor(scope: Construct) {
    const config: RepositoryProps = {
      repositoryName: `${name}-${environment}`,
      removalPolicy: RemovalPolicy.DESTROY,
      lifecycleRules: [
        {
          description: "Remove untagged image older than 1 day",
          rulePriority: 1,
          tagStatus: TagStatus.UNTAGGED,
          maxImageAge: Duration.days(1),
        },
        {
          description: "Keep only up to 10 images",
          rulePriority: 2,
          tagStatus: TagStatus.ANY,
          maxImageCount: 10,
        },
      ],
    };

    super(scope, `${id}Repository-${environment}`, config);
  }
}
