import hudson.model.*
import templates.*

Build build = Executor.currentExecutor().currentExecutable
ParametersAction parametersAction = build.getAction(ParametersAction)
def buildParams = parametersAction.parameters

CIBuildJobTemplate.create("CI Build "+buildParams.JENIKINS_PROJECT_NAME, buildParams)
IncrementBuildJobTemplate.create("Increment Build "+buildParams.JENIKINS_PROJECT_NAME, buildParams)
RCBuildJobTemplate.create("RC Build "+buildParams.JENIKINS_PROJECT_NAME, buildParams)


