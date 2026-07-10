@echo off

set CP=bin;lib/*

for /R src %%f in (*.java) do (
    echo %%f >> sources.txt
)

javac -source 8 -target 8 -cp "lib/*" -d bin @sources.txt

java -cp "%CP%" desktop.src.com.ludobox.DesktopLauncher