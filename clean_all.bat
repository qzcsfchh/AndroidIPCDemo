@echo off
color 2f
mode con:cols=80 lines=25

for /r . %%a in (.) do (
if exist "%%a\build" rd /s /q "%%a\build" 

if exist "%%a\*.iml" del /s /q "%%a\*.iml"

if exist "%%a\local.properties" del /s /q "%%a\local.properties"

if exist "%%a\.gradle" rd /s /q "%%a\.gradle"

if exist "%%a\.idea" rd /s /q "%%a\.idea"

)

@echo clean finished
@pause