FROM jenkins/jenkins:alpine
USER root
RUN apk add docker
RUN apk add python3
RUN apk add pipx
RUN pipx install --include-deps ansible
RUN pipx ensurepath
RUN mkdir /home/root