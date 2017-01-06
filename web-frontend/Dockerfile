FROM nginx
COPY eventer/nginx.conf /etc/nginx/nginx.conf
COPY eventer/ /opt/src/eventer
RUN apt-get update
RUN apt-get install -y curl
RUN curl -sl https://deb.nodesource.com/setup_6.x | sh
RUN apt-get install -y nodejs
RUN npm install -g angular-cli
WORKDIR /opt/src/eventer
RUN ng build
RUN cp -r dist/* /usr/share/nginx/html
