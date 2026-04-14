#!/bin/bash
# Convenience wrapper to always use Java 21 for this project
export JAVA_HOME=/usr/lib/jvm/java-21-openjdk
exec "$(dirname "$0")/mvnw" "$@"
