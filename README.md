<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>README - EduGrade Global</title>
<style>
    :root {
        --primary: #4f46e5;
        --secondary: #1e293b;
        --accent: #10b981;
        --bg: #f8fafc;
        --text: #334155;
    }
    body {
        font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        line-height: 1.6;
        color: var(--text);
        background-color: var(--bg);
        margin: 0;
        padding: 0;
    }
    .container {
        max-width: 900px;
        margin: 40px auto;
        background: white;
        padding: 40px;
        border-radius: 12px;
        shadow: 0 4px 6px -1px rgb(0 0 0 / 0.1);
        border: 1px solid #e2e8f0;
    }
    h1 { color: var(--secondary); border-bottom: 3px solid var(--primary); padding-bottom: 10px; }
    h2 { color: var(--primary); margin-top: 30px; border-bottom: 1px solid #e2e8f0; }
    h3 { color: var(--secondary); }
    code {
        background: #f1f5f9;
        padding: 2px 6px;
        border-radius: 4px;
        font-family: 'Courier New', Courier, monospace;
        color: #be185d;
    }
    pre {
        background: #1e293b;
        color: #f8fafc;
        padding: 15px;
        border-radius: 8px;
        overflow-x: auto;
    }
    .badge {
        display: inline-block;
        padding: 4px 12px;
        border-radius: 20px;
        font-size: 0.85em;
        font-weight: bold;
        margin-right: 5px;
    }
    .badge-mongo { background: #dcfce7; color: #166534; }
    .badge-neo { background: #dbeafe; color: #1e40af; }
    .badge-cass { background: #fef3c7; color: #92400e; }
    table {
        width: 100%;
        border-collapse: collapse;
        margin: 20px 0;
    }
    th, td {
        text-align: left;
        padding: 12px;
        border-bottom: 1px solid #e2e8f0;
    }
    th { background: #f1f5f9; }
    .setup-step { margin-bottom: 20px; }
</style>
</head>
<body>

<div class="container">
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