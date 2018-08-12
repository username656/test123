This docker image is a build agent as neither generic nor jervis generic ansible projects have similar.
docker tag  6bd186cf61f8 registry2.swarm.devfactory.com/aurea-zero-based/build-agent:node-10-stretch-browsers-ansible
docker push registry2.swarm.devfactory.com/aurea-zero-based/build-agent:node-10-stretch-browsers-ansible
docker run --name custom-deployment registry2.swarm.devfactory.com/aurea-zero-based/build-agent:node-10-stretch-browsers-ansible sleep 100000
