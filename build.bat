@echo off
echo Building project...

if not exist bin mkdir bin

REM Correctly compile with the JUnit jar in classpath
javac -cp "lib\junit-platform-console-standalone-1.10.2.jar" -d bin src\*.java test\*.java

if %errorlevel% neq 0 (
    echo Compilation failed.
    exit /b %errorlevel%
)

echo Build successful.

cd .\bin
java Main
