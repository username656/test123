//CI Job
multibranchPipelineJob("${JENIKINS_PROJECT_NAME}/CI Build") {
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
            scriptPath('aurea-zero-based/cicd/pipeline/jobs/ci-build.jenkins')
        }
    }
    orphanedItemStrategy {
        discardOldItems {
            daysToKeep(4)
            numToKeep(30)
        }
    }
}

//Increment Job
multibranchPipelineJob("${JENIKINS_PROJECT_NAME}/Increment Build") {
    branchSources {
        git {
            remote("${GIT_REPO_URL}")
            credentialsId("${GIT_CREDENTIALS_ID}")
            includes('develop')
        }
    }
    factory {
        workflowMultiBranchProjectFactory {
            scriptPath('aurea-zero-based/cicd/pipeline/jobs/increment-build.jenkins')
        }
    }
    orphanedItemStrategy {
        discardOldItems {
            daysToKeep(4)
            numToKeep(30)
        }
    }
}

//RC Job
multibranchPipelineJob("${JENIKINS_PROJECT_NAME}/RC Build") {
    branchSources {
        github {
            scanCredentialsId("${GIT_CREDENTIALS_ID}")
            repoOwner("${GITHUB_REPO_OWNER}")
            repository("${GITHUB_REPO_NAME}")
            includes('develop')
        }
    }
    factory {
        workflowMultiBranchProjectFactory {
            scriptPath('aurea-zero-based/cicd/pipeline/jobs/rc-build.jenkins')
        }
    }
    orphanedItemStrategy {
        discardOldItems {
            daysToKeep(4)
            numToKeep(30)
        }
    }
}





