@echo off
REM ********   PODESAVANJA - MENJA STUDENT *********
set JCUP_HOME="/home/bogdan/Downloads/java_cup_v10k"
set JAVA_HOME="/home/bogdan/java/jdk-25.0.1"
set PARSER_CLASS_NAME="MPParser"
set CUP_SPEC_NAME="MP_error.cup"


REM ********   POZIV JAVA CUP APLIKACIJE  ***********
echo vrednost : %JCUP_HOME%
%JAVA_HOME%\bin\java -classpath %JCUP_HOME% java_cup.Main -parser %PARSER_CLASS_NAME% -symbols sym < %CUP_SPEC_NAME%

PAUSE
