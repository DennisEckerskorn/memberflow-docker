import React, { useEffect, useState } from "react";
import api from "../../api/axiosConfig";
import ErrorMessage from "../common/ErrorMessage";
import "../styles/ContentArea.css";

const TrainingGroupList = () => {
  const [groups, setGroups] = useState([]);
  const [teachers, setTeachers] = useState([]);
  const [editingGroupId, setEditingGroupId] = useState(null);
  const [editedGroup, setEditedGroup] = useState({});
  const [errorMsg, setErrorMsg] = useState("");
  const [successMsg, setSuccessMsg] = useState("");

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      const groupRes = await api.get("/training-groups/getAll");
      const teacherRes = await api.get("/teachers/getAll");
      setGroups(groupRes.data);
      setTeachers(teacherRes.data);
    } catch (err) {
      setErrorMsg("❌ Error al cargar los grupos o profesores.");
      console.error(err);
    }
  };

  const handleEditClick = (group) => {
    setEditingGroupId(group.id);
    setEditedGroup({
      name: group.name,
      level: group.level,
      schedule: group.schedule.slice(0, 16),
      teacherId: group.teacherId,
    });
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setEditedGroup((prev) => ({ ...prev, [name]: value }));
  };

  const handleUpdate = async (id) => {
    try {
      await api.put(`/training-groups/update/${id}`, {
        ...editedGroup,
        schedule: new Date(editedGroup.schedule).toISOString(),
        teacherId: parseInt(editedGroup.teacherId),
        studentIds: []
      });
      setSuccessMsg("✅ Grupo actualizado correctamente.");
      setErrorMsg("");
      setEditingGroupId(null);
      fetchData();
    } catch (err) {
      setErrorMsg("❌ Error al actualizar el grupo.");
      setSuccessMsg("");
      console.error(err);
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm("¿Estás seguro de que deseas eliminar este grupo?")) return;

    try {
      await api.delete(`/training-groups/delete/${id}`);
      setSuccessMsg("✅ Grupo eliminado correctamente.");
      fetchData();
    } catch (err) {
      setErrorMsg("❌ Error al eliminar el grupo.");
      console.error(err);
    }
  };

  const getTeacherName = (id) => {
    const teacher = teachers.find((t) => t.id === id);
    return teacher ? `${teacher.user?.name} ${teacher.user?.surname}` : `(ID ${id})`;
  };

  return (
    <div className="card">
      <h2>Grupos de Entrenamiento</h2>

      <ErrorMessage message={errorMsg} type="error" />
      <ErrorMessage message={successMsg} type="success" />

      <div className="table-wrapper">
        <table className="styled-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Nombre</th>
              <th>Nivel</th>
              <th>Horario</th>
              <th>Profesor</th>
              <th>Acciones</th>
            </tr>
          </thead>
          <tbody>
            {groups.map((group) => (
              <tr key={group.id}>
                {editingGroupId === group.id ? (
                  <>
                    <td>{group.id}</td>
                    <td>
                      <input
                        name="name"
                        value={editedGroup.name}
                        onChange={handleChange}
                      />
                    </td>
                    <td>
                      <input
                        name="level"
                        value={editedGroup.level}
                        onChange={handleChange}
                      />
                    </td>
                    <td>
                      <input
                        type="datetime-local"
                        name="schedule"
                        value={editedGroup.schedule}
                        onChange={handleChange}
                      />
                    </td>
                    <td>
                      <select
                        name="teacherId"
                        value={editedGroup.teacherId}
                        onChange={handleChange}
                      >
                        <option value="">-- Selecciona profesor --</option>
                        {teachers.map((t) => (
                          <option key={t.id} value={t.id}>
                            {t.user?.name} {t.user?.surname}
                          </option>
                        ))}
                      </select>
                    </td>
                    <td>
                      <button className="edit-btn" onClick={() => handleUpdate(group.id)}>✅</button>
                      <button className="delete-btn" onClick={() => setEditingGroupId(null)}>❌</button>
                    </td>
                  </>
                ) : (
                  <>
                    <td>{group.id}</td>
                    <td>{group.name}</td>
                    <td>{group.level}</td>
                    <td>{new Date(group.schedule).toLocaleString()}</td>
                    <td>{getTeacherName(group.teacherId)}</td>
                    <td>
                      <button className="edit-btn" onClick={() => handleEditClick(group)}>✏️</button>
                      <button className="delete-btn" onClick={() => handleDelete(group.id)}>🗑️</button>
                    </td>
                  </>
                )}
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default TrainingGroupList;
