package templates

class IncrementBuildJobTemplate {
    static void create(jobName, params) {

        multibranchPipelineJob(jobName) {
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
    }
}

return this;