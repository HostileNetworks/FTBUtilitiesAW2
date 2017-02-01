pushd %~dp0
call gradlew clean
call gradlew setupDecompWorkspace
call gradlew eclipse
pause