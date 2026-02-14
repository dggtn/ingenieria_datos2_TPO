import './App.css'
import 'tailwind/tailwind.css';

import { useState } from 'react';
import axios from 'axios';

interface ConversionResponse {
  nota_original: string;
  pais_origen: string;
  equivalencia_sudafrica: string;
}

function App() {
  const [estudianteId, setEstudianteId] = useState('');
  const [pais, setPais] = useState('Argentina');
  const [notaBase, setNotaBase] = useState('');
  const [resultado, setResultado] = useState<ConversionResponse | null>(null);
  const [loading, setLoading] = useState(false);

  const handleRegistrar = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    try {
      await axios.post(`http://localhost:8080/api/registrar?estudianteId=${estudianteId}&pais=${pais}&notaBase=${notaBase}`);

      const res = await axios.get(`http://localhost:8080/api/simular-conversion?nota=${notaBase}&pais=${pais}`);
      setResultado({
        nota_original: notaBase,
        pais_origen: pais,
        equivalencia_sudafrica: res.data.equivalencia_sudafrica
      });
    } catch (error) {
      alert("Error al procesar la solicitud");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-blue-500  font-sans-900 p-8 ">
      <header className="max-w-4xl mx-auto text-center mb-12">
        <h1 className="min-h-screen bg-blue-500  font-sans-900 p-8">EduGrade Global</h1>
        <p className="font-sans  text-lg">Sistema de Equivalencias Internacionales </p>
      </header>

      <main className="max-w-2xl mx-auto space-y-8">
        {/* Formulario de Registro */}
        <section className="bg-white p-8 rounded-2xl shadow-xl border border-slate-100">
          <form onSubmit={handleRegistrar} className="space-y-6">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label className="block text-sm font-medium mb-1">ID Estudiante</label>
                <input 
                  type="text" required
                  className="w-full border border-slate-300 p-2.5 rounded-lg focus:ring-2 focus:ring-indigo-500 outline-none"
                  value={estudianteId} onChange={(e) => setEstudianteId(e.target.value)}
                  placeholder="Ej: AR-4466"
                />
              </div>
              <div>
                <label className="block text-sm font-medium mb-1">País de Origen</label>
                <select 
                  className="w-full border border-slate-300 p-2.5 rounded-lg focus:ring-2 focus:ring-indigo-500 outline-none"
                  value={pais} onChange={(e) => setPais(e.target.value)}
                >
                  <option value="Argentina">Argentina</option>
                  <option value="Alemania">Alemania</option>
                  <option value="USA">USA</option>
                  <option value="Inglaterra">Inglaterra</option>
                </select>
              </div>
            </div>

            <div>
              <label className="block text-sm font-medium mb-1">Calificación Original</label>
              <input 
                type="number" step="0.1" required
                className="w-full border border-slate-300 p-2.5 rounded-lg focus:ring-2 focus:ring-indigo-500 outline-none"
                value={notaBase} onChange={(e) => setNotaBase(e.target.value)}
                placeholder="Ej: 8.5 o 1.3"
              />
            </div>

            <button 
              type="submit" 
              disabled={loading}
              className="w-full bg-indigo-600 hover:bg-indigo-700 text-white font-bold py-3 rounded-xl transition-all shadow-lg shadow-indigo-200 disabled:opacity-50"
            >
              {loading ? 'Procesando...' : 'Registrar y Convertir'}
            </button>
          </form>
        </section>

        {/* Output de Resultados */}
        {resultado && (
          <section className="bg-indigo-900 text-white p-8 rounded-2xl shadow-2xl animate-in fade-in slide-in-from-bottom-4 duration-500">
            <h2 className="text-xl font-semibold mb-6 border-b border-indigo-800 pb-2">Resultado de la Operación</h2>
            <div className="grid grid-cols-2 gap-8">
              <div className="text-center p-4 bg-indigo-800 rounded-xl">
                <p className="text-indigo-300 text-sm uppercase tracking-wider mb-1">Nota {resultado.pais_origen}</p>
                <p className="text-4xl font-bold">{resultado.nota_original}</p>
              </div>
              <div className="text-center p-4 bg-emerald-600 rounded-xl">
                <p className="text-emerald-100 text-sm uppercase tracking-wider mb-1">Equivalencia Sudáfrica</p>
                <p className="text-4xl font-bold">{resultado.equivalencia_sudafrica}</p>
              </div>
            </div>
            <p className="mt-6 text-center text-indigo-300 text-sm italic">
              * Datos persistidos en MongoDB y calculados según escala Ministerial.
            </p>
          </section>
        )}
      </main>
    </div>
  );
}

export default App;