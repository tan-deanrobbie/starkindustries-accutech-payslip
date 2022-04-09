FROM openjdk:8

ARG VERSION

ADD target/payslip-$VERSION.jar payslip.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "payslip.jar"]