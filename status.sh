#!/bin/bash

# Colors for output
BLUE='\033[0;34m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

NAMESPACE="quay-solver"

echo ""
echo -e "${YELLOW}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo -e "${YELLOW}  📊 Quay Solver Deployment Status${NC}"
echo -e "${YELLOW}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo ""

echo -e "${BLUE}🔹 Pods:${NC}"
kubectl get pods -n ${NAMESPACE}
echo ""

echo -e "${BLUE}🔹 Services:${NC}"
kubectl get svc -n ${NAMESPACE}
echo ""

echo -e "${BLUE}🔹 Ingress:${NC}"
kubectl get ingress -n ${NAMESPACE}
echo ""

echo -e "${BLUE}🔹 PersistentVolumeClaims:${NC}"
kubectl get pvc -n ${NAMESPACE}
echo ""

echo -e "${GREEN}🌐 Access URL:${NC} https://northr3nd.duckdns.org/quay-assignment-plan-or-tools"
echo ""
