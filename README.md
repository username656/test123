# aurea-zero-based

## Client's requirements:
* Client jenkins folder should be created;
* Client git credentials should be registered on jenkins and has read access to this project; 
* Aline Product should be created;

## Installation
Move `aurea-zero-based-init` into /usr/local/bin and grant execution permission by `chmod +x`

## Preconditions
* git, java(1.8+), groovy(2.7+) installed
* user that has access to http://jenkins.aureacentral.com/ and aline;
* empty folder in http://jenkins.aureacentral.com/ with write access for that user;
* aline product with write access for that user;
* find two free ports on `dl6.aureacentral.com` to use for deployment. Default port are: 10234(UI) and 10233(API);
* optional: Central Database configs if integration is required. 

## Initialisation
Trigger `aurea-zero-based-init` inside a project that you want to initialise.
When you run for the first time you will be asked to fill in aurea-zero-based.properties file.
Once this is done trigger cloning tool once again. For central database: `mysql_on`, `mysql_url`, `db_user`, 
`db_password` should be specified in `aurea-zero-based.properties`. 
Otherwise by default H2 db will be used;

## Recommendations
`docker-compose.yml` has hardcoded ports to access your ui/api: 10234 and 10233.
To not intersect with another clients it's recommended to define another ports.
Check CDH to see available ports.
     
## Jenkins project pre-requisite
* folder in [central jenkins](http://jenkins.aureacentral.com/) with write access
* https github credentials. In case 2 factor authentication - github token should be configured and used.
* during initialisation fill in appropriate configuration in .

Under the hood ZBW will create jenkins credentials and configure CICD to use it automatically during cloning.

## Central Docker Hosts
`dlb1.aureacentral.com` - build docker host;
`registry2.swarm.devfactory.com` - docker registry;
`dl6.aureacentral.com` - docker host to deploy. 

## Engine Yard deployments
To deploy ZBW to Engine Yard please follow the steps in the [document](https://docs.google.com/document/d/1HjDh_5iPErTn1PhO8jAxas_yPSYqdzGOPvk2AjTZ9yA/edit#)

Alternatively you can  use EngineYard CLI to improve/automate deployment process: 
* install EY cli `pip3 install --upgrade --extra-index-url https://pypi.swarm.devfactory.com/ eycli`
* configure cli with `ey configure` 
* check list of command available with `ey help`
* create new application with `ey create-application` eg. `ey create-application --health_check_path /health --git_repo_url git@github.com:stankonia/cybertron.git --name westeros `
* create new environment with `ey create-environment` or update with  `ey update-environment` 
* there any many options available in CLI make sure that you run `ey help` and `ey <command> help` to check for commands samples
* also with in DevFactory Slack channel [#ey_nextgen_support](https://devfactorydev.slack.com/messages/C5RGB8JUB)

# Support
Contact to Alexander Yushchenko(alexander.yushchenko@aurea.com) 
or Vladan Petrovic (vladan.petrovic@aurea.com) in case of any questions/issues.
