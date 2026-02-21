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

type Page = 'home' | 'reportes' | 'equivalencias';

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
}

interface MateriaOpcion {
  id: string;
  nombre: string;
}

interface EquivalenciaMateriaDestino {
  idMateriaEquivalente?: string;
  nombreMateriaEquivalente?: string;
  idInstitucionDestino?: string;
  nombreInstitucionDestino?: string;
  activa?: boolean;
}

interface FilaValidacionEquivalencia {
  materiaObjetivoId: string;
  materiaObjetivoNombre: string;
  aprobadaPorEquivalencia: boolean;
  materiaOrigen?: string;
  institucionOrigen?: string;
  notaConvertidaSudafrica?: number;
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
  const [detalleAcademico, setDetalleAcademico] = useState<any[]>([]);
  const [equivEstudianteId, setEquivEstudianteId] = useState('');
  const [equivUniversidadObjetivoId, setEquivUniversidadObjetivoId] = useState('');
  const [equivRows, setEquivRows] = useState<FilaValidacionEquivalencia[]>([]);
  const [equivLoading, setEquivLoading] = useState(false);
  const [page, setPage] = useState<Page>('home');
  const [fieldErrors, setFieldErrors] = useState<Record<string, string>>({});

  useEffect(() => {
    const syncPageFromHash = () => {
      const hash = window.location.hash;
      if (hash === '#/reportes') {
        setPage('reportes');
      } else if (hash === '#/equivalencias') {
        setPage('equivalencias');
      } else {
        setPage('home');
      }
    };

    syncPageFromHash();
    window.addEventListener('hashchange', syncPageFromHash);
    return () => window.removeEventListener('hashchange', syncPageFromHash);
  }, []);

  useEffect(() => {
    const cargarOpciones = async () => {
      try {
        const [estudiantesRes] = await Promise.all([
          axios.get('http://localhost:8080/api/estudiantes/opciones')
        ]);
        setOpcionesEstudiantes(estudiantesRes.data || []);
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
    const nextErrors: Record<string, string> = {};
    if (!estudianteId.trim()) nextErrors.estudianteId = 'Ingresá el ID de estudiante';
    if (!institucionId.trim()) nextErrors.institucionId = 'Ingresá el ID de institución';
    if (!materiaId.trim()) nextErrors.materiaId = 'Ingresá el ID de materia';
    if (!pais.trim()) nextErrors.pais = 'Seleccioná un país';
    if (!fechaNormativa) nextErrors.fechaNormativa = 'Seleccioná fecha normativa';

    const requiredByPais: Record<string, string[]> = {
      Argentina: ['primer_parcial', 'segundo_parcial', 'examen_final'],
      USA: ['semester', 'semester_2'],
      Inglaterra: ['coursework', 'mock_exam', 'final_grade'],
      Alemania: ['KlassenArbeit', 'MundlichArbeit'],
      Sudafrica: ['parcial', 'trabajos_practicos', 'final']
    };
    const required = requiredByPais[pais] || [];
    for (const key of required) {
      const value = gradeDetails?.[key];
      if (value === undefined || value === null || value === '') {
        nextErrors[key] = `Completá ${key}`;
      }
    }

    if (Object.keys(nextErrors).length > 0) {
      setFieldErrors(nextErrors);
      return;
    }
    setFieldErrors({});
    setLoading(true);
    try {
      const payload = {
        estudiante: estudianteId,
        estudianteId: estudianteId,
        institucion: institucionId,
        institucionId: institucionId,
        materia: materiaId,
        materiaId: materiaId,
        pais: pais,
        metadatos: {
          ...gradeDetails,
          fecha_normativa: fechaNormativa
        }
      };
      const response = await axios.post('http://localhost:8080/api/calificaciones/registrar', {
        ...payload
      });
      setResultado({ equivalencia_sudafrica: response.data.conversiones });
    } catch (error) {
      alert('Error al registrar calificacion.');
    } finally {
      setLoading(false);
    }
  };

  const validarEquivalencias = async () => {
    if (!equivEstudianteId.trim()) return alert('Ingresá el ID del estudiante');
    if (!equivUniversidadObjetivoId.trim()) return alert('Ingresá el ID de la universidad objetivo');

    setEquivLoading(true);
    try {
      const [detalleRes, materiasObjetivoRes] = await Promise.all([
        axios.get(`http://localhost:8080/api/estudiantes/${equivEstudianteId.trim()}/detalle-completo`),
        axios.get(`http://localhost:8080/api/materias/instituciones/${equivUniversidadObjetivoId.trim()}/opciones`)
      ]);

      const detalleEstudiante: any[] = detalleRes.data || [];
      const materiasObjetivo: MateriaOpcion[] = (materiasObjetivoRes.data || [])
        .slice()
        .sort((a: MateriaOpcion, b: MateriaOpcion) => (a.nombre || '').localeCompare(b.nombre || ''));

      if (materiasObjetivo.length === 0) {
        setEquivRows([]);
        alert('La universidad objetivo no tiene materias cargadas.');
        return;
      }

      const equivalenciasResults = await Promise.allSettled(
        detalleEstudiante
          .filter((d) => !!d.materiaId)
          .map(async (d) => {
            try {
              const materiaIdEncoded = encodeURIComponent(String(d.materiaId));
              const resp = await axios.get(`http://localhost:8080/api/materias/${materiaIdEncoded}/equivalencias`);
              return {
                materiaOrigenId: d.materiaId as string,
                materiaOrigenNombre: d.materia as string,
                institucionOrigen: d.institucion as string,
                notaConvertidaSudafrica: d.notaConvertidaSudafrica as number | undefined,
                equivalencias: (resp.data || []) as EquivalenciaMateriaDestino[]
              };
            } catch (e) {
              throw { materiaId: d.materiaId, causa: e };
            }
          })
      );

      const equivalenciasPorMateriaOrigen = equivalenciasResults
        .filter((r): r is PromiseFulfilledResult<{
          materiaOrigenId: string;
          materiaOrigenNombre: string;
          institucionOrigen: string;
          notaConvertidaSudafrica?: number;
          equivalencias: EquivalenciaMateriaDestino[];
        }> => r.status === 'fulfilled')
        .map((r) => r.value);

      const erroresEquivalencias = equivalenciasResults.filter((r) => r.status === 'rejected').length;
      const materiasConError = equivalenciasResults
        .filter((r): r is PromiseRejectedResult => r.status === 'rejected')
        .map((r) => (r.reason as any)?.materiaId)
        .filter((id): id is string => !!id);

      const rows: FilaValidacionEquivalencia[] = materiasObjetivo.map((mObjetivo) => {
        let match: FilaValidacionEquivalencia | null = null;

        for (const origen of equivalenciasPorMateriaOrigen) {
          const existeEq = origen.equivalencias.some(
            (eq) =>
              eq.idInstitucionDestino === equivUniversidadObjetivoId.trim() &&
              eq.idMateriaEquivalente === mObjetivo.id &&
              eq.activa !== false
          );

          if (existeEq) {
            const candidato: FilaValidacionEquivalencia = {
              materiaObjetivoId: mObjetivo.id,
              materiaObjetivoNombre: mObjetivo.nombre,
              aprobadaPorEquivalencia: true,
              materiaOrigen: origen.materiaOrigenNombre,
              institucionOrigen: origen.institucionOrigen,
              notaConvertidaSudafrica: origen.notaConvertidaSudafrica
            };

            if (!match || (candidato.notaConvertidaSudafrica ?? -1) > (match.notaConvertidaSudafrica ?? -1)) {
              match = candidato;
            }
          }
        }

        if (match) return match;

        return {
          materiaObjetivoId: mObjetivo.id,
          materiaObjetivoNombre: mObjetivo.nombre,
          aprobadaPorEquivalencia: false
        };
      });

      setEquivRows(rows);
      if (erroresEquivalencias > 0) {
        alert(
          `Se validó parcialmente. ${erroresEquivalencias} materia(s) no pudieron consultar equivalencias.` +
          (materiasConError.length > 0 ? ` IDs: ${materiasConError.join(', ')}` : '')
        );
      }
    } catch (error) {
      setEquivRows([]);
      const errorMsg =
        (axios.isAxiosError(error) && (error.response?.data as any)?.message) ||
        (axios.isAxiosError(error) && typeof error.response?.data === 'string' ? error.response?.data : null) ||
        'No se pudo validar equivalencias. Verificá IDs y datos cargados.';
      alert(errorMsg);
    } finally {
      setEquivLoading(false);
    }
  };

  const clearFieldError = (key: string) => {
    setFieldErrors((prev) => {
      if (!prev[key]) return prev;
      const next = { ...prev };
      delete next[key];
      return next;
    });
  };

  return (
    <main className="font-['Quicksand'] bg-green-600 w-full min-h-screen py-10 px-4 m-0">
      <div className="max-w-7xl mx-auto">
        <nav className="mb-8 bg-white/95 rounded-full px-6 py-4 shadow-xl flex items-center justify-between">
          <span className="font-black text-green-700">EduGrade Global</span>
          <div className="flex items-center gap-6">
            <a href="#/" className={`font-bold hover:underline ${page === 'home' ? 'text-green-700' : 'text-blue-700'}`}>Home</a>
            <a href="#/reportes" className={`font-bold hover:underline ${page === 'reportes' ? 'text-green-700' : 'text-blue-700'}`}>Reportes</a>
            <a href="#/equivalencias" className={`font-bold hover:underline ${page === 'equivalencias' ? 'text-green-700' : 'text-blue-700'}`}>Equivalencias</a>
          </div>
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
                  <input
                    className={`w-full border-2 p-4 rounded-2xl ${fieldErrors.estudianteId ? 'border-red-500 bg-red-50' : 'border-slate-100'}`}
                    placeholder="ID Alumno"
                    value={estudianteId}
                    onChange={e => { setEstudianteId(e.target.value); clearFieldError('estudianteId'); }}
                  />
                  {fieldErrors.estudianteId && <p className="text-sm text-red-600">{fieldErrors.estudianteId}</p>}
                  <input
                    className={`w-full border-2 p-4 rounded-2xl ${fieldErrors.institucionId ? 'border-red-500 bg-red-50' : 'border-slate-100'}`}
                    placeholder="ID Institución"
                    value={institucionId}
                    onChange={e => { setInstitucionId(e.target.value); clearFieldError('institucionId'); }}
                  />
                  {fieldErrors.institucionId && <p className="text-sm text-red-600">{fieldErrors.institucionId}</p>}
                  <input
                    className={`w-full border-2 p-4 rounded-2xl ${fieldErrors.materiaId ? 'border-red-500 bg-red-50' : 'border-slate-100'}`}
                    placeholder="ID Materia"
                    value={materiaId}
                    onChange={e => { setMateriaId(e.target.value); clearFieldError('materiaId'); }}
                  />
                  {fieldErrors.materiaId && <p className="text-sm text-red-600">{fieldErrors.materiaId}</p>}
                  <select
                    className={`w-full border-2 p-4 rounded-2xl bg-white ${fieldErrors.pais ? 'border-red-500 bg-red-50' : 'border-slate-100'}`}
                    value={pais}
                    onChange={e => { setPais(e.target.value); setGradeDetails({}); clearFieldError('pais'); }}
                  >
                    <option value="Argentina">Argentina</option>
                    <option value="Alemania">Alemania</option>
                    <option value="USA">USA</option>
                    <option value="Inglaterra">Inglaterra</option>
                    <option value="Sudafrica">Sudafrica</option>
                  </select>
                  {fieldErrors.pais && <p className="text-sm text-red-600">{fieldErrors.pais}</p>}
                  <input
                    type="date"
                    className={`w-full border-2 p-4 rounded-2xl bg-white ${fieldErrors.fechaNormativa ? 'border-red-500 bg-red-50' : 'border-slate-100'}`}
                    value={fechaNormativa}
                    onChange={e => { setFechaNormativa(e.target.value); clearFieldError('fechaNormativa'); }}
                  />
                  {fieldErrors.fechaNormativa && <p className="text-sm text-red-600">{fieldErrors.fechaNormativa}</p>}
                  <div className="bg-green-50/50 p-4 rounded-2xl border border-dashed border-green-200">
                    {pais === 'Argentina' && <ArgentinaForm setGradeDetails={setGradeDetails} fieldErrors={fieldErrors} clearFieldError={clearFieldError} />}
                    {pais === 'Alemania' && <GermanyForm setGradeDetails={setGradeDetails} fieldErrors={fieldErrors} clearFieldError={clearFieldError} />}
                    {pais === 'USA' && <USAForm setGradeDetails={setGradeDetails} fieldErrors={fieldErrors} clearFieldError={clearFieldError} />}
                    {pais === 'Inglaterra' && <EnglandForm setGradeDetails={setGradeDetails} fieldErrors={fieldErrors} clearFieldError={clearFieldError} />}
                    {pais === 'Sudafrica' && <SouthAfricaForm setGradeDetails={setGradeDetails} fieldErrors={fieldErrors} clearFieldError={clearFieldError} />}
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
        ) : page === 'reportes' ? (
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
          </section>
        ) : (
          <section className="bg-white/95 backdrop-blur-sm p-8 rounded-[2.5rem] shadow-2xl">
            <h2 className="text-3xl font-black text-slate-800 text-center mb-8">Validación de Equivalencias</h2>
            <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-4">
              <input
                className="w-full border-2 border-slate-100 p-4 rounded-2xl"
                placeholder="ID Estudiante"
                value={equivEstudianteId}
                onChange={(e) => setEquivEstudianteId(e.target.value)}
              />
              <input
                className="w-full border-2 border-slate-100 p-4 rounded-2xl"
                placeholder="ID Universidad Objetivo"
                value={equivUniversidadObjetivoId}
                onChange={(e) => setEquivUniversidadObjetivoId(e.target.value)}
              />
              <button
                onClick={validarEquivalencias}
                className="w-full bg-violet-600 hover:bg-violet-700 text-white py-4 rounded-2xl font-bold shadow-lg transition-all active:scale-95"
              >
                {equivLoading ? 'Validando...' : 'Validar Transferencia'}
              </button>
            </div>

            {equivRows.length > 0 && (
              <div className="mt-6">
                <table className="w-full border-collapse bg-white rounded-2xl overflow-hidden shadow">
                  <thead>
                    <tr className="bg-slate-100 text-slate-700 text-left">
                      <th className="px-3 py-2">Materia Objetivo</th>
                      <th className="px-3 py-2">Estado</th>
                      <th className="px-3 py-2">Materia Origen</th>
                      <th className="px-3 py-2">Institución Origen</th>
                      <th className="px-3 py-2">Nota SA</th>
                    </tr>
                  </thead>
                  <tbody>
                    {equivRows.map((row) => (
                      <tr key={row.materiaObjetivoId} className="border-t border-slate-100">
                        <td className="px-3 py-2 font-semibold text-slate-700">{row.materiaObjetivoNombre}</td>
                        <td className="px-3 py-2">
                          {row.aprobadaPorEquivalencia ? (
                            <span className="bg-emerald-100 text-emerald-700 font-bold px-3 py-1 rounded-full">Aprobada por equivalencia</span>
                          ) : (
                            <span className="bg-slate-100 text-slate-600 font-bold px-3 py-1 rounded-full">No equivalente</span>
                          )}
                        </td>
                        <td className="px-3 py-2 text-slate-700">{row.materiaOrigen ?? '-'}</td>
                        <td className="px-3 py-2 text-slate-700">{row.institucionOrigen ?? '-'}</td>
                        <td className="px-3 py-2 text-slate-700">
                          {row.notaConvertidaSudafrica != null ? row.notaConvertidaSudafrica.toFixed(2) : '-'}
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}
          </section>
        )}
      </div>
    </main>
  );
}
