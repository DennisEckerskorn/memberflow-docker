import React, { useEffect, useState } from "react";
import api from "../../api/axiosConfig";
import ErrorMessage from "../common/ErrorMessage";
import "../styles/ContentArea.css";

const TrainingGroupStudentManager = () => {
  const [groups, setGroups] = useState([]);
  const [students, setStudents] = useState([]);
  const [selectedGroupId, setSelectedGroupId] = useState("");
  const [groupStudents, setGroupStudents] = useState([]);
  const [errorMsg, setErrorMsg] = useState("");
  const [successMsg, setSuccessMsg] = useState("");

  useEffect(() => {
    api.get("/training-groups/getAll").then((res) => setGroups(res.data));
    api.get("/students/getAll").then((res) => setStudents(res.data));
  }, []);

  const loadGroupStudents = async (groupId) => {
    setSelectedGroupId(groupId);
    setErrorMsg("");
    setSuccessMsg("");

    if (!groupId) return;

    try {
      const group = await api.get(`/training-groups/findById/${groupId}`);
      setGroupStudents(group.data.studentIds || []);
    } catch (err) {
      console.error("Error al cargar alumnos del grupo", err);
      const msg =
        err.response?.data?.message || "❌ Error al cargar alumnos del grupo.";
      setErrorMsg(msg);
      setGroupStudents([]);
    }
  };

  const handleAssign = async (studentId) => {
    try {
      await api.put(`/training-groups/assign-student`, null, {
        params: {
          groupId: selectedGroupId,
          studentId,
        },
      });
      setSuccessMsg("✅ Alumno asignado correctamente.");
      setErrorMsg("");
      loadGroupStudents(selectedGroupId);
    } catch (err) {
      console.error(err);
      const msg =
        err.response?.data?.message || "❌ No se pudo asignar el alumno.";
      setErrorMsg(msg);
      setSuccessMsg("");
    }
  };

  const handleRemove = async (studentId) => {
    try {
      await api.put(`/training-groups/remove-student`, null, {
        params: {
          groupId: selectedGroupId,
          studentId,
        },
      });
      setSuccessMsg("✅ Alumno eliminado del grupo.");
      setErrorMsg("");
      loadGroupStudents(selectedGroupId);
    } catch (err) {
      console.error(err);
      const msg =
        err.response?.data?.message || "❌ Error al eliminar el alumno.";
      setErrorMsg(msg);
      setSuccessMsg("");
    }
  };

  return (
    <div className="content-area">
      <div className="card">
        <h2>👥 Gestión de Alumnos por Grupo</h2>

        <label htmlFor="groupSelect">🏷️ Selecciona un grupo:</label>
        <select
          id="groupSelect"
          value={selectedGroupId}
          onChange={(e) => loadGroupStudents(e.target.value)}
        >
          <option value="">-- Seleccionar Grupo --</option>
          {groups.map((group) => (
            <option key={group.id} value={group.id}>
              {group.name} ({group.level})
            </option>
          ))}
        </select>

        <ErrorMessage message={errorMsg} type="error" />
        <ErrorMessage message={successMsg} type="success" />

        {selectedGroupId && (
          <>
            <hr style={{ margin: "20px 0" }} />
            <h3>📋 Alumnos Asignados</h3>
            {groupStudents.length === 0 ? (
              <p>No hay alumnos asignados a este grupo.</p>
            ) : (
              <ul className="group-student-list">
                {groupStudents.map((id) => {
                  const student = students.find((s) => s.id === id);
                  return (
                    <li key={id}>
                      🎓 {student?.user?.name} {student?.user?.surname}
                      <button
                        className="delete-button"
                        onClick={() => handleRemove(id)}
                      >
                        Eliminar
                      </button>
                    </li>
                  );
                })}
              </ul>
            )}

            <hr style={{ margin: "20px 0" }} />
            <h3>➕ Añadir Alumnos Disponibles</h3>
            <ul className="group-student-list">
              {students
                .filter((s) => !groupStudents.includes(s.id))
                .map((s) => (
                  <li key={s.id}>
                    👤 {s.user?.name} {s.user?.surname}
                    <button
                      className="add-button"
                      onClick={() => handleAssign(s.id)}
                    >
                      Añadir
                    </button>
                  </li>
                ))}
            </ul>
          </>
        )}
      </div>
    </div>
  );
};

export default TrainingGroupStudentManager;
