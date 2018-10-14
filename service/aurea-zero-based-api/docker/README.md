- `libnumcpus.so` is created to limit CPU inside pods. 
See https://confluence.devfactory.com/display/DE/Be+careful+while+put+java+in+docker#Becarefulwhileputjavaindocker-Whatislibnumcpus.so?

- `libyjpagent.so` yourkit profile binary agent for current docker arch.

To build locally execute from project root:
```
docker build --pull -t aurea-kayako-api -f ./service/kayako-api/Dockerfile ./service/kayako-api
```
