# aurea-zero-based

##Client's requirements:
* Client jenkins folder should be created;
* Client git credentials should be registered on jenkins and has read access to this project; 
* Aline Product should be created;

## Installation
Move `aurea-zero-based-init` into /usr/local/bin and grant execution permission by `chmod +x`

## Preconditions
* git, java(1.8+), groovy(2.7+) installed
* User that has access to http://jenkins.aureacentral.com/ and aline;
* Empty folder in http://jenkins.aureacentral.com/ with write access for that user;
* Aline product with write access for that user;
* Fill in `aurea-zero-based.properties` after first run.
* For central database: `mysql_on`, `mysql_url`, `db_user`, `db_password` should be specified in
aurea-zero-based.properties. Otherwise by default H2 is used;

## Initialisation
Trigger `aurea-zero-based-init` inside a project that you want to initialise.
When you run for the first time you will be asked to fill in aurea-zero-based.properties file.
Once this is done trigger cloning tool once again.

## Jenkins project pre-requisite
* folder in [central jenkins](http://jenkins.aureacentral.com/) with write access
* https github credentials. In case 2 factor authentication - github token should be configured and used.
* during initialisation fill in appropriate configuration in .

Under the hood ZBW will create jenkins credentials and configure CICD to use it automatically during cloning.

# Support
Contact to Alexander Yushchenko(alexander.yushchenko@aurea.com) 
or Vladan Petrovic (vladan.petrovic@aurea.com) in case of any questions/issues.
