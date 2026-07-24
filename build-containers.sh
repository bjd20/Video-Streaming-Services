#!/bin/bash
set -e

# All services defined in the project
ALL_SERVICES=("eureka-server" "api-gateway" "account-service" "video-service")

SERVICES_TO_BUILD=()

if [ "$#" -eq 0 ]; then
  echo "No services specified. Building all services."
  SERVICES_TO_BUILD=("${ALL_SERVICES[@]}")
else
  # Use $* for echo to print as a single string
  echo "Building specified services: $*"
  SERVICES_TO_BUILD=("$@")
fi

# Rebuild each specified service
for service in "${SERVICES_TO_BUILD[@]}"; do
  echo "Building $service..."
  if [ -d "$service" ]; then
    (cd "$service" && ./gradlew clean build -x test)
  else
    echo "Warning: Service directory '$service' not found. Skipping build."
  fi
done

# Rebuild docker images and start/restart containers
if [ "$#" -eq 0 ]; then
  echo "Rebuilding and starting all Docker containers..."
  docker-compose down
  docker-compose up --build -d
else
  # Use [*] for echo to print as a single string
  echo "Rebuilding and restarting specified Docker containers: ${SERVICES_TO_BUILD[*]}"
  # Use [@] for docker-compose to pass as separate arguments
  docker-compose up --build -d "${SERVICES_TO_BUILD[@]}"
fi

# Check all containers
echo "Current container status:"
docker-compose ps
