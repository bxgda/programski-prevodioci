@echo off
echo ===== DEBUG JAVA CUP =====

REM 
set JCUP_HOME=C:\Users\aleks\OneDrive\Desktop\Fakultet\4. godina\7. Semestar\Programski prevodioci\Lab03\java_cup_v10k
set JAVA_HOME=C:\Program Files\Java\jdk-1.8
set PARSER_CLASS_NAME=parser_err
set CUP_SPEC_NAME=parser_err.cup

echo.
echo [1] Provera JAVA_HOME
if not exist "%JAVA_HOME%\bin\java.exe" (
    echo GRESKA: java.exe nije pronadjen
    goto kraj
)
echo OK: java.exe postoji

echo.
echo [2] Provera JCUP_HOME
if not exist "%JCUP_HOME%" (
    echo GRESKA: JCUP_HOME folder ne postoji
    goto kraj
)
echo OK: JCUP_HOME postoji

echo.
echo [3] Provera Main.class
if not exist "%JCUP_HOME%\java_cup\Main.class" (
    echo GRESKA: java_cup\Main.class NIJE pronadjen
    goto kraj
)
echo OK: Main.class postoji

echo.
echo [4] Provera CUP fajla
if not exist "%CUP_SPEC_NAME%" (
    echo GRESKA: %CUP_SPEC_NAME% nije pronadjen u trenutnom folderu
    goto kraj
)
echo OK: CUP fajl postoji

echo.
echo [5] Pokretanje Java CUP-a
"%JAVA_HOME%\bin\java" -classpath "%JCUP_HOME%" java_cup.Main ^
    -parser %PARSER_CLASS_NAME% -symbols sym < %CUP_SPEC_NAME%

:kraj
echo.
pause
