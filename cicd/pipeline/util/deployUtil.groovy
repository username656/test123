//Deploy related functions

def deploy(String tag) {
    if (currentBuild.result == null || currentBuild.result == 'SUCCESS') {
        sh "export tag="+tag+"; " +
                "docker stop sample-zbw-api || true && docker rm sample-zbw-api || true;" +
                "docker stop sample-zbw-ui || true && docker rm sample-zbw-ui || true; " +
                "docker-compose up -d;"
    }
}

return this;
