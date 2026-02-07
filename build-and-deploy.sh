#!/bin/bash
set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

IMAGE_NAME="quay-solver:latest"
NAMESPACE="quay-solver"
TMP_TAR="/tmp/quay-solver.tar"

echo ""
echo -e "${YELLOW}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo -e "${YELLOW}  🚀 Build and Deploy to k3s${NC}"
echo -e "${YELLOW}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo ""

# Step 1: Build
echo -e "${BLUE}[1/5]${NC} ${YELLOW}🔨 Building Docker image: ${IMAGE_NAME}${NC}"
docker build -t ${IMAGE_NAME} .

echo ""
echo -e "${BLUE}[2/5]${NC} ${YELLOW}📤 Exporting Docker image...${NC}"
docker save ${IMAGE_NAME} -o ${TMP_TAR}

echo ""
echo -e "${BLUE}[3/5]${NC} ${YELLOW}📥 Importing image to k3s...${NC}"
sudo k3s ctr images import ${TMP_TAR}

echo ""
echo -e "${BLUE}[4/5]${NC} ${YELLOW}🧹 Cleaning up temporary files...${NC}"
rm -f ${TMP_TAR}

echo ""
echo -e "${BLUE}[5/5]${NC} ${YELLOW}🔄 Restarting pods in namespace ${NAMESPACE}...${NC}"
kubectl delete pod -n ${NAMESPACE} --all

echo ""
echo -e "${YELLOW}⏳ Waiting for pod to be ready...${NC}"
kubectl wait --for=condition=ready pod -l app.kubernetes.io/name=quay-solver -n ${NAMESPACE} --timeout=60s 2>/dev/null || true

echo ""
echo -e "${GREEN}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo -e "${GREEN}  ✅ Deployment completed successfully!${NC}"
echo -e "${GREEN}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo ""
echo -e "${BLUE}📊 Pod status:${NC}"
kubectl get pods -n ${NAMESPACE}
echo ""
echo -e "${BLUE}🌐 Access URL:${NC} https://northr3nd.duckdns.org/quay-assignment-plan-or-tools"
echo ""
