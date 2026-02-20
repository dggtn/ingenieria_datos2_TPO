import React, { useEffect, useState } from 'react';
import axios from 'axios';
import ArgentinaForm from './components/argentinaForm';
import GermanyForm from './components/alemaniaForm';
import USAForm from './components/usaForm';
import EnglandForm from './components/englandForm';
import './App.css';

interface ConversionResponse {
  equivalencia_sudafrica: number;
}

type Page = 'home' | 'reportes';

interface InstitucionRanking {
  institucionId: string;
  institucionNombre: string;
  promedioConversionSudafrica: number;
  cantidadEstudiantes: number;
}

export default function App() {
  const [estudianteId, setEstudianteId] = useState('');
  const [consultaEstudianteId, setConsultaEstudianteId] = useState('');
  const [institucionId, setInstitucionId] = useState('');
  const [materiaId, setMateriaId] = useState('');
  const [pais, setPais] = useState('Argentina');
  const [gradeDetails, setGradeDetails] = useState<any>({});
  const [fechaNormativa, setFechaNormativa] = useState<string>(new Date().toISOString().slice(0, 10));
  const [resultado, setResultado] = useState<ConversionResponse | null>(null);
  const [loading, setLoading] = useState(false);
  const [nombreMejorPais, setNombreMejorPais] = useState<string | null>(null);
  const [rankingInstituciones, setRankingInstituciones] = useState<InstitucionRanking[]>([]);
  const [detalleAcademico, setDetalleAcademico] = useState<any[]>([]);
  const [page, setPage] = useState<Page>('home');

  useEffect(() => {
    const syncPageFromHash = () => {
      const hash = window.location.hash;
      setPage(hash === '#/reportes' ? 'reportes' : 'home');
    };

    syncPageFromHash();
    window.addEventListener('hashchange', syncPageFromHash);
    return () => window.removeEventListener('hashchange', syncPageFromHash);
  }, []);

  const consultarDetalle = async () => {
    if (!consultaEstudianteId) return alert('Ingresá un ID de Alumno');
    setLoading(true);
    try {
      const response = await axios.get(`http://localhost:8080/api/estudiantes/${consultaEstudianteId}/detalle-completo`);
      setDetalleAcademico(response.data);
    } catch (error) {
      alert('No se encontró actividad académica para el ID: ' + consultaEstudianteId);
    } finally {
      setLoading(false);
    }
  };

  const fetchRankingMejorPais = async () => {
    setLoading(true);
    try {
      const response = await axios.get('http://localhost:8080/api/reportes/top-paises');
      if (response.data && response.data.length > 0) {
        setNombreMejorPais(response.data[0].nombre);
      }
    } catch (error) {
      alert('Error al conectar con Cassandra.');
    } finally {
      setLoading(false);
    }
  };

  const fetchRankingMejorInstituto = async () => {
    setLoading(true);
    try {
      const response = await axios.get('http://localhost:8080/api/reportes/instituciones-ranking');
      setRankingInstituciones(response.data || []);
    } catch (error) {
      alert('Error al conectar con Cassandra.');
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
        metadatos: {
          ...gradeDetails,
          fecha_normativa: fechaNormativa
        },
        institucion: institucionId
      });
      setResultado({ equivalencia_sudafrica: response.data.conversiones });
    } catch (error) {
      alert('Error al registrar calificacion.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <main className="font-['Quicksand'] bg-green-600 w-full min-h-screen py-10 px-4 m-0">
      <div className="max-w-4xl mx-auto">
        <nav className="mb-8 bg-white/95 rounded-full px-6 py-4 shadow-xl flex items-center justify-between">
          <span className="font-black text-green-700">EduGrade Global</span>
          {page === 'home' ? (
            <a href="#/reportes" className="text-blue-700 font-bold hover:underline">Ir a Reportes</a>
          ) : (
            <a href="#/" className="text-blue-700 font-bold hover:underline">Volver a Home</a>
          )}
        </nav>

        {page === 'home' ? (
          <>
            <header className="text-center mb-10">
              <h1 className="text-4xl font-black text-white mb-6 tracking-tight drop-shadow-md">
                Registro Académico
              </h1>
            </header>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
              <section className="bg-white/95 backdrop-blur-sm p-8 rounded-[2.5rem] shadow-2xl">
                <h2 className="text-2xl font-bold mb-6 text-green-800 text-center">Registro de Nota</h2>
                <form onSubmit={handleRegistrar} className="space-y-4">
                  <input className="w-full border-2 border-slate-100 p-4 rounded-2xl" placeholder="ID Alumno" value={estudianteId} onChange={e => setEstudianteId(e.target.value)} />
                  <input className="w-full border-2 border-slate-100 p-4 rounded-2xl" placeholder="ID Institución" value={institucionId} onChange={e => setInstitucionId(e.target.value)} />
                  <input className="w-full border-2 border-slate-100 p-4 rounded-2xl" placeholder="ID Materia" value={materiaId} onChange={e => setMateriaId(e.target.value)} />
                  <select className="w-full border-2 border-slate-100 p-4 rounded-2xl bg-white" value={pais} onChange={e => setPais(e.target.value)}>
                    <option value="Argentina">Argentina</option>
                    <option value="Alemania">Alemania</option>
                    <option value="USA">USA</option>
                    <option value="Inglaterra">Inglaterra</option>
                  </select>
                  <input
                    type="date"
                    className="w-full border-2 border-slate-100 p-4 rounded-2xl bg-white"
                    value={fechaNormativa}
                    onChange={e => setFechaNormativa(e.target.value)}
                  />
                  <div className="bg-green-50/50 p-4 rounded-2xl border border-dashed border-green-200">
                    {pais === 'Argentina' && <ArgentinaForm setGradeDetails={setGradeDetails} />}
                    {pais === 'Alemania' && <GermanyForm setGradeDetails={setGradeDetails} />}
                    {pais === 'USA' && <USAForm setGradeDetails={setGradeDetails} />}
                    {pais === 'Inglaterra' && <EnglandForm setGradeDetails={setGradeDetails} />}
                  </div>
                  <button type="submit" className="w-full bg-green-500 hover:bg-green-600 text-white font-bold py-4 rounded-2xl shadow-lg">
                    Registrar y Convertir
                  </button>
                </form>
              </section>

              <section className="bg-white/95 backdrop-blur-sm p-8 rounded-[2.5rem] shadow-2xl flex flex-col">
                <h2 className="text-2xl font-black text-blue-700 text-center mb-6">Consultar Calificaciones</h2>
                <input
                  className="w-full border-2 border-slate-100 p-4 rounded-2xl mb-4"
                  placeholder="ID Alumno"
                  value={consultaEstudianteId}
                  onChange={e => setConsultaEstudianteId(e.target.value)}
                />
                <button onClick={consultarDetalle}
                  className="w-full bg-blue-500 hover:bg-blue-600 text-white py-4 rounded-2xl font-bold shadow-lg mb-6 transition-all active:scale-95">
                  Consultar
                </button>
                <div className="space-y-4 overflow-y-auto max-h-[400px] pr-2">
                  {detalleAcademico.map((item, i) => (
                    <div key={i} className="bg-white px-5 py-3 rounded-full border shadow-sm flex justify-between items-center border-slate-100">
                      <span className="font-bold text-slate-700">{item.materia}</span>
                      <span className="bg-green-100 text-green-700 font-black px-4 py-1 rounded-full">{item.notaOriginal}</span>
                    </div>
                  ))}
                </div>
              </section>
            </div>

            {resultado && (
              <div className="mt-10 bg-slate-900 text-white p-10 rounded-[3rem] text-center shadow-2xl border-4 border-slate-800">
                <p className="text-emerald-400 font-bold uppercase tracking-widest text-sm mb-2">Equivalencia Sudáfrica</p>
                <p className="text-8xl font-black mb-2">{resultado.equivalencia_sudafrica}</p>
                <p className="text-slate-400 text-xl font-medium">puntos sobre 100</p>
              </div>
            )}
          </>
        ) : (
          <section className="bg-white/95 backdrop-blur-sm p-8 rounded-[2.5rem] shadow-2xl">
            <h2 className="text-3xl font-black text-slate-800 text-center mb-8">Reportes</h2>
            <div className="flex flex-wrap justify-center gap-4">
              <button onClick={fetchRankingMejorInstituto} className="bg-orange-500 hover:bg-orange-600 text-white font-bold py-3 px-8 rounded-full transition-all shadow-xl active:scale-95">
                {loading ? '...' : 'Mejor Instituto'}
              </button>
              <button onClick={fetchRankingMejorPais} className="bg-pink-500 hover:bg-pink-600 text-white font-bold py-3 px-8 rounded-full transition-all shadow-xl active:scale-95">
                {loading ? '...' : `País Top: ${nombreMejorPais || 'Ver'}`}
              </button>
            </div>
            {rankingInstituciones.length > 0 && (
              <div className="mt-8 overflow-x-auto">
                <table className="w-full border-collapse bg-white rounded-2xl overflow-hidden shadow">
                  <thead>
                    <tr className="bg-slate-100 text-slate-700 text-left">
                      <th className="px-4 py-3">Institución</th>
                      <th className="px-4 py-3">Promedio</th>
                      <th className="px-4 py-3">Estudiantes</th>
                    </tr>
                  </thead>
                  <tbody>
                    {rankingInstituciones.map((row, idx) => (
                      <tr
                        key={row.institucionId}
                        className={`border-t border-slate-100 ${idx === 0 ? 'font-bold text-[18px] bg-amber-50' : 'text-base'}`}
                      >
                        <td className="px-4 py-3">{row.institucionNombre}</td>
                        <td className="px-4 py-3">{row.promedioConversionSudafrica.toFixed(2)}</td>
                        <td className="px-4 py-3">{row.cantidadEstudiantes}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}
            <div className="text-center mt-8">
              <a href="#/" className="text-blue-700 font-bold hover:underline">Volver a Home</a>
            </div>
          </section>
        )}
      </div>
    </main>
  );
}
