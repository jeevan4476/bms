#!/bin/bash
# Set Java 21 which is required for this project
export JAVA_HOME=/usr/lib/jvm/java-21-openjdk

# Check if port 8080 is already in use
PID=$(lsof -t -i:8080)
if [ -n "$PID" ]; then
    echo "Port 8080 is already in use by process $PID. Stopping it..."
    kill -9 $PID
    sleep 1
fi

# Run the application
# If no arguments are passed, default to spring-boot:run
if [ $# -eq 0 ]; then
    echo "Starting BMS Application..."
    exec "$(dirname "$0")/mvnw" spring-boot:run
else
    exec "$(dirname "$0")/mvnw" "$@"
fi
