const toNumber = (value: unknown): number => {
  const n = Number(value);
  return Number.isFinite(n) ? n : 0;
};

const southAfricaForm = ({ setGradeDetails, fieldErrors = {}, clearFieldError = () => {} }: any) => {
  const updateField = (field: 'parcial' | 'trabajos_practicos' | 'final', value: string) => {
    setGradeDetails((prev: any) => {
      const next = {
        ...prev,
        [field]: toNumber(value)
      };
      const promedio =
        (toNumber(next.parcial) + toNumber(next.trabajos_practicos) + toNumber(next.final)) / 3;
      return {
        ...next,
        score: Math.round(promedio * 100) / 100
      };
    });
  };

  return (
    <div className="space-y-3">
      <input
        type="number"
        placeholder="Parcial"
        onChange={(e) => {
          clearFieldError('parcial');
          updateField('parcial', e.target.value);
        }}
        className={`w-full border p-2 rounded-lg ${fieldErrors.parcial ? 'border-red-500 bg-red-50' : ''}`}
      />
      {fieldErrors.parcial && <p className="text-sm text-red-600">{fieldErrors.parcial}</p>}
      <input
        type="number"
        placeholder="Trabajos Practicos"
        onChange={(e) => {
          clearFieldError('trabajos_practicos');
          updateField('trabajos_practicos', e.target.value);
        }}
        className={`w-full border p-2 rounded-lg ${fieldErrors.trabajos_practicos ? 'border-red-500 bg-red-50' : ''}`}
      />
      {fieldErrors.trabajos_practicos && <p className="text-sm text-red-600">{fieldErrors.trabajos_practicos}</p>}
      <input
        type="number"
        placeholder="Final"
        onChange={(e) => {
          clearFieldError('final');
          updateField('final', e.target.value);
        }}
        className={`w-full border p-2 rounded-lg ${fieldErrors.final ? 'border-red-500 bg-red-50' : ''}`}
      />
      {fieldErrors.final && <p className="text-sm text-red-600">{fieldErrors.final}</p>}
    </div>
  );
};

export default southAfricaForm;
