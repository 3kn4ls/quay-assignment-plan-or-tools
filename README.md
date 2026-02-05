# Definición del Proyecto: Optimización de Operaciones en Terminal Portuaria (BAP + QCAP)

## 1. Objetivo General
Desarrollar un solucionador de optimización utilizando **Google OR-Tools** para resolver de forma integrada dos problemas clásicos de logística portuaria:
1.  **Berth Allocation Problem (BAP):** Asignación de espacio en muelle y tiempo de atraque.
2.  **Quay Crane Assignment Problem (QCAP):** Asignación de recursos (grúas/cuadrillas) por turno de trabajo.

El objetivo es generar un plan de atraque y una matriz de asignación de recursos que respete todas las restricciones físicas y operativas.

---

## 2. Entidades y Datos de Entrada

### 2.1. El Muelle (Berth)
* **Longitud Total ($L_{muelle}$):** Espacio continuo disponible en metros.
* **Profundidad ($Depth_{x}$):** El calado (profundidad) disponible puede variar a lo largo del muelle (o ser constante).
* **Horizonte de Tiempo:** Discretizado en "Shifts" (Jornadas/Turnos).

### 2.2. El Tiempo (Shifts)
* La unidad mínima de tiempo es el **Shift** (Jornada).
* Duración del Shift: **6 horas**.
* El horizonte de planificación es un conjunto de Shifts consecutivos $T = \{1, 2, ..., N\}$.

### 2.3. Los Buques (Vessels)
Cada buque $i$ tiene las siguientes propiedades:
* **Carga de Trabajo ($M_i$):** Cantidad total de movimientos de contenedor necesarios para completar la operativa.
* **Dimensiones ($Loa_i$):** Longitud del barco en metros (Length Over All).
* **Calado ($Draft_i$):** Profundidad necesaria para atracar.
* **Productividad ($Prod_i$):** Movimientos que realiza 1 Cuadrilla/Grúa en 1 Shift (ej: 120 movimientos/shift).
* **Ventana de Tiempo ($ETW_i, ETC_i$):**
    * $ETW$: *Estimated Time of Window* (Fecha/Hora más temprana de llegada).
    * $ETC$: *Estimated Time of Completion* (Fecha/Hora límite deseada para salir).
* **Disponibilidad de Turnos ($S_i$):** Lista de Shifts específicos donde el buque puede ser operado (ej: no trabaja fines de semana o noches, o disponibilidad total).
* **Límites de Grúas ($MaxCranes_i$):** Máximo número de grúas que caben físicamente en el buque (función de su $Loa$).

### 2.4. Recursos (Grúas y Cuadrillas)
* **Relación 1:1:** 1 Grúa requiere 1 Cuadrilla (GANG). Se tratan como un recurso unificado.
* **Disponibilidad Global:** Existe un número máximo de grúas/cuadrillas totales disponibles en la terminal por cada Shift $t$.

---

## 3. Variables de Decisión (Lo que el modelo debe decidir)

1.  **Posición de Atraque ($p_i$):** Coordenada (metro) donde comienza el barco en el muelle.
2.  **Tiempo de Inicio ($t_{start, i}$):** Shift en el que comienza la operación.
3.  **Tiempo de Fin ($t_{end, i}$):** Shift en el que termina la operación.
4.  **Asignación de Grúas ($q_{i,t}$):** Cantidad de cuadrillas asignadas al barco $i$ en el turno $t$.

---

## 4. Restricciones del Modelo

### 4.1. Restricciones Espaciales (Muelle)
* **Límites del Muelle:** El buque debe caber dentro de la longitud del muelle:
    $p_i + Loa_i \le L_{muelle}$
* **No Solapamiento Espacial:** Dos buques no pueden ocupar el mismo metro de muelle simultáneamente si sus tiempos se solapan.
* **Restricción de Profundidad (Draft):** Para la posición asignada $p_i$, la profundidad del muelle debe ser mayor o igual al calado del buque:
    $Depth(p_i) \ge Draft_i$

### 4.2. Restricciones Temporales
* **Ventana de Llegada:** El buque no puede ser operado antes de su llegada:
    $t_{start, i} \ge ETW_i$
* **Continuidad (Opcional pero recomendada):** Una vez inicia la operación, debe ser continua hasta finalizar (o modelar interrupciones permitidas).
* **No Solapamiento Temporal:** Si dos buques comparten posición en el muelle, no pueden solaparse en el tiempo.

### 4.3. Restricciones de Recursos y Operatividad
* **Cumplimiento de Demanda:** La suma de la productividad generada por las cuadrillas asignadas debe cubrir los movimientos del buque:
    $\sum_{t} (q_{i,t} \times Prod_i) \ge M_i$
* **Capacidad Máxima por Buque:** No se pueden asignar más grúas que las que el buque permite por longitud:
    $q_{i,t} \le MaxCranes_i$
* **Capacidad Global de la Terminal:** La suma de grúas asignadas a todos los barcos en un turno $t$ no puede superar el total de grúas de la terminal:
    $\sum_{i} q_{i,t} \le TotalGrúasDisponible_t$

---

## 5. Función Objetivo (Sugerida)
El solver debe buscar una solución que minimice una combinación de:
1.  **Tiempo de espera:** Minimizar $(t_{end, i} - ETW_i)$.
2.  **Makespan:** Terminar todos los barcos lo antes posible.
3.  **Uso de Recursos:** Optimizar el uso de grúas (compactar el cronograma).

---

## 6. Stack Tecnológico Sugerido
* **Lenguaje:** Python.
* **Librería Principal:** Google OR-Tools (Módulo `cp_model` - Constraint Programming).
* **Visualización:** Matplotlib o Plotly (para generar un diagrama de Gantt Espacio-Tiempo).
