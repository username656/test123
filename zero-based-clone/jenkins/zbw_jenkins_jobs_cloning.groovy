import hudson.model.*
import templates.*

//Build build = Executor.currentExecutor().currentExecutable
//ParametersAction parametersAction = build.getAction(ParametersAction)
//def buildParams = parametersAction.parameters

//def params = bindings.variables
//def cIJobCreation = load "templates/ci-build-job.groovy"
//cIJobCreation.createCIJob("CI Build "+${JENIKINS_PROJECT_NAME}, null)

multibranchPipelineJob("CI Build "+${JENIKINS_PROJECT_NAME}) {
    branchSources {
        github {
            scanCredentialsId("${GIT_CREDENTIALS_ID}")
            repoOwner("${GITHUB_REPO_OWNER}")
            repository("${GITHUB_REPO_NAME}")
            includes('feature/* bugfix/* hotfix/* env/stage revert-* revert/* bugifx/* env/dev')
            buildOriginBranchWithPR()
        }
    }
    factory {
        workflowMultiBranchProjectFactory {
            scriptPath('cicd/pipeline/jobs/ci-build.jenkins')
        }
    }
    orphanedItemStrategy {
        discardOldItems {
            daysToKeep(4)
            numToKeep(30)
        }
    }
}

multibranchPipelineJob("Increment Build "+ ${JENIKINS_PROJECT_NAME}) {
    branchSources {
        git {
            remote(params.GIT_REPO_URL)
            credentialsId(params.GIT_CREDENTIALS_ID)
            includes('develop')
        }
    }
    factory {
        workflowMultiBranchProjectFactory {
            scriptPath('cicd/pipeline/jobs/increment-build.jenkins')
        }
    }
    orphanedItemStrategy {
        discardOldItems {
            daysToKeep(4)
            numToKeep(30)
        }
    }
}

multibranchPipelineJob("RC Build "+ ${JENIKINS_PROJECT_NAME}) {
    branchSources {
        github {
            scanCredentialsId(params.GIT_CREDENTIALS_ID)
            repoOwner(params.GITHUB_REPO_OWNER)
            repository(params.GITHUB_REPO_NAME)
            includes('develop')
        }
    }
    factory {
        workflowMultiBranchProjectFactory {
            scriptPath('cicd/pipeline/jobs/rc-build.jenkins')
        }
    }
    orphanedItemStrategy {
        discardOldItems {
            daysToKeep(4)
            numToKeep(30)
        }
    }
}



//IncrementBuildJobTemplate.create("Increment Build "+ ${JENIKINS_PROJECT_NAME}, params)
//RCBuildJobTemplate.create("RC Build "+ ${JENIKINS_PROJECT_NAME}, params)


