FROM alpine:3.22.1
USER root
RUN apk update
RUN apk add --no-cache openssh-client
RUN apk add sshpass
CMD sshpass -p $SSH_PASS ssh -N -g -p $SSH_PORT \
    -L $LOCAL_PORT_NGINX:localhost:$REMOTE_PORT_NGINX \
    -L $LOCAL_PORT_WILDFLY:localhost:$REMOTE_PORT_WILDFLY \
    -L $LOCAL_PORT_WILDFLY_MANAGEMENT:localhost:$REMOTE_PORT_WILDFLY_MANAGEMENT \
    -o StrictHostKeyChecking=no $SSH_USER@$SSH_HOST