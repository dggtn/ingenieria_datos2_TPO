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
  const [materiaId, setMateriaId] = useState(''); 
  const [pais, setPais] = useState('Argentina');
  const [gradeDetails, setGradeDetails] = useState<any>({});
  const [resultado, setResultado] = useState<ConversionResponse | null>(null);
  const [loading, setLoading] = useState(false);
  const [nombreMejorPais, setNombreMejorPais] = useState<string | null>(null);
  const [nombreMejorInstituto, setNombreMejorInstituto] = useState<string | null>(null);

 const fetchRankingMejorPais = async () => {
    setLoading(true);
    try {
      const response = await axios.get('http://localhost:8080/api/reportes/top-paises'); 
      if (response.data && response.data.length > 0) {
        setNombreMejorPais(response.data[0].nombre);
      } else {
        alert("Aún no hay datos en Cassandra. ¿Corriste el proceso de sincronización?");
      }
    } catch (error) {
      console.error(error);
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
      } else {
        alert("Aún no hay datos en Cassandra. ¿Corriste el proceso de sincronización?");
      }
    } catch (error) {
      console.error(error);
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
      });
      setResultado({ equivalencia_sudafrica: response.data.conversiones.sudafrica });
    } catch (error) {
      alert("Error al registrar.");
    } finally {
      setLoading(false);
    }
  };
  
  const handleHistorial = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    try {
      const response = await axios.post('http://localhost:8080/api/estudiantes/Historial', { 
        estudiante: estudianteId 
      });
      setResultado({
        equivalencia_sudafrica: response.data.conversiones.sudafrica,
        historial_estudiante: response.data.historial_estudiante
      });
    } catch (error) {
      alert("Error al generar historial.");
    } finally {
      setLoading(false);
    }
  };

  return (
<main className="font-['Quicksand'] bold bg-green-50 py-10 px-4">
  <div className="max-w-4xl mx-auto">
    <header className="text-center mb-10">
      <h1 className="text-4xl font-black text-slate-800 mb-6 tracking-tight bold">
        EduGrade Global
      </h1>
      
      <div className="flex flex-wrap justify-center gap-4">
        <button 
          onClick={fetchRankingMejorInstituto}
          className="bg-orange-500 hover:bg-orange-600 text-white font-bold py-3 px-8 rounded-full transition-all shadow-lg hover:shadow-orange-200 active:scale-95"
        >
          {loading ? '...' : `Mejor Instituto: ${nombreMejorInstituto || 'Ver'}`}
        </button>
        
        <button 
          onClick={fetchRankingMejorPais}
          className="bg-pink-500 hover:bg-pink-600 text-white font-bold py-3 px-8 rounded-full transition-all shadow-lg hover:shadow-pink-200 active:scale-95"
        >
          {loading ? '...' : `País Top: ${nombreMejorPais || 'Ver'}`}
        </button>
      </div>
    </header>

    <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
   
      <section className="bg-white p-8 rounded-[2.5rem] shadow-xl border border-green-100">
        <h2 className="text-2xl font-bold mb-6 text-slate-700 text-center bold">Registro</h2>
        <form onSubmit={handleRegistrar} className="space-y-5">
          <input 
            className="w-full border-2 border-slate-100 p-4 rounded-2xl focus:border-green-400 outline-none transition-colors" 
            placeholder="ID Alumno" 
            value={estudianteId} 
            onChange={e => setEstudianteId(e.target.value)} 
          />
          <select 
            className="w-full border-2 border-slate-100 p-4 rounded-2xl bg-white focus:border-green-400 outline-none appearance-none" 
            value={pais} 
            onChange={e => setPais(e.target.value)}
          >
            <option value="Argentina">Argentina</option>
            <option value="Alemania">Alemania</option>
            <option value="USA">USA</option>
            <option value="Inglaterra">Inglaterra</option>
          </select>
          
          <div className="bg-slate-50 p-4 rounded-2xl border border-dashed border-slate-200">
            {pais === "Argentina" && <ArgentinaForm setGradeDetails={setGradeDetails} />}
            {pais === "Alemania" && <GermanyForm setGradeDetails={setGradeDetails} />}
            {pais === "USA" && <USAForm setGradeDetails={setGradeDetails} />}
            {pais === "Inglaterra" && <EnglandForm setGradeDetails={setGradeDetails} />}
          </div>

          <button type="submit" className="w-full bg-green-500 hover:bg-green-600 text-white font-bold py-4 rounded-2xl shadow-md transition-transform active:scale-95">
            Registrar
          </button>
        </form>
      </section>
      <section className="bg-white p-8 rounded-[2.5rem] shadow-xl border border-blue-100">
        <h2 className="text-2xl font-bold mb-6 text-slate-700 text-center bold">Historial</h2>
        <form onSubmit={handleHistorial} className="space-y-5">
          <input 
            className="w-full border-2 border-slate-100 p-4 rounded-2xl focus:border-blue-400 outline-none transition-colors" 
            placeholder="ID Alumno" 
            value={estudianteId} 
            onChange={e => setEstudianteId(e.target.value)} 
          />
          <button type="submit" className="w-full bg-blue-500 hover:bg-blue-600 text-white font-bold py-4 rounded-2xl shadow-md transition-transform active:scale-95">
            Ver Historial
          </button>
        </form>
      </section>
    </div>

    {resultado && (
      <div className="mt-10 bg-slate-900 text-white p-10 rounded-[3rem] text-center shadow-2xl border-4 border-slate-800">
        <p className="text-emerald-400 font-bold uppercase tracking-widest text-sm mb-2">Resultado Sudáfrica</p>
        <p className="text-7xl font-black mb-6">{resultado.equivalencia_sudafrica}</p>
        {resultado.historial_estudiante && (
          <div className="bg-slate-800 p-6 rounded-3xl text-left border border-slate-700 overflow-hidden">
             <pre className="text-xs font-mono text-indigo-300 overflow-auto max-h-40">
               {JSON.stringify(resultado.historial_estudiante, null, 2)}
             </pre>
          </div>
        )}
      </div>
    )}
  </div>
</main>)
}
