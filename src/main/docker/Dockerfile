FROM java:8
ADD deliveryorder-web.jar app.jar
ADD healthcheck.sh healthcheck.sh
RUN bash -c 'chmod +x /healthcheck.sh'
RUN bash -c 'touch /app.jar'
ENTRYPOINT ["/bin/bash", "/healthcheck.sh"]