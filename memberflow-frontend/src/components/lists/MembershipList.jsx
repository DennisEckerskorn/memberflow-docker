import React, { useEffect, useState } from "react";
import api from "../../api/axiosConfig";
import ErrorMessage from "../common/ErrorMessage";
import "../styles/ContentArea.css";

const MembershipList = () => {
  const [students, setStudents] = useState([]);
  const [memberships, setMemberships] = useState([]);
  const [errorMsg, setErrorMsg] = useState("");
  const [successMsg, setSuccessMsg] = useState("");

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      const [studentRes, membershipRes] = await Promise.all([
        api.get("/students/getAll"),
        api.get("/memberships/getAll"),
      ]);
      setStudents(studentRes.data);
      setMemberships(membershipRes.data);
    } catch (error) {
      setErrorMsg("Error al cargar los datos");
    }
  };

  const handleMembershipChange = async (studentId, newMembershipId) => {
    setErrorMsg("");
    setSuccessMsg("");

    try {
      await api.put(`/students/updateMembership/${studentId}`, null, {
        params: { membershipId: newMembershipId },
      });
      setSuccessMsg("✅ Membresía actualizada correctamente");
      fetchData();
    } catch (err) {
      console.error(err);
      setErrorMsg("❌ Error al actualizar la membresía");
    }
  };

  const getMembershipInfo = (membershipId) => {
    const m = memberships.find((m) => m.id === membershipId);
    return m
      ? `${m.type} (${m.startDate} - ${m.endDate}) - ${m.status}`
      : "No asignada";
  };

  return (
    <div className="card">
      <h2>Ver Membresías de Estudiantes</h2>

      <ErrorMessage type="success" message={successMsg} />
      <ErrorMessage type="error" message={errorMsg} />

      <table className="styled-table">
        <thead>
          <tr>
            <th>Nombre</th>
            <th>Membresía</th>
            <th>Cambiar Membresía</th>
          </tr>
        </thead>
        <tbody>
          {students.map((student) => (
            <tr key={student.id}>
              <td>{student.user?.name || "Sin nombre"}</td>
              <td>{getMembershipInfo(student.membershipId)}</td>
              <td>
                <select
                  value={student.membershipId || ""}
                  onChange={(e) =>
                    handleMembershipChange(student.id, e.target.value)
                  }
                >
                  <option value="">-- Seleccionar --</option>
                  {memberships.map((m) => (
                    <option key={m.id} value={m.id}>
                      {m.type} ({m.status})
                    </option>
                  ))}
                </select>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default MembershipList;
