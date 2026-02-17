import './App.css'
import { useState } from 'react';
import axios from 'axios';
import ArgentinaForm from './components/argentinaForm';
import GermanyForm from './components/alemaniaForm';
import USAForm from './components/usaForm';
import EnglandForm from './components/englandForm'; 

interface ConversionResponse {
  equivalencia_sudafrica: number;
  historial_estudiante?: any[];
}

function App() {
  const [estudianteId, setEstudianteId] = useState('');
  const [materiaId, setMateriaId] = useState(''); 
  const [pais, setPais] = useState('');
  const [gradeDetails, setGradeDetails] = useState<any>({});
  const [resultado, setResultado] = useState<ConversionResponse | null>(null);
  const [loading, setLoading] = useState(false);
  const verRanking = async () => {
  try {
    const response = await axios.get('http://localhost:8080/api/reportes/ranking-alerta');
    alert(response.data); 
  } catch (error) {
    alert("Todavía no hay datos calculados en Cassandra.");
  }
};
  const handleRegistrar = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    try {
      const response = await axios.post(
        'http://localhost:8080/api/calificaciones/registrar', 
        {
          estudiante: estudianteId,
            materia: materiaId,     
            pais: pais,
            metadatos: gradeDetails,
          }, 
      );
      console.log(response.data)
      setResultado({
        equivalencia_sudafrica: response.data.conversiones.sudafrica 
      });

    } catch (error) {
      console.error(error);
      alert("Error al registrar. Verificá que el Backend esté corriendo y los datos sean correctos.");
    } finally {
      setLoading(false);
    }
  };
  
  const handleHistorial = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    try {
      const response = await axios.post(
        'http://localhost:8080/api/estudiantes/Historial', 
        {
          estudiante: estudianteId,
          }, 
      );
      console.log(response.data)
      setResultado({
        equivalencia_sudafrica: response.data.conversiones.sudafrica,
        historial_estudiante: response.data.historial_estudiante
      });

    } catch (error) {
      console.error(error);
      alert("Error al generar historial. Verificá que el Backend esté corriendo y los datos sean correctos.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <main className=" bg-green-50-100 min-h-screen py-10 px-4">
    <div className="  rounded-lg">
      <div className="text-center mb-12 ">
        <p className="font-sans text-4xl font-bold text-slate-800">Bienvenido a EduGrade Global</p>
        <p className="font-sans text-2xl text-slate-600">Sistema de Equivalencias Internacionales</p>
      </div>
     <button onClick={verRanking}
className="mb-3 mt-4 bg-orange-600 hover:bg-amber-700 text-white font-bold py-2 px-4
 rounded-lg transition-colors duration-200 ml-5">
          Estudiante con mejor calificación 
        </button>
         <button className="mb-3 mt-4 bg-blue-600 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded-lg transition-colors duration-200 ml-5" onClick={() => {
          alert(" Pais con mejores calificaciones es:");
        }}>
          Pais con mejor calificación
        </button>
      <main className="max-w-2xl mx-auto space-y-8">
        <section className="bg-white p-8 rounded-2xl shadow-xl border border-slate-100">
          <form onSubmit={handleRegistrar} className="space-y-6">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label className="block text-sm font-medium mb-1 text-slate-700">ID Estudiante</label>
                <input 
                  type="text" required
                  className="w-full border border-slate-300 p-2.5 rounded-lg focus:ring-2 focus:ring-indigo-500 outline-none"
                  value={estudianteId} onChange={(e) => setEstudianteId(e.target.value)}
                  placeholder="Ej: AR-4466"
                />
              </div>
              <div>
                <label className="block text-sm font-medium mb-1 text-slate-700">ID Materia</label>
                <input 
                  type="text" required
                  className="w-full border border-slate-300 p-2.5 rounded-lg focus:ring-2 focus:ring-indigo-500 outline-none"
                  value={materiaId} onChange={(e) => setMateriaId(e.target.value)}
                  placeholder="Ej: TPO-1"
                />
              </div>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label className="block text-sm font-medium mb-1 text-slate-700">País de Origen</label>
                <select 
                  className="w-full border border-slate-300 p-2.5 rounded-lg focus:ring-2 focus:ring-indigo-500 outline-none bg-white"
                  value={pais} onChange={(e) => setPais(e.target.value)}
                >
                  <option value="Argentina">Argentina</option>
                  <option value="Alemania">Alemania</option>
                  <option value="USA">USA</option>
                  <option value="Inglaterra">Inglaterra</option>
                </select>
              </div>
              <div>
                <label className="block text-sm font-medium mb-1 text-slate-700">Calificación Original</label>
                <div>
                  <label className="block text-sm font-medium mb-3 text-slate-700">
                    Detalle de Calificación
                  </label>

                  {pais === "Argentina" && <ArgentinaForm setGradeDetails={setGradeDetails} />}
                  {pais === "Alemania" && <GermanyForm setGradeDetails={setGradeDetails} />}
                  {pais === "USA" && <USAForm setGradeDetails={setGradeDetails} />}
                  {pais === "Inglaterra" && <EnglandForm setGradeDetails={setGradeDetails} />}
                </div>
              </div>
            </div>

            <button 
              type="submit" 
              disabled={loading}
              className="w-full bg-green-600 hover:bg-green-700 text-white font-bold py-3 rounded-xl transition-all shadow-lg shadow-green-200 disabled:opacity-50"
            >
              {loading ? 'Procesando...' : 'Registrar y Convertir'}
            </button>
          </form>
        </section>

        {resultado && (
          <section className="bg-indigo-900 text-white p-8 rounded-2xl shadow-2xl animate-in fade-in slide-in-from-bottom-4 duration-500">
            <h2 className="text-xl font-semibold mb-6 border-b border-indigo-800 pb-2">Resultado de la Operación</h2>
            <div className="grid grid-cols-1  gap-8">
              <div className="text-center p-4 bg-emerald-600 rounded-xl">
                <p className="text-emerald-100 text-sm uppercase tracking-wider mb-1">Equivalencia Sudáfrica</p>
                <p className="text-4xl font-bold">{resultado.equivalencia_sudafrica}</p>
              </div>
            </div>
            <p className="mt-6 text-center text-indigo-300 text-xs italic">
               TPO EduGrade Global - Persistido en MongoDB y Convertido - 2026
            </p>
          </section>
        )}

        <button className="mt-4 bg-red-600 hover:bg-red-700 text-white font-bold py-2 px-4 rounded-lg transition-colors duration-200" onClick={() => {
          setEstudianteId("");
          setMateriaId("");
          setPais("Argentina");
          setGradeDetails({});
        }}>
          Limpiar Formulario
        </button>

            <section className="bg-white p-8 rounded-2xl shadow-xl border border-slate-100">
          <form onSubmit={handleRegistrar} className="space-y-6">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label className="block text-sm font-medium mb-1 text-slate-700">ID Estudiante</label>
                <input 
                  type="text" required
                  className="w-full border border-slate-300 p-2.5 rounded-lg focus:ring-2 focus:ring-indigo-500 outline-none"
                  value={estudianteId} onChange={(e) => setEstudianteId(e.target.value)}
                  placeholder="Ej: AR-4466"
                />
              </div>
            </div>
            <button 
              type="submit" 
              disabled={loading}
              className="w-full bg-green-600 hover:bg-green-700 text-white font-bold py-3 rounded-xl transition-all shadow-lg shadow-green-200 disabled:opacity-50"
            >Ver Historial:
              {loading ? 'Procesando...' : 'Historial Estudiante'}
            </button>
          </form>
        </section>

        {resultado && (
          <section className="bg-indigo-900 text-white p-8 rounded-2xl shadow-2xl animate-in fade-in slide-in-from-bottom-4 duration-500">
            <h2 className="text-xl font-semibold mb-6 border-b border-indigo-800 pb-2">Resultado :</h2>
            <div className="grid grid-cols-1  gap-8">
              <div className="text-center p-4 bg-emerald-600 rounded-xl">
                <p className="text-emerald-100 text-sm uppercase tracking-wider mb-1">Historial</p>
                <p className="text-4xl font-bold">{resultado.historial_estudiante}</p>
              </div>
            </div>
            <p className="mt-6 text-center text-indigo-300 text-xs italic">
               TPO EduGrade Global - Persistido en MongoDB y Convertido - 2026
            </p>
          </section>
        )}

        <button className="mt-4 bg-red-600 hover:bg-red-700 text-white font-bold py-2 px-4 rounded-lg transition-colors duration-200" onClick={() => {
          setEstudianteId("");
        }}>
          Limpiar Formulario
        </button>
      </main>
    </div>
    </main>
  );
}

export default App;