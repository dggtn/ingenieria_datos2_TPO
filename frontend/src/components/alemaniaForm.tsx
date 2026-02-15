const alemaniaForm = ({ setGradeDetails }: any) => {
  return (
    <div className="space-y-3">
      <input type="number" min="1" max="6" placeholder="Klassenarbeit 1"
        onChange={(e) =>
          setGradeDetails((prev: any) => ({
            ...prev,
            klassenarbeiten: [
              Number(e.target.value),
              prev.klassenarbeiten?.[1]
            ]
          }))
        }
        className="w-full border p-2 rounded-lg"
      />
      <input type="number" min="1" max="6" placeholder="Mündliche Mitarbeit"
        onChange={(e) =>
          setGradeDetails((prev: any) => ({
            ...prev,
            muendliche_mitarbeit: Number(e.target.value)
          }))
        }
        className="w-full border p-2 rounded-lg"
      />
    </div>
  );
};
export default alemaniaForm;