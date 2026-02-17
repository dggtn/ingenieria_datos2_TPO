<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>
<h1>Diagrama de Arquitectura</h1>
    <img width="713" height="453" alt="{9FA1ADBC-D515-4034-95C8-5A1924F28CC1}" src="https://github.com/user-attachments/assets/b05f7d56-28f5-40aa-bc22-bef0b1469ac3" />
<h1>Diagrama Entidad-Relación (Lógico / NoSQL)</h1>
<img width="292" height="686" alt="{AE42650F-CBA4-47A7-B250-6A938C33F4F8}" src="https://github.com/user-attachments/assets/c00c1329-8205-4796-a221-34f0b4a93ab3" />
<h1>Diagrama de Flujo de Datos (Conversión/Ranking)</h1>
    <h3>cómo extraemos los datos de Neo4j para alimentar la analítica en Cassandra.</h3>
    <img width="959" height="518" alt="{3804DF02-886E-4693-BD86-0D02BAD6B5E3}" src="https://github.com/user-attachments/assets/243fd07f-7568-466e-af7e-331a6e2b7ab1" />

<div class="container">
    <h1>UX/UI</h1>
<img width="610" height="859" alt="{6A14858D-47AE-4C10-BF1C-85F728955AD3}" src="https://github.com/user-attachments/assets/8f6d0900-e7f9-43ae-a8e9-a1598c641ed6" />


<h1>EduGrade Global - Sistema de Equivalencias</h1>
<p>Plataforma de gestión académica integral diseñada para el registro de calificaciones internacionales y cálculo de equivalencias para el sistema de <strong>Sudáfrica</strong>.</p>

<h2>Arquitectura Políglota</h2>
<p>Este proyecto implementa tres modelos de persistencia distintos para optimizar cada flujo de datos:</p>
<ul>
    <li><span class="badge badge-mongo">MongoDB</span> Calificaciones originales y metadatos del estudiante.</li>
    <li><span class="badge badge-neo">Neo4j</span> Historial de movilidad académica y relaciones institucionales.</li>
    <li><span class="badge badge-cass">Cassandra</span> Reportes analíticos de promedios masivos para el Ministerio.</li>
</ul>



<h2>Requisitos del Sistema</h2>
<ul>
    <li>Docker Desktop (Motor de contenedores)</li>
    <li>Java 17+ (Spring Boot Backend)</li>
    <li>Node.js 18+ (React Frontend)</li>
    <li>DBeaver (Visualización de datos)</li>
</ul>

<h2>Guía de Instalación</h2>

<div class="setup-step">
    <h3>1. Infraestructura (Bases de Datos)</h3>
    <p>Levantar los contenedores desde la raíz:</p>
    <pre>docker-compose up -d</pre>
</div>

<div class="setup-step">
    <h3>2. Configuración de Cassandra</h3>
    <p>Conectarse vía DBeaver y ejecutar el siguiente script para habilitar el espacio de nombres:</p>
    <pre>CREATE KEYSPACE IF NOT EXISTS edugrade WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};</pre>
</div>

<div class="setup-step">
    <h3>3. Ejecución del Backend</h3>
    <pre>cd backend
mvn spring-boot:run</pre>
</div>

<div class="setup-step">
    <h3>4. Ejecución del Frontend</h3>
    <pre>cd frontend
npm install
npm run dev</pre>
</div>

<h2>🔗 Endpoints Principales (API)</h2>
<table>
    <thead>
        <tr>
            <th>Método</th>
            <th>Ruta</th>
            <th>Acción</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td><code>POST</code></td>
            <td>/api/calificaciones/registrar</td>
            <td>Guarda en Mongo y convierte a Sudáfrica.</td>
        </tr>
        <tr>
            <td><code>GET</code></td>
            <td>/api/calificaciones/simular-conversion</td>
            <td>Previsualiza la nota sin persistir.</td>
        </tr>
        <tr>
            <td><code>GET</code></td>
            <td>/api/reportes/promedio</td>
            <td>Reportes analíticos desde Cassandra.</td>
        </tr>
    </tbody>
</table>



<hr>
<p style="text-align: center; color: #94a3b8; font-size: 0.9em;">
    TPO Desarrollo de Aplicaciones - Arquitectura de Datos - 2026
</p>
</div>

</body>
</html>
