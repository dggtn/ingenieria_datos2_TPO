const argentinaForm = ({ setGradeDetails }: any) => {
  return (
    <div className="space-y-3">
      <input type="number" placeholder="Primer Parcial"
        onChange={(e) =>
          setGradeDetails((prev: any) => ({
            ...prev,
            primer_parcial: Number(e.target.value)
          }))
        }
        className="w-full border p-2 rounded-lg"
      />
      <input type="number" placeholder="Segundo Parcial"
        onChange={(e) =>
          setGradeDetails((prev: any) => ({
            ...prev,
            segundo_parcial: Number(e.target.value)
          }))
        }
        className="w-full border p-2 rounded-lg"
      />
      <input type="number" placeholder="Examen Final"
        onChange={(e) =>
          setGradeDetails((prev: any) => ({
            ...prev,
            tercer_parcial: Number(e.target.value)
          }))
        }
        className="w-full border p-2 rounded-lg"
      />
    </div>
  );
};
export default argentinaForm;