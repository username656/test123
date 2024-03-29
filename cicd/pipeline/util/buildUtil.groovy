// General purpose build related functions
// Ideally these functions should be in a Jenkins Shared Library,
// but till we have a common repo for that, the below should suffice

def checkout() {
    checkout scm
}

def buildWithoutJacoco(gradleOptions = "") {
    sh "./gradlew clean build $gradleOptions"
    echo 'Ran build successfully'
    junit allowEmptyResults: true, testResults: '**/build/test-results/**/*.xml'
    echo 'Generated unit test results'
}

def buildUi(gradleOptions = "") {
    sh "./gradlew clean :ui:build $gradleOptions"
    echo 'Ran build successfully'
}

def build(gradleOptions = "") {
    buildWithoutJacoco(gradleOptions)
    jacoco buildOverBuild: true, changeBuildStatus: true, deltaBranchCoverage: '15', deltaClassCoverage: '15',
            deltaComplexityCoverage: '15', deltaInstructionCoverage: '15', deltaLineCoverage: '15',
            deltaMethodCoverage: '15',
            exclusionPattern: '**/*Application.class,**/*Test.class,**/*Config.class,**/*Consts.class',
            maximumBranchCoverage: '70', maximumClassCoverage: '70', maximumComplexityCoverage: '70',
            maximumInstructionCoverage: '70', maximumLineCoverage: '80', maximumMethodCoverage: '70',
            minimumBranchCoverage: '70', minimumClassCoverage: '70', minimumComplexityCoverage: '70',
            minimumInstructionCoverage: '70', minimumLineCoverage: '80', minimumMethodCoverage: '70'

    echo 'Recorded Test coverage report'
}

def buildWithAline() {
    devfactory(portfolio: 'TestPFAurea', product: 'ZeroBasedProject', types: 'Java',
            jacocoFile: "**/*.exec") {
        build("--continue -x findbugsMain -x findbugsTest")
    }
}

def runE2eUI() {
    sh "cd ui/autotest-protractor; ./e2e-ui.sh "
    echo "Finished UI e2e tests (protractor)."
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

def buildDockerImage(String tag, String workspace, String dockerImageName) {
    sh "cd cicd/scripts; ./build-docker.sh $tag $workspace $dockerImageName;"
    echo 'Built Docker Image $dockerImageName'
}

def pushDockerImageToRegistry(String tag, String dockerImageName) {
    sh "cd cicd/scripts; ./push-docker-to-registry.sh $tag $dockerImageName;"
    echo 'Pushed docker image to Docker Registry successfully'
}

def buildAndPushDocker(String tag, String workspace, String dockerImageName) {
    buildDockerImage(tag, workspace, dockerImageName)
    pushDockerImageToRegistry(tag, dockerImageName)
}

this
