// src/components/forms/InvoiceLineItem.jsx

import React from 'react';

const InvoiceLineItem = ({ index, line, products, onUpdate, onRemove }) => {
  const handleChange = (e) => {
    onUpdate(index, { ...line, [e.target.name]: e.target.value });
  };

  const selectedProduct = products.find(p => p.id === parseInt(line.productServiceId));

  return (
    <div className="content-area" style={{ marginBottom: "1rem", display: "flex", flexDirection: "column", gap: "0.5rem" }}>
      <select
        name="productServiceId"
        value={line.productServiceId}
        onChange={handleChange}
        className="form-select"
      >
        <option value="">Selecciona producto</option>
        {products.map(p => (
          <option key={p.id} value={p.id}>
            {p.name} - {p.price}€ ({p.type})
          </option>
        ))}
      </select>

      <input
        type="number"
        name="quantity"
        min="1"
        value={line.quantity}
        onChange={handleChange}
        className="form-input"
        placeholder="Cantidad"
      />

      {selectedProduct && (
        <div style={{ fontSize: '0.9rem', color: '#666' }}>
          <strong>{selectedProduct.name}</strong>: {selectedProduct.description}
        </div>
      )}

      <button className="btn btn-danger" onClick={() => onRemove(index)}>Eliminar producto</button>
    </div>
  );
};

export default InvoiceLineItem;
