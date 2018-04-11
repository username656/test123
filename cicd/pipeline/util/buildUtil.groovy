// General purpose build related functions
// Ideally these functions should be in a Jenkins Shared Library,
// but till we have a common repo for that, the below should suffice

def checkout() {
    checkout scm
}

def buildService() {
    def gitCommit = sh(returnStdout: true, script: 'git rev-parse HEAD').trim()
    shortCommit = gitCommit.take(6)
    sh "./gradlew clean build cpdCheck"
    echo 'Ran build successfully'
    junit allowEmptyResults: true, testResults: '**/build/test-results/**/*.xml'
    echo 'Generated unit test results'
    step([$class: 'JacocoPublisher', classPattern: '**/build/classes/java/main', sourcePattern: '**/src/main/java'])
    echo 'Recorded Test coverage report'
    return shortCommit
}

def buildServiceWithAline() {
    def gitCommit = sh(returnStdout: true, script: 'git rev-parse HEAD').trim()
    shortCommit = gitCommit.take(6)
    devfactory (portfolio: 'TestPFAurea', product: 'ZeroBasedProject', productVersion: 'Develop',
            types: 'Java') {
        //Removing findbugs as it is slowing down the build and giving some errors
        sh "./gradlew clean build cpdCheck"
    }
    echo 'Ran build successfully'
    junit allowEmptyResults: true, testResults: '**/build/test-results/**/*.xml'
    echo 'Generated unit test results'
    step([$class: 'JacocoPublisher', classPattern: '**/build/classes/java/main', sourcePattern: '**/src/main/java'])
    echo 'Recorded Test coverage report'
    return shortCommit
}

def buildUI() {
    sh "cd ui; npm install --@devfactory:registry=http://nexus-rapid-proto.devfactory.com/repository/npm-proto/; " +
            "npm run build"
    echo "Finished the UI build"
}

def runIntegrationTests() {
    //empty for now
}

def runAcceptanceTests() {
    stage('Run acceptance tests') {
        //empty for now
    }
}

def runPerfTests() {
    stage('Run Performance Tests') {
        //empty for now
    }
}

def buildDockerImage(String branchName, String tag, String workspace, String dockerImageName) {
    sh "cd cicd/scripts; ./build-docker.sh $branchName $tag $workspace $dockerImageName;"
    echo 'Built Docker Image'
}

def pushDockerImageToRegistry(String branchName, String tag, String dockerImageName) {
    withCredentials([usernamePassword(credentialsId: 'docker-registry-login-id', usernameVariable: 'USERID', passwordVariable: 'PASSWORD')]) {
        sh "cd cicd/scripts; ./push-docker-to-registry.sh $branchName $tag $dockerImageName $USERID $PASSWORD;"
        echo 'Pushed docker image to Docker Registry successfully'
    }
}

return this;
