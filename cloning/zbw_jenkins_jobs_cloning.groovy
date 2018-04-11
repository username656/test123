String jenikinsProjectName ="${JENKINS_PROJECT_NAME}"
String gitCredentialsId = "${GIT_CREDENTIALS_ID}"
String gitRepoUrl = "${GIT_REPO_URL}"
String githubRepoOwner = "${GITHUB_REPO_OWNER}"
String githubRepoName = "${GITHUB_REPO_NAME}"

//CI Job
multibranchPipelineJob(jenikinsProjectName + "/CI Build") {
    branchSources {
        github {
            scanCredentialsId(gitCredentialsId)
            repoOwner(githubRepoOwner)
            repository(githubRepoName)
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

//Increment Job
multibranchPipelineJob(jenikinsProjectName + "/Increment Build") {
    branchSources {
        git {
            remote(gitRepoUrl)
            credentialsId(gitCredentialsId)
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

//RC Job
multibranchPipelineJob(jenikinsProjectName+"/RC Build") {
    branchSources {
        github {
            scanCredentialsId(gitCredentialsId)
            repoOwner(githubRepoOwner)
            repository(githubRepoName)
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
