FROM adoptopenjdk/openjdk14

MAINTAINER mandeep.sidhu2@gmail.com

COPY target/tabby-0.0.1-SNAPSHOT.jar /tabby.jar

ENV DATABASE_URL=postgres://ohrflgkigxnbna:93e6ec71da1d5798028cedcb596f3470887169b7631c22bd8da7eab117e74279@ec2-50-17-197-184.compute-1.amazonaws.com:5432/db95mtqffp6g1d
ENV DB_PASSWORD=93e6ec71da1d5798028cedcb596f3470887169b7631c22bd8da7eab117e74279
ENV DB_URL=jdbc:postgresql://ec2-50-17-197-184.compute-1.amazonaws.com:5432/db95mtqffp6g1d
ENV DB_USERNAME=ohrflgkigxnbna
ENV HEROKU_POSTGRESQL_RED_URL=postgres://ypyzufgrftmnsw:bfe231b844adc9204fcdebf34aa734002c949381d7d89891de3575aec76b2d71@ec2-3-224-188-122.compute-1.amazonaws.com:5432/d9ekc6f8ssht1b
ENV REDIS_HOST_URL=ec2-54-224-171-77.compute-1.amazonaws.com
ENV REDIS_PASSWORD=pd0e3dcc09d6b839610992efb1693e3ba18ab86d2fbe380f8faecbb5bea55a084
ENV REDIS_PORT=10489
ENV SERVER_PORT=8082

ENTRYPOINT ["java","-Dserver.port=8082","-Dspring.profiles.active=prod","-jar","/tabby.jar"]

EXPOSE 8082

#docker build -t springio/tabby-spring-boot-docker .
#docker run -p 8082:8082 springio/tabby-spring-boot-docker