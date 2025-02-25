#!/bin/bash

./mvnw install:install-file \
    -Dfile=Libs/Stemmer.jar \
    -DgroupId=mitos.stemmer \
    -DartifactId=stemmer \
    -Dversion=1.0 \
    -Dpackaging=jar
