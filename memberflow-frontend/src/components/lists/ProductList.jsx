import React, { useEffect, useState } from "react";
import api from "../../api/axiosConfig";
import ErrorMessage from "../common/ErrorMessage";
import "../styles/ContentArea.css";

const ProductList = () => {
  const [products, setProducts] = useState([]);
  const [ivaTypes, setIvaTypes] = useState([]);
  const [editIndex, setEditIndex] = useState(null);
  const [editableProduct, setEditableProduct] = useState(null);
  const [errorMsg, setErrorMsg] = useState("");
  const [successMsg, setSuccessMsg] = useState("");

  useEffect(() => {
    fetchProducts();
    fetchIvaTypes();
  }, []);

  const fetchProducts = async () => {
    try {
      const res = await api.get("/products-services/getAll");
      setProducts(res.data);
    } catch (err) {
      setErrorMsg("❌ Error al cargar los productos.");
    }
  };

  const fetchIvaTypes = async () => {
    try {
      const res = await api.get("/iva-types/getAll");
      setIvaTypes(res.data);
    } catch (err) {
      setErrorMsg("❌ Error al cargar tipos de IVA.");
    }
  };

  const startEdit = (index) => {
    setEditIndex(index);
    setEditableProduct({ ...products[index] });
  };

  const cancelEdit = () => {
    setEditIndex(null);
    setEditableProduct(null);
  };

  const handleChange = (field, value) => {
    setEditableProduct((prev) => ({
      ...prev,
      [field]: field === "ivaTypeId" ? parseInt(value) : value,
    }));
  };

  const handleSave = async () => {
    try {
      const dto = {
        ...editableProduct,
        price: parseFloat(editableProduct.price)
      };
      await api.put("/products-services/update", dto);
      setSuccessMsg("✅ Producto actualizado correctamente.");
      setErrorMsg("");
      cancelEdit();
      fetchProducts();
    } catch (err) {
      setErrorMsg("❌ Error al actualizar el producto.");
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm("¿Seguro que quieres eliminar este producto?")) {
      try {
        await api.delete(`/products-services/deleteById/${id}`);
        fetchProducts();
        setSuccessMsg("✅ Producto eliminado correctamente.");
        setErrorMsg("");
      } catch {
        setErrorMsg("❌ Error al eliminar el producto.");
      }
    }
  };

  const getIVADisplay = (ivaTypeId) => {
    const iva = ivaTypes.find((i) => i.id === Number(ivaTypeId));
    return iva?.percentage ? `${iva.percentage}%` : "";
  };

  return (
    <div className="card">
      <h2>Lista de Productos y Servicios</h2>
      <ErrorMessage message={errorMsg} type="error" />
      <ErrorMessage message={successMsg} type="success" />

      <div className="table-wrapper">
        <table className="styled-table">
          <thead>
            <tr>
              <th>Nombre</th>
              <th>Precio (€)</th>
              <th>IVA</th>
              <th>Tipo</th>
              <th>Estado</th>
              <th>Acciones</th>
            </tr>
          </thead>
          <tbody>
            {products.map((p, index) => {
              const isEditing = index === editIndex;
              return (
                <tr key={p.id}>
                  <td>
                    {isEditing ? (
                      <input
                        type="text"
                        value={editableProduct.name}
                        onChange={(e) => handleChange("name", e.target.value)}
                      />
                    ) : (
                      p.name
                    )}
                  </td>
                  <td>
                    {isEditing ? (
                      <input
                        type="number"
                        step="0.01"
                        value={editableProduct.price}
                        onChange={(e) => handleChange("price", e.target.value)}
                      />
                    ) : (
                      `${p.price.toFixed(2)} €`
                    )}
                  </td>
                  <td>
                    {isEditing ? (
                      <select
                        value={editableProduct.ivaTypeId || ""}
                        onChange={(e) => handleChange("ivaTypeId", e.target.value)}
                      >
                        <option value="">-- IVA --</option>
                        {ivaTypes.map((iva) => (
                          <option key={iva.id} value={iva.id}>
                            {iva.percentage}%
                          </option>
                        ))}
                      </select>
                    ) : (
                      getIVADisplay(p.ivaTypeId)
                    )}
                  </td>
                  <td>
                    {isEditing ? (
                      <select
                        value={editableProduct.type}
                        onChange={(e) => handleChange("type", e.target.value)}
                      >
                        <option value="PRODUCT">Producto</option>
                        <option value="SERVICE">Servicio</option>
                      </select>
                    ) : (
                      p.type
                    )}
                  </td>
                  <td>
                    {isEditing ? (
                      <select
                        value={editableProduct.status}
                        onChange={(e) => handleChange("status", e.target.value)}
                      >
                        <option value="ACTIVE">Activo</option>
                        <option value="INACTIVE">Inactivo</option>
                      </select>
                    ) : (
                      p.status
                    )}
                  </td>
                  <td>
                    {isEditing ? (
                      <>
                        <button className="edit-btn" onClick={handleSave}>💾</button>
                        <button className="delete-btn" onClick={cancelEdit}>❌</button>
                      </>
                    ) : (
                      <>
                        <button className="edit-btn" onClick={() => startEdit(index)}>✏️</button>
                        <button className="delete-btn" onClick={() => handleDelete(p.id)}>🗑️</button>
                      </>
                    )}
                  </td>
                </tr>
              );
            })}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default ProductList;
