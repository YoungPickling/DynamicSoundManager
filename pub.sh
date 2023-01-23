#!/bin/bash

rm -r maven

./gradlew publish

scp -r maven/* ubuntu@wandhoven.ddns.net:/media/B/html/maven/

rm -r maven
