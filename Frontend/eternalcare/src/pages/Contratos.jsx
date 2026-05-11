    import { Eye, PencilLine, Plus, X } from 'lucide-react';
    import Button from 'react-bootstrap/Button';
    import Form from 'react-bootstrap/Form';
    import Modal from 'react-bootstrap/Modal';
    import Table from 'react-bootstrap/Table';
    import SectionHeader from '../components/SectionHeader';
    import useContratos from '../hooks/useContratos';

    const Contratos = () => {
        const {
            contratos,
            loading,
            error,
            showModal,
            formData,
            totalContratos,
            handleOpenModal,
            handleCloseModal,
            handleChange,
            handleSubmit,
            handleEditContrato,
            handleVerDetalle,
        } = useContratos();

        return (
            <div className="page-surface">
                <SectionHeader
                    eyebrow="Módulo de contratos"
                    title="Contratos"
                    description="Listado, creación y acciones rápidas sobre contratos activos."
                />

                <div className="row g-3 mb-4">
                    <div className="col-md-4">
                        <div className="card card-ec h-100">
                            <div className="card-body">
                                <p className="text-secondary mb-1">Total de contratos</p>
                                <h2 className="fw-bold mb-0">{loading ? '...' : totalContratos}</h2>
                            </div>
                        </div>
                    </div>
                    <div className="col-md-4">
                        <div className="card card-ec h-100">
                            <div className="card-body">
                                <p className="text-secondary mb-1">Con formulario reactivo</p>
                                <h2 className="fw-bold mb-0">Sí</h2>
                            </div>
                        </div>
                    </div>
                    <div className="col-md-4">
                        <div className="card card-ec h-100">
                            <div className="card-body">
                                <p className="text-secondary mb-1">Tabla responsiva</p>
                                <h2 className="fw-bold mb-0">Bootstrap</h2>
                            </div>
                        </div>
                    </div>
                </div>

                <div className="card card-ec">
                    <div className="card-body p-4">
                        <div className="d-flex flex-column flex-md-row justify-content-between align-items-md-center gap-3 mb-4">
                            <div>
                                <h2 className="h4 fw-bold mb-1">Contratos registrados</h2>
                                <p className="text-secondary mb-0">Datos consumidos desde contratosService.js.</p>
                            </div>
                            <Button className="btn-primary-ec d-inline-flex align-items-center gap-2" onClick={handleOpenModal}>
                                <Plus size={18} />
                                Nuevo Contrato
                            </Button>
                        </div>

                        {error ? <div className="alert alert-warning mb-4">{error}</div> : null}

                        <div className="table-responsive-md">
                            <Table hover responsive className="align-middle mb-0">
                                <thead className="table-light">
                                    <tr>
                                        <th>Rut del Cliente</th>
                                        <th>ID del Espacio</th>
                                        <th>Tipo de Plan</th>
                                        <th className="text-nowrap">Acciones</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {loading ? (
                                        <tr>
                                            <td colSpan={4} className="text-center py-5 text-secondary">
                                                Cargando contratos...
                                            </td>
                                        </tr>
                                    ) : contratos.length > 0 ? (
                                        contratos.map((contrato, index) => (
                                            <tr key={contrato.id ?? `${contrato.rutCliente ?? 'contrato'}-${index}`}>
                                                <td>{contrato.rutCliente ?? contrato.rut ?? '-'}</td>
                                                <td>{contrato.idEspacio ?? contrato.espacioId ?? '-'}</td>
                                                <td>{contrato.tipoPlan ?? contrato.plan ?? '-'}</td>
                                                <td>
                                                    <div className="d-flex flex-wrap gap-2">
                                                        <Button
                                                            variant="outline-primary"
                                                            size="sm"
                                                            className="d-inline-flex align-items-center gap-1"
                                                            type="button"
                                                            onClick={() => handleEditContrato(contrato)}
                                                        >
                                                            <PencilLine size={16} />
                                                            Editar
                                                        </Button>
                                                        <Button
                                                            variant="outline-secondary"
                                                            size="sm"
                                                            className="d-inline-flex align-items-center gap-1"
                                                            type="button"
                                                            onClick={() => handleVerDetalle(contrato)}
                                                        >
                                                            <Eye size={16} />
                                                            Ver detalle
                                                        </Button>
                                                    </div>
                                                </td>
                                            </tr>
                                        ))
                                    ) : (
                                        <tr>
                                            <td colSpan={4} className="text-center py-5 text-secondary">
                                                No hay contratos cargados todavía.
                                            </td>
                                        </tr>
                                    )}
                                </tbody>
                            </Table>
                        </div>
                    </div>
                </div>

                <Modal show={showModal} onHide={handleCloseModal} centered>
                    <Modal.Header closeButton>
                        <Modal.Title>Nuevo Contrato</Modal.Title>
                    </Modal.Header>
                    <Form onSubmit={handleSubmit}>
                        <Modal.Body>
                            <Form.Group className="mb-3" controlId="rutCliente">
                                <Form.Label>Rut del Cliente</Form.Label>
                                <Form.Control
                                    type="text"
                                    name="rutCliente"
                                    value={formData.rutCliente}
                                    onChange={handleChange}
                                    placeholder="12.345.678-9"
                                    required
                                />
                            </Form.Group>

                            <Form.Group className="mb-3" controlId="idEspacio">
                                <Form.Label>ID del Espacio (Inventario)</Form.Label>
                                <Form.Control
                                    type="text"
                                    name="idEspacio"
                                    value={formData.idEspacio}
                                    onChange={handleChange}
                                    placeholder="ESP-001"
                                    required
                                />
                            </Form.Group>

                            <Form.Group controlId="tipoPlan">
                                <Form.Label>Tipo de Plan</Form.Label>
                                <Form.Select name="tipoPlan" value={formData.tipoPlan} onChange={handleChange} required>
                                    <option value="">Selecciona un plan</option>
                                    <option value="basico">Básico</option>
                                    <option value="estandar">Estándar</option>
                                    <option value="premium">Premium</option>
                                </Form.Select>
                            </Form.Group>
                        </Modal.Body>
                        <Modal.Footer>
                            <Button
                                variant="outline-secondary"
                                onClick={handleCloseModal}
                                className="d-inline-flex align-items-center gap-1"
                            >
                                <X size={16} />
                                Cerrar
                            </Button>
                            <Button type="submit" className="btn-primary-ec">
                                Guardar contrato
                            </Button>
                        </Modal.Footer>
                    </Form>
                </Modal>
            </div>
        );
    };

    export default Contratos;