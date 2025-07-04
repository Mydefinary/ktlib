#!/bin/bash

GATEWAY_URL="http://48.214.162.91:8080"
ENDPOINTS=(
    "/actuator/health"
    "/auth/register/user"
)

for EP in "${ENDPOINTS[@]}"; do
    echo ">> Checking ${GATEWAY_URL}${EP} ..."
    curl -v "${GATEWAY_URL}${EP}"
    echo -e "\n----------------------------------------\n"
done
