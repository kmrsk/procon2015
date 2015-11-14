@echo off
mkdir out
for /f "tokens=1,2,3" %%i in (list.txt) do (
    echo %%i
    node puyosimu.js %%j %%k > out\%%i.log
)