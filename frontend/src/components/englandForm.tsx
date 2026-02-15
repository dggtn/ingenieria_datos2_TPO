const englandForm = ({ setGradeDetails }: any) => {
  return (
    <div className="space-y-4">
      <h3 className="font-semibold">Autumn Term</h3>
      <input type="string" placeholder="Coursework"
        onChange={(e) =>
          setGradeDetails((prev: any) => ({
            ...prev,
                coursework: String(e.target.value)
          }))
        }
        className="w-full border p-2 rounded-lg"
      />
      <input type="string" placeholder="Mock Exam"
        onChange={(e) =>
          setGradeDetails((prev: any) => ({
            ...prev,
                mock_exam: String(e.target.value)
            
          }))
        }
        className="w-full border p-2 rounded-lg"
      />

      <input type="string" placeholder="Final Grade"
        onChange={(e) =>
          setGradeDetails((prev: any) => ({
            ...prev,
            final_grade: String(e.target.value)
          }))
        }
        className="w-full border p-2 rounded-lg"
      />
    </div>
  );
};
export default englandForm;