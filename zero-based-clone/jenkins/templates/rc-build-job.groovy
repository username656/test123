package templates

class RCBuildJobTemplate {
    static void create(jobName, params) {

        multibranchPipelineJob(jobName) {
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
    }
}