import React, { useEffect, useState } from "react";
import api from "../../api/axiosConfig";
import "../styles/ContentArea.css";

const ViewTimetable = () => {
  const [groups, setGroups] = useState([]);

  useEffect(() => {
    api.get("/training-groups/getAll").then((res) => setGroups(res.data));
  }, []);

  const days = ["Dom", "Lun", "Mar", "Mié", "Jue", "Vie", "Sáb"];
  const hours = [
    "07:00", "08:00", "09:00", "10:00", "11:00",
    "12:00", "13:00", "14:00", "15:00", "16:00",
    "17:00", "18:00", "19:00", "20:00", "21:00",
  ];

  return (
    <div className="card">
      <h2>Horario Semanal</h2>
      <table className="styled-table" style={{ marginTop: "20px" }}>
        <thead>
          <tr>
            <th>Hora</th>
            {days.map((day) => (
              <th key={day}>{day}</th>
            ))}
          </tr>
        </thead>
        <tbody>
          {hours.map((h) => (
            <tr key={h}>
              <td>{h}</td>
              {days.map((_, dayIndex) => (
                <td key={h + dayIndex}>
                  {groups
                    .filter((g) => {
                      const date = new Date(g.schedule);
                      return (
                        date.getHours() === parseInt(h.split(":")[0]) &&
                        date.getDay() === dayIndex
                      );
                    })
                    .map((g) => (
                      <div key={g.id}>{g.name} ({g.level})</div>
                    ))}
                </td>
              ))}
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default ViewTimetable;
