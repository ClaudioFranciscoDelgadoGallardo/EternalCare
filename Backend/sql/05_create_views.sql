
SET search_path TO public;

CREATE OR REPLACE VIEW vw_panel_administrador AS
SELECT
    (SELECT COUNT(*) FROM clientes WHERE estado = 'activo') AS total_clientes_activos,
    (SELECT COUNT(*) FROM contratos WHERE estado = 'ACTIVO') AS contratos_activos,
    (SELECT COUNT(*) FROM espacios WHERE estado = 'disponible') AS espacios_disponibles,
    (SELECT COUNT(*) FROM espacios WHERE estado = 'ocupado') AS espacios_ocupados,
    (SELECT COUNT(*) FROM ceremonias WHERE estado IN ('PROGRAMADA', 'CONFIRMADA')) AS ceremonias_pendientes,
    (SELECT COUNT(*) FROM perfiles_usuarios WHERE rol = 'ADMIN' AND activo = true) AS administradores_activos;

CREATE OR REPLACE VIEW vw_panel_cliente AS
SELECT
    c.id AS cliente_id,
    c.rut,
    c.nombres,
    c.apellidos,
    c.email,
    c.telefono,
    c.estado,
    COUNT(DISTINCT co.id) FILTER (WHERE co.estado = 'ACTIVO') AS contratos_activos,
    COUNT(DISTINCT ce.id) FILTER (WHERE ce.estado IN ('PROGRAMADA', 'CONFIRMADA')) AS ceremonias_pendientes,
    MIN(ce.fecha_hora) FILTER (WHERE ce.estado IN ('PROGRAMADA', 'CONFIRMADA')) AS proxima_ceremonia,
    STRING_AGG(DISTINCT e.codigo, ', ' ORDER BY e.codigo) FILTER (WHERE e.codigo IS NOT NULL) AS espacios_asociados
FROM clientes c
LEFT JOIN contratos co ON co.cliente_id = c.id
LEFT JOIN ceremonias ce ON ce.cliente_id = c.id
LEFT JOIN espacios e ON e.id = co.espacio_id
WHERE c.auth_user_id = auth.uid()
GROUP BY c.id, c.rut, c.nombres, c.apellidos, c.email, c.telefono, c.estado;

CREATE OR REPLACE VIEW vw_clientes_activos AS
SELECT
    c.id,
    c.rut,
    c.nombres,
    c.apellidos,
    c.email,
    c.telefono,
    c.estado,
    COUNT(co.id) FILTER (WHERE co.estado = 'ACTIVO') AS contratos_activos,
    MAX(co.fecha_inicio) AS ultimo_contrato_inicio
FROM clientes c
LEFT JOIN contratos co ON co.cliente_id = c.id
GROUP BY c.id, c.rut, c.nombres, c.apellidos, c.email, c.telefono, c.estado;

CREATE OR REPLACE VIEW vw_espacios_disponibles AS
SELECT
    e.id,
    e.codigo,
    e.sector,
    e.tipo,
    e.capacidad,
    e.georreferenciacion,
    e.descripcion,
    e.updated_at
FROM espacios e
WHERE e.estado = 'disponible';

CREATE OR REPLACE VIEW vw_contratos_activos_cliente AS
SELECT
    c.id AS contrato_id,
    c.cliente_id,
    c.rut_cliente,
    cl.nombres,
    cl.apellidos,
    c.espacio_id,
    e.codigo AS espacio_codigo,
    e.sector,
    e.tipo AS tipo_espacio,
    c.fecha_inicio,
    c.fecha_termino,
    c.monto_mensual,
    c.estado,
    c.updated_at
FROM contratos c
LEFT JOIN clientes cl ON cl.id = c.cliente_id
JOIN espacios e ON e.id = c.espacio_id
WHERE c.estado = 'ACTIVO';

CREATE OR REPLACE VIEW vw_agenda_ceremonias AS
SELECT
    ce.id AS ceremonia_id,
    ce.cliente_id,
    cl.rut AS cliente_rut,
    cl.nombres AS cliente_nombres,
    cl.apellidos AS cliente_apellidos,
    ce.tipo_ceremonia,
    ce.fecha_hora,
    ce.espacio_id,
    e.codigo AS espacio_codigo,
    ce.ubicacion,
    ce.estado AS estado_ceremonia,
    ce.contrato_id,
    c.rut_cliente,
    c.estado AS estado_contrato,
    ce.responsable_usuario_id,
    pu.nombres AS responsable_nombres,
    pu.apellidos AS responsable_apellidos,
    ce.observacion
FROM ceremonias ce
LEFT JOIN clientes cl ON cl.id = ce.cliente_id
LEFT JOIN contratos c ON c.id = ce.contrato_id
LEFT JOIN espacios e ON e.id = ce.espacio_id
LEFT JOIN perfiles_usuarios pu ON pu.id = ce.responsable_usuario_id;

CREATE OR REPLACE VIEW vw_ocupacion_por_sector AS
SELECT
    e.sector,
    COUNT(*) AS espacios_totales,
    COUNT(*) FILTER (WHERE e.estado = 'reservado') AS espacios_reservados,
    COUNT(*) FILTER (WHERE e.estado = 'ocupado') AS espacios_ocupados,
    COUNT(*) FILTER (WHERE e.estado = 'disponible') AS espacios_disponibles,
    ROUND(
        (COUNT(*) FILTER (WHERE e.estado = 'ocupado')::numeric / NULLIF(COUNT(*), 0)::numeric) * 100,
        2
    ) AS porcentaje_ocupacion
FROM espacios e
GROUP BY e.sector;

CREATE OR REPLACE VIEW vw_historial_reciente AS
SELECT
    h.id,
    h.entidad,
    h.registro_id,
    h.accion,
    h.detalle,
    h.usuario_id,
    h.created_at
FROM historial_eventos h
ORDER BY h.created_at DESC;

-- MODIFICACIÓN CRÍTICA: Vista para el trabajo diario de los operadores
CREATE OR REPLACE VIEW vw_tareas_operador_hoy AS
SELECT 
    ce.id AS ceremonia_id,
    ce.fecha_hora,
    ce.tipo_ceremonia,
    e.sector,
    e.codigo AS espacio,
    c.nombres || ' ' || c.apellidos AS cliente,
    ce.estado
FROM ceremonias ce
JOIN espacios e ON ce.espacio_id = e.id
JOIN clientes c ON ce.cliente_id = c.id
WHERE DATE(ce.fecha_hora) = CURRENT_DATE
  AND ce.estado IN ('PROGRAMADA', 'CONFIRMADA')
ORDER BY ce.fecha_hora ASC;

