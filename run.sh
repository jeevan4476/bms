#!/bin/bash
export JAVA_HOME=/usr/lib/jvm/java-21-openjdk
exec "$(dirname "$0")/mvnw" "$@"
