def deploy() {
    if (currentBuild.result == null || currentBuild.result == 'SUCCESS') {
        sh "docker stop ${ENV_NAME}_aurea-zero-based_ui || true && docker rm ${ENV_NAME}_aurea-zero-based_ui || true;" +
                "docker stop ${ENV_NAME}_aurea-zero-based_api || true && docker rm ${ENV_NAME}_aurea-zero-based_api || true; "
        sh "docker-compose  rm -fs; docker-compose pull; docker-compose up -d;"
    }
}

this
