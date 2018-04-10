@Grapes(
        @Grab(group='com.squareup.okhttp3', module='okhttp', version='3.10.0')
)

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.FormBody

def cli = new CliBuilder(usage: 'showdate.groovy -[chflms] [date] [prefix]')
// Create the list of options.
cli.with {
    h longOpt: 'help', 'Show usage information'
    u longOpt: 'user', args: 1, 'AD username'
    p longOpt: 'password', args: 1, 'AD password'
    j longOpt: 'jenkins_project', args: 1, 'Jenkins project'
    g longOpt: 'github_url', args: 1, 'Use DateFormat#LONG format'
    go longOpt: 'github_owner', args: 1, 'Github owner'
    gr longOpt: 'github_repo', args: 1, 'Github repository'
    ap longOpt: 'aline_product', args: 1, 'Aline product'
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


// Cloning jenkins jobs
def githubUrlEncoded = URLEncoder.encode(options.g, "UTF-8")
def params = [GIT_REPO_URL        : githubUrlEncoded,
           GIT_CREDENTIALS_ID  : options.cred_id,
           GITHUB_REPO_OWNER   : options.go,
           GITHUB_REPO_NAME    : options.gr,
           JENKINS_PROJECT_NAME: options.j
]

OkHttpClient client = new OkHttpClient();

String auth = options.u + ":" + options.p;
String authHeader = "Basic " +  auth.bytes.encodeBase64().toString();

Request request = new Request.Builder()
        .url("http://jenkins.aureacentral.com/job/KayakoRewrite/job/Zero%20Base%20Website%20Jenkins%20Job%20Cloning/" +
            "buildWithParameters?" + params.collect { k,v -> "$k=$v" }.join('&'))
        .post(new FormBody.Builder().build())
        .addHeader("Cache-Control", "no-cache")
        .addHeader("Authorization", authHeader)
        .addHeader("Postman-Token", "ef229d63-c086-4cb6-8bac-65a7ef254d6a")
        .build();

Response response = client.newCall(request).execute();
assert response.code == 201
println "Jenkins projects are updated: " + response


// Cloning aline


////URL triggerJob = new URL("http://${options.u}:${passwordEncoded}@jenkins.aureacentral.com/job/KayakoRewrite/job/Zero%20Base%20Website%20Jenkins%20Job%20Cloning/buildWithParameters?" + params.collect { k,v -> "$k=$v" }.join('&'))
//def connection = triggerJob.openConnection()
//connection.with {
//    doOutput = true
//    requestMethod = 'POST'
//    println content.text
//}
