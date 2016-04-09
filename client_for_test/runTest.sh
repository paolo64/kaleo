#!/bin/sh
set -x
DATA_FILE=data.json
curl localhost:9000/geturldata --data-binary @${DATA_FILE} -H 'Content-type:application/json; charset=utf-8' 
