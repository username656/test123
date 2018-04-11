# aurea-zero-based

##Client's requirements:
* Client jenkins folder should be created;
* Client git credentials should be registered on jenkins and has read access to this project; 
* Aline Product should be created;

## Initialisation
* `git submodule add https://github.com/trilogy-group/aurea-zero-based.git`
* Invoke aurea-zero-based/update.groovy from root directory with parameters like:
```
groovy aurea-zero-based/update.groovy -u ayushchenko -p $AD_PASSWORD \ 
    -j KayakoRewrite/Cloned -g https://github.com/sahkaa/cloned-zbw.git 
    -go sahkaa -gr cloned-zbw -cred_id kayako-rw-sample-git-id 
    -gb master -g https://github.com/trilogy-group/aurea-kayako.git -ap 3924 -apn Cloned-ZBW
```


