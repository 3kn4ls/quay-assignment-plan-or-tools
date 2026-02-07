#!/bin/bash

# Colors for output
BLUE='\033[0;34m'
NC='\033[0m' # No Color

NAMESPACE="quay-solver"

# Get pod name
POD=$(kubectl get pods -n ${NAMESPACE} -o name | head -1 | sed 's/pod\///')

if [ -z "$POD" ]; then
    echo "No pods found in namespace ${NAMESPACE}"
    exit 1
fi

echo -e "${BLUE}📋 Showing logs for pod: ${POD}${NC}"
echo ""

# Follow logs with tail
kubectl logs -n ${NAMESPACE} ${POD} -f --tail=50
