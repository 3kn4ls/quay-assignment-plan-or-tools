# Scripts de Despliegue

Scripts de automatización para compilar y desplegar la aplicación en el cluster k3s.

## 🎯 Inicio Rápido

```bash
# Menú interactivo (recomendado)
./manage.sh

# O directamente
./update.sh
```

## Índice de Scripts

| Script | Descripción | Uso Principal |
|--------|-------------|---------------|
| `manage.sh` | Menú interactivo | **Interfaz fácil** |
| `build.sh` | Construye imagen Docker | Solo build |
| `deploy.sh` | Despliega al cluster | Solo deploy |
| `build-and-deploy.sh` | Build + Deploy completo | **Actualización principal** |
| `update.sh` | Atajo de build-and-deploy | Actualización rápida |
| `k8s-apply.sh` | Aplica configs de k8s | Primera instalación |
| `status.sh` | Muestra estado del deployment | Verificación |
| `logs.sh` | Muestra logs en tiempo real | Debugging |
| `uninstall.sh` | Elimina el deployment completo | Limpieza |

## Scripts Disponibles

### 🔨 `build.sh`
Construye únicamente la imagen Docker.

```bash
./build.sh
```

**Cuándo usar:** Cuando solo quieres construir la imagen sin desplegarla.

---

### 📦 `deploy.sh`
Despliega la imagen Docker al cluster k3s (asume que la imagen ya está construida).

```bash
./deploy.sh
```

**Cuándo usar:** Cuando ya construiste la imagen y solo quieres actualizar el deployment.

**Qué hace:**
1. Exporta la imagen Docker a un archivo tar
2. Importa la imagen al runtime de k3s
3. Reinicia los pods para usar la nueva imagen
4. Espera a que el pod esté listo

---

### 🚀 `build-and-deploy.sh`
Script completo que construye la imagen y la despliega al cluster.

```bash
./build-and-deploy.sh
```

**Cuándo usar:** Este es el script principal para actualizar la aplicación después de hacer cambios.

**Qué hace:**
1. Construye la imagen Docker
2. Exporta la imagen a tar
3. Importa a k3s
4. Limpia archivos temporales
5. Reinicia los pods
6. Muestra el estado final

---

### ⚡ `update.sh`
Atajo para `build-and-deploy.sh`.

```bash
./update.sh
```

**Cuándo usar:** Forma más rápida de escribir el comando de actualización completa.

---

### 📋 `logs.sh`
Muestra los logs en tiempo real del pod.

```bash
./logs.sh
```

**Cuándo usar:** Para debuggear o ver qué está pasando en la aplicación.

---

### 📊 `status.sh`
Muestra el estado completo del deployment.

```bash
./status.sh
```

**Cuándo usar:** Para verificar el estado de todos los recursos en el cluster.

**Muestra:**
- Pods
- Services
- Ingress
- PersistentVolumeClaims
- URL de acceso

---

### 📋 `k8s-apply.sh`
Aplica todas las configuraciones de Kubernetes al cluster.

```bash
./k8s-apply.sh
```

**Cuándo usar:** Primera instalación o cuando cambias las configuraciones de k8s (yamls).

**Qué hace:**
1. Aplica namespace
2. Aplica PVC
3. Aplica middleware
4. Aplica deployment
5. Aplica service
6. Aplica ingress

---

### 🗑️ `uninstall.sh`
Elimina completamente el deployment del cluster.

```bash
./uninstall.sh
```

**Cuándo usar:** Para limpiar completamente el deployment.

**⚠️ ADVERTENCIA:** Esto eliminará todos los datos en el PVC!

---

## Flujos de Trabajo

### 🆕 Primera Instalación

```bash
# 1. Construir imagen
./build.sh

# 2. Aplicar configuraciones de k8s
./k8s-apply.sh

# 3. Desplegar imagen
./deploy.sh

# 4. Verificar estado
./status.sh
```

### 🔄 Actualización de Código

Después de hacer cambios en el código:

```bash
# Opción rápida
./update.sh

# O el comando completo
./build-and-deploy.sh
```

### 🐛 Debugging

```bash
# Ver estado
./status.sh

# Ver logs en tiempo real
./logs.sh

# Si hay problemas, reconstruir todo
./build-and-deploy.sh
```

### 🔧 Solo Cambios en K8s (yamls)

Si solo modificaste archivos yaml:

```bash
./k8s-apply.sh
```

### 🧪 Testing Local

Para probar la imagen localmente sin desplegar:

```bash
./build.sh
docker run -p 5000:5000 quay-solver:latest
# Acceder en http://localhost:5000
```

### 🗑️ Limpieza Completa

```bash
./uninstall.sh
```

---

## Requisitos

- Docker instalado y en ejecución
- `kubectl` configurado para el cluster k3s
- Permisos sudo para comandos k3s
- Cluster k3s corriendo

---

## URL de Acceso

Después del despliegue, la aplicación está disponible en:

**https://northr3nd.duckdns.org/quay-assignment-plan-or-tools**

---

## Notas

- Los scripts usan colores para mejor visualización (solo en terminales que lo soporten)
- El namespace usado es `quay-solver`
- La imagen se llama `quay-solver:latest`
- El archivo temporal durante el despliegue se guarda en `/tmp/quay-solver.tar`
