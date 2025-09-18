FROM alpine:3.22.1
USER root
RUN apk update
RUN apk add --no-cache openssh-client
RUN apk add sshpass
CMD sshpass -p $SSH_PASS ssh -N -g -p $SSH_PORT -L $LOCAL_PORT_NGINX:localhost:$REMOTE_PORT_NGINX \
    -L $LOCAL_PORT_TOMCAT:localhost:$REMOTE_PORT_TOMCAT -o StrictHostKeyChecking=no $SSH_USER@$SSH_HOST