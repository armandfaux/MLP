@echo off
echo Running tests...

REM Run tests with JUnit standalone
java -jar lib\junit-platform-console-standalone-1.10.2.jar --class-path bin --scan-class-path

pause
