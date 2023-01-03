FROM openjdk:17
COPY ./out/production/parser_backend_api/ /tmp
WORKDIR /tmp
ENTRYPOINT ["java","parser_backend_api"]