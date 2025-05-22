import React, { useState, useEffect } from "react";
import api from "../../api/axiosConfig";
import ErrorMessage from "../common/ErrorMessage";
import "../styles/ContentArea.css";

const UserList = () => {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [editingUserId, setEditingUserId] = useState(null);
  const [editedUserData, setEditedUserData] = useState({});
  const [errorMsg, setErrorMsg] = useState("");
  const [successMsg, setSuccessMsg] = useState("");

  useEffect(() => {
    fetchUsers();
  }, []);

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

  const formatRoleName = (roleName) => {
    switch (roleName) {
      case "ROLE_STUDENT": return "Estudiante";
      case "ROLE_TEACHER": return "Profesor";
      case "ROLE_ADMIN": return "Administrador";
      default: return roleName;
    }
  };

  const handleEditClick = (user) => {
    setEditingUserId(user.id);
    setEditedUserData({ ...user });
  };

  const handleEditChange = (e) => {
    setEditedUserData({
      ...editedUserData,
      [e.target.name]: e.target.value
    });
  };

  const handleConfirmUpdate = async () => {
    try {
      const res = await api.put(`/users/update/${editingUserId}`, editedUserData);
      const updatedUser = res.data;
      setUsers(users.map((u) => (u.id === updatedUser.id ? updatedUser : u)));
      setEditingUserId(null);
      setEditedUserData({});
      setSuccessMsg("✅ Usuario actualizado correctamente.");
      setErrorMsg("");
    } catch (err) {
      console.error(err);
      const msg = err.response?.data?.message || "❌ Error al actualizar el usuario.";
      setErrorMsg(msg);
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
              <th>Teléfono</th>
              <th>Dirección</th>
              <th>Rol</th>
              <th>Estado</th>
              <th>Acciones</th>
            </tr>
          </thead>
          <tbody>
            {users.map((user) => (
              <tr key={user.id}>
                {editingUserId === user.id ? (
                  <>
                    <td><input name="name" value={editedUserData.name} onChange={handleEditChange} /></td>
                    <td><input name="surname" value={editedUserData.surname} onChange={handleEditChange} /></td>
                    <td><input name="email" value={editedUserData.email} onChange={handleEditChange} /></td>
                    <td><input name="phoneNumber" value={editedUserData.phoneNumber} onChange={handleEditChange} /></td>
                    <td><input name="address" value={editedUserData.address} onChange={handleEditChange} /></td>
                    <td>{formatRoleName(user.roleName)}</td>
                    <td>
                      <select name="status" value={editedUserData.status} onChange={handleEditChange}>
                        <option value="ACTIVE">Activo</option>
                        <option value="INACTIVE">Inactivo</option>
                      </select>
                    </td>
                    <td>
                      <button className="edit-btn" onClick={handleConfirmUpdate}>✅</button>
                      <button className="delete-btn" onClick={() => setEditingUserId(null)}>❌</button>
                    </td>
                  </>
                ) : (
                  <>
                    <td>{user.name}</td>
                    <td>{user.surname}</td>
                    <td>{user.email}</td>
                    <td>{user.phoneNumber}</td>
                    <td>{user.address}</td>
                    <td>{formatRoleName(user.roleName)}</td>
                    <td>{user.status}</td>
                    <td>
                      <button className="edit-btn" onClick={() => handleEditClick(user)}>✏️</button>
                      <button className="delete-btn" onClick={() => handleDelete(user.id)}>🗑️</button>
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

export default UserList;
