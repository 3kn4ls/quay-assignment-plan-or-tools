#!/bin/bash
set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

IMAGE_NAME="quay-solver:latest"

echo -e "${YELLOW}🔨 Building Docker image: ${IMAGE_NAME}${NC}"
echo ""

docker build -t ${IMAGE_NAME} .

echo ""
echo -e "${GREEN}✅ Image built successfully: ${IMAGE_NAME}${NC}"
