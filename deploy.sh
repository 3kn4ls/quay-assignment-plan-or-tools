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

echo -e "${YELLOW}📦 Deploying ${IMAGE_NAME} to k3s cluster${NC}"
echo ""

# Export image to tar
echo -e "${BLUE}📤 Exporting Docker image...${NC}"
docker save ${IMAGE_NAME} -o ${TMP_TAR}

# Import to k3s
echo -e "${BLUE}📥 Importing image to k3s...${NC}"
sudo k3s ctr images import ${TMP_TAR}

# Clean up tar file
echo -e "${BLUE}🧹 Cleaning up temporary files...${NC}"
rm -f ${TMP_TAR}

# Restart pods to use new image
echo -e "${BLUE}🔄 Restarting pods in namespace ${NAMESPACE}...${NC}"
kubectl delete pod -n ${NAMESPACE} --all

# Wait for pod to be ready
echo -e "${BLUE}⏳ Waiting for pod to be ready...${NC}"
kubectl wait --for=condition=ready pod -l app.kubernetes.io/name=quay-solver -n ${NAMESPACE} --timeout=60s

echo ""
echo -e "${GREEN}✅ Deployment completed successfully!${NC}"
echo ""
echo -e "${BLUE}📊 Pod status:${NC}"
kubectl get pods -n ${NAMESPACE}
