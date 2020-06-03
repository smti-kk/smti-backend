#!/bin/sh

while ! nc -z db 5432; do
    echo "Waiting for PostgreSql to start..."
    sleep 1
done

while ! nc -z redis 6379; do
    echo "Waiting for Redis to start..."
    sleep 1
done

exec java -cp app:app/lib/* ru.cifrak.telecomit.backend.BackendApplication
