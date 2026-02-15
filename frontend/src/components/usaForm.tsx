const usaForm = ({ setGradeDetails }: any) => {
  return (
    <div className="space-y-3">
      <input type="string" placeholder="Semester 1 Final Exam"
        onChange={(e) =>
          setGradeDetails((prev: any) => ({
            ...prev,
              semester: String(e.target.value)
            
          }))
        }
        className="w-full border p-2 rounded-lg"
      />
      <input type="number" placeholder="Cumulative GPA"
        step="0.1"
        onChange={(e) =>
          setGradeDetails((prev: any) => ({
            ...prev,
            gpa: Number(e.target.value)
          }))
        }
        className="w-full border p-2 rounded-lg"
      />
    </div>
  );
};
export default usaForm;
