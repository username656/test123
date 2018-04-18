#!/usr/bin/env groovy

@Grapes(
        @Grab(group='com.squareup.okhttp3', module='okhttp', version='3.10.0')
)
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import java.nio.file.Files
import java.nio.file.Paths
import okhttp3.*

import java.util.concurrent.TimeUnit

// Init properties
Properties properties = new Properties()
File propertiesFile = new File('aurea-zero-based.properties')
if(!propertiesFile.exists()) {
    Files.copy(Paths.get('aurea-zero-based/cloning/aurea-zero-based-template.properties'),
            Paths.get('aurea-zero-based.properties'))
    println "Fill in aurea-zero-base.properties file to proceed."
    System.exit(0)
}

propertiesFile.withInputStream {
    properties.load(it)
}

def getAlineProductNameById(OkHttpClient client, id, authHeader) {
    def params = [productIds: [id]]

    RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
            new JsonBuilder(params).toPrettyString());
    Request request = new Request.Builder()
            .url("https://aline-mdm.devfactory.com/api/1.0/getProductById")
            .post(body)
            .addHeader("Authorization", authHeader)
            .build();
    Response response = client.newCall(request).execute()
    assert response.code == 200
    def alineProduct = new JsonSlurper().parseText(response.body().string())
    return alineProduct['responseDataList'][0]['name']
}

githubUrl = "https://github.com/" + properties.github_owner + "/" + properties.github_repo + ".git"

OkHttpClient client = new OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build();

if (!properties.ad_password) {
    properties.ad_password = System.getenv("AD_PASSWORD")
}

// Basic authorization is used both for aline and jenkins
String auth = properties.ad_user + ":" + properties.ad_password
String authHeader = "Basic " +  auth.bytes.encodeBase64().toString();

def alineProductName = getAlineProductNameById(client, properties.aline_product_id, authHeader)

// Template initialisation
println "Starting cicd/gradle templates initialisation"
new File("aurea-zero-based/.git").deleteDir()

new AntBuilder().sequential {
    def myDir = "."
    copy(todir: myDir) {
        fileset(dir: "aurea-zero-based") {
            include(name: "gradle*")
            include(name: "*gradle")
            include(name: "gradle/**/*")
            include(name: "cicd/**/*")
            include(name: "config/**/*")
            include(name: "ui/**/*")
            include(name: "service/**/*")
            include(name: "lombok.config")
            include(name: "devfactory.yml")
            include(name: "docker-compose.yml")
        }
    }
    chmod(dir: myDir, perm:"+x", includes:"gradlew")
    chmod(dir: "cicd/scripts/", perm:"+x", includes:"*.sh")
}
Files.copy(Paths.get('aurea-zero-based/.gitignore'), Paths.get('.gitignore'))

static def copyAndReplaceText(source, dest, Closure replaceText){
    dest.write(replaceText(source.text))
}

def source = new File('cicd/pipeline/util/buildUtil.groovy')

copyAndReplaceText(source, source) {
    it.replaceAll('Develop', properties.github_branch)
    it.replaceAll('ZeroBasedProject', alineProductName)
}
println "Templates are copied"

// aline integration
if (properties.aline_product_id) {
    println 'Starting aline integration'
    def inputFile = new File("aurea-zero-based/cloning/aline-product-version-template.json")
    def alineJson = new JsonSlurper().parseText(inputFile.text)
    alineJson['name'] = properties.github_branch
    alineJson['productId'] = properties.aline_product_id
    alineJson['svnRepoConfigs'][0]['svnRepo'] = githubUrl + '/?branch=' + properties.github_branch

    RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
            new JsonBuilder(alineJson).toPrettyString());
    Request request = new Request.Builder()
            .url("https://aline-mdm.devfactory.com/api/1.0/crud/saveProductVersion")
            .post(body)
            .addHeader("Authorization", authHeader)
            .build();
    Response response = client.newCall(request).execute();
    println "Aline processed: $response"
    assert response.code == 201
}

// Cloning jenkins jobs
def githubUrlEncoded = URLEncoder.encode(githubUrl, "UTF-8")
def params = [GIT_REPO_URL        : githubUrlEncoded,
           GIT_CREDENTIALS_ID  : properties.jenkins_credentials_id,
           GITHUB_REPO_OWNER   : properties.github_owner,
           GITHUB_REPO_NAME    : properties.github_repo,
           JENKINS_PROJECT_NAME: properties.jenkins_project
]

Request request = new Request.Builder()
        .url("http://jenkins.aureacentral.com/job/AureaZeroBased/job/Zero%20Base%20Website%20Jenkins%20Job%20Cloning/" +
            "buildWithParameters?" + params.collect { k,v -> "$k=$v" }.join('&'))
        .post(new FormBody.Builder().build())
        .addHeader("Authorization", authHeader)
        .build();

Response response = client.newCall(request).execute();
assert response.code == 201
println "Jenkins jobs are updated: " + response
