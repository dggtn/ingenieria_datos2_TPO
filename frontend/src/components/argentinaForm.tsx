const argentinaForm = ({ setGradeDetails, fieldErrors = {}, clearFieldError = () => {} }: any) => {
  return (
    <div className="space-y-3">
      <input type="number" placeholder="Primer Parcial"
        onChange={(e) =>
          {
            clearFieldError('primer_parcial');
            setGradeDetails((prev: any) => ({
              ...prev,
              primer_parcial: Number(e.target.value) * 1.0
            }));
          }
        }
        className={`w-full border p-2 rounded-lg ${fieldErrors.primer_parcial ? 'border-red-500 bg-red-50' : ''}`}
      />
      {fieldErrors.primer_parcial && <p className="text-sm text-red-600">{fieldErrors.primer_parcial}</p>}
      <input type="number" placeholder="Segundo Parcial"
        onChange={(e) =>
          {
            clearFieldError('segundo_parcial');
            setGradeDetails((prev: any) => ({
              ...prev,
              segundo_parcial: Number(e.target.value) * 1.0
            }));
          }
        }
        className={`w-full border p-2 rounded-lg ${fieldErrors.segundo_parcial ? 'border-red-500 bg-red-50' : ''}`}
      />
      {fieldErrors.segundo_parcial && <p className="text-sm text-red-600">{fieldErrors.segundo_parcial}</p>}
      <input type="number" placeholder="Examen Final"
        onChange={(e) =>
          {
            clearFieldError('examen_final');
            setGradeDetails((prev: any) => ({
              ...prev,
              examen_final: Number(e.target.value) * 1.0
            }));
          }
        }
        className={`w-full border p-2 rounded-lg ${fieldErrors.examen_final ? 'border-red-500 bg-red-50' : ''}`}
      />
      {fieldErrors.examen_final && <p className="text-sm text-red-600">{fieldErrors.examen_final}</p>}
    </div>
  );
};
export default argentinaForm;
