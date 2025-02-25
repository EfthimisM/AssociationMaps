@echo off
setlocal

set JAR_PATH=Libs\Stemmer.jar
set GROUP_ID=mitos.stemmer
set ARTIFACT_ID=stemmer
set VERSION=1.0
set PACKAGING=jar

call mvnw install:install-file ^
    -Dfile=%JAR_PATH% ^
    -DgroupId=%GROUP_ID% ^
    -DartifactId=%ARTIFACT_ID% ^
    -Dversion=%VERSION% ^
    -Dpackaging=%PACKAGING%

endlocal

