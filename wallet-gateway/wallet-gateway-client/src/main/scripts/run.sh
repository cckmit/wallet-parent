#! /bin/bash

# @author zengfucheng
# @date 2018-03-14

BASEDIR=`dirname "$0"`/..
BASEDIR=`(cd "$BASEDIR"; pwd)`
ls -l "$0" | grep -e '->' > /dev/null 2>&1
if [ $? = 0 ]; then
  #this is softlink
  _PWD=`pwd`
  _EXEDIR=`dirname "$0"`
  cd "$_EXEDIR"
  _BASENAME=`basename "$0"`
  _REALFILE=`ls -l "$_BASENAME" | sed 's/.*->\ //g'`
   BASEDIR=`dirname "$_REALFILE"`/..
   BASEDIR=`(cd "$BASEDIR"; pwd)`
   cd "$_PWD"
fi

export LANG=zh_CN.UTF-8
## VERSION="`pwd|rev|awk -F \/ '{print $2}'|rev`";
APP_NAME="${app.name}"
APP_VERSION="${deploy.version}"
ENV_PROFILE="@spring.profile@"
MAIN_CLASS="org.wallet.gateway.client.run.ServiceStarter"
CLASS_PATH="$BASEDIR/conf:$BASEDIR/lib/$APP_NAME-$APP_VERSION.jar"
export CLASSPATH=$CLASS_PATH:$CLASSPATH

JMXREMOTE_PORT=1100
LOG_PATH=/var/log/java/$APP_NAME;
JAVA_OPTS="$JAVA_OPTS -Dspring.profiles.active=$ENV_PROFILE -Dorg.wallet.service.port=/tmp/$APP_NAME"
JAVA_OPTS="$JAVA_OPTS -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=$JMXREMOTE_PORT -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false";
JAVA_OPTS="$JAVA_OPTS -XX:+PrintGC -XX:+PrintGCTimeStamps -XX:+PrintGCDetails -Xloggc:$LOG_PATH/gc.log -Ddubbo.shutdown.hook=true -Ddubbo.service.shutdown.wait=30000";
JAVA_OPTS="$JAVA_OPTS -XX:+UseG1GC -XX:G1HeapRegionSize=16m -XX:G1ReservePercent=10 -XX:ParallelGCThreads=8 -XX:ConcGCThreads=8 -XX:-UseGCOverheadLimit -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=$LOG_PATH/heapdump.hprof";
START_CMD="$JAVA_HOME/bin/java -server -d64 $JAVA_OPTS $MAIN_CLASS $APP_NAME start";
STOP_CMD="$JAVA_HOME/bin/java -Dorg.wallet.service.port=/tmp/$APP_NAME $MAIN_CLASS $APP_NAME stop";

check_port() {
	port_count=`echo -n | telnet 127.0.0.1 "$JMXREMOTE_PORT" | grep Connected | wc -l`;
	if [ $port_count -gt 0 ]; then
		echo "[ERROR]: The port [$JMXREMOTE_PORT] has been used!!";
		exit 0;	
	fi
}

start_() {
	pids_count=`ps -ef|grep "$MAIN_CLASS" |grep "$JMXREMOTE_PORT" | grep -v "grep" | wc -l`
	if [ $pids_count -eq 0 ]; then
		check_port;
		touch $BASEDIR/conf/flag;
		nohup $START_CMD 1>/dev/null 2>&1 &
		while [ true ]
		do
			success=`head -1 $BASEDIR/conf/flag`;
			if [ "$success" == "0" ]; then
				printf "\n";
				printf "[INFO]: Service[$APP_NAME] has started successfully !!\n"
				printf "[INFO]: For more information, please see $LOG_PATH/out.log !!\n"
				rm -rf $BASEDIR/conf/flag;
				break;
			elif [ "$success" == "-1" ]; then
				printf "\n";
				printf "[INFO]: Service[$APP_NAME] startup failed!\n"
				printf "[INFO]: For more information, please see $LOG_PATH/error/out_error.log !!\n"
				rm -rf $BASEDIR/conf/flag;
				break;
			fi
			counter=`expr $counter + 1`
			printf "."
			sleep 0.5
		done
	else
		echo "[ERROR]: The application[$APP_NAME] is running, does not started again !!"
		exit 0
	fi
}

stop_() {
	pids=`ps -ef|grep "$MAIN_CLASS" |grep "$JMXREMOTE_PORT" |grep -v "grep" | awk '{print $2}'`
	if [ "$pids" == "" ]; then
		echo "[INFO]: Application[$APP_NAME] does not started !!"
	else
		touch $BASEDIR/conf/flag;
		nohup $STOP_CMD 1>/dev/null 2>&1 &
		while [ true ]
		do
			success=`head -1 $BASEDIR/conf/flag`;
			if [ "$success" == "0" ]; then
				printf "\n";
				printf "[INFO]: Service[$APP_NAME] has been closed !!\n"
				rm -rf $BASEDIR/conf/flag;
				break;
			elif [ "$success" == "-1" ]; then
				printf "\n";
				printf "[INFO]: Service[$APP_NAME] close failed !!\n"
				printf "[INFO]: For more information, please see $LOG_PATH/error/out_error.log !!\n"
				rm -rf $BASEDIR/conf/flag;
				break;
				exit 0;
			fi
			counter=`expr $counter + 1`
			printf "."
			sleep 0.5
		done
	fi
}

restart_() {
	stop_;
        total=6
        counter=0
        while [ $counter -le 6 ]
        do
            printf ".";
            sleep 0.5;
            counter=`expr $counter + 1`;
        done
        start_;
}

if [ "$1" == "start" ]; then
    start_
elif [ "$1" == "stop" ]; then
	stop_
elif [ "$1" == "restart" ]; then
	restart_
else
	read -p "input [start|stop|restart] :" action
	if [ "$action" == "start" ]; then
		start_
	elif [ "$action" == "stop" ]; then
		stop_
	elif [ "$action" == "restart" ]; then
		restart_
	else
		echo "[ERROR]: Invalid input..."
		exit 0
	fi
fi