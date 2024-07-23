import { Alarm, AlarmProps, ComparisonOperator, Metric } from "aws-cdk-lib/aws-cloudwatch";
import { LambdaAction } from "aws-cdk-lib/aws-cloudwatch-actions";
import { Function } from "aws-cdk-lib/aws-lambda";
import { FilterPattern, LogGroup, MetricFilter, MetricFilterProps } from "aws-cdk-lib/aws-logs";
import { Construct } from "constructs";
import env from "../env";

export type CloudWatchConstructProps = {
  logGroup: LogGroup;
};

const { environment } = env;
const { id } = env.stack;

export default class CloudWatchConstruct extends Construct {
  constructor(scope: Construct, props: CloudWatchConstructProps) {
    super(scope, `${id}CloudWatch-${environment}`);

    const metricFilter = this.createMetricFilter(props.logGroup);
    const metric = metricFilter.metric({
      label: "Errors",
      statistic: "n",
    });

    this.createAlarm(metric);
  }

  private createMetricFilter(logGroup: LogGroup) {
    const config: MetricFilterProps = {
      logGroup,
      filterName: `${id}ErrorMetricFilter-${environment}`,
      filterPattern: FilterPattern.literal(" ERROR "),
      metricNamespace: logGroup.logGroupName,
      metricName: "error",
    };

    return new MetricFilter(this, `${id}ErrorMetricFilter-${environment}`, config);
  }

  private createAlarm(metric: Metric) {
    const lambda = Function.fromFunctionName(this, `AlarmNotifier-${environment}`, `alarm-notifier-${environment}`);
    const config: AlarmProps = {
      alarmName: `${id}ErrorMetricAlarm-${environment}`,
      alarmDescription: "Call the notifier lambda service where there are errors logs within 5 minutes",
      comparisonOperator: ComparisonOperator.GREATER_THAN_THRESHOLD,
      evaluationPeriods: 1,
      threshold: 0,
      metric,
    };
    const alarm = new Alarm(this, `${id}ErrorMetricAlarm-${environment}`, config);
    alarm.addAlarmAction(new LambdaAction(lambda));
  }
}
