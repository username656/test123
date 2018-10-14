This docker image is a build agent as neither generic nor jervis generic ansible projects have similar.
docker tag  6bd186cf61f8 registry2.swarm.devfactory.com/aurea-zero-based/build-agent:node-10-stretch-browsers-ansible11
docker push registry2.swarm.devfactory.com/aurea-zero-based/build-agent:node-10-stretch-browsers-ansible11
docker run -d --name custom-deployment registry2.swarm.devfactory.com/aurea-zero-based/build-agent:node-10-stretch-browsers-ansible11 sleep 100000
