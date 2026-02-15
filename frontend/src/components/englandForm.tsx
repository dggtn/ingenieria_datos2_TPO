const englandForm = ({ setGradeDetails }: any) => {
  return (
    <div className="space-y-4">
      <h3 className="font-semibold">Autumn Term</h3>
      <input type="number" placeholder="Coursework"
        onChange={(e) =>
          setGradeDetails((prev: any) => ({
            ...prev,
            terms: {
              ...prev.terms,
              autumn_term: {
                ...prev.terms?.autumn_term,
                coursework: Number(e.target.value)
              }
            }
          }))
        }
        className="w-full border p-2 rounded-lg"
      />
      <input type="number" placeholder="Mock Exam"
        onChange={(e) =>
          setGradeDetails((prev: any) => ({
            ...prev,
            terms: {
              ...prev.terms,
              autumn_term: {
                ...prev.terms?.autumn_term,
                mock_exam: Number(e.target.value)
              }
            }
          }))
        }
        className="w-full border p-2 rounded-lg"
      />

      <input type="number" placeholder="Final Grade"
        onChange={(e) =>
          setGradeDetails((prev: any) => ({
            ...prev,
            final_grade: Number(e.target.value)
          }))
        }
        className="w-full border p-2 rounded-lg"
      />
    </div>
  );
};
export default englandForm;