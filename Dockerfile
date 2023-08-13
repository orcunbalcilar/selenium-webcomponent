# The base image that includes Java JDK
FROM maven:3.9.2-eclipse-temurin-17 as build

WORKDIR /home/app

# Copy the pom.xml file to download dependencies
COPY pom.xml .

# Copy your source code to the WORKDIR
COPY src ./src

# Build the application. Adjust this if your build command differs.
RUN mvn -f pom.xml clean -Dcucumber.features=src/test/resources/WebComponent.feature test