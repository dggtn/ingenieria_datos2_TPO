const alemaniaForm = ({ setGradeDetails, fieldErrors = {}, clearFieldError = () => {} }: any) => {
  return (
    <div className="space-y-3">
      <input
        type="number"
        step="0.1"
        min="1"
        max="6"
        placeholder="Klassenarbeit 1"
        onChange={(e) => {
          clearFieldError('KlassenArbeit');
          setGradeDetails((prev: any) => ({
            ...prev,
            KlassenArbeit: parseFloat(e.target.value),
          }));
        }}
        className={`w-full border p-2 rounded-lg ${fieldErrors.KlassenArbeit ? 'border-red-500 bg-red-50' : ''}`}
      />
      {fieldErrors.KlassenArbeit && <p className="text-sm text-red-600">{fieldErrors.KlassenArbeit}</p>}
      <input
        type="number"
        step="0.1"
        min="1"
        max="6"
        placeholder="Mundliche Mitarbeit"
        onChange={(e) => {
          clearFieldError('MundlichArbeit');
          setGradeDetails((prev: any) => ({
            ...prev,
            MundlichArbeit: parseFloat(e.target.value)
          }));
        }}
        className={`w-full border p-2 rounded-lg ${fieldErrors.MundlichArbeit ? 'border-red-500 bg-red-50' : ''}`}
      />
      {fieldErrors.MundlichArbeit && <p className="text-sm text-red-600">{fieldErrors.MundlichArbeit}</p>}
    </div>
  );
};

export default alemaniaForm;
