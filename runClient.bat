@echo off

REM 默认 Java 地址
set JAVA_HOME=C:\Program Files\Java\jdk-8

start cmd /k gradlew runClient
