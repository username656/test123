#!/usr/bin/env groovy
@Grapes([
        @Grab(group = 'com.squareup.okhttp3', module = 'okhttp', version = '3.10.0'),
        @Grab(group = 'org.apache.commons', module = 'commons-lang3', version = '3.7')
])

import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import okhttp3.*
import org.apache.commons.lang3.RandomStringUtils

import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.concurrent.TimeUnit

import static groovy.io.FileType.FILES

static def copyAndReplaceText(source, dest, Closure replaceText) {
    dest.write(replaceText(source.text))
}

def executeAndPrint(command) {
    def sout = new StringBuilder(), serr = new StringBuilder()
    def proc = command.execute()
    proc.consumeProcessOutput(sout, serr)
    proc.waitForOrKill(20000)

    println "Executing $command"
    println "out> $sout err> $serr"
//    check that we don't have errors. We could not check for status as status code could be different on success.
    assert proc.exitValue() != 1
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

try {
// Init properties
    Properties properties = new Properties()
    File propertiesFile = new File('aurea-zero-based.properties')
    if (!propertiesFile.exists()) {
        Files.copy(Paths.get('aurea-zero-based/cloning/aurea-zero-based-template.properties'),
                Paths.get('aurea-zero-based.properties'))
        println "Fill in aurea-zero-base.properties file to proceed."
        System.exit(0)
    }

    propertiesFile.withInputStream {
        properties.load(it)
    }

    if (properties.version != "1.4") {
        Files.copy(Paths.get('aurea-zero-based/cloning/aurea-zero-based-template.properties'),
                Paths.get('aurea-zero-based.properties'), StandardCopyOption.REPLACE_EXISTING)
        println "Properties version mismatch. Pls fill in aurea-zero-base.properties file to proceed again."
        System.exit(0)
    }



    githubUrl = "https://github.com/" + properties.github_owner + "/" + properties.github_repo + ".git"

    OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();

    if (!properties.ad_password) {
        properties.ad_password = System.getenv("AD_PASSWORD")
    }
    if (!properties.project_name) {
        properties.project_name = Paths.get(".").toAbsolutePath().getParent().getFileName().toString()
    }
    properties.project_name=((String)properties.project_name)
            .replaceAll("[ _]", "-").toLowerCase()

// Basic authorization is used both for aline and jenkins
    String auth = properties.ad_user + ":" + properties.ad_password
    String authHeader = "Basic " + auth.bytes.encodeBase64().toString();

    if (!properties.jenkins_credentials_id) {
//    create credentials automatically in jenkins
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        properties.jenkins_credentials_id = RandomStringUtils.randomAlphabetic(32)
        String json = '{ "": "0", "credentials": {"scope": "GLOBAL","id": "${id}",' +
                '"username": "${username}","password": "${password}","description": "Auto generated credential by ' +
                'zero-base cloning","$class": "com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl"}}';
        json = json.replace('${username}', (String) properties.github_user)
                .replace('${password}', (String) properties.github_password)
                .replace('${id}', (String) properties.jenkins_credentials_id)
        def jenkinsProject = (String) properties.jenkins_project
        def projects = []
        if (jenkinsProject.indexOf('/') > 0) {
            for (def project in jenkinsProject.split("/")) {
                projects.add(project)
            }
            jenkinsProject = projects.join("/job/")
        }

        RequestBody body = RequestBody.create(mediaType, "json=" + URLEncoder.encode(json, "UTF-8"));
        Request request = new Request.Builder()
                .url("http://jenkins.aureacentral.com/job/" + jenkinsProject + "/credentials/store/folder/domain/_/createCredentials")
                .post(body)
                .addHeader("Authorization", authHeader)
                .build();

        Response response = client.newCall(request).execute()

        assert response.code == 200
    }

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
                include(name: "devspaces/**/*")
                include(name: "lombok.config")
                include(name: "devfactory.yml")
                include(name: "docker-compose.yml")
            }
        }
        chmod(dir: myDir, perm: "+x", includes: "gradlew")
        chmod(dir: "cicd/scripts/", perm: "+x", includes: "*.sh")
        chmod(dir: "ui/autotest-protractor/", perm: "+x", includes: "*.sh")
    }
    Files.copy(Paths.get('aurea-zero-based/.gitignore'), Paths.get('.gitignore'),
            StandardCopyOption.REPLACE_EXISTING)
    Files.copy(Paths.get('aurea-zero-based/.stignore'), Paths.get('.stignore'),
            StandardCopyOption.REPLACE_EXISTING)

    use_mysql = ((String) properties.use_mysql).toBoolean()
    println "Use mysql as database: ${use_mysql}"
    if (use_mysql) {
        Files.copy(Paths.get('aurea-zero-based/cloning/application-mysql.yml'),
                Paths.get('service/aurea-zero-based-api/src/main/resources/application.yml'), StandardCopyOption.REPLACE_EXISTING)
        Files.copy(Paths.get('aurea-zero-based/cloning/build-mysql.gradle'),
                Paths.get('service/aurea-zero-based-api/build.gradle'), StandardCopyOption.REPLACE_EXISTING)

    }

    new File('.').traverse(type: FILES, nameFilter: ~/(.*\.java|.*\.groovy|.*\.sh|.*\Dockerfile|.*\.gradle|.*\.jenkins|.*\.yml|.*environment.prod.ts)/) {
        copyAndReplaceText(it.getAbsoluteFile(), it.getAbsoluteFile()) {
            it.replaceAll('Develop', properties.github_branch)
                    .replaceAll('ZeroBasedProject', alineProductName)
                    .replaceAll('aurea-zero-based', properties.project_name)
        }
    }

//Mysql url adjustment
    source = new File('service/aurea-zero-based-api/src/main/resources/application.yml')
    copyAndReplaceText(source, source) {
        it.replaceAll('mysql_url', properties.mysql_url)
    }

    Files.move(Paths.get('service/aurea-zero-based-api'), Paths.get('service/' + properties.project_name + '-api'),
            StandardCopyOption.REPLACE_EXISTING)

    println "Templates are copied"

    aline_enabled = ((String) properties.aline_enabled).toBoolean()
    println "Aline integration is ${aline_enabled}"
// aline integration
    if (properties.aline_product_id && aline_enabled) {
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

// Validation:
    def envs = ['dev', 'qa', 'staging', 'regression', 'prod']
    if (!envs.contains(properties.env_name)) {
        println("env_name(${properties.env_name}) could have values from $envs only!")
    }

// Cloning jenkins jobs
    def githubUrlEncoded = URLEncoder.encode(githubUrl, "UTF-8")
    def params = [GIT_REPO_URL              : githubUrlEncoded,
                  GIT_CREDENTIALS_ID        : properties.jenkins_credentials_id,
                  GITHUB_REPO_OWNER         : properties.github_owner,
                  GITHUB_REPO_NAME          : properties.github_repo,
                  SPRING_DATASOURCE_USERNAME: properties.db_user,
                  SPRING_DATASOURCE_PASSWORD: properties.db_password,
                  MYSQL_ON                  : properties.mysql_on,
                  ENV_NAME                  : properties.env_name,
                  BACKEND_URL               : "http://${properties.env_name}-${properties.project_name}-api.internal-webproxy.aureacentral.com",
                  AUREA_API_UI_URL          : "http://${properties.env_name}-${properties.project_name}-ui.internal-webproxy.aureacentral.com",
                  JENKINS_PROJECT_NAME      : properties.jenkins_project
    ]

    Request request = new Request.Builder()
            .url("http://jenkins.aureacentral.com/job/AureaZeroBased/job/Zero%20Base%20Website%20Jenkins%20Job%20Cloning/" +
            "buildWithParameters?" + params.collect { k, v -> "$k=$v" }.join('&'))
            .post(new FormBody.Builder().build())
            .addHeader("Authorization", authHeader)
            .build();

    Response response = client.newCall(request).execute()
    assert response.code == 201
    println "Jenkins jobs are updated: " + response

    git_push_enabled = ((String) properties.git_push_enabled).toBoolean()
    println "Push into github: ${git_push_enabled}"
    if (git_push_enabled) {
        executeAndPrint("git add .")
        executeAndPrint('git commit -m "Init"')
        executeAndPrint("git push origin " + properties.github_branch)
    }
    println "Repository is initialised and pushed."

} finally {
    Paths.get("aurea-zero-based").toFile().deleteDir()
}
