String jenikinsProjectName = "${JENKINS_PROJECT_NAME}"
String gitCredentialsId = "${GIT_CREDENTIALS_ID}"
String gitRepoUrl = "${GIT_REPO_URL}"
String githubRepoOwner = "${GITHUB_REPO_OWNER}"
String githubRepoName = "${GITHUB_REPO_NAME}"
String dbUser = "${SPRING_DATASOURCE_USERNAME}"
String dbPassword = "${SPRING_DATASOURCE_PASSWORD}"
String envName = "${ENV_NAME}"
String portUi = "${PORT_UI}"
String portApi = "${PORT_API}"
Boolean mysqlOn = "${MYSQL_ON}"

//CI Job
multibranchPipelineJob(jenikinsProjectName + "/CI Build") {
    branchSources {
        github {
            scanCredentialsId(gitCredentialsId)
            repoOwner(githubRepoOwner)
            repository(githubRepoName)
            includes('feature/* bugfix/* hotfix/* env/stage revert-* revert/* bugifx/*')
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
        periodic(7)
    }
}

//Increment Job
pipelineJob(jenikinsProjectName + "/Increment Build") {
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
            scriptPath('cicd/pipeline/jobs/increment-build.jenkins')
        }
    }
    triggers {
        scm('H/7 * * * *')
    }
}

//RC Job
pipelineJob(jenikinsProjectName + "/RC_Build") {
    environmentVariables(
            SPRING_DATASOURCE_USERNAME: dbUser,
            SPRING_DATASOURCE_PASSWORD: dbPassword,
            ENV_NAME: envName,
            PORT_API: portApi,
            PORT_UI: portUi
    )

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
        cron('H 23 * * *')
    }
}
