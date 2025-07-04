# kubenetes deploy 자동화 코드
# 1) 실행 권한 부여
# chmod +x smart_redeploy.sh  
# 2) 사용 - publication 배
# ./smart_redeploy.sh publication
# ./smart_redeploy.sh gateway


#!/bin/bash

# 색상
GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# 서비스 이름
SERVICE=${1:-gateway}  # 기본값 publication
IMAGE_NAME="kt16project.azurecr.io/${SERVICE}"
TAG=$(date +"%m%d%H%M")
DEPLOYMENT_NAME=${SERVICE}
K8S_PATH="kubernetes"

echo -e "${GREEN}🚀 [1] ${SERVICE} 서비스 최신 코드 빌드 시작...${NC}"

cd ${SERVICE} || { echo -e "${RED}❌ ${SERVICE} 디렉토리 없음. 종료${NC}"; exit 1; }

if mvn package -B -DskipTests; then
    echo -e "${GREEN}✅ Maven 패키징 완료${NC}"
else
    echo -e "${RED}❌ Maven 빌드 실패. build_error.log 확인${NC}"
    mvn package -B -DskipTests > build_error.log 2>&1
    exit 1
fi

echo -e "${GREEN}🚀 [2] Docker 이미지 빌드: ${IMAGE_NAME}:${TAG}${NC}"
if docker build -t ${IMAGE_NAME}:${TAG} .; then
    echo -e "${GREEN}✅ Docker 이미지 빌드 완료${NC}"
else
    echo -e "${RED}❌ Docker 빌드 실패. 스크립트 종료${NC}"
    exit 1
fi

echo -e "${GREEN}🚀 [3] Docker 이미지 ACR 푸시 중...${NC}"
if docker push ${IMAGE_NAME}:${TAG}; then
    echo -e "${GREEN}✅ Docker 이미지 푸시 완료${NC}"
else
    echo -e "${RED}❌ Docker 푸시 실패. 스크립트 종료${NC}"
    exit 1
fi

echo -e "${GREEN}🚀 [4] Kubernetes ${DEPLOYMENT_NAME} 배포 갱신 중...${NC}"

# deployment.yaml 의 image TAG 갱신
echo -e "${GREEN}🔄 deployment.yaml 내 이미지 TAG 업데이트 중...${NC}"
sed -i "s#\(image: ${IMAGE_NAME}:\).*#\1${TAG}#g" ${K8S_PATH}/deployment.yaml

kubectl apply -f ${K8S_PATH}/deployment.yaml
kubectl apply -f ${K8S_PATH}/service.yaml

echo -e "${GREEN}🔄 [5] ${DEPLOYMENT_NAME} Deployment 재시작 중...${NC}"
kubectl rollout restart deployment ${DEPLOYMENT_NAME}

echo -e "${GREEN}⏳ [6] 배포 진행 상태 모니터링...${NC}"
kubectl rollout status deployment ${DEPLOYMENT_NAME}

echo -e "${GREEN}📋 [7] 현재 Pod 상태:${NC}"
kubectl get pods -o wide | grep ${DEPLOYMENT_NAME}

POD_NAME=$(kubectl get pods --no-headers -o custom-columns=":metadata.name" | grep ${DEPLOYMENT_NAME} | head -n 1)

echo -e "${GREEN}🪵 [8] 실시간 로그 확인 시작...${NC}"
kubectl logs -f ${POD_NAME}

echo -e "${GREEN}✅ [완료] ${SERVICE} 최신 코드 → 이미지 → 배포까지 자동 처리 완료${NC}"

# (옵션) Slack/Discord 알림 연동
# curl -X POST -H "Content-Type: application/json" -d "{\"content\": \"✅ ${SERVICE} ${TAG} 배포 완료\"}" https://discord.com/api/webhooks/....

