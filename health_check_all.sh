#!/bin/bash

# ==============================
# health_check_all.sh
# 모든 서비스 /actuator/health Gateway 통해 200 OK 확인 스크립트
# ==============================

# [1] 변수 설정
GATEWAY_URL="http://<GATEWAY-EXTERNAL-IP>:8080"  # gateway 외부 IP 및 포트
SERVICES=("author" "user" "point" "book" "manuscript" "periodsubscribe" "subscribe" "publication" "frontend")
JWT_TOKEN="<YOUR_VALID_JWT_TOKEN>"

# [2] 점검 시작
echo "🔍 Gateway를 통한 /actuator/health 점검 시작..."

for SERVICE in "${SERVICES[@]}"; do
    echo "-----------------------------------------"
    echo "🔹 [${SERVICE}] 점검 중..."
    URL="${GATEWAY_URL}/${SERVICE}/actuator/health"

    # JWT 헤더 포함 요청
    HTTP_STATUS=$(curl -s -o response_${SERVICE}.json -w "%{http_code}" -H "Authorization: Bearer ${JWT_TOKEN}" "$URL")

    if [ "$HTTP_STATUS" == "200" ]; then
        STATUS=$(jq -r '.status' response_${SERVICE}.json)
        echo "✅ [${SERVICE}] ${URL} -> HTTP 200 (${STATUS})"
    else
        echo "❌ [${SERVICE}] ${URL} -> HTTP ${HTTP_STATUS}"
        cat response_${SERVICE}.json
    fi

    rm response_${SERVICE}.json
done

echo "✅ 점검 완료."

