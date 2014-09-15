@echo off
java -cp %~dp0/lib/ecj-4.4.jar;%~dp0/bin jasmCompiler.Compiler %1 %2