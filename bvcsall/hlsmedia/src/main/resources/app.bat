@echo off
if not defined JAVA_HOME (
  set JAVA_HOME=../jdk1.6.0
  set JAVA_HOME=D:/java/jdk1.6.0
)
set jvmsettings=-XX:+HeapDumpOnOutOfMemoryError -verbose:gc -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:gc.log -Dfile.encoding=UTF-8 -Dsun.jnu.encoding=UTF-8 -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=9086 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false

set parameters=-jar server.jar -Dctxconfig server.properties -Dlogback logback.xml

setlocal enabledelayedexpansion

set cmdable=%JAVA_HOME%/bin/java %jvmsettings% %parameters%
echo "#################################################################"
echo %cmdable%
echo "#################################################################"
%cmdable%

endlocal