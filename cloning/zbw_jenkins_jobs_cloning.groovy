String jenikinsProjectName = "${JENKINS_PROJECT_NAME}"
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
    configure {
        it / factory(class: 'org.jenkinsci.plugins.workflow.multibranch.WorkflowBranchProjectFactory') {
            owner(class: 'org.jenkinsci.plugins.workflow.multibranch.WorkflowMultiBranchProject', reference: '../..')
            scriptPath('cicd/pipeline/jobs/ci-build.jenkins')
        }
    }
    orphanedItemStrategy {
        discardOldItems {
            daysToKeep(4)
            numToKeep(30)
        }
    }
    triggers {
        cron('H */4 * * *')
    }
}

//Incremental Job
pipelineJob(jenikinsProjectName + "/Incremental Build") {
    logRotator {
        daysToKeep(10)
        numToKeep(30)
    }
    concurrentBuild(false)
    definition {
        cpsScm {
            scm {
                git {
                    branch("develop")
                    remote {
                        url(gitRepoUrl)
                        credentials(gitCredentialsId)
                    }
                    extensions {
                        localBranch()
                        wipeOutWorkspace()
                    }
                }
            }
            scriptPath('cicd/pipeline/jobs/incremental-build.jenkins')
        }
    }
    triggers {
        cron('H */4 * * *')
    }
}

//RC Job
pipelineJob(jenikinsProjectName + "/RC_Build") {
    logRotator {
        daysToKeep(10)
        numToKeep(30)
    }
    concurrentBuild(false)
    definition {
        cpsScm {
            scm {
                git {
                    branch("develop")
                    remote {
                        url(gitRepoUrl)
                        credentials(gitCredentialsId)
                    }
                }
            }
            scriptPath('cicd/pipeline/jobs/rc-build.jenkins')
        }
    }
    triggers {
        cron('@daily')
    }
}
