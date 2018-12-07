This docker image is a build agent as neither generic nor jervis generic ansible projects have similar.
docker tag  fe848901a87c registry2.swarm.devfactory.com/aurea-kayako/jenkins-agent:node-10-stretch-browsers-ansible-additional-features-3
docker push registry2.swarm.devfactory.com/aurea-kayako/jenkins-agent:node-10-stretch-browsers-ansible-additional-features-3
docker run -d --name custom-deployment registry2.swarm.devfactory.com/aurea-kayako/jenkins-agent:node-10-stretch-browsers-ansible-additional-features-3 sleep 100000
  
