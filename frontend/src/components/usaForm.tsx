const usaForm = ({ setGradeDetails, fieldErrors = {}, clearFieldError = () => {} }: any) => {
  return (
    <div className="space-y-3">
      <input type="string" placeholder="Score"
        onChange={(e) =>
          {
            clearFieldError('semester');
            setGradeDetails((prev: any) => ({
              ...prev,
              semester: String(e.target.value)
            }));
          }
        }
        className={`w-full border p-2 rounded-lg ${fieldErrors.semester ? 'border-red-500 bg-red-50' : ''}`}
      />
      {fieldErrors.semester && <p className="text-sm text-red-600">{fieldErrors.semester}</p>}
      <input type="string" placeholder="Score"
        onChange={(e) =>
          {
            clearFieldError('semester_2');
            setGradeDetails((prev: any) => ({
              ...prev,
              semester_2: String(e.target.value)
            }));
          }
        }
        className={`w-full border p-2 rounded-lg ${fieldErrors.semester_2 ? 'border-red-500 bg-red-50' : ''}`}
      />
      {fieldErrors.semester_2 && <p className="text-sm text-red-600">{fieldErrors.semester_2}</p>}
    </div>
  );
};
export default usaForm;
