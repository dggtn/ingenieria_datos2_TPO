import React, { useState } from 'react';
import axios from 'axios';

type EstudianteResponse = {
  id: string;
  nombre: string;
  email: string;
  paisOrigen: string;
  institucionActual?: string | { id?: string; nombre?: string };
  materias?: any[];
  historialAcademico?: any[];
};

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

  const formatearInstitucion = (institucion?: string | { id?: string; nombre?: string }) => {
    if (!institucion) return '-';
    if (typeof institucion === 'string') return institucion;
    const nombre = institucion.nombre || '';
    const id = institucion.id || '';
    if (nombre && id) return `${nombre} (${id})`;
    return nombre || id || '-';
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
      await axios.post('http://localhost:8080/api/estudiantes', {
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
            </div>
          )}
        </section>
      </div>

      {mensaje && (
        <div className="mt-6 bg-white/95 rounded-2xl p-4 text-slate-700 font-semibold shadow">
          {mensaje}
        </div>
      )}
    </div>
  );
}
