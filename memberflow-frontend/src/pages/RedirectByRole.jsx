import React, { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { getRoleFromToken } from "../utils/jwtHelper";

const RedirectByRole = () => {
  const navigate = useNavigate();

  useEffect(() => {
    console.log("Entrando en RedirectByRole");

    const role = getRoleFromToken();
    console.log("Rol detectado:", role);

    switch (role) {
      case "FULL_ACCESS":
        navigate("/admin/dashboard");
        break;
      case "MANAGE_STUDENTS":
        navigate("/teacher/dashboard");
        break;
      case "VIEW_OWN_DATA":
        navigate("/student/dashboard");
        break;
      default:
        navigate("/");
    }
  }, [navigate]);

  return <p>Redirecting...</p>;
};

export default RedirectByRole;
