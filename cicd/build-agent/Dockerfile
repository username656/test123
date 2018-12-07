FROM circleci/node:10-stretch-browsers

LABEL com.trilogy.company="Aurea" \
    com.trilogy.team="KayakoRewrite" \
    com.trilogy.product="kayako" \
    com.trilogy.service="jenkins-agent" \
    com.trilogy.stage="dev" \
    com.trilogy.maintainer.skype="sahkalit" \
    com.trilogy.maintainer.email="alexander.yushchenko@aurea.com"

USER root
ENV ANSIBLE_VAULT_PASSWORD_FILE /opt/vault-env
ADD files/vault-env /bin/vault-env

RUN apt-get update && \
    chmod +x /bin/vault-env && \
    apt-get install -y curl vim git htop wget net-tools dnsutils unzip python-pip && \
    curl -o- https://download.docker.com/linux/debian/dists/stretch/pool/stable/amd64/docker-ce_18.03.0~ce-0~debian_amd64.deb > docker.deb && \
    dpkg -i docker.deb || rm docker.deb && \
    apt --fix-broken -y install && \
    apt-key adv --keyserver keyserver.ubuntu.com --recv-keys 93C4A3FD7BB9C367 && \
    echo deb http://ppa.launchpad.net/ansible/ansible-2.5/ubuntu trusty main >>  /etc/apt/sources.list && \
    apt-get update && \
    apt-get install ansible  -y && \
    pip uninstall docker-py; pip uninstall docker; pip install docker docker-compose "pywinrm>=0.2.2" PyVmomi python-dateutil boto boto3 botocore && \
    npm cache verify && npm install -g @angular/cli@latest
