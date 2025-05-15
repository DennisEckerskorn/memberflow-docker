import React from 'react';

const InvoiceLineItem = ({ index, line, products, onUpdate, onRemove }) => {
  const handleChange = (e) => {
    onUpdate(index, { ...line, [e.target.name]: e.target.value });
  };

  const selectedProduct = products.find(p => p.id === parseInt(line.productServiceId));
  const quantity = parseInt(line.quantity) || 1;
  const price = selectedProduct?.price || 0;
  const ivaPercentage = selectedProduct?.ivaType?.percentage || 0;
  const totalConIVA = price * quantity * (1 + ivaPercentage / 100);

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
        <>
          <div style={{ fontSize: '0.9rem', color: '#666' }}>
            <strong>{selectedProduct.name}</strong>: {selectedProduct.description}
          </div>
          <div style={{ fontSize: '0.9rem', color: '#333' }}>
            <strong>Total con IVA:</strong> {totalConIVA.toFixed(2)} €
          </div>
        </>
      )}

      <button className="btn btn-danger" onClick={() => onRemove(index)}>Eliminar producto</button>
    </div>
  );
};

export default InvoiceLineItem;
