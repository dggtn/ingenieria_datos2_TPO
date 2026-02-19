import React, { useState } from 'react';
import axios from 'axios';

type Curso = {
  id: string;
  nombre: string;
};

type InstitucionResponse = {
  id: string;
  nombre: string;
  pais: string;
  provincia: string;
  nivelEducativo: string;
  curriculum: Curso[];
};

type Props = {
  onBackHome: () => void;
};

export default function InstitucionesPage({ onBackHome }: Props) {
  const [loading, setLoading] = useState(false);
  const [mensaje, setMensaje] = useState('');

  const [padron, setPadron] = useState('');
  const [nombre, setNombre] = useState('');
  const [pais, setPais] = useState('');
  const [provincia, setProvincia] = useState('');
  const [nivelEducativo, setNivelEducativo] = useState('Secundaria');

  const [idModificar, setIdModificar] = useState('');
  const [nombreMod, setNombreMod] = useState('');
  const [paisMod, setPaisMod] = useState('');
  const [provinciaMod, setProvinciaMod] = useState('');
  const [nivelMod, setNivelMod] = useState('Secundaria');

  const [idEliminar, setIdEliminar] = useState('');
  const [idConsulta, setIdConsulta] = useState('');
  const [institucionConsultada, setInstitucionConsultada] = useState<InstitucionResponse | null>(null);

  const [modalCursosAbierto, setModalCursosAbierto] = useState(false);
  const [cursos, setCursos] = useState<Curso[]>([]);
  const [nuevoCursoId, setNuevoCursoId] = useState('');
  const [nuevoCursoNombre, setNuevoCursoNombre] = useState('');
  const [cursoEditandoId, setCursoEditandoId] = useState<string | null>(null);
  const [cursoEditandoNombre, setCursoEditandoNombre] = useState('');

  const limpiarAlta = () => {
    setPadron('');
    setNombre('');
    setPais('');
    setProvincia('');
    setNivelEducativo('Secundaria');
  };

  const limpiarModificar = () => {
    setIdModificar('');
    setNombreMod('');
    setPaisMod('');
    setProvinciaMod('');
    setNivelMod('Secundaria');
  };

  const limpiarBaja = () => setIdEliminar('');
  const limpiarConsulta = () => setIdConsulta('');

  const handleAlta = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!padron || !nombre) {
      setMensaje('Completa al menos padron y nombre para dar de alta.');
      return;
    }
    setLoading(true);
    setMensaje('');
    try {
      await axios.post('http://localhost:8080/api/instituciones/registrar', {
        padron,
        nombre,
        pais,
        provincia,
        nivelEducativo
      });
      setMensaje('Alta de institucion exitosa.');
    } catch (error) {
      setMensaje('Alta de institucion fallida.');
    } finally {
      setLoading(false);
      limpiarAlta();
    }
  };

  const handleModificar = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!idModificar) {
      setMensaje('Ingresa el padron/ID de la institucion a modificar.');
      return;
    }
    setLoading(true);
    setMensaje('');
    try {
      await axios.put(`http://localhost:8080/api/instituciones/${idModificar}`, {
        nombre: nombreMod || null,
        pais: paisMod || null,
        provincia: provinciaMod || null,
        nivelEducativo: nivelMod || null
      });
      setMensaje('Modificacion de institucion exitosa.');
    } catch (error) {
      setMensaje('Modificacion de institucion fallida.');
    } finally {
      setLoading(false);
      limpiarModificar();
    }
  };

  const handleEliminar = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!idEliminar) {
      setMensaje('Ingresa el padron/ID de la institucion a eliminar.');
      return;
    }
    setLoading(true);
    setMensaje('');
    try {
      await axios.delete(`http://localhost:8080/api/instituciones/${idEliminar}`);
      setMensaje('Baja de institucion exitosa.');
    } catch (error) {
      setMensaje('Baja de institucion fallida.');
    } finally {
      setLoading(false);
      limpiarBaja();
    }
  };

  const handleConsultar = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!idConsulta) {
      setMensaje('Ingresa el padron/ID de la institucion a consultar.');
      return;
    }
    setLoading(true);
    setMensaje('');
    try {
      const response = await axios.get(`http://localhost:8080/api/instituciones/${idConsulta}`);
      setInstitucionConsultada(response.data);
      setMensaje('Consulta de institucion exitosa.');
    } catch (error) {
      setInstitucionConsultada(null);
      setMensaje('Consulta de institucion fallida.');
    } finally {
      setLoading(false);
      limpiarConsulta();
    }
  };

  const cargarCursos = async (idInstitucion: string) => {
    const response = await axios.get(`http://localhost:8080/api/instituciones/${idInstitucion}/curriculum`);
    setCursos(response.data || []);
  };

  const abrirModalCursos = async () => {
    if (!institucionConsultada) return;
    setLoading(true);
    try {
      await cargarCursos(institucionConsultada.id);
      setModalCursosAbierto(true);
      setMensaje('Curriculum cargado.');
    } catch (error) {
      setMensaje('No se pudo cargar el curriculum.');
    } finally {
      setLoading(false);
    }
  };

  const crearCurso = async () => {
    if (!institucionConsultada) return;
    if (!nuevoCursoId || !nuevoCursoNombre) {
      setMensaje('Para crear curso, completa ID y nombre.');
      return;
    }
    setLoading(true);
    try {
      await axios.post(`http://localhost:8080/api/instituciones/${institucionConsultada.id}/cursos`, {
        id: nuevoCursoId,
        nombre: nuevoCursoNombre
      });
      await cargarCursos(institucionConsultada.id);
      setNuevoCursoId('');
      setNuevoCursoNombre('');
      setMensaje('Curso creado correctamente.');
    } catch (error) {
      setMensaje('No se pudo crear el curso.');
    } finally {
      setLoading(false);
    }
  };

  const guardarEdicionCurso = async (cursoId: string) => {
    if (!institucionConsultada) return;
    if (!cursoEditandoNombre.trim()) {
      setMensaje('El nombre del curso no puede estar vacio.');
      return;
    }
    setLoading(true);
    try {
      await axios.put(`http://localhost:8080/api/instituciones/${institucionConsultada.id}/cursos/${cursoId}`, {
        nombre: cursoEditandoNombre.trim()
      });
      setCursoEditandoId(null);
      setCursoEditandoNombre('');
      await cargarCursos(institucionConsultada.id);
      setMensaje('Curso modificado correctamente.');
    } catch (error) {
      setMensaje('No se pudo modificar el curso.');
    } finally {
      setLoading(false);
    }
  };

  const eliminarCurso = async (cursoId: string) => {
    if (!institucionConsultada) return;
    setLoading(true);
    try {
      await axios.delete(`http://localhost:8080/api/instituciones/${institucionConsultada.id}/cursos/${cursoId}`);
      await cargarCursos(institucionConsultada.id);
      setMensaje('Curso eliminado correctamente.');
    } catch (error) {
      setMensaje('No se pudo eliminar el curso.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="max-w-5xl mx-auto">
      <div className="flex justify-between items-center mb-6">
        <h2 className="text-3xl font-black text-white drop-shadow-md">ABM de Instituciones</h2>
        <button type="button" onClick={onBackHome} className="bg-white text-green-700 px-6 py-2 rounded-full font-bold shadow">
          Volver a Home
        </button>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-4 gap-6">
        <section className="bg-white/95 p-6 rounded-[2rem] shadow-2xl">
          <h3 className="text-xl font-black text-green-800 mb-4">Alta</h3>
          <form onSubmit={handleAlta} className="space-y-3">
            <input className="w-full border-2 border-slate-100 p-3 rounded-xl" placeholder="Padron" value={padron} onChange={(e) => setPadron(e.target.value)} />
            <input className="w-full border-2 border-slate-100 p-3 rounded-xl" placeholder="Nombre" value={nombre} onChange={(e) => setNombre(e.target.value)} />
            <input className="w-full border-2 border-slate-100 p-3 rounded-xl" placeholder="Pais" value={pais} onChange={(e) => setPais(e.target.value)} />
            <input className="w-full border-2 border-slate-100 p-3 rounded-xl" placeholder="Provincia" value={provincia} onChange={(e) => setProvincia(e.target.value)} />
            <select className="w-full border-2 border-slate-100 p-3 rounded-xl bg-white" value={nivelEducativo} onChange={(e) => setNivelEducativo(e.target.value)}>
              <option value="Primaria">Primaria</option>
              <option value="Secundaria">Secundaria</option>
              <option value="Universidad">Universidad</option>
            </select>
            <button type="submit" className="w-full bg-green-500 hover:bg-green-600 text-white py-3 rounded-xl font-bold shadow">
              {loading ? 'Procesando...' : 'Crear Institucion'}
            </button>
          </form>
        </section>

        <section className="bg-white/95 p-6 rounded-[2rem] shadow-2xl">
          <h3 className="text-xl font-black text-blue-700 mb-4">Modificar</h3>
          <form onSubmit={handleModificar} className="space-y-3">
            <input className="w-full border-2 border-slate-100 p-3 rounded-xl" placeholder="Padron/ID" value={idModificar} onChange={(e) => setIdModificar(e.target.value)} />
            <input className="w-full border-2 border-slate-100 p-3 rounded-xl" placeholder="Nuevo nombre" value={nombreMod} onChange={(e) => setNombreMod(e.target.value)} />
            <input className="w-full border-2 border-slate-100 p-3 rounded-xl" placeholder="Nuevo pais" value={paisMod} onChange={(e) => setPaisMod(e.target.value)} />
            <input className="w-full border-2 border-slate-100 p-3 rounded-xl" placeholder="Nueva provincia" value={provinciaMod} onChange={(e) => setProvinciaMod(e.target.value)} />
            <select className="w-full border-2 border-slate-100 p-3 rounded-xl bg-white" value={nivelMod} onChange={(e) => setNivelMod(e.target.value)}>
              <option value="Primaria">Primaria</option>
              <option value="Secundaria">Secundaria</option>
              <option value="Universidad">Universidad</option>
            </select>
            <button type="submit" className="w-full bg-blue-500 hover:bg-blue-600 text-white py-3 rounded-xl font-bold shadow">
              {loading ? 'Procesando...' : 'Modificar Institucion'}
            </button>
          </form>
        </section>

        <section className="bg-white/95 p-6 rounded-[2rem] shadow-2xl">
          <h3 className="text-xl font-black text-rose-700 mb-4">Baja</h3>
          <form onSubmit={handleEliminar} className="space-y-3">
            <input className="w-full border-2 border-slate-100 p-3 rounded-xl" placeholder="Padron/ID" value={idEliminar} onChange={(e) => setIdEliminar(e.target.value)} />
            <button type="submit" className="w-full bg-rose-500 hover:bg-rose-600 text-white py-3 rounded-xl font-bold shadow">
              {loading ? 'Procesando...' : 'Eliminar Institucion'}
            </button>
          </form>
        </section>

        <section className="bg-white/95 p-6 rounded-[2rem] shadow-2xl">
          <h3 className="text-xl font-black text-cyan-700 mb-4">Consulta</h3>
          <form onSubmit={handleConsultar} className="space-y-3">
            <input className="w-full border-2 border-slate-100 p-3 rounded-xl" placeholder="Padron/ID" value={idConsulta} onChange={(e) => setIdConsulta(e.target.value)} />
            <button type="submit" className="w-full bg-cyan-500 hover:bg-cyan-600 text-white py-3 rounded-xl font-bold shadow">
              {loading ? 'Procesando...' : 'Consultar Institucion'}
            </button>
          </form>
          {institucionConsultada && (
            <div className="mt-4 text-left text-sm text-slate-700 space-y-2">
              <p><strong>ID:</strong> {institucionConsultada.id}</p>
              <p><strong>Nombre:</strong> {institucionConsultada.nombre}</p>
              <p><strong>Pais:</strong> {institucionConsultada.pais}</p>
              <p><strong>Provincia:</strong> {institucionConsultada.provincia}</p>
              <p><strong>Nivel:</strong> {institucionConsultada.nivelEducativo}</p>
              <button type="button" onClick={abrirModalCursos} className="mt-2 w-full bg-indigo-500 hover:bg-indigo-600 text-white py-2 rounded-xl font-bold shadow">
                Consultar materias
              </button>
            </div>
          )}
        </section>
      </div>

      {modalCursosAbierto && institucionConsultada && (
        <div className="fixed inset-0 bg-black/50 z-50 flex items-center justify-center p-4">
          <div className="bg-white rounded-3xl shadow-2xl w-full max-w-3xl p-6">
            <div className="flex justify-between items-center mb-4">
              <h3 className="text-2xl font-black text-slate-800">
                Cursos de {institucionConsultada.nombre}
              </h3>
              <button
                type="button"
                onClick={() => setModalCursosAbierto(false)}
                className="bg-slate-100 hover:bg-slate-200 text-slate-700 px-4 py-2 rounded-xl font-bold"
              >
                Cerrar
              </button>
            </div>

            <div className="mb-4 p-3 bg-slate-50 rounded-xl border border-slate-200">
              <div className="flex items-center gap-2 mb-2">
                <button type="button" onClick={crearCurso} className="bg-green-500 hover:bg-green-600 text-white w-9 h-9 rounded-full font-black text-xl leading-none">
                  +
                </button>
                <span className="font-bold text-slate-700">Crear curso</span>
              </div>
              <div className="grid grid-cols-1 md:grid-cols-2 gap-2">
                <input
                  className="border-2 border-slate-100 p-2 rounded-lg"
                  placeholder="ID curso"
                  value={nuevoCursoId}
                  onChange={(e) => setNuevoCursoId(e.target.value)}
                />
                <input
                  className="border-2 border-slate-100 p-2 rounded-lg"
                  placeholder="Nombre curso"
                  value={nuevoCursoNombre}
                  onChange={(e) => setNuevoCursoNombre(e.target.value)}
                />
              </div>
            </div>

            <div className="overflow-auto max-h-[360px] border border-slate-200 rounded-xl">
              <table className="w-full text-left border-collapse">
                <thead className="bg-slate-100 sticky top-0">
                  <tr>
                    <th className="px-4 py-3 text-slate-700 font-black">ID</th>
                    <th className="px-4 py-3 text-slate-700 font-black">Nombre</th>
                    <th className="px-4 py-3 text-slate-700 font-black text-right">Acciones</th>
                  </tr>
                </thead>
                <tbody>
                  {cursos.length === 0 && (
                    <tr>
                      <td colSpan={3} className="px-4 py-4 text-slate-500 text-center">
                        No hay cursos cargados.
                      </td>
                    </tr>
                  )}
                  {cursos.map((curso) => (
                    <tr key={curso.id} className="border-t border-slate-100">
                      <td className="px-4 py-3 font-semibold text-slate-700">{curso.id}</td>
                      <td className="px-4 py-3 text-slate-700">
                        {cursoEditandoId === curso.id ? (
                          <input
                            className="w-full border-2 border-slate-200 p-2 rounded-lg"
                            value={cursoEditandoNombre}
                            onChange={(e) => setCursoEditandoNombre(e.target.value)}
                          />
                        ) : (
                          curso.nombre
                        )}
                      </td>
                      <td className="px-4 py-3">
                        <div className="flex justify-end gap-2">
                          {cursoEditandoId === curso.id ? (
                            <>
                              <button
                                type="button"
                                onClick={() => guardarEdicionCurso(curso.id)}
                                className="bg-blue-500 hover:bg-blue-600 text-white px-3 py-1 rounded-lg font-bold"
                              >
                                Guardar
                              </button>
                              <button
                                type="button"
                                onClick={() => {
                                  setCursoEditandoId(null);
                                  setCursoEditandoNombre('');
                                }}
                                className="bg-slate-200 hover:bg-slate-300 text-slate-700 px-3 py-1 rounded-lg font-bold"
                              >
                                Cancelar
                              </button>
                            </>
                          ) : (
                            <>
                              <button
                                type="button"
                                onClick={() => {
                                  setCursoEditandoId(curso.id);
                                  setCursoEditandoNombre(curso.nombre);
                                }}
                                className="bg-amber-500 hover:bg-amber-600 text-white px-3 py-1 rounded-lg font-bold"
                              >
                                Modificar
                              </button>
                              <button
                                type="button"
                                onClick={() => eliminarCurso(curso.id)}
                                className="bg-rose-500 hover:bg-rose-600 text-white px-3 py-1 rounded-lg font-bold"
                              >
                                Eliminar
                              </button>
                            </>
                          )}
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
