FROM openjdk:8 as build
WORKDIR /workspace/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src
#COPY .m2 .m2

RUN ./mvnw -Dmaven.repo.local=.m2 install -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM openjdk:8
RUN apt-get update && apt-get install -y netcat
VOLUME /tmp
ARG DEPENDENCY=/workspace/app/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
ADD ./docker/run.sh /app
ADD ./docker/version /app
ADD ./docker/revision /app

EXPOSE 8080
ENTRYPOINT /app/run.sh
