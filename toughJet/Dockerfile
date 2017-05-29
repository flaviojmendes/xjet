FROM frolvlad/alpine-oraclejdk8:slim
ENTRYPOINT ["java","-agentpath:/dynatrace/agent/lib64/libdtagent.so=name=ToughJet_XJET,server=dtserver.dojoapm_default:9998","-Djava.security.egd=file:/dev/./urandom","-jar","/app/build/libs/toughJet-1.0-SNAPSHOT.jar"]
