

SET search_path TO public;

CREATE UNIQUE INDEX IF NOT EXISTS uq_clientes_rut
    ON clientes (rut);

CREATE UNIQUE INDEX IF NOT EXISTS uq_clientes_auth_user_id
    ON clientes (auth_user_id)
    WHERE auth_user_id IS NOT NULL;

CREATE INDEX IF NOT EXISTS idx_clientes_created_by_user_id
    ON clientes (created_by_user_id);

CREATE UNIQUE INDEX IF NOT EXISTS uq_clientes_email
    ON clientes (email)
    WHERE email IS NOT NULL;

CREATE INDEX IF NOT EXISTS idx_espacios_sector_estado
    ON espacios (sector, estado);

CREATE UNIQUE INDEX IF NOT EXISTS uq_espacios_codigo
    ON espacios (codigo)
    WHERE codigo IS NOT NULL;

CREATE INDEX IF NOT EXISTS idx_espacios_codigo_estado
    ON espacios (codigo, estado);

CREATE INDEX IF NOT EXISTS idx_espacios_tipo_estado
    ON espacios (tipo, estado);

CREATE INDEX IF NOT EXISTS idx_espacios_created_by_user_id
    ON espacios (created_by_user_id);

CREATE INDEX IF NOT EXISTS idx_contratos_cliente_estado
    ON contratos (cliente_id, estado);

CREATE INDEX IF NOT EXISTS idx_contratos_rut_estado
    ON contratos (rut_cliente, estado);

CREATE INDEX IF NOT EXISTS idx_contratos_espacio_estado
    ON contratos (espacio_id, estado);

CREATE INDEX IF NOT EXISTS idx_contratos_fecha_inicio
    ON contratos (fecha_inicio DESC);

CREATE INDEX IF NOT EXISTS idx_contratos_created_by_user_id
    ON contratos (created_by_user_id);

CREATE INDEX IF NOT EXISTS idx_ceremonias_cliente_estado
    ON ceremonias (cliente_id, estado);

CREATE INDEX IF NOT EXISTS idx_ceremonias_fecha_hora
    ON ceremonias (fecha_hora);

CREATE INDEX IF NOT EXISTS idx_ceremonias_espacio_fecha
    ON ceremonias (espacio_id, fecha_hora);

CREATE INDEX IF NOT EXISTS idx_ceremonias_created_by_user_id
    ON ceremonias (created_by_user_id);

CREATE INDEX IF NOT EXISTS idx_ceremonias_tipo_estado
    ON ceremonias (tipo_ceremonia, estado);

CREATE INDEX IF NOT EXISTS idx_ceremonias_contrato_id
    ON ceremonias (contrato_id);

CREATE INDEX IF NOT EXISTS idx_historial_eventos_entidad_registro
    ON historial_eventos (entidad, registro_id, created_at DESC);

CREATE INDEX IF NOT EXISTS idx_historial_eventos_usuario
    ON historial_eventos (usuario_id, created_at DESC);

-- One active contract per space
CREATE UNIQUE INDEX IF NOT EXISTS uq_contrato_activo_por_espacio
    ON contratos (espacio_id)
    WHERE estado = 'ACTIVO';

CREATE UNIQUE INDEX IF NOT EXISTS uq_contrato_activo_por_cliente
    ON contratos (cliente_id)
    WHERE cliente_id IS NOT NULL AND estado = 'ACTIVO';

-- Avoid double booking for same place/time in active states
CREATE UNIQUE INDEX IF NOT EXISTS uq_ceremonia_ubicacion_fecha_activa
    ON ceremonias (ubicacion, fecha_hora)
    WHERE estado IN ('PROGRAMADA', 'CONFIRMADA');

CREATE UNIQUE INDEX IF NOT EXISTS uq_ceremonia_espacio_fecha_activa
    ON ceremonias (espacio_id, fecha_hora)
    WHERE espacio_id IS NOT NULL AND estado IN ('PROGRAMADA', 'CONFIRMADA');


