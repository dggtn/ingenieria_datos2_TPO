import React, { useState } from 'react';
import axios from 'axios';

type EstudianteResponse = {
  id: string;
  nombre: string;
  email: string;
  paisOrigen: string;
  institucionActual?: string | { id?: string; nombre?: string };
  materias?: CursoMateria[];
  historialAcademico?: HistorialItem[];
};

type Institucion = { id?: string; nombre?: string; pais?: string };
type Parcial = { nombre: string; nota: number };
type Materia = { id: string; nombre: string; notaFinal?: number; fechaFinalizacion?: string; notasParciales?: Parcial[] };
type CursoMateria = { materia?: Materia; notaFinal?: number; fechaRendida?: string };
type HistorialItem = { institucion?: Institucion; historialMaterias?: Materia[] };
type InstitucionFila = { key: string; tipo: 'ACTUAL' | 'HISTORICA'; institucion: Institucion; materias: Materia[] };

type Props = {
  onBackHome: () => void;
};

export default function EstudiantesPage({ onBackHome }: Props) {
  const [loading, setLoading] = useState(false);
  const [mensaje, setMensaje] = useState('');

  const [idNacional, setIdNacional] = useState('');
  const [nombre, setNombre] = useState('');
  const [email, setEmail] = useState('');
  const [paisOrigen, setPaisOrigen] = useState('');
  const [institucionActual, setInstitucionActual] = useState('');

  const [idModificar, setIdModificar] = useState('');
  const [nombreMod, setNombreMod] = useState('');
  const [emailMod, setEmailMod] = useState('');
  const [paisMod, setPaisMod] = useState('');

  const [idEliminar, setIdEliminar] = useState('');
  const [idConsulta, setIdConsulta] = useState('');
  const [estudianteConsultado, setEstudianteConsultado] = useState<EstudianteResponse | null>(null);
  const [modalInstitucionesAbierto, setModalInstitucionesAbierto] = useState(false);
  const [modalMateriasAbierto, setModalMateriasAbierto] = useState(false);
  const [modalNotasAbierto, setModalNotasAbierto] = useState(false);
  const [institucionSeleccionada, setInstitucionSeleccionada] = useState<InstitucionFila | null>(null);
  const [materiaSeleccionada, setMateriaSeleccionada] = useState<Materia | null>(null);

  const formatearInstitucion = (institucion?: string | { id?: string; nombre?: string }) => {
    if (!institucion) return '-';
    if (typeof institucion === 'string') return institucion;
    const nombre = institucion.nombre || '';
    const id = institucion.id || '';
    if (nombre && id) return `${nombre} (${id})`;
    return nombre || id || '-';
  };

  const normalizarInstitucion = (institucion?: string | { id?: string; nombre?: string }): Institucion => {
    if (!institucion) return { id: '-', nombre: 'Sin institucion' };
    if (typeof institucion === 'string') return { id: institucion, nombre: institucion };
    return { id: institucion.id || '-', nombre: institucion.nombre || institucion.id || '-', pais: (institucion as any).pais };
  };

  const mapearMateriaDesdeCurso = (c: CursoMateria): Materia | null => {
    if (!c.materia) return null;
    return {
      ...c.materia,
      notaFinal: c.materia.notaFinal ?? c.notaFinal,
      fechaFinalizacion: c.materia.fechaFinalizacion ?? c.fechaRendida
    };
  };

  const obtenerInstitucionesAsignadas = (): InstitucionFila[] => {
    if (!estudianteConsultado) return [];
    const actual: InstitucionFila = {
      key: `actual-${normalizarInstitucion(estudianteConsultado.institucionActual).id}`,
      tipo: 'ACTUAL',
      institucion: normalizarInstitucion(estudianteConsultado.institucionActual),
      materias: (estudianteConsultado.materias || [])
        .map(mapearMateriaDesdeCurso)
        .filter(Boolean) as Materia[]
    };
    const historicas: InstitucionFila[] = (estudianteConsultado.historialAcademico || []).map((h, i) => ({
      key: `hist-${h.institucion?.id || i}`,
      tipo: 'HISTORICA',
      institucion: h.institucion || { id: '-', nombre: 'Sin nombre' },
      materias: h.historialMaterias || []
    }));
    return [actual, ...historicas];
  };

  const recargarEstudiante = async (id: string) => {
    const response = await axios.get(`http://localhost:8080/api/estudiantes/${id}`);
    const actualizado = response.data as EstudianteResponse;
    setEstudianteConsultado(actualizado);
    if (institucionSeleccionada) {
      const actualInst = normalizarInstitucion(actualizado.institucionActual);
      const actualMaterias = (actualizado.materias || [])
        .map(mapearMateriaDesdeCurso)
        .filter(Boolean) as Materia[];
      const historicas = (actualizado.historialAcademico || []).map((h, i) => ({
        key: `hist-${h.institucion?.id || i}`,
        tipo: 'HISTORICA' as const,
        institucion: h.institucion || { id: '-', nombre: 'Sin nombre' },
        materias: h.historialMaterias || []
      }));
      const filas: InstitucionFila[] = [{ key: `actual-${actualInst.id || 'na'}`, tipo: 'ACTUAL', institucion: actualInst, materias: actualMaterias }, ...historicas];
      const filaNueva = filas.find((f) => f.key === institucionSeleccionada.key);
      if (filaNueva) {
        setInstitucionSeleccionada(filaNueva);
        if (materiaSeleccionada) {
          const materiaNueva = filaNueva.materias.find((m) => m.id === materiaSeleccionada.id);
          if (materiaNueva) setMateriaSeleccionada(materiaNueva);
        }
      }
    }
  };

  const abrirModalMaterias = (fila: InstitucionFila) => {
    setInstitucionSeleccionada(fila);
    setModalMateriasAbierto(true);
  };

  const abrirModalNotas = (materia: Materia) => {
    setMateriaSeleccionada(materia);
    setModalNotasAbierto(true);
  };

  const crearCurso = async () => {
    if (!estudianteConsultado || !institucionSeleccionada) return;
    if (institucionSeleccionada.tipo !== 'ACTUAL') return setMensaje('Solo se puede crear curso en institucion actual.');
    const codigo = window.prompt('ID del curso:');
    const nombreCurso = window.prompt('Nombre del curso:');
    if (!codigo || !nombreCurso) return;
    setLoading(true);
    try {
      await axios.post(`http://localhost:8080/api/materias/estudiantes/${estudianteConsultado.id}`, { codigo, nombre: nombreCurso });
      await recargarEstudiante(estudianteConsultado.id);
      setMensaje('Curso creado correctamente.');
    } catch {
      setMensaje('No se pudo crear el curso.');
    } finally {
      setLoading(false);
    }
  };

  const modificarCurso = async (idMateria: string, nombreActual: string) => {
    const nombreNuevo = window.prompt('Nuevo nombre del curso:', nombreActual);
    if (!nombreNuevo) return;
    setLoading(true);
    try {
      await axios.put(`http://localhost:8080/api/materias/${idMateria}`, { nombre: nombreNuevo });
      if (estudianteConsultado) await recargarEstudiante(estudianteConsultado.id);
      setMensaje('Curso modificado correctamente.');
    } catch {
      setMensaje('No se pudo modificar el curso.');
    } finally {
      setLoading(false);
    }
  };

  const eliminarCurso = async (idMateria: string) => {
    if (!estudianteConsultado) return;
    setLoading(true);
    try {
      await axios.delete(`http://localhost:8080/api/materias/estudiantes/${estudianteConsultado.id}/${idMateria}`);
      await recargarEstudiante(estudianteConsultado.id);
      setMensaje('Curso eliminado correctamente.');
    } catch {
      setMensaje('No se pudo eliminar el curso.');
    } finally {
      setLoading(false);
    }
  };

  const guardarFinal = async (materia: Materia) => {
    if (!estudianteConsultado) return;
    const notaFinal = window.prompt('Nota final:', materia.notaFinal != null ? String(materia.notaFinal) : '');
    const fechaFinalizacion = window.prompt('Fecha finalizacion (YYYY-MM-DD):', materia.fechaFinalizacion ? String(materia.fechaFinalizacion).slice(0, 10) : '');
    if (!notaFinal || !fechaFinalizacion) return;
    setLoading(true);
    try {
      await axios.put(`http://localhost:8080/api/calificaciones/estudiantes/${estudianteConsultado.id}/materias/${materia.id}/final`, {
        notaFinal: Number(notaFinal),
        fechaFinalizacion
      });
      await recargarEstudiante(estudianteConsultado.id);
      setMensaje('Final actualizado correctamente.');
    } catch {
      setMensaje('No se pudo actualizar el final.');
    } finally {
      setLoading(false);
    }
  };

  const crearParcial = async (materia: Materia) => {
    if (!estudianteConsultado) return;
    if (institucionSeleccionada?.tipo !== 'ACTUAL') {
      setMensaje('Solo se puede gestionar parciales en cursos de la institucion actual.');
      return;
    }
    const nombreParcial = window.prompt('Nombre del parcial:');
    const notaParcial = window.prompt('Nota del parcial:');
    if (!nombreParcial || !notaParcial) return;
    setLoading(true);
    try {
      await axios.post(
        `http://localhost:8080/api/calificaciones/estudiantes/${estudianteConsultado.id}/materias/${materia.id}/parciales`,
        { nombre: nombreParcial, nota: Number(notaParcial) }
      );
      await recargarEstudiante(estudianteConsultado.id);
      setMensaje('Parcial agregado correctamente.');
    } catch {
      setMensaje('No se pudo agregar el parcial.');
    } finally {
      setLoading(false);
    }
  };

  const modificarParcial = async (materia: Materia, parcial: Parcial) => {
    if (!estudianteConsultado) return;
    if (institucionSeleccionada?.tipo !== 'ACTUAL') {
      setMensaje('Solo se puede gestionar parciales en cursos de la institucion actual.');
      return;
    }
    const nuevoNombre = window.prompt('Nombre del parcial:', parcial.nombre);
    const nuevaNota = window.prompt('Nota del parcial:', String(parcial.nota));
    if (!nuevoNombre || !nuevaNota) return;
    setLoading(true);
    try {
      await axios.put(
        `http://localhost:8080/api/calificaciones/estudiantes/${estudianteConsultado.id}/materias/${materia.id}/parciales/${encodeURIComponent(parcial.nombre)}`,
        { nombre: nuevoNombre, nota: Number(nuevaNota) }
      );
      await recargarEstudiante(estudianteConsultado.id);
      setMensaje('Parcial modificado correctamente.');
    } catch {
      setMensaje('No se pudo modificar el parcial.');
    } finally {
      setLoading(false);
    }
  };

  const eliminarParcial = async (materia: Materia, parcial: Parcial) => {
    if (!estudianteConsultado) return;
    if (institucionSeleccionada?.tipo !== 'ACTUAL') {
      setMensaje('Solo se puede gestionar parciales en cursos de la institucion actual.');
      return;
    }
    setLoading(true);
    try {
      await axios.delete(
        `http://localhost:8080/api/calificaciones/estudiantes/${estudianteConsultado.id}/materias/${materia.id}/parciales/${encodeURIComponent(parcial.nombre)}`
      );
      await recargarEstudiante(estudianteConsultado.id);
      setMensaje('Parcial eliminado correctamente.');
    } catch {
      setMensaje('No se pudo eliminar el parcial.');
    } finally {
      setLoading(false);
    }
  };

  const limpiarAlta = () => {
    setIdNacional('');
    setNombre('');
    setEmail('');
    setPaisOrigen('');
    setInstitucionActual('');
  };

  const limpiarModificar = () => {
    setIdModificar('');
    setNombreMod('');
    setEmailMod('');
    setPaisMod('');
  };

  const limpiarBaja = () => setIdEliminar('');
  const limpiarConsulta = () => setIdConsulta('');

  const handleAlta = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!idNacional || !nombre) {
      setMensaje('Completá al menos ID nacional y nombre para dar de alta.');
      return;
    }
    setLoading(true);
    setMensaje('');
    try {
      await axios.post('http://localhost:8080/api/estudiantes/registrar', {
        idNacional,
        nombre,
        email,
        paisOrigen,
        institucionActual
      });
      setMensaje('Alta de estudiante exitosa.');
    } catch (error) {
      setMensaje('Alta de estudiante fallida.');
    } finally {
      setLoading(false);
      limpiarAlta();
    }
  };

  const handleModificar = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!idModificar) {
      setMensaje('Ingresá el ID del estudiante a modificar.');
      return;
    }
    setLoading(true);
    setMensaje('');
    try {
      await axios.put(`http://localhost:8080/api/estudiantes/${idModificar}`, {
        nombre: nombreMod || null,
        email: emailMod || null,
        paisOrigen: paisMod || null
      });
      setMensaje('Modificacion de estudiante exitosa.');
    } catch (error) {
      setMensaje('Modificacion de estudiante fallida.');
    } finally {
      setLoading(false);
      limpiarModificar();
    }
  };

  const handleEliminar = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!idEliminar) {
      setMensaje('Ingresá el ID del estudiante a eliminar.');
      return;
    }
    setLoading(true);
    setMensaje('');
    try {
      await axios.delete(`http://localhost:8080/api/estudiantes/${idEliminar}`);
      setMensaje('Baja de estudiante exitosa.');
    } catch (error) {
      setMensaje('Baja de estudiante fallida.');
    } finally {
      setLoading(false);
      limpiarBaja();
    }
  };

  const handleConsultar = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!idConsulta) {
      setMensaje('Ingresá el ID del estudiante a consultar.');
      return;
    }
    setLoading(true);
    setMensaje('');
    try {
      const response = await axios.get(`http://localhost:8080/api/estudiantes/${idConsulta}`);
      setEstudianteConsultado(response.data);
      setMensaje('Consulta de estudiante exitosa.');
    } catch (error) {
      setEstudianteConsultado(null);
      setMensaje('Consulta de estudiante fallida.');
    } finally {
      setLoading(false);
      limpiarConsulta();
    }
  };

  return (
    <div className="max-w-5xl mx-auto">
      <div className="flex justify-between items-center mb-6">
        <h2 className="text-3xl font-black text-white drop-shadow-md">ABM de Estudiantes</h2>
        <button
          type="button"
          onClick={onBackHome}
          className="bg-white text-green-700 px-6 py-2 rounded-full font-bold shadow"
        >
          Volver a Home
        </button>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-4 gap-6">
        <section className="bg-white/95 p-6 rounded-[2rem] shadow-2xl">
          <h3 className="text-xl font-black text-green-800 mb-4">Alta</h3>
          <form onSubmit={handleAlta} className="space-y-3">
            <input className="w-full border-2 border-slate-100 p-3 rounded-xl" placeholder="ID Nacional" value={idNacional} onChange={(e) => setIdNacional(e.target.value)} />
            <input className="w-full border-2 border-slate-100 p-3 rounded-xl" placeholder="Nombre" value={nombre} onChange={(e) => setNombre(e.target.value)} />
            <input className="w-full border-2 border-slate-100 p-3 rounded-xl" placeholder="Email" value={email} onChange={(e) => setEmail(e.target.value)} />
            <input className="w-full border-2 border-slate-100 p-3 rounded-xl" placeholder="Pais Origen" value={paisOrigen} onChange={(e) => setPaisOrigen(e.target.value)} />
            <input className="w-full border-2 border-slate-100 p-3 rounded-xl" placeholder="Institucion Actual" value={institucionActual} onChange={(e) => setInstitucionActual(e.target.value)} />
            <button type="submit" className="w-full bg-green-500 hover:bg-green-600 text-white py-3 rounded-xl font-bold shadow">
              {loading ? 'Procesando...' : 'Crear Estudiante'}
            </button>
          </form>
        </section>

        <section className="bg-white/95 p-6 rounded-[2rem] shadow-2xl">
          <h3 className="text-xl font-black text-blue-700 mb-4">Modificar</h3>
          <form onSubmit={handleModificar} className="space-y-3">
            <input className="w-full border-2 border-slate-100 p-3 rounded-xl" placeholder="ID Nacional" value={idModificar} onChange={(e) => setIdModificar(e.target.value)} />
            <input className="w-full border-2 border-slate-100 p-3 rounded-xl" placeholder="Nuevo nombre" value={nombreMod} onChange={(e) => setNombreMod(e.target.value)} />
            <input className="w-full border-2 border-slate-100 p-3 rounded-xl" placeholder="Nuevo email" value={emailMod} onChange={(e) => setEmailMod(e.target.value)} />
            <input className="w-full border-2 border-slate-100 p-3 rounded-xl" placeholder="Nuevo pais" value={paisMod} onChange={(e) => setPaisMod(e.target.value)} />
            <button type="submit" className="w-full bg-blue-500 hover:bg-blue-600 text-white py-3 rounded-xl font-bold shadow">
              {loading ? 'Procesando...' : 'Modificar Estudiante'}
            </button>
          </form>
        </section>

        <section className="bg-white/95 p-6 rounded-[2rem] shadow-2xl">
          <h3 className="text-xl font-black text-rose-700 mb-4">Baja</h3>
          <form onSubmit={handleEliminar} className="space-y-3">
            <input className="w-full border-2 border-slate-100 p-3 rounded-xl" placeholder="ID Nacional" value={idEliminar} onChange={(e) => setIdEliminar(e.target.value)} />
            <button type="submit" className="w-full bg-rose-500 hover:bg-rose-600 text-white py-3 rounded-xl font-bold shadow">
              {loading ? 'Procesando...' : 'Eliminar Estudiante'}
            </button>
          </form>
        </section>

        <section className="bg-white/95 p-6 rounded-[2rem] shadow-2xl">
          <h3 className="text-xl font-black text-cyan-700 mb-4">Consulta</h3>
          <form onSubmit={handleConsultar} className="space-y-3">
            <input className="w-full border-2 border-slate-100 p-3 rounded-xl" placeholder="ID Nacional" value={idConsulta} onChange={(e) => setIdConsulta(e.target.value)} />
            <button type="submit" className="w-full bg-cyan-500 hover:bg-cyan-600 text-white py-3 rounded-xl font-bold shadow">
              {loading ? 'Procesando...' : 'Consultar Estudiante'}
            </button>
          </form>
          {estudianteConsultado && (
            <div className="mt-4 text-left text-sm text-slate-700 space-y-1">
              <p><strong>ID:</strong> {estudianteConsultado.id}</p>
              <p><strong>Nombre:</strong> {estudianteConsultado.nombre}</p>
              <p><strong>Email:</strong> {estudianteConsultado.email}</p>
              <p><strong>Pais:</strong> {estudianteConsultado.paisOrigen}</p>
              <p><strong>Institucion:</strong> {formatearInstitucion(estudianteConsultado.institucionActual)}</p>
              <button
                type="button"
                onClick={() => setModalInstitucionesAbierto(true)}
                className="mt-2 w-full bg-indigo-500 hover:bg-indigo-600 text-white py-2 rounded-xl font-bold shadow"
              >
                Consultar institucion materia
              </button>
            </div>
          )}
        </section>
      </div>

      {modalInstitucionesAbierto && estudianteConsultado && (
        <div className="fixed inset-0 bg-black/50 z-50 flex items-center justify-center p-4">
          <div className="bg-white rounded-3xl shadow-2xl w-full max-w-5xl p-6">
            <div className="flex justify-between items-center mb-4">
              <h3 className="text-2xl font-black text-slate-800">Instituciones asignadas</h3>
              <button type="button" onClick={() => setModalInstitucionesAbierto(false)} className="bg-slate-100 hover:bg-slate-200 text-slate-700 px-4 py-2 rounded-xl font-bold">Cerrar</button>
            </div>
            <div className="mb-4 p-3 bg-emerald-50 border border-emerald-200 rounded-xl">
              <p className="font-black text-emerald-800 text-xs uppercase">Institucion actual</p>
              <p className="font-bold text-emerald-900">{formatearInstitucion(estudianteConsultado.institucionActual)}</p>
            </div>
            <div className="overflow-auto max-h-[360px] border border-slate-200 rounded-xl">
              <table className="w-full text-left border-collapse">
                <thead className="bg-slate-100 sticky top-0">
                  <tr>
                    <th className="px-4 py-3 font-black text-slate-700">Tipo</th>
                    <th className="px-4 py-3 font-black text-slate-700">ID</th>
                    <th className="px-4 py-3 font-black text-slate-700">Institucion</th>
                    <th className="px-4 py-3 font-black text-slate-700 text-right">Acciones</th>
                  </tr>
                </thead>
                <tbody>
                  {obtenerInstitucionesAsignadas().map((fila) => (
                    <tr key={fila.key} className="border-t border-slate-100">
                      <td className="px-4 py-3">{fila.tipo}</td>
                      <td className="px-4 py-3 font-semibold">{fila.institucion.id || '-'}</td>
                      <td className="px-4 py-3">{fila.institucion.nombre || '-'}</td>
                      <td className="px-4 py-3">
                        <div className="flex justify-end gap-2">
                          <button type="button" onClick={() => setMensaje('Eliminar institucion historica requiere endpoint dedicado.')} className="bg-rose-500 hover:bg-rose-600 text-white px-3 py-1 rounded-lg font-bold">Eliminar</button>
                          <button type="button" onClick={() => setMensaje('Modificar institucion historica requiere endpoint dedicado.')} className="bg-amber-500 hover:bg-amber-600 text-white px-3 py-1 rounded-lg font-bold">Modificar</button>
                          <button type="button" onClick={() => abrirModalMaterias(fila)} className="bg-blue-500 hover:bg-blue-600 text-white px-3 py-1 rounded-lg font-bold">Materias cursadas</button>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        </div>
      )}

      {modalMateriasAbierto && institucionSeleccionada && (
        <div className="fixed inset-0 bg-black/50 z-50 flex items-center justify-center p-4">
          <div className="bg-white rounded-3xl shadow-2xl w-full max-w-5xl p-6">
            <div className="flex justify-between items-center mb-4">
              <h3 className="text-2xl font-black text-slate-800">Cursos de {institucionSeleccionada.institucion.nombre || institucionSeleccionada.institucion.id}</h3>
              <button type="button" onClick={() => setModalMateriasAbierto(false)} className="bg-slate-100 hover:bg-slate-200 text-slate-700 px-4 py-2 rounded-xl font-bold">Cerrar</button>
            </div>
            <div className="mb-3 flex justify-end">
              <button type="button" onClick={crearCurso} className="bg-green-500 hover:bg-green-600 text-white w-10 h-10 rounded-full text-2xl font-black leading-none">+</button>
            </div>
            <div className="overflow-auto max-h-[360px] border border-slate-200 rounded-xl">
              <table className="w-full text-left border-collapse">
                <thead className="bg-slate-100 sticky top-0">
                  <tr>
                    <th className="px-4 py-3 font-black text-slate-700">ID</th>
                    <th className="px-4 py-3 font-black text-slate-700">Curso</th>
                    <th className="px-4 py-3 font-black text-slate-700">Final</th>
                    <th className="px-4 py-3 font-black text-slate-700 text-right">Acciones</th>
                  </tr>
                </thead>
                <tbody>
                  {institucionSeleccionada.materias.length === 0 && (
                    <tr><td colSpan={4} className="px-4 py-4 text-center text-slate-500">No hay cursos cargados.</td></tr>
                  )}
                  {institucionSeleccionada.materias.map((materia) => (
                    <tr key={materia.id} className="border-t border-slate-100">
                      <td className="px-4 py-3 font-semibold">{materia.id}</td>
                      <td className="px-4 py-3">{materia.nombre}</td>
                      <td className="px-4 py-3">{materia.notaFinal ?? '-'}</td>
                      <td className="px-4 py-3">
                        <div className="flex justify-end gap-2">
                          <button type="button" onClick={() => eliminarCurso(materia.id)} className="bg-rose-500 hover:bg-rose-600 text-white px-3 py-1 rounded-lg font-bold">Eliminar</button>
                          <button type="button" onClick={() => modificarCurso(materia.id, materia.nombre)} className="bg-amber-500 hover:bg-amber-600 text-white px-3 py-1 rounded-lg font-bold">Modificar</button>
                          <button type="button" onClick={() => abrirModalNotas(materia)} className="bg-indigo-500 hover:bg-indigo-600 text-white px-3 py-1 rounded-lg font-bold">Listar notas</button>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        </div>
      )}

      {modalNotasAbierto && materiaSeleccionada && (
        <div className="fixed inset-0 bg-black/50 z-50 flex items-center justify-center p-4">
          <div className="bg-white rounded-3xl shadow-2xl w-full max-w-3xl p-6">
            <div className="flex justify-between items-center mb-4">
              <h3 className="text-2xl font-black text-slate-800">Notas de {materiaSeleccionada.nombre}</h3>
              <button type="button" onClick={() => setModalNotasAbierto(false)} className="bg-slate-100 hover:bg-slate-200 text-slate-700 px-4 py-2 rounded-xl font-bold">Cerrar</button>
            </div>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mb-4">
              <div className="bg-slate-50 rounded-xl p-4 border border-slate-200">
                <p className="text-xs font-black uppercase text-slate-500">Calificacion final</p>
                <p className="text-2xl font-black text-slate-900">{materiaSeleccionada.notaFinal ?? '-'}</p>
              </div>
              <div className="bg-slate-50 rounded-xl p-4 border border-slate-200">
                <p className="text-xs font-black uppercase text-slate-500">Fecha de aprobacion</p>
                <p className="text-2xl font-black text-slate-900">{materiaSeleccionada.fechaFinalizacion ? String(materiaSeleccionada.fechaFinalizacion).slice(0, 10) : '-'}</p>
              </div>
            </div>
            <div className="mb-3 flex justify-end">
              <div className="flex gap-2">
                <button type="button" onClick={() => crearParcial(materiaSeleccionada)} className="bg-green-500 hover:bg-green-600 text-white px-4 py-2 rounded-lg font-bold">+ Parcial</button>
                <button type="button" onClick={() => guardarFinal(materiaSeleccionada)} className="bg-blue-500 hover:bg-blue-600 text-white px-4 py-2 rounded-lg font-bold">Guardar final</button>
              </div>
            </div>
            <div className="overflow-auto max-h-[240px] border border-slate-200 rounded-xl">
              <table className="w-full text-left border-collapse">
                <thead className="bg-slate-100 sticky top-0">
                  <tr>
                    <th className="px-4 py-3 font-black text-slate-700">Parcial</th>
                    <th className="px-4 py-3 font-black text-slate-700">Nota</th>
                    <th className="px-4 py-3 font-black text-slate-700 text-right">Acciones</th>
                  </tr>
                </thead>
                <tbody>
                  {(materiaSeleccionada.notasParciales || []).length === 0 && (
                    <tr><td colSpan={3} className="px-4 py-4 text-center text-slate-500">Sin parciales cargados.</td></tr>
                  )}
                  {(materiaSeleccionada.notasParciales || []).map((parcial) => (
                    <tr key={parcial.nombre} className="border-t border-slate-100">
                      <td className="px-4 py-3">{parcial.nombre}</td>
                      <td className="px-4 py-3">{parcial.nota}</td>
                      <td className="px-4 py-3">
                        <div className="flex justify-end gap-2">
                          <button type="button" onClick={() => modificarParcial(materiaSeleccionada, parcial)} className="bg-amber-500 hover:bg-amber-600 text-white px-3 py-1 rounded-lg font-bold">Modificar</button>
                          <button type="button" onClick={() => eliminarParcial(materiaSeleccionada, parcial)} className="bg-rose-500 hover:bg-rose-600 text-white px-3 py-1 rounded-lg font-bold">Eliminar</button>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        </div>
      )}

      {mensaje && (
        <div className="mt-6 bg-white/95 rounded-2xl p-4 text-slate-700 font-semibold shadow">
          {mensaje}
        </div>
      )}
    </div>
  );
}
