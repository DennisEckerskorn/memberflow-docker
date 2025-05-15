import React, { useEffect, useState } from "react";
import api from "../../api/axiosConfig";
import "../styles/ContentArea.css";

const ProductList = () => {
  const [products, setProducts] = useState([]);
  const [ivaTypes, setIvaTypes] = useState([]);
  const [editIndex, setEditIndex] = useState(null);
  const [editableProduct, setEditableProduct] = useState(null);

  const fetchProducts = async () => {
    const res = await api.get("/products-services/getAll");
    setProducts(res.data);
  };

  const fetchIvaTypes = async () => {
    const res = await api.get("/iva-types/getAll");
    setIvaTypes(res.data);
  };

  useEffect(() => {
    fetchProducts();
    fetchIvaTypes();
  }, []);

  useEffect(() => {
  console.log("IVA Types cargados:", ivaTypes);
}, [ivaTypes]);

  const startEdit = (index) => {
    setEditIndex(index);
    setEditableProduct({ ...products[index] }); // contiene ivaTypeId directamente
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
        id: editableProduct.id,
        name: editableProduct.name,
        price: parseFloat(editableProduct.price),
        type: editableProduct.type,
        status: editableProduct.status,
        ivaTypeId: editableProduct.ivaTypeId,
      };

      await api.put("/products-services/update", dto);
      alert("✅ Producto actualizado correctamente.");
      setEditIndex(null);
      setEditableProduct(null);
      fetchProducts();
    } catch (err) {
      console.error(err);
      alert("❌ Error al actualizar el producto.");
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm("¿Estás seguro de que deseas eliminar este producto?")) {
      await api.delete(`/products-services/deleteById/${id}`);
      fetchProducts();
    }
  };

 const getIVADisplay = (ivaTypeId) => {
  const iva = ivaTypes.find((i) => i.id === Number(ivaTypeId));
  return iva ? `${iva.percentage}%` : "Sin IVA";
};


  return (
    <div className="content-area">
      <h2>Lista de Productos y Servicios</h2>

      <table className="table" style={{ marginTop: "2rem" }}>
        <thead>
          <tr>
            <th>ID</th>
            <th>Nombre</th>
            <th>Precio</th>
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
                <td>{p.id}</td>
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
                      value={editableProduct.price}
                      step="0.01"
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
                          {iva.name} ({iva.percentage}%)
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
                      <button className="btn btn-primary btn-sm" onClick={handleSave}>
                        💾 Guardar
                      </button>
                      <button className="btn btn-secondary btn-sm" onClick={cancelEdit}>
                        ❌ Cancelar
                      </button>
                    </>
                  ) : (
                    <>
                      <button className="btn btn-secondary btn-sm" onClick={() => startEdit(index)}>
                        ✏️ Editar
                      </button>
                      <button className="btn btn-danger btn-sm" onClick={() => handleDelete(p.id)}>
                        🗑️ Eliminar
                      </button>
                    </>
                  )}
                </td>
              </tr>
            );
          })}
        </tbody>
      </table>
    </div>
  );
};

export default ProductList;
