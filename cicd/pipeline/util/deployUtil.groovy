def deploy() {
    if (currentBuild.result == null || currentBuild.result == 'SUCCESS') {
        def dockerComposeProject = "${ENV_STAGE}_aurea-zero-based"
        println "deploying with docker-compose for env ${ENV_STAGE}, DOCKER_HOST ${DOCKER_HOST}, " +
                "AUREA_API_UI_URL ${AUREA_API_UI_URL}, BACKEND_URL ${BACKEND_URL}"
        sh "docker-compose -p $dockerComposeProject rm -fs; docker-compose -p $dockerComposeProject pull; " +
                "docker-compose -p $dockerComposeProject up -d;"
    }
}

this
