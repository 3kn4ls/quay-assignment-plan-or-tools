# Guía Rápida de Despliegue

## 🎯 Lo Más Rápido

```bash
# Actualizar aplicación después de cambios
./update.sh

# O usar el menú interactivo
./manage.sh
```

## 📋 Comandos Esenciales

### Actualizar la aplicación
```bash
./update.sh
# o
./build-and-deploy.sh
```

### Ver estado
```bash
./status.sh
```

### Ver logs
```bash
./logs.sh
```

## 🆕 Primera Instalación

Si es la primera vez que despliegas:

```bash
# 1. Construir imagen
./build.sh

# 2. Aplicar configuraciones de Kubernetes
./k8s-apply.sh

# 3. Desplegar la imagen
./deploy.sh

# 4. Verificar que todo funciona
./status.sh
```

## 📚 Documentación Completa

Para más detalles sobre todos los scripts disponibles, consulta:
- [SCRIPTS.md](SCRIPTS.md) - Documentación completa de todos los scripts

## 🌐 Acceso

Una vez desplegado, la aplicación está disponible en:

**https://northr3nd.duckdns.org/quay-assignment-plan-or-tools**

## ❓ Problemas Comunes

### El pod no inicia
```bash
./logs.sh  # Ver qué está pasando
```

### Cambié los yamls de k8s
```bash
./k8s-apply.sh  # Aplicar los cambios
```

### La imagen no se actualiza
```bash
./build-and-deploy.sh  # Reconstruir y desplegar
```

### Quiero empezar de cero
```bash
./uninstall.sh  # Eliminar todo
# Luego seguir los pasos de "Primera Instalación"
```
