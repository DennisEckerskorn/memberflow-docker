import React, { useEffect, useState } from "react";
import api from "../../api/axiosConfig";
import ErrorMessage from "../common/ErrorMessage";

const TrainingGroupForm = () => {
  const [formData, setFormData] = useState({
    name: "",
    level: "",
    schedule: "",
    teacherId: "",
  });

  const [teachers, setTeachers] = useState([]);
  const [successMsg, setSuccessMsg] = useState("");
  const [errorMsg, setErrorMsg] = useState("");

  useEffect(() => {
    api.get("/teachers/getAll")
      .then((res) => setTeachers(res.data))
      .catch((err) => console.error("Error loading teachers", err));
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSuccessMsg("");
    setErrorMsg("");

    try {
      await api.post("/training-groups/create", {
        name: formData.name,
        level: formData.level,
        schedule: formData.schedule,
        teacherId: parseInt(formData.teacherId),
      });
      setSuccessMsg("Training group created successfully.");
      setFormData({ name: "", level: "", schedule: "", teacherId: "" });
    } catch (err) {
      console.error("Error al crear grupo", err);
      const backendMsg =
        err.response?.data?.message ||
        err.response?.data?.error ||
        "❌ Error al crear el grupo. Verifica los datos.";
      setErrorMsg(backendMsg);
    }
    
  };

  return (
    <div className="card">
      <h2>Create Training Group</h2>
      <form onSubmit={handleSubmit} className="form">
        <label>Name:</label>
        <input type="text" name="name" value={formData.name} onChange={handleChange} required />

        <label>Level:</label>
        <input type="text" name="level" value={formData.level} onChange={handleChange} required />

        <label>Schedule:</label>
        <input type="datetime-local" name="schedule" value={formData.schedule} onChange={handleChange} required />

        <label>Teacher:</label>
        <select name="teacherId" value={formData.teacherId} onChange={handleChange} required>
          <option value="">-- Select Teacher --</option>
          {teachers.map((teacher) => (
            <option key={teacher.id} value={teacher.id}>
              {teacher.user?.name} {teacher.user?.surname}
            </option>
          ))}
        </select>

        <button type="submit">Create Group</button>
      </form>

      <ErrorMessage message={errorMsg} type="error" />
      <ErrorMessage message={successMsg} type="success" />

    </div>
  );
};

export default TrainingGroupForm;
