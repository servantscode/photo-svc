#Dockerfile

FROM servantscode/tomcat-elk-logging

LABEL maintainer="greg@servantscode.org"

COPY ./build/libs/photo-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war
