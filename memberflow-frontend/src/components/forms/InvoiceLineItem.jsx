import React from 'react';

const InvoiceLineItem = ({ index, line, products, onUpdate, onRemove }) => {
  const handleChange = (e) => {
    onUpdate(index, { ...line, [e.target.name]: e.target.value });
  };

  const selectedProduct = products.find(p => p.id === parseInt(line.productServiceId));
  const quantity = parseInt(line.quantity) || 1;
  const price = selectedProduct?.price || 0;
  const iva = selectedProduct?.ivaType?.percentage || 0;
  const totalConIVA = price * quantity * (1 + iva / 100);

  return (
    <div className="card" style={{ padding: "20px", marginBottom: "1rem" }}>
      <div className="form-column">
        <label>Producto</label>
        <select
          name="productServiceId"
          value={line.productServiceId}
          onChange={handleChange}
          className="form-select"
          required
        >
          <option value="">Selecciona producto</option>
          {products.map((p) => (
            <option key={p.id} value={p.id}>
              {p.name} - {p.price}€ ({p.type})
            </option>
          ))}
        </select>

        <label>Cantidad</label>
        <input
          type="number"
          name="quantity"
          min="1"
          value={line.quantity}
          onChange={handleChange}
          className="form-input"
          required
        />

        {selectedProduct && (
          <div className="invoice-line-summary">
            <p><strong>{selectedProduct.name}</strong>: {selectedProduct.description}</p>
            <p><strong>IVA:</strong> {iva}%</p>
            <p><strong>Total con IVA:</strong> {totalConIVA.toFixed(2)} €</p>
          </div>
        )}

        <button className="btn btn-danger" onClick={() => onRemove(index)}>
          ❌ Eliminar Producto
        </button>
      </div>
    </div>
  );
};

export default InvoiceLineItem;
