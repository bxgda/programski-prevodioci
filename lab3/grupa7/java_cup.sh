#!/bin/bash

JCUP_HOME="$HOME/Downloads/java_cup_v10k"
PARSER_CLASS_NAME="MPParser"
CUP_SPEC_NAME="MP_error.cup"
PROJECT_SRC="$HOME/eclipse-workspace/CUP_Analizator/src"

cd "$JCUP_HOME" || exit 1

java -cp . java_cup.Main \
  -parser "$PARSER_CLASS_NAME" \
  -symbols sym \
  < "$PROJECT_SRC/$CUP_SPEC_NAME"
