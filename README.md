Grupo-10
Colaboradores:
Daniela Gangotena: 1185437 [@dggtn]
Marisol Semperena 1160264
Alejandro Valente 1196776
Tecnologías Usadas en el Desarrollo del TPO:
Frontend
HTML5
CSS3
JavaScript
React
Redux
Backend
API REST
Git
JAVA
Spring
Springboot
Mongo DB
Cassandra DB
IRIS
Neo4j
Herramientas de Desarrollo
Intellij
Postman
npm
Consignas
EduGrade Global es un sistema nacional unificado de calificaciones que el Ministerio de Educación de Sudáfrica debe implementar para registrar, normalizar, convertir y analizar calificaciones de todos los niveles educativos, considerando trayectorias académicas mixtas y movilidad internacional entre los sistemas del Reino Unido, Estados Unidos, Alemania y Argentina. El sistema debe conservar la calificación en su formato original, permitir conversiones coexistentes entre múltiples escalas, modelar relaciones complejas entre estudiantes, instituciones y evaluaciones, y generar análisis oficiales para estadísticas, equivalencias y políticas públicas. 
Además, debe garantizar auditoría, trazabilidad e inmutabilidad de los registros mediante un enfoque append-only, y cumplir la restricción de utilizar al menos tres modelos de bases de datos NoSQL distintos, cada uno justificado según su función dentro del sistema.

Requisitos Funcionales:
1. Registro Académico Oficial
Alta de estudiantes con múltiples trayectorias educativas.
Asociación a instituciones, niveles, ciclos lectivos y materias.
Registro de evaluaciones parciales, finales y extraordinarias.
Almacenamiento íntegro de la calificación original, sin pérdida de información.
2. Conversión y Normalización
Definición de reglas de conversión versionadas entre sistemas.
Posibilidad de múltiples conversiones válidas simultáneas.
Registro del origen, método y fecha de cada conversión.
No se permite sobrescribir la nota original.
3. Relaciones Académicas Complejas
Un estudiante puede:
Cambiar de país o sistema educativo.
Repetir materias bajo distintos esquemas.
Tener equivalencias parciales.
Una materia puede mapearse a múltiples materias equivalentes en otros sistemas.
4. Análisis y Reportes Oficiales
Promedios por:
Región
Institución
Sistema educativo
Año lectivo
Comparación histórica entre sistemas.
Detección de desvíos estadísticos.
5. Auditoría y Trazabilidad
Todo cambio debe ser trazable.
Historial completo de:
Correcciones
Recalificaciones
Cambios normativos
Acceso para organismos de control.

Requisitos de tecnicos de entrega
El trabajo por realizar es el siguiente:

Persistencia Políglota en Docker
Orquestar al menos 3 bases NoSQL diferentes (ej. MongoDB, Neo4j, Cassandra, Redis, IRIS, InfluxDB, etc.) usando docker-compose.yml.
Volúmenes persistentes para todos los servicios.
Configuraciones de clustering/replicación básicas donde aplique (ej. replica set MongoDB, N/R/W en Cassandra).
Healthchecks y redes internas definidas.
API Backend 100% Operacional
Desarrollar una API REST (cualquier tecnología: Node.js/Express, Spring Boot, FastAPI/Python, .NET, Go, etc.).
ABM completo (Create, Read, Update, Delete) para las entidades principales:
Estudiante
Institución
Materia/Evaluación
Calificación (con formato original + metadatos)
Trayectoria académica
Endpoints obligatorios de negocio:
Registrar calificación en formato original (POST)
Convertir calificación a otra escala (GET/POST con parámetros)
Obtener trayectoria completa de un estudiante (incluyendo conversiones)
Reportes analíticos (promedio por país, distribución de notas, etc.)
Demostración 100% implementada de conversiones múltiples (lógica en backend, no solo frontend).
Documentación automática con Swagger/OpenAPI (o Postman collection exportada).




Escalabilidad y Volumen
El sistema debe soportar la inserción y consulta eficiente de al menos 1 millón de calificaciones (1M registros).
Generar script de carga masiva (Python, Node, etc.) que inserte 1M calificaciones variadas (distribuidas por países, niveles, tipos de evaluación).
Demostrar consultas razonablemente rápidas (ej. promedio de un estudiante con 50+ calificaciones, reporte nacional top-10 materias).
Auditoría e Inmutabilidad
Registros de calificaciones inmutables (no update/delete directo; usar append-only o versionado).
Cada calificación debe tener: timestamp, usuario auditor (simulado), hash o ID inmutable.
Frontend (Optativo – suma nota base)
Desarrollar una interfaz web simple (React, Vue, Angular, HTML+JS vanilla, etc.) que consuma la API.
Funcionalidades mínimas:
Formulario de registro de calificación
Visualización de trayectoria y conversiones
Tabla de reportes analíticos básicos
Responsive y usable (no se exige diseño premium).
