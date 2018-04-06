package templates

class CIBuildJobTemplate {
    static void create(jobName, params) {

        multibranchPipelineJob(jobName) {
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
    }
}

return this;