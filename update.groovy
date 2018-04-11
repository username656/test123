@Grapes(
        @Grab(group='com.squareup.okhttp3', module='okhttp', version='3.10.0')
)
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import okhttp3.*

import java.util.concurrent.TimeUnit

def cli = new CliBuilder(usage: 'create/update zero-web-site dependencies')
// Create the list of options.
cli.with {
    h longOpt: 'help', 'Show usage information'
    u longOpt: 'user', args: 1, 'AD username'
    p longOpt: 'password', args: 1, 'AD password'
    j longOpt: 'jenkins_project', args: 1, 'Jenkins project'
    g longOpt: 'github_url', args: 1, 'Github repo url'
    gb longOpt: 'github_branch', args: 1, 'Github repo branch'
    go longOpt: 'github_owner', args: 1, 'Github owner'
    gr longOpt: 'github_repo', args: 1, 'Github repository'
    ap longOpt: 'aline_product_id', args: 1, 'Aline product id'
    apn longOpt: 'aline_product_name', args: 1, 'Aline product name'
    cred_id longOpt: 'jenkins_scm_credentials_id', args: 1, 'Client github jenkins credentials id'
}
def options = cli.parse(args)
if (!options) {
    return
}
// Show usage text when -h or --help option is used.
if (options.h) {
    cli.usage()
    return
}

// Template initialisation
println "Starting cicd/gradle templates initialisation"
new File("aurea-zero-based/.git").deleteDir()

builder = new AntBuilder()
builder.sequential {
    def myDir = "."
    copy(todir: myDir) {
        fileset(dir: "aurea-zero-based") {
            include(name: "gradle*")
            include(name: "gradle/**/*")
            include(name: "cicd/**/*")
            include(name: "ui/**/*")
            include(name: ".gitignore")
            include(name: "docker-compose.yml")
        }
    }
    copy(todir: myDir) {
        fileset(dir: "aurea-zero-based/cloning/gradle")
    }
}

static def copyAndReplaceText(source, dest, Closure replaceText){
    dest.write(replaceText(source.text))
}

def source = new File('cicd/pipeline/util/buildUtil.groovy')

copyAndReplaceText(source, source) {
    it.replaceAll('Develop', options.gb)
    it.replaceAll('ZeroBasedProject', options.apn)
}
println "Templates are copied"

OkHttpClient client = new OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build();

// Basic authorization is used both for aline and jenkins
String auth = options.u + ":" + options.p;
String authHeader = "Basic " +  auth.bytes.encodeBase64().toString();

// aline integration
if (options.ap) {
    println 'Starting aline integration'
    def inputFile = new File("aurea-zero-based/cloning/aline-product-version-template.json")
    def alineJson = new JsonSlurper().parseText(inputFile.text)
    alineJson['name'] = options.gb
    alineJson['productId'] = options.ap
    alineJson['svnRepoConfigs'][0]['svnRepo'] = options.g + '/?branch=' + options.gb

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
def githubUrlEncoded = URLEncoder.encode(options.g, "UTF-8")
def params = [GIT_REPO_URL        : githubUrlEncoded,
           GIT_CREDENTIALS_ID  : options.cred_id,
           GITHUB_REPO_OWNER   : options.go,
           GITHUB_REPO_NAME    : options.gr,
           JENKINS_PROJECT_NAME: options.j
]

Request request = new Request.Builder()
        .url("http://jenkins.aureacentral.com/job/KayakoRewrite/job/Zero%20Base%20Website%20Jenkins%20Job%20Cloning/" +
            "buildWithParameters?" + params.collect { k,v -> "$k=$v" }.join('&'))
        .post(new FormBody.Builder().build())
        .addHeader("Authorization", authHeader)
        .build();

Response response = client.newCall(request).execute();
assert response.code == 201
println "Jenkins jobs are updated: " + response
