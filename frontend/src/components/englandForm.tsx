const englandForm = ({ setGradeDetails, fieldErrors = {}, clearFieldError = () => {} }: any) => {
  return (
    <div className="space-y-4">
      <h3 className="font-semibold">Autumn Term</h3>
      <input type="string" placeholder="Coursework"
        onChange={(e) =>
          {
            clearFieldError('coursework');
            setGradeDetails((prev: any) => ({
              ...prev,
              coursework: String(e.target.value)
            }));
          }
        }
        className={`w-full border p-2 rounded-lg ${fieldErrors.coursework ? 'border-red-500 bg-red-50' : ''}`}
      />
      {fieldErrors.coursework && <p className="text-sm text-red-600">{fieldErrors.coursework}</p>}
      <input type="string" placeholder="Mock Exam"
        onChange={(e) =>
          {
            clearFieldError('mock_exam');
            setGradeDetails((prev: any) => ({
              ...prev,
              mock_exam: String(e.target.value)
            }));
          }
        }
        className={`w-full border p-2 rounded-lg ${fieldErrors.mock_exam ? 'border-red-500 bg-red-50' : ''}`}
      />
      {fieldErrors.mock_exam && <p className="text-sm text-red-600">{fieldErrors.mock_exam}</p>}

      <input type="string" placeholder="Final Grade"
        onChange={(e) =>
          {
            clearFieldError('final_grade');
            setGradeDetails((prev: any) => ({
              ...prev,
              final_grade: String(e.target.value)
            }));
          }
        }
        className={`w-full border p-2 rounded-lg ${fieldErrors.final_grade ? 'border-red-500 bg-red-50' : ''}`}
      />
      {fieldErrors.final_grade && <p className="text-sm text-red-600">{fieldErrors.final_grade}</p>}
    </div>
  );
};
export default englandForm;
