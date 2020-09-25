#!/bin/bash
mvn -Dtest=GenerateSwagger -Dtest-profile=GenerateSwagger test && rm -rf angular &&
java -jar ./swagger-codegen-cli.jar generate -i swagger.json -l typescript-angular -o angular