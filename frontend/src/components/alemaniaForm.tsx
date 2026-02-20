const alemaniaForm = ({ setGradeDetails }: any) => {
  return (
    <div className="space-y-3">
      <input type="number" step="0.1" min="1" max="6" placeholder="Klassenarbeit 1"
        onChange={(e) =>
          setGradeDetails((prev: any) => ({
            ...prev,
            KlassenArbeit:
              parseFloat(e.target.value),
          }))
        }
        className="w-full border p-2 rounded-lg"
      />
      <input type="number" step="0.1" min="1" max="6" placeholder="Mundliche Mitarbeit"
        onChange={(e) =>
          setGradeDetails((prev: any) => ({
            ...prev,
            MundlichArbeit: parseFloat(e.target.value)
          }))
        }
        className="w-full border p-2 rounded-lg"
      />
    </div>
  );
};
export default alemaniaForm;
