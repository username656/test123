import hudson.model.*

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





