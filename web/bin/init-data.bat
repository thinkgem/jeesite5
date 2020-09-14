@echo off
rem /**
rem  * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
rem  *
rem  * Author: ThinkGem@163.com
rem  */
echo.
echo [信息] 初始化数据库，请谨慎操作。
echo.
pause
echo.
echo [信息] 为安全期间，请先备份 application.yml 里设置的数据库。
echo [信息] 此操作可能会清空您的现有数据表，并恢复初始状态。
echo [信息] 若提示，表已存在，请先删除数据表后再操作。
echo.
echo [信息] 确认继续吗？否则请关闭窗口。（5）
echo.
pause
echo.
echo [信息] 您真的确认继续吗？否则请关闭窗口。（4）
echo.
pause
echo.
echo [信息] 您真的确认继续吗？否则请关闭窗口。（3）
echo.
pause
echo.
echo [信息] 您真的确认继续吗？否则请关闭窗口。（2）
echo.
pause
echo.
echo [信息] 您真的确认继续吗？否则请关闭窗口。（1）
echo.
pause
echo.

%~d0
cd %~dp0

cd ..
call mvn test -Dmaven.test.skip=false -Dtest=com.jeesite.test.InitCoreData -Djeesite.initdata=true -U

pause