#!/bin/bash

# Wait for PostgreSQL to be ready
echo "Waiting for PostgreSQL..."
until curl -s "$DATABASE_URL" > /dev/null 2>&1; do
  echo "PostgreSQL is unavailable - sleeping"
  sleep 1
done
echo "PostgreSQL is up - executing application"

# Start the application with production profile
exec java -jar \
    -Dspring.profiles.active=prod \
    -Dserver.port=${PORT:-8080} \
    -Dspring.datasource.url=${DATABASE_URL} \
    -Dbot.token=${BOT_TOKEN} \
    -Dbot.name=${BOT_NAME} \
    /app/app.jar 