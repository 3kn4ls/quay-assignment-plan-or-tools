#!/bin/bash
set -e

# Colors for output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo ""
echo -e "${YELLOW}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo -e "${YELLOW}  📋 Applying Kubernetes Configurations${NC}"
echo -e "${YELLOW}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo ""

echo -e "${BLUE}Applying configurations in order...${NC}"
echo ""

echo -e "${BLUE}[1/6]${NC} Namespace"
kubectl apply -f k8s/namespace.yaml

echo -e "${BLUE}[2/6]${NC} PersistentVolumeClaim"
kubectl apply -f k8s/pvc.yaml

echo -e "${BLUE}[3/6]${NC} Middleware"
kubectl apply -f k8s/middleware.yaml

echo -e "${BLUE}[4/6]${NC} Deployment"
kubectl apply -f k8s/deployment.yaml

echo -e "${BLUE}[5/6]${NC} Service"
kubectl apply -f k8s/service.yaml

echo -e "${BLUE}[6/6]${NC} Ingress"
kubectl apply -f k8s/ingress.yaml

echo ""
echo -e "${GREEN}✅ All configurations applied successfully!${NC}"
echo ""
echo -e "${BLUE}Current status:${NC}"
kubectl get all,ingress,middleware,pvc -n quay-solver
echo ""
