import React from "react";

const ErrorMessage = ({ message, type = "error" }) => {
  if (!message) return null;

  const styles = {
    padding: "10px",
    marginTop: "10px",
    borderRadius: "5px",
    color: type === "error" ? "#a94442" : "#155724",
    backgroundColor: type === "error" ? "#f8d7da" : "#d4edda",
    border: type === "error" ? "1px solid #f5c6cb" : "1px solid #c3e6cb",
  };

  return <div style={styles}>{message}</div>;
};

export default ErrorMessage;
