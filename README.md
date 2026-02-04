<h1>EduGrade Global – Sistema Nacional de Calificaciones Multimodelo</h1>

<h2>Grupo-10</h2>

<h2>Colaboradores</h2>
<ul>
  <li><strong>Daniela Gangotena</strong> – 1185437 [@dggtn]</li>
  <li><strong>Marisol Semperena</strong> – 1160264</li>
  <li><strong>Alejandro Valente</strong> – 1196776</li>
</ul>

<hr>

<h2>Descripción General</h2>

<p>
<strong>EduGrade Global</strong> es un sistema nacional unificado de calificaciones que el Ministerio de Educación de Sudáfrica debe implementar para registrar, normalizar, convertir y analizar calificaciones de todos los niveles educativos. El sistema contempla trayectorias académicas mixtas y movilidad internacional entre los sistemas educativos del <strong>Reino Unido</strong>, <strong>Estados Unidos</strong>, <strong>Alemania</strong> y <strong>Argentina</strong>, preservando siempre la calificación original. Además, permite conversiones coexistentes entre múltiples escalas, soporta análisis oficiales para estadísticas, equivalencias y políticas públicas, y garantiza auditoría, trazabilidad e inmutabilidad de los registros mediante un enfoque <em>append-only</em>, utilizando al menos <strong>tres modelos de bases de datos NoSQL</strong> justificados por su función dentro del sistema.
</p>

<hr>

<h2>Tecnologías Usadas en el Desarrollo del TPO</h2>

<h3>Frontend</h3>
<ul>
  <li>HTML5</li>
  <li>CSS3</li>
  <li>JavaScript</li>
  <li>React</li>
  <li>Redux</li>
</ul>

<h3>Backend</h3>
<ul>
  <li>API REST</li>
  <li>Java</li>
  <li>Spring</li>
  <li>Spring Boot</li>
  <li>Git</li>
</ul>

<h3>Bases de Datos</h3>
<ul>
  <li>MongoDB</li>
  <li>Cassandra DB</li>
  <li>IRIS</li>
  <li>Neo4j</li>
</ul>

<h3>Herramientas de Desarrollo</h3>
<ul>
  <li>IntelliJ IDEA</li>
  <li>Postman</li>
  <li>npm</li>
</ul>

<hr>

<h2>Consignas del Sistema</h2>
<ul>
  <li>Registro de calificaciones en su <strong>formato original</strong>.</li>
  <li>Conversión múltiple y simultánea entre escalas.</li>
  <li>Modelado de relaciones académicas complejas.</li>
  <li>Generación de análisis oficiales y reportes estadísticos.</li>
  <li>Auditoría, trazabilidad e inmutabilidad de los registros.</li>
</ul>

<hr>

<h2>Requisitos Funcionales</h2>

<h3>1. Registro Académico Oficial</h3>
<ul>
  <li>Alta de estudiantes con múltiples trayectorias educativas.</li>
  <li>Asociación a instituciones, niveles, ciclos lectivos y materias.</li>
  <li>Registro de evaluaciones parciales, finales y extraordinarias.</li>
  <li>Almacenamiento íntegro de la calificación original.</li>
</ul>

<h3>2. Conversión y Normalización</h3>
<ul>
  <li>Reglas de conversión versionadas entre sistemas.</li>
  <li>Múltiples conversiones válidas simultáneas.</li>
  <li>Registro de origen, método y fecha de conversión.</li>
  <li>Prohibición de sobrescritura de la nota original.</li>
</ul>

<h3>3. Relaciones Académicas Complejas</h3>
<ul>
  <li>Cambio de país o sistema educativo.</li>
  <li>Repetición de materias bajo distintos esquemas.</li>
  <li>Equivalencias parciales.</li>
  <li>Mapeo de materias equivalentes entre sistemas.</li>
</ul>

<h3>4. Análisis y Reportes Oficiales</h3>
<ul>
  <li>Promedios por región, institución, sistema y año lectivo.</li>
  <li>Comparaciones históricas entre sistemas.</li>
  <li>Detección de desvíos estadísticos.</li>
</ul>

<h3>5. Auditoría y Trazabilidad</h3>
<ul>
  <li>Trazabilidad completa de cambios.</li>
  <li>Historial de correcciones, recalificaciones y cambios normativos.</li>
  <li>Acceso para organismos de control.</li>
</ul>

<hr>

<h2>Requisitos Técnicos de Entrega</h2>

<h3>Persistencia Políglota en Docker</h3>
<ul>
  <li>Orquestación de al menos <strong>3 bases NoSQL</strong> con Docker Compose.</li>
  <li>Volúmenes persistentes.</li>
  <li>Configuración básica de clustering/replicación.</li>
  <li>Healthchecks y redes internas.</li>
</ul>

<h3>API Backend 100% Operacional</h3>
<ul>
  <li>API REST funcional.</li>
  <li>ABM completo de entidades principales.</li>
  <li>Endpoints de negocio obligatorios.</li>
  <li>Conversión múltiple implementada en backend.</li>
  <li>Documentación con Swagger/OpenAPI o Postman.</li>
</ul>

<hr>

<h2>Escalabilidad y Volumen</h2>
<ul>
  <li>Soporte para al menos <strong>1 millón de calificaciones</strong>.</li>
  <li>Script de carga masiva.</li>
  <li>Consultas analíticas eficientes.</li>
</ul>

<hr>

<h2>Auditoría e Inmutabilidad</h2>
<ul>
  <li>Registros inmutables.</li>
  <li>Estrategia append-only o versionado.</li>
  <li>Timestamp, auditor y hash inmutable por registro.</li>
</ul>

<hr>

<h2>Frontend (Optativo – Suma Nota Base)</h2>
<ul>
  <li>Formulario de registro de calificaciones.</li>
  <li>Visualización de trayectorias y conversiones.</li>
  <li>Reportes analíticos básicos.</li>
  <li>Diseño responsive y usable.</li>
</ul>
