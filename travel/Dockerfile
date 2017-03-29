FROM frolvlad/alpine-oraclejdk8:slim
ENTRYPOINT ["java","-agentpath:/app/dynatrace/agent/lib64/libdtagent.so=name=JavaApplication_Java_XJet,server=dtserver.dojoapm_default:9998","-Djava.security.egd=file:/dev/./urandom","-jar","/app/build/libs/xjet-1.0-SNAPSHOT.jar"]
