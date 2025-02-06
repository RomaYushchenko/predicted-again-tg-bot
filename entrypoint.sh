#!/bin/bash

# Function to check if PostgreSQL is ready
check_postgres() {
    local retries=30
    local wait_time=2

    # Extract connection details from DATABASE_URL
    if [[ $DATABASE_URL =~ ^postgres[ql]?://([^:]+):([^@]+)@([^:]+):([0-9]+)/(.+)$ ]]; then
        local db_user="${BASH_REMATCH[1]}"
        local db_pass="${BASH_REMATCH[2]}"
        local db_host="${BASH_REMATCH[3]}"
        local db_port="${BASH_REMATCH[4]}"
        local db_name="${BASH_REMATCH[5]}"

        echo "Waiting for PostgreSQL to be ready..."
        
        while [ $retries -gt 0 ]; do
            if PGPASSWORD=$db_pass pg_isready -h "$db_host" -p "$db_port" -U "$db_user" -d "$db_name" > /dev/null 2>&1; then
                echo "PostgreSQL is ready!"
                return 0
            fi
            
            retries=$((retries-1))
            echo "PostgreSQL is not ready yet (remaining attempts: $retries)..."
            sleep $wait_time
        done

        echo "Could not connect to PostgreSQL after multiple attempts"
        return 1
    else
        echo "Invalid DATABASE_URL format"
        return 1
    fi
}

# Check if PostgreSQL is ready
if ! check_postgres; then
    exit 1
fi

# Start the application with production profile
exec java -jar \
    -Dspring.profiles.active=prod \
    -Dserver.port=${PORT:-8080} \
    -Dspring.datasource.url=${DATABASE_URL} \
    -Dbot.token=${BOT_TOKEN} \
    -Dbot.name=${BOT_NAME} \
    -XX:+UseContainerSupport \
    -XX:MaxRAMPercentage=75.0 \
    -Djava.security.egd=file:/dev/./urandom \
    /app/app.jar 