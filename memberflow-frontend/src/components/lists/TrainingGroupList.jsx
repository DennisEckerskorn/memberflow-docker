import React, { useEffect, useState } from "react";
import api from "../../api/axiosConfig";
import ErrorMessage from "../common/ErrorMessage";
import "../styles/ContentArea.css";

const TrainingGroupList = () => {
  const [groups, setGroups] = useState([]);
  const [teachers, setTeachers] = useState([]);
  const [editingGroupId, setEditingGroupId] = useState(null);
  const [newTeacherId, setNewTeacherId] = useState("");

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    const groupRes = await api.get("/training-groups/getAll");
    const teacherRes = await api.get("/teachers/getAll");
    setGroups(groupRes.data);
    setTeachers(teacherRes.data);
  };

  const handleUpdateTeacher = async (groupId) => {
    const group = groups.find((g) => g.id === groupId);
    const updatedGroup = {
      ...group,
      teacherId: parseInt(newTeacherId),
    };

    await api.put(`/training-groups/update/${groupId}`, updatedGroup);
    setEditingGroupId(null);
    fetchData();
  };

  const handleDelete = async (groupId) => {
    if (window.confirm("Are you sure you want to delete this group?")) {
      await api.delete(`/training-groups/delete/${groupId}`);
      fetchData();
    }
  };

  return (
    <div className="card">
      <h2>Grupos de entrenamiento</h2>
      <div className="table-wrapper">
        <table className="styled-table">
          <thead>
            <tr>
              <th>ID Grupo</th>
              <th>Nombre</th>
              <th>Nivel</th>
              <th>Fecha / Hora</th>
              <th>Profesor</th>
              <th>Acciones</th>
            </tr>
          </thead>
          <tbody>
            {groups.map((group) => (
              <tr key={group.id}>
                <td>{group.id}</td>
                <td>{group.name}</td>
                <td>{group.level}</td>
                <td>{new Date(group.schedule).toLocaleString()}</td>
                <td>
                  {editingGroupId === group.id ? (
                    <select
                      value={newTeacherId}
                      onChange={(e) => setNewTeacherId(e.target.value)}
                    >
                      <option value="">-- Select --</option>
                      {teachers.map((teacher) => (
                        <option key={teacher.id} value={teacher.id}>
                          {teacher.user?.name} {teacher.user?.surname}
                        </option>
                      ))}
                    </select>
                  ) : (
                    `${group.teacherId}`
                  )}
                </td>
                <td>
                  {editingGroupId === group.id ? (
                    <button onClick={() => handleUpdateTeacher(group.id)}>
                      Save
                    </button>
                  ) : (
                    <button
                      onClick={() => {
                        setEditingGroupId(group.id);
                        setNewTeacherId(group.teacherId || "");
                      }}
                    >
                      Edit Teacher
                    </button>
                  )}
                  <button onClick={() => handleDelete(group.id)}>Delete</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default TrainingGroupList;
