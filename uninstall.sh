#!/bin/bash

# Colors for output
RED='\033[0;31m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

NAMESPACE="quay-solver"

echo ""
echo -e "${YELLOW}⚠️  WARNING: This will remove the entire deployment!${NC}"
echo -e "${YELLOW}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo ""
echo "This will delete:"
echo "  - Namespace: ${NAMESPACE}"
echo "  - All pods, services, ingress"
echo "  - PersistentVolumeClaim (data will be lost!)"
echo "  - Middleware configuration"
echo ""
read -p "Are you sure you want to continue? (type 'yes' to confirm): " confirm

if [ "$confirm" != "yes" ]; then
    echo ""
    echo -e "${BLUE}Cancelled. No changes made.${NC}"
    echo ""
    exit 0
fi

echo ""
echo -e "${RED}🗑️  Deleting all resources in namespace ${NAMESPACE}...${NC}"
echo ""

kubectl delete namespace ${NAMESPACE}

echo ""
echo -e "${BLUE}✅ Uninstall complete.${NC}"
echo ""
echo "Note: The Docker image 'quay-solver:latest' is still on your system."
echo "To remove it, run: docker rmi quay-solver:latest"
echo ""
