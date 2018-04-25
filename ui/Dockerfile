FROM node:latest

ENV NODE_OPTIONS --max_old_space_size=8192
ENV NG_BUILD_ARGS --prod --aot

# Create a directory where our app will be placed
RUN mkdir -p /usr/src/app

# Change directory so that our commands run inside this new directory
WORKDIR /usr/src/app

# Copy npm package files
COPY package.json package-lock.* /usr/src/app/

# Install npm dependencies
RUN npm install --@devfactory:registry=http://nexus-rapid-proto.devfactory.com/repository/npm-proto/

# Copy app files to out container
COPY . /usr/src/app

# Build app
RUN node_modules/.bin/ng build $NG_BUILD_ARGS

FROM nginx:latest

ENV BACKEND_URL http://mockbin.com/request
ENV BACKEND_URL2 http://mockbin.com/request
ENV BACKEND_URL3 http://mockbin.com/request
ENV PROXY_API2 /api2
ENV PROXY_API3 /api3

RUN mkdir -p /etc/app

# donwload nginx configuration files see https://github.com/trilogy-group/dfproto-ey-docker
# this is done to avoid copying this files on all projects
ADD "http://nexus-rapid-proto.devfactory.com/repository/npm-proto/@devfactory/dfproto-ey-docker/-/dfproto-ey-docker-1.0.0.tgz" /etc/app

RUN tar -xzvf /etc/app/dfproto-ey-docker-1.0.0.tgz -C /etc/app

# Copy build files from first image to nginx dir
RUN rm -rf /usr/share/nginx/html/*
COPY --from=0 /usr/src/app/dist/ /usr/share/nginx/html/

# Copying nginx config
RUN rm -f /etc/nginx/nginx.conf && cp /etc/app/package/mime.types /etc/nginx/mime.types && chmod +x /etc/app/package/entrypoint.sh

# Expose port 80
EXPOSE 80

LABEL com.trilogy.company=aurea
LABEL com.trilogy.maintainer=alexander.yushchenko@aurea.com
LABEL com.trilogy.maintainer=alexander.yushchenko
LABEL com.trilogy.product=aurea-zero-based
LABEL com.trilogy.service=sample-zbw-ui
LABEL com.trilogy.stage=dev
LABEL com.trilogy.team=Easier.BackendRewrite
LABEL SERVICE_NAME=sample-zbw-ui
LABEL SERVICE_TAGS="trilogy.expose-v2,trilogy.http,trilogy.endpoint=sample-zbw-ui.webproxy.aureacentral.com sample-zbw-ui.devfactory.com"

# Copy custom nginx.conf file and run entrypoint.sh to start nginx server
WORKDIR /etc/app
CMD ["/etc/app/package/entrypoint.sh"]
