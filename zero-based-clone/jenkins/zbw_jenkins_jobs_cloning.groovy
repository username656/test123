import hudson.model.*
import templates.*

//Build build = Executor.currentExecutor().currentExecutable
//ParametersAction parametersAction = build.getAction(ParametersAction)
//def buildParams = parametersAction.parameters

//def params = bindings.variables
def cIJobCreation = load "templates/ci-build-job.groovy"
cIJobCreation.createCIJob("CI Build "+${JENIKINS_PROJECT_NAME}, null)

IncrementBuildJobTemplate.create("Increment Build "+ ${JENIKINS_PROJECT_NAME}, params)
RCBuildJobTemplate.create("RC Build "+ ${JENIKINS_PROJECT_NAME}, params)


