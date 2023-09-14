#!/usr/bin/env bash

openssl genpkey -algorithm RSA -out private_key.pem
openssl pkcs8 -topk8 -inform PEM -outform PEM -in private_key.pem -out private_key_pkcs8.pem -nocrypt
openssl rsa -pubout -in private_key_pkcs8.pem -out public_key.pem
mv private_key.pem app.key
mv public_key.pem app.pub

