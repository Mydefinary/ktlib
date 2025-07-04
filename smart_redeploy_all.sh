#!/bin/bash

# ==================================
# smart_redeploy_all.sh
# 모든 서비스 자동 빌드 -> ACR 푸시 -> 롤링 배포 -> 로그 확인
# ==================================

# [1] 빌드할 서비스 목록
SERVICES=(author user point book manuscript periodsubscribe subscribe publication frontend gateway)

# [2] 공통 변수
ACR_NAME=kt16project
ACR_URL=${ACR_NAME}.azurecr.io
timestamp=$(date +%m%d%H%M)

# [3] 현재 스크립트 위치 기준 루트 디렉토리로 이동
cd "$(dirname "$0")"
ROOT_DIR=$(pwd)

for SERVICE in "${SERVICES[@]}"; do
    echo "=============================="
    echo "🚀 [${SERVICE}] 빌드 및 배포 시작"
    echo "=============================="

    SERVICE_DIR="${ROOT_DIR}/${SERVICE}"

    if [ ! -d "$SERVICE_DIR" ]; then
        echo "❌ [${SERVICE}] 디렉토리 없음, 스킵합니다."
        continue
    fi

    cd "$SERVICE_DIR"

    # 1) Maven Build
    echo "🔨 [${SERVICE}] Maven 빌드 중..."
    mvn package -B -DskipTests
    if [ $? -ne 0 ]; then
        echo "❌ [${SERVICE}] Maven 빌드 실패, 스킵합니다."
        cd "$ROOT_DIR"
        continue
    fi

    # 2) Docker Build & Push
    IMAGE_NAME="${ACR_URL}/${SERVICE}:${timestamp}"
    echo "🐳 [${SERVICE}] Docker 이미지 빌드 중..."
    docker build -t "$IMAGE_NAME" .

    echo "📦 [${SERVICE}] Docker 이미지 ACR 푸시 중..."
    az acr login --name "$ACR_NAME"
    docker push "$IMAGE_NAME"

    # 3) Kubernetes Rolling Update
    echo "🚀 [${SERVICE}] Kubernetes 롤링 배포 중..."
    kubectl set image deployment/${SERVICE} ${SERVICE}=${IMAGE_NAME} --record

    echo "✅ [${SERVICE}] 배포 완료"
    cd "$ROOT_DIR"

done

echo "🎉 모든 서비스 빌드 및 배포 완료"