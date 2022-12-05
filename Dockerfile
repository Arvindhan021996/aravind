ARG AIML_ROOT_CONTAINER_IMAGE=registry.gitlab.com/iron-mountain1/gto/commercial-platforms/insight2.0/aiml/app-stack/aiml-slim-docker-images/jre14:1.0.13

FROM $AIML_ROOT_CONTAINER_IMAGE

COPY build/libs/*.jar /home/irmuser

EXPOSE 8080

CMD java -cp /home/irmuser/CoreServicesApiAutomation-*.jar org.junit.runner.JUnitCore com.userApi.Runner.RunnerTest
