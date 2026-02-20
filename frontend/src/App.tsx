import React, { useEffect, useState } from 'react';
import axios from 'axios';
import ArgentinaForm from './components/argentinaForm';
import GermanyForm from './components/alemaniaForm';
import USAForm from './components/usaForm';
import EnglandForm from './components/englandForm';
import SouthAfricaForm from './components/southAfricaForm';
import './App.css';

interface ConversionResponse {
  equivalencia_sudafrica: number;
}

type Page = 'home' | 'reportes';

interface InstitucionRanking {
  institucionId: string;
  institucionNombre: string;
  institucionProvincia?: string;
  promedioConversionSudafrica: number;
  cantidadEstudiantes: number;
}

interface ProvinciaRanking {
  provincia: string;
  promedioConversionSudafrica: number;
  cantidadEstudiantes: number;
}

interface NivelEducativoRanking {
  nivelEducativo: string;
  promedioConversionSudafrica: number;
  cantidadEstudiantes: number;
}

interface Opcion {
  id: string;
  nombre: string;
  pais?: string;
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
  const [rankingInstituciones, setRankingInstituciones] = useState<InstitucionRanking[]>([]);
  const [rankingProvincias, setRankingProvincias] = useState<ProvinciaRanking[]>([]);
  const [rankingNivelesEducativos, setRankingNivelesEducativos] = useState<NivelEducativoRanking[]>([]);
  const [opcionesEstudiantes, setOpcionesEstudiantes] = useState<Opcion[]>([]);
  const [opcionesInstituciones, setOpcionesInstituciones] = useState<Opcion[]>([]);
  const [opcionesMaterias, setOpcionesMaterias] = useState<Opcion[]>([]);
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

  useEffect(() => {
    const cargarOpciones = async () => {
      try {
        const [estudiantesRes, institucionesRes, materiasRes] = await Promise.all([
          axios.get('http://localhost:8080/api/estudiantes/opciones'),
          axios.get('http://localhost:8080/api/instituciones/opciones'),
          axios.get('http://localhost:8080/api/materias/opciones')
        ]);
        setOpcionesEstudiantes(estudiantesRes.data || []);
        setOpcionesInstituciones(institucionesRes.data || []);
        setOpcionesMaterias(materiasRes.data || []);
      } catch (error) {
        alert('No se pudieron cargar las opciones para registrar calificacion.');
      }
    };
    cargarOpciones();
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

  const fetchRankingMejorInstituto = async () => {
    setLoading(true);
    try {
      const response = await axios.get('http://localhost:8080/api/reportes/instituciones-ranking');
      setRankingInstituciones(response.data || []);
      setRankingProvincias([]);
      setRankingNivelesEducativos([]);
    } catch (error) {
      alert('Error al conectar con Cassandra.');
    } finally {
      setLoading(false);
    }
  };

  const fetchRankingProvincias = async () => {
    setLoading(true);
    try {
      const response = await axios.get('http://localhost:8080/api/reportes/provincias-ranking');
      setRankingProvincias(response.data || []);
      setRankingInstituciones([]);
      setRankingNivelesEducativos([]);
    } catch (error) {
      alert('Error al conectar con Cassandra.');
    } finally {
      setLoading(false);
    }
  };

  const fetchRankingNivelesEducativos = async () => {
    setLoading(true);
    try {
      const response = await axios.get('http://localhost:8080/api/reportes/niveles-educativos-ranking');
      setRankingNivelesEducativos(response.data || []);
      setRankingInstituciones([]);
      setRankingProvincias([]);
    } catch (error) {
      alert('Error al conectar con Cassandra.');
    } finally {
      setLoading(false);
    }
  };

  const handleRegistrar = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!estudianteId || !institucionId || !materiaId) {
      alert('Seleccioná estudiante, institución y materia.');
      return;
    }
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
      setEstudianteId('');
      setInstitucionId('');
      setMateriaId('');
      setPais('');
      setGradeDetails({});
      setFechaNormativa(new Date().toISOString().slice(0, 10));
    } catch (error) {
      alert('Error al registrar calificacion.');
    } finally {
      setLoading(false);
    }
  };

  const handleInstitucionChange = async (id: string) => {
    setInstitucionId(id);
    if (!id) {
      setPais('');
      setGradeDetails({});
      return;
    }
    try {
      const response = await axios.get(`http://localhost:8080/api/instituciones/${id}`);
      setPais(response.data?.pais || '');
    } catch (error) {
      const seleccionada = opcionesInstituciones.find((op) => op.id === id);
      setPais(seleccionada?.pais || '');
    }
    setGradeDetails({});
  };

  return (
    <main className="font-['Quicksand'] bg-green-600 w-full min-h-screen py-10 px-4 m-0">
      <div className="max-w-7xl mx-auto">
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
                  <select className="w-full border-2 border-slate-100 p-4 rounded-2xl bg-white" value={estudianteId} onChange={e => setEstudianteId(e.target.value)}>
                    <option value="">Seleccionar Estudiante</option>
                    {opcionesEstudiantes.map((op) => (
                      <option key={op.id} value={op.id}>{op.nombre}</option>
                    ))}
                  </select>
                  <select className="w-full border-2 border-slate-100 p-4 rounded-2xl bg-white" value={institucionId} onChange={e => handleInstitucionChange(e.target.value)}>
                    <option value="">Seleccionar Institución</option>
                    {opcionesInstituciones.map((op) => (
                      <option key={op.id} value={op.id}>{op.nombre}</option>
                    ))}
                  </select>
                  <select className="w-full border-2 border-slate-100 p-4 rounded-2xl bg-white" value={materiaId} onChange={e => setMateriaId(e.target.value)}>
                    <option value="">Seleccionar Materia</option>
                    {opcionesMaterias.map((op) => (
                      <option key={op.id} value={op.id}>{op.nombre}</option>
                    ))}
                  </select>
                  <input
                    className="w-full border-2 border-slate-100 p-4 rounded-2xl bg-slate-50 text-slate-700"
                    value={pais || ''}
                    placeholder="País (autocompletado por institución)"
                    readOnly
                  />
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
                    {pais === 'Sudafrica' && <SouthAfricaForm setGradeDetails={setGradeDetails} />}
                  </div>
                  <button type="submit" className="w-full bg-green-500 hover:bg-green-600 text-white font-bold py-4 rounded-2xl shadow-lg">
                    Registrar y Convertir
                  </button>
                </form>
              </section>

              <section className="bg-white/95 backdrop-blur-sm p-8 rounded-[2.5rem] shadow-2xl flex flex-col">
                <h2 className="text-2xl font-black text-blue-700 text-center mb-6">Consultar Calificaciones</h2>
                <select
                  className="w-full border-2 border-slate-100 p-4 rounded-2xl mb-4 bg-white"
                  value={consultaEstudianteId}
                  onChange={e => setConsultaEstudianteId(e.target.value)}
                >
                  <option value="">Seleccionar Estudiante</option>
                  {opcionesEstudiantes.map((op) => (
                    <option key={op.id} value={op.id}>{op.nombre}</option>
                  ))}
                </select>
                <button onClick={consultarDetalle}
                  className="w-full bg-blue-500 hover:bg-blue-600 text-white py-4 rounded-2xl font-bold shadow-lg mb-6 transition-all active:scale-95">
                  Consultar
                </button>
                <div className="pr-2">
                  {detalleAcademico.length > 0 && (
                    <table className="w-full border-collapse bg-white rounded-2xl overflow-hidden shadow">
                      <thead>
                        <tr className="bg-slate-100 text-slate-700 text-left">
                          <th className="px-3 py-2">Materia</th>
                          <th className="px-3 py-2">Institución</th>
                          <th className="px-3 py-2">Nota Original</th>
                          <th className="px-3 py-2">Nota SA</th>
                        </tr>
                      </thead>
                      <tbody>
                        {detalleAcademico.map((item, i) => (
                          <tr key={i} className="border-t border-slate-100">
                            <td className="px-3 py-2 font-semibold text-slate-700">{item.materia}</td>
                            <td className="px-3 py-2 text-slate-700">{item.institucion ?? '-'}</td>
                            <td className="px-3 py-2">
                              <span className="bg-green-100 text-green-700 font-black px-3 py-1 rounded-full">{item.notaOriginal}</span>
                            </td>
                            <td className="px-3 py-2 text-slate-700">
                              {item.notaConvertidaSudafrica != null ? Number(item.notaConvertidaSudafrica).toFixed(2) : '-'}
                            </td>
                          </tr>
                        ))}
                      </tbody>
                    </table>
                  )}
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
                {loading ? '...' : 'Ranking mejores institutos'}
              </button>
              <button onClick={fetchRankingProvincias} className="bg-teal-600 hover:bg-teal-700 text-white font-bold py-3 px-8 rounded-full transition-all shadow-xl active:scale-95">
                {loading ? '...' : 'Ranking por Provincias'}
              </button>
              <button onClick={fetchRankingNivelesEducativos} className="bg-indigo-600 hover:bg-indigo-700 text-white font-bold py-3 px-8 rounded-full transition-all shadow-xl active:scale-95">
                {loading ? '...' : 'Ranking por Nivel Educativo'}
              </button>
            </div>
            {rankingInstituciones.length > 0 && (
              <div className="mt-8">
                <table className="w-full border-collapse bg-white rounded-2xl overflow-hidden shadow">
                  <thead>
                    <tr className="bg-slate-100 text-slate-700 text-left">
                      <th className="px-4 py-3">Institución</th>
                      <th className="px-4 py-3">Provincia</th>
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
                        <td className="px-4 py-3">{row.institucionProvincia ?? '-'}</td>
                        <td className="px-4 py-3">{row.promedioConversionSudafrica.toFixed(2)}</td>
                        <td className="px-4 py-3">{row.cantidadEstudiantes}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}
            {rankingProvincias.length > 0 && (
              <div className="mt-8">
                <table className="w-full border-collapse bg-white rounded-2xl overflow-hidden shadow">
                  <thead>
                    <tr className="bg-slate-100 text-slate-700 text-left">
                      <th className="px-4 py-3">Provincia (Sudáfrica)</th>
                      <th className="px-4 py-3">Promedio</th>
                      <th className="px-4 py-3">Estudiantes</th>
                    </tr>
                  </thead>
                  <tbody>
                    {rankingProvincias.map((row, idx) => (
                      <tr
                        key={row.provincia}
                        className={`border-t border-slate-100 ${idx === 0 ? 'font-bold text-[18px] bg-amber-50' : 'text-base'}`}
                      >
                        <td className="px-4 py-3">{row.provincia}</td>
                        <td className="px-4 py-3">{row.promedioConversionSudafrica.toFixed(2)}</td>
                        <td className="px-4 py-3">{row.cantidadEstudiantes}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}
            {rankingNivelesEducativos.length > 0 && (
              <div className="mt-8">
                <table className="w-full border-collapse bg-white rounded-2xl overflow-hidden shadow">
                  <thead>
                    <tr className="bg-slate-100 text-slate-700 text-left">
                      <th className="px-4 py-3">Nivel Educativo (Sudáfrica)</th>
                      <th className="px-4 py-3">Promedio</th>
                      <th className="px-4 py-3">Estudiantes</th>
                    </tr>
                  </thead>
                  <tbody>
                    {rankingNivelesEducativos.map((row, idx) => (
                      <tr
                        key={row.nivelEducativo}
                        className={`border-t border-slate-100 ${idx === 0 ? 'font-bold text-[18px] bg-amber-50' : 'text-base'}`}
                      >
                        <td className="px-4 py-3">{row.nivelEducativo}</td>
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
