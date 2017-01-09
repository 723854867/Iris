#!/bin/bash
#############################################################
#            一个简单的启动和关闭JAVA程序的脚本             #
#                        by djyin                           #
#############################################################

##################### 一般情况下需要设置#####################
# JAVA程序的名称，不能重复 
PROG=backservice-praise
# JVM 环境
JAVA_HOME=/home/maishi/jdk1.7.0
jvmsettings=""
#jvmsettings="-server -Xms4G -Xmx4G -Xss256k -XX:PermSize=64M -XX:MaxPermSize=64M -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -XX:InitiatingHeapOccupancyPercent=80 -XX:MaxTenuringThreshold=20 -XX:+OptimizeStringConcat -XX:-RelaxAccessControlCheck  -XX:+UseCompressedOops  -XX:+HeapDumpOnOutOfMemoryError  -verbose:gc -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:gc.log -Dfile.encoding=UTF-8 -Dsun.jnu.encoding=UTF-8 -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=63770 -Dcom.sun.management.jmxremote -Djava.rmi.server.hostname=192.168.108.160 -Dcom.sun.management.jmxremote.port=9086 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false"

# 运行参数
parameters="-jar -Dconfig.path=config.properties -Dconsumer.path=consumer.properties back-praise-service-1.0.0.jar"
echo "${JAVA_HOME}/bin/java ${jvmsettings} ${parameters}"

##################### 一般情况下不需要设置####################

export JAVA_HOME=$JAVA_HOME
export PATH=$JAVA_HOME/bin:$PATH

# pid 文件，需要修改保证不同
PIDFILE=/var/run/${PROG}.pid
LOCKFILE=/var/lock/subsys/${PROG}.lock

# 载入killproc等命令
. /etc/rc.d/init.d/functions
. /etc/init.d/functions

RETVAL=0
#javaexec=java
javaexec=$JAVA_HOME/bin/java
prog=${PROG-java application}
exec="nohup $javaexec -Dpidfile=$pidfile $jvmsettings $parameters &"
pidfile=${PIDFILE-/var/run/java.pid}
lockfile=${LOCKFILE-/var/lock/subsys/java.lock}


start() {
        pidofproc -p "$pidfile" && exit 5
        echo -n $"Starting java application daemon: $prog "
        > nohup.out
        rm *.applicationContext.xml
        #daemon --pidfile="$pidfile" $exec 
        nohup $javaexec -Dpidfile=$pidfile $jvmsettings $parameters &
        echo $! > $pidfile
        echo_success
        RETVAL=$?
        echo
        [ $RETVAL -eq 0 ] && touch $lockfile
        return $RETVAL
}
stop() {
        echo -n $"Shutting down java application daemon: $prog "
        killproc -p "$pidfile" $exec
        RETVAL=$?
        echo
        [ $RETVAL -eq 0 ] && rm -f $lockfile
        return $RETVAL
}
rhstatus() {
        status -p "$pidfile" -l "$lockfile" "$prog"
}
restart() {
        stop
        start
}

case "$1" in
  start)
        start
        ;;
  stop)
        stop
        ;;
  restart)
        restart
        ;;
  reload)
        exit 3
        ;;
  force-reload)
        restart
        ;;
  status)
        rhstatus
        ;;
  condrestart|try-restart)
        rhstatus >/dev/null 2>&1 || exit 0
        restart
        ;;
  *)
        echo $"Usage: $0 {start|stop|restart|status}"
        exit 3
esac

exit $?
