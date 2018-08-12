#!/usr/bin/env bash

if [ -z "$BACKEND_URL" ]; then
    BACKEND_URL=""
fi
echo "BACKEND_URL is [$BACKEND_URL]"

#runtime environment configuration
sed  s@BACKEND_URL@${BACKEND_URL}@g /etc/app/docker/environment.json > /usr/share/nginx/html/assets/environment.json

/etc/app/package/entrypoint.sh
