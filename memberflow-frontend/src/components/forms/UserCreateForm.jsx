import React, { useState, useEffect } from "react";
import api from "../../api/axiosConfig";
import ErrorMessage from "../common/ErrorMessage";


const UserCreateForm = () => {
  const today = new Date().toISOString().split("T")[0];

  const [formData, setFormData] = useState({
    name: "",
    surname: "",
    email: "",
    password: "",
    phoneNumber: "",
    address: "",
    status: "ACTIVE",
    roleName: "",
  });

  const [studentData, setStudentData] = useState({
    dni: "",
    birthdate: today,
    belt: "",
    parentName: "",
    medicalReport: "",
    membershipId: "",
  });

  const [teacherData, setTeacherData] = useState({
    discipline: "",
  });

  const [roles, setRoles] = useState([]);
  const [memberships, setMemberships] = useState([]);
  const [successMsg, setSuccessMsg] = useState("");
  const [errorMsg, setErrorMsg] = useState("");

  useEffect(() => {
    api
      .get("/roles/getAll")
      .then((res) => setRoles(res.data))
      .catch((err) => console.error("Error al cargar roles", err));
  }, []);

  useEffect(() => {
    api
      .get("/roles/getAll")
      .then((res) => setRoles(res.data))
      .catch((err) => console.error("Error al cargar roles", err));

    api
      .get("/memberships/getAll")
      .then((res) => setMemberships(res.data))
      .catch((err) => console.error("Error al cargar membresías", err));
  }, []);

  const handleChangeForm = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleChangeStudent = (e) => {
    setStudentData({ ...studentData, [e.target.name]: e.target.value });
  };

  const handleChangeTeacher = (e) => {
    setTeacherData({ ...teacherData, [e.target.name]: e.target.value });
  };

  const resetForm = () => {
    setFormData({
      name: "",
      surname: "",
      email: "",
      password: "",
      phoneNumber: "",
      address: "",
      status: "ACTIVE",
      roleName: "",
    });
    setStudentData({
      dni: "",
      birthdate: today,
      belt: "",
      parentName: "",
      medicalReport: "",
    });
    setTeacherData({
      discipline: "",
    });
  };

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

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSuccessMsg("");
    setErrorMsg("");

    try {
      if (formData.roleName === "ROLE_STUDENT") {
        await api.post("/students/register", {
          name: formData.name,
          surname: formData.surname,
          email: formData.email,
          password: formData.password,
          phoneNumber: formData.phoneNumber,
          address: formData.address,
          status: formData.status,
          roleName: formData.roleName,
          dni: studentData.dni,
          birthdate: studentData.birthdate,
          belt: studentData.belt,
          parentName: studentData.parentName,
          medicalReport: studentData.medicalReport,
          membershipId: studentData.membershipId || null,
        });
      } else if (formData.roleName === "ROLE_TEACHER") {
        await api.post("/teachers/create", {
          user: {
            name: formData.name,
            surname: formData.surname,
            email: formData.email,
            password: formData.password,
            phoneNumber: formData.phoneNumber,
            address: formData.address,
            status: formData.status,
            roleName: formData.roleName,
          },
          discipline: teacherData.discipline,
        });
      } else if (formData.roleName === "ROLE_ADMIN") {
        await api.post("/admins/create", {
          user: {
            name: formData.name,
            surname: formData.surname,
            email: formData.email,
            password: formData.password,
            phoneNumber: formData.phoneNumber,
            address: formData.address,
            status: formData.status,
            roleName: formData.roleName,
          },
        });
      }

      setSuccessMsg("✅ Usuario creado correctamente");
      resetForm();
    } catch (err) {
      console.error(err);
      const backendMsg =
        err.response?.data?.message ||
        err.response?.data?.error ||
        "❌ Error al crear usuario. Verifica los datos.";
    
      setErrorMsg(backendMsg);
    }
    
  };

  return (
    <div className="card">
      <h2>Crear nuevo usuario</h2>
      <form
        onSubmit={handleSubmit}
        style={{ display: "flex", flexDirection: "column", gap: "15px" }}
      >
        {/* Datos básicos */}
        <input
          type="text"
          name="name"
          placeholder="Nombre"
          value={formData.name}
          onChange={handleChangeForm}
          required
        />
        <input
          type="text"
          name="surname"
          placeholder="Apellido"
          value={formData.surname}
          onChange={handleChangeForm}
          required
        />
        <input
          type="email"
          name="email"
          placeholder="Correo electrónico"
          value={formData.email}
          onChange={handleChangeForm}
          required
        />
        <input
          type="password"
          name="password"
          placeholder="Contraseña"
          value={formData.password}
          onChange={handleChangeForm}
          required
        />
        <input
          type="text"
          name="phoneNumber"
          placeholder="Teléfono"
          value={formData.phoneNumber}
          onChange={handleChangeForm}
          required
        />
        <input
          type="text"
          name="address"
          placeholder="Dirección"
          value={formData.address}
          onChange={handleChangeForm}
          required
        />

        <select
          name="roleName"
          value={formData.roleName}
          onChange={handleChangeForm}
          required
        >
          <option value="">Selecciona un rol</option>
          {roles.map((role) => (
            <option key={role.id} value={role.name}>
              {formatRoleName(role.name)}
            </option>
          ))}
        </select>

        <select
          name="status"
          value={formData.status}
          onChange={handleChangeForm}
          required
        >
          <option value="ACTIVE">Activo</option>
          <option value="INACTIVE">Inactivo</option>
        </select>

        {/* Datos si es Student */}
        {formData.roleName === "ROLE_STUDENT" && (
          <>
            <h3>Datos de Estudiante</h3>
            <input
              type="text"
              name="dni"
              placeholder="DNI"
              value={studentData.dni}
              onChange={handleChangeStudent}
              required
            />
            <input
              type="date"
              name="birthdate"
              value={studentData.birthdate}
              onChange={handleChangeStudent}
              required
            />
            <select
              name="belt"
              value={studentData.belt}
              onChange={handleChangeStudent}
              required
            >
              <option value="">Selecciona un cinturón</option>
              <option value="Blanco">Blanco</option>
              <option value="Azul">Azul</option>
              <option value="Morado">Morado</option>
              <option value="Marrón">Marrón</option>
              <option value="Negro">Negro</option>
            </select>
            <input
              type="text"
              name="parentName"
              placeholder="Nombre del Tutor (opcional)"
              value={studentData.parentName}
              onChange={handleChangeStudent}
            />
            <input
              type="text"
              name="medicalReport"
              placeholder="Informe Médico (opcional)"
              value={studentData.medicalReport}
              onChange={handleChangeStudent}
            />
          </>
        )}

        {/* Datos si es Teacher */}
        {formData.roleName === "ROLE_TEACHER" && (
          <>
            <h3>Datos de Profesor</h3>
            <input
              type="text"
              name="discipline"
              placeholder="Disciplina"
              value={teacherData.discipline}
              onChange={handleChangeTeacher}
              required
            />
          </>
        )}

        <button type="submit">Crear usuario</button>
      </form>

      {/* Mensajes de resultado */}
      <ErrorMessage message={successMsg} type="success" />
      <ErrorMessage message={errorMsg} type="error" />

    </div>
  );
};

export default UserCreateForm;
