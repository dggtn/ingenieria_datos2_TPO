const toNumber = (value: unknown): number => {
  const n = Number(value);
  return Number.isFinite(n) ? n : 0;
};

const southAfricaForm = ({ setGradeDetails }: any) => {
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
        onChange={(e) => updateField('parcial', e.target.value)}
        className="w-full border p-2 rounded-lg"
      />
      <input
        type="number"
        placeholder="Trabajos Prácticos"
        onChange={(e) => updateField('trabajos_practicos', e.target.value)}
        className="w-full border p-2 rounded-lg"
      />
      <input
        type="number"
        placeholder="Final"
        onChange={(e) => updateField('final', e.target.value)}
        className="w-full border p-2 rounded-lg"
      />
    </div>
  );
};

export default southAfricaForm;
