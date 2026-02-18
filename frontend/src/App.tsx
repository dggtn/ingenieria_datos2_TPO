import React, { useState } from 'react';
import axios from 'axios';
import ArgentinaForm from './components/argentinaForm';
import GermanyForm from './components/alemaniaForm';
import USAForm from './components/usaForm';
import EnglandForm from './components/englandForm'; 
import './App.css';

interface ConversionResponse {
  equivalencia_sudafrica: number;
  historial_estudiante?: any[];
}

export default function App() {
  const [estudianteId, setEstudianteId] = useState('');
  const [institucionId, setInstitucionId] = useState('');
  const [materiaId, setMateriaId] = useState(''); 
  const [pais, setPais] = useState('Argentina');
  const [gradeDetails, setGradeDetails] = useState<any>({});
  const [resultado, setResultado] = useState<ConversionResponse | null>(null);
  const [loading, setLoading] = useState(false);
  const [nombreMejorPais, setNombreMejorPais] = useState<string | null>(null);
  const [nombreMejorInstituto, setNombreMejorInstituto] = useState<string | null>(null);
  const [detalleAcademico, setDetalleAcademico] = useState<any[]>([]);

  // 1. Consultar Trayectoria Completa (Neo4j)
  const consultarDetalle = async () => {
    if (!estudianteId) return alert("Ingresá un ID de Alumno");
    setLoading(true);
    try {
      const response = await axios.get(`http://localhost:8080/api/estudiantes/${estudianteId}/detalle-completo`);
      setDetalleAcademico(response.data);
    } catch (error) {
      alert("No se encontró actividad académica para el ID: "+ estudianteId);
    } finally {
      setLoading(false);
    }
  };

  // 2. Rankings (Cassandra)
  const fetchRankingMejorPais = async () => {
    setLoading(true);
    try {
      const response = await axios.get('http://localhost:8080/api/reportes/top-paises'); 
      if (response.data && response.data.length > 0) {
        setNombreMejorPais(response.data[0].nombre);
      }
    } catch (error) {
      alert("Error al conectar con Cassandra.");
    } finally {
      setLoading(false);
    }
  };

  const fetchRankingMejorInstituto = async () => {
    setLoading(true);
    try {
      const response = await axios.get('http://localhost:8080/api/reportes/top-institutos'); 
      if (response.data && response.data.length > 0) {
        setNombreMejorInstituto(response.data[0].nombre);
      }
    } catch (error) {
      alert("Error al conectar con Cassandra.");
    } finally {
      setLoading(false);
    }
  };

  const handleRegistrar = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    try {
      const response = await axios.post('http://localhost:8080/api/calificaciones/registrar', {
        estudiante: estudianteId,
        materia: materiaId,     
        pais: pais,
        metadatos: gradeDetails,
        institucion: institucionId
      });
      setResultado({ equivalencia_sudafrica: response.data.conversiones });
    } catch (error) {
      alert("Error al registrar calificacion.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <main className="font-['Quicksand'] bg-green-600 w-full min-h-screen py-10 px-4 m-0">
      <div className="max-w-4xl mx-auto">
        <header className="text-center mb-10">
          <h1 className="text-4xl font-black text-white mb-6 tracking-tight drop-shadow-md">
            EduGrade Global
          </h1>
          
          <div className="flex flex-wrap justify-center gap-4">
            <button 
              onClick={fetchRankingMejorInstituto}
              className="bg-orange-500 hover:bg-orange-600 text-white font-bold py-3 px-8 rounded-full transition-all shadow-xl active:scale-95"
            >
              {loading ? '...' : `Mejor Instituto: ${nombreMejorInstituto || 'Ver'}`}
            </button>
            
            <button 
              onClick={fetchRankingMejorPais}
              className="bg-pink-500 hover:bg-pink-600 text-white font-bold py-3 px-8 rounded-full transition-all shadow-xl active:scale-95"
            >
              {loading ? '...' : `País Top: ${nombreMejorPais || 'Ver'}`}
            </button>
          </div>
        </header>

        <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
          {/* Panel de Registro */}
          <section className="bg-white/95 backdrop-blur-sm p-8 rounded-[2.5rem] shadow-2xl border-none">
            <h2 className="text-2xl font-bold mb-6 text-green-800 text-center">Registro de Nota</h2>
            <form onSubmit={handleRegistrar} className="space-y-4">
              <input 
                className="w-full border-2 border-slate-100 p-4 rounded-2xl focus:border-green-400 outline-none transition-colors" 
                placeholder="ID Alumno" value={estudianteId} onChange={e => setEstudianteId(e.target.value)} 
              />
              <input 
                className="w-full border-2 border-slate-100 p-4 rounded-2xl focus:border-green-400 outline-none transition-colors" 
                placeholder="ID Institución" value={institucionId} onChange={e => setInstitucionId(e.target.value)} 
              />
              <input 
                className="w-full border-2 border-slate-100 p-4 rounded-2xl focus:border-green-400 outline-none transition-colors" 
                placeholder="ID Materia" value={materiaId} onChange={e => setMateriaId(e.target.value)} 
              />
              <select 
                className="w-full border-2 border-slate-100 p-4 rounded-2xl bg-white focus:border-green-400 outline-none" 
                value={pais} onChange={e => setPais(e.target.value)}
              >
                <option value="Argentina">Argentina</option>
                <option value="Alemania">Alemania</option>
                <option value="USA">USA</option>
                <option value="Inglaterra">Inglaterra</option>
              </select>
              
              <div className="bg-green-50/50 p-4 rounded-2xl border border-dashed border-green-200">
                {pais === "Argentina" && <ArgentinaForm setGradeDetails={setGradeDetails} />}
                {pais === "Alemania" && <GermanyForm setGradeDetails={setGradeDetails} />}
                {pais === "USA" && <USAForm setGradeDetails={setGradeDetails} />}
                {pais === "Inglaterra" && <EnglandForm setGradeDetails={setGradeDetails} />}
              </div>

              <button type="submit" className="w-full bg-green-500 hover:bg-green-600 text-white font-bold py-4 rounded-2xl shadow-lg transition-all active:scale-95">
                Registrar y Convertir
              </button>
            </form>
          </section>

          {/* Panel de Trayectoria */}
          <section className="bg-white/95 backdrop-blur-sm p-8 rounded-[2.5rem] shadow-2xl border-none flex flex-col">
            <h2 className="text-2xl font-black text-blue-700 text-center mb-6">Grafo Académico</h2>
            <button 
              onClick={consultarDetalle}
              className="w-full bg-blue-500 hover:bg-blue-600 text-white py-4 rounded-2xl font-bold transition-all shadow-lg active:scale-95 mb-6"
            >
              Consultar Neo4j
            </button>

            <div className="space-y-4 overflow-y-auto max-h-[400px] pr-2">
              {detalleAcademico.map((inst, i) => (
                <div key={i} className="bg-white p-5 rounded-[2rem] border border-slate-100 shadow-sm">
                  <h3 className="text-md font-bold text-slate-800 mb-3 flex items-center">
                    <span className="bg-blue-100 p-2 rounded-full mr-2 text-xs">🏫</span>
                    {inst.institucion}
                  </h3>
                  <div className="flex flex-wrap gap-2">
                    {inst.materias.map((m: any, j: number) => (
                      <div key={j} className="bg-slate-50 px-3 py-1.5 rounded-full border border-slate-100 text-[11px] flex gap-2">
                        <span className="font-bold text-slate-600">{m[0]}</span>
                        <span className="text-blue-600 font-bold">{m[1]}</span>
                        <span className="text-slate-400">({m[2]})</span>
                      </div>
                    ))}
                  </div>
                </div>
              ))}
            </div>
          </section>
        </div>

        {/* Modal de Resultado Sudáfrica */}
        {resultado && (
          <div className="mt-10 bg-slate-900 text-white p-10 rounded-[3rem] text-center shadow-2xl border-4 border-slate-800 animate-bounce-short">
            <p className="text-emerald-400 font-bold uppercase tracking-widest text-sm mb-2">Equivalencia Sudáfrica</p>
            <p className="text-8xl font-black mb-2">{resultado.equivalencia_sudafrica}</p>
            <p className="text-slate-400 text-xl font-medium">puntos sobre 100</p>
          </div>
        )}
      </div>
    </main>
  );
}