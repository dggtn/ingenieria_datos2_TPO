const usaForm = ({ setGradeDetails }: any) => {
  return (
    <div className="space-y-3">
      <input type="string" placeholder="Score"
        onChange={(e) =>
          setGradeDetails((prev: any) => ({
            ...prev,
            semester: String(e.target.value)
          }))
        }
        className="w-full border p-2 rounded-lg"
      />
      <input type="string" placeholder="Score"
        onChange={(e) =>
          setGradeDetails((prev: any) => ({
            ...prev,
            semester_2: String(e.target.value)
          }))
        }
        className="w-full border p-2 rounded-lg"
      />
    </div>
  );
};
export default usaForm;
