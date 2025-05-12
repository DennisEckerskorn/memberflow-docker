import React, { useState, useEffect } from "react";
import api from "../../api/axiosConfig";
import ErrorMessage from "../common/ErrorMessage";
import "../styles/ContentArea.css";

const UserList = () => {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [errorMsg, setErrorMsg] = useState("");
  const [successMsg, setSuccessMsg] = useState("");

  useEffect(() => {
    fetchUsers();
  }, []);

  const formatRoleName = (roleName) => {
    switch (roleName) {
      case "ROLE_STUDENT":
        return "Estudiante";
      case "ROLE_TEACHER":
        return "Profesor";
      case "ROLE_ADMIN":
        return "Administrador";
      default:
        return roleName;
    }
  };
  

  const fetchUsers = async () => {
    try {
      const res = await api.get("/users/getAll");
      setUsers(res.data);
      setErrorMsg("");
    } catch (err) {
      console.error(err);
      const msg = err.response?.data?.message || "❌ Error al cargar los usuarios.";
      setErrorMsg(msg);
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm("¿Estás seguro de que deseas eliminar este usuario?")) return;

    try {
      await api.delete(`/users/delete/${id}`);
      setUsers(users.filter((u) => u.id !== id));
      setSuccessMsg("✅ Usuario eliminado correctamente.");
      setErrorMsg("");
    } catch (err) {
      console.error(err);
      const msg = err.response?.data?.message || "❌ Error al eliminar el usuario.";
      setErrorMsg(msg);
      setSuccessMsg("");
    }
  };

  if (loading)
    return (
      <div className="card">
        <p>Cargando usuarios...</p>
      </div>
    );

  return (
    <div className="card">
      <h2>Listado de Usuarios</h2>
      <ErrorMessage message={errorMsg} type="error" />
      <ErrorMessage message={successMsg} type="success" />

      <div className="table-wrapper">
        <table className="styled-table">
          <thead>
            <tr>
              <th>Nombre</th>
              <th>Apellido</th>
              <th>Email</th>
              <th>Rol</th>
              <th>Estado</th>
              <th>Acciones</th>
            </tr>
          </thead>
          <tbody>
            {users.map((user) => (
              <tr key={user.id}>
                <td>{user.name}</td>
                <td>{user.surname}</td>
                <td>{user.email}</td>
                <td>{formatRoleName(user.roleName)}</td>
                <td>{user.status}</td>
                <td>
                  <button className="edit-btn" onClick={() => alert("✏️ Edición no implementada")}>✏️</button>
                  <button className="delete-btn" onClick={() => handleDelete(user.id)}>🗑️</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default UserList;
