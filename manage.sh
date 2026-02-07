#!/bin/bash

# Colors for output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

clear
echo ""
echo -e "${YELLOW}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo -e "${YELLOW}  🚢 Quay Assignment Solver - Manager${NC}"
echo -e "${YELLOW}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo ""
echo -e "${CYAN}Selecciona una opción:${NC}"
echo ""
echo -e "  ${GREEN}1)${NC} 🚀 Build + Deploy (actualizar aplicación)"
echo -e "  ${GREEN}2)${NC} 🔨 Solo Build"
echo -e "  ${GREEN}3)${NC} 📦 Solo Deploy"
echo -e "  ${GREEN}4)${NC} 📊 Ver Estado"
echo -e "  ${GREEN}5)${NC} 📋 Ver Logs"
echo -e "  ${GREEN}6)${NC} 📋 Aplicar configuraciones K8s"
echo -e "  ${GREEN}7)${NC} 🗑️  Uninstall"
echo -e "  ${GREEN}0)${NC} ❌ Salir"
echo ""
read -p "Opción: " choice

case $choice in
    1)
        echo ""
        ./build-and-deploy.sh
        ;;
    2)
        echo ""
        ./build.sh
        ;;
    3)
        echo ""
        ./deploy.sh
        ;;
    4)
        echo ""
        ./status.sh
        ;;
    5)
        echo ""
        ./logs.sh
        ;;
    6)
        echo ""
        ./k8s-apply.sh
        ;;
    7)
        echo ""
        ./uninstall.sh
        ;;
    0)
        echo ""
        echo -e "${BLUE}👋 Hasta luego!${NC}"
        echo ""
        exit 0
        ;;
    *)
        echo ""
        echo -e "${YELLOW}⚠️  Opción inválida${NC}"
        echo ""
        ;;
esac
