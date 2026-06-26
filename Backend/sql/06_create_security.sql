
SET search_path TO public;

REVOKE ALL ON SCHEMA public FROM PUBLIC;
GRANT USAGE ON SCHEMA public TO anon, authenticated, service_role;

ALTER TABLE clientes ENABLE ROW LEVEL SECURITY;
ALTER TABLE perfiles_usuarios ENABLE ROW LEVEL SECURITY;
ALTER TABLE espacios ENABLE ROW LEVEL SECURITY;
ALTER TABLE contratos ENABLE ROW LEVEL SECURITY;
ALTER TABLE ceremonias ENABLE ROW LEVEL SECURITY;
ALTER TABLE historial_eventos ENABLE ROW LEVEL SECURITY;

GRANT SELECT ON ALL TABLES IN SCHEMA public TO authenticated, service_role;
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO service_role;
-- Permitir INSERT y UPDATE explícitos a roles autenticados (controlado por RLS)
GRANT INSERT, UPDATE ON clientes TO authenticated;
GRANT INSERT, UPDATE ON espacios TO authenticated;
GRANT INSERT, UPDATE ON contratos TO authenticated;
GRANT INSERT, UPDATE ON ceremonias TO authenticated;

GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO authenticated, service_role;

ALTER DEFAULT PRIVILEGES IN SCHEMA public
GRANT SELECT ON TABLES TO authenticated, service_role;

ALTER DEFAULT PRIVILEGES IN SCHEMA public
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO service_role;

ALTER DEFAULT PRIVILEGES IN SCHEMA public
GRANT USAGE, SELECT ON SEQUENCES TO authenticated, service_role;

-- POLÍTICAS PARA CLIENTES
DROP POLICY IF EXISTS clientes_select_authenticated ON clientes;
CREATE POLICY clientes_select_authenticated ON clientes FOR SELECT TO authenticated
USING (
	auth.uid() = auth_user_id
	OR EXISTS (
		SELECT 1 FROM perfiles_usuarios pu WHERE pu.id = auth.uid() AND pu.rol IN ('ADMIN', 'OPERADOR') AND pu.activo = true
	)
);

CREATE POLICY clientes_insert_authenticated ON clientes FOR INSERT TO authenticated
WITH CHECK (
    EXISTS (SELECT 1 FROM perfiles_usuarios pu WHERE pu.id = auth.uid() AND pu.rol IN ('ADMIN', 'OPERADOR') AND pu.activo = true)
);

CREATE POLICY clientes_update_authenticated ON clientes FOR UPDATE TO authenticated
USING (
    EXISTS (SELECT 1 FROM perfiles_usuarios pu WHERE pu.id = auth.uid() AND pu.rol IN ('ADMIN', 'OPERADOR') AND pu.activo = true)
);

-- POLÍTICAS PARA PERFILES
DROP POLICY IF EXISTS perfiles_usuario_select_self ON perfiles_usuarios;
CREATE POLICY perfiles_usuario_select_self ON perfiles_usuarios FOR SELECT TO authenticated
USING (auth.uid() = id OR EXISTS (SELECT 1 FROM perfiles_usuarios pu WHERE pu.id = auth.uid() AND pu.rol = 'ADMIN'));

-- POLÍTICAS PARA ESPACIOS
DROP POLICY IF EXISTS espacios_select_authenticated ON espacios;
CREATE POLICY espacios_select_authenticated ON espacios FOR SELECT TO authenticated USING (true);

CREATE POLICY espacios_insert_authenticated ON espacios FOR INSERT TO authenticated
WITH CHECK (
    EXISTS (SELECT 1 FROM perfiles_usuarios pu WHERE pu.id = auth.uid() AND pu.rol IN ('ADMIN', 'OPERADOR') AND pu.activo = true)
);

CREATE POLICY espacios_update_authenticated ON espacios FOR UPDATE TO authenticated
USING (
    EXISTS (SELECT 1 FROM perfiles_usuarios pu WHERE pu.id = auth.uid() AND pu.rol IN ('ADMIN', 'OPERADOR') AND pu.activo = true)
);

-- POLÍTICAS PARA CONTRATOS
DROP POLICY IF EXISTS contratos_select_authenticated ON contratos;
CREATE POLICY contratos_select_authenticated ON contratos FOR SELECT TO authenticated
USING (
    EXISTS (
        SELECT 1 FROM perfiles_usuarios pu WHERE pu.id = auth.uid() AND pu.rol IN ('ADMIN', 'OPERADOR') AND pu.activo = true
    )
    OR EXISTS (
        SELECT 1 FROM clientes c WHERE c.id = contratos.cliente_id AND c.auth_user_id = auth.uid()
    )
);

CREATE POLICY contratos_insert_authenticated ON contratos FOR INSERT TO authenticated
WITH CHECK (
    EXISTS (SELECT 1 FROM perfiles_usuarios pu WHERE pu.id = auth.uid() AND pu.rol IN ('ADMIN', 'OPERADOR') AND pu.activo = true)
);

CREATE POLICY contratos_update_authenticated ON contratos FOR UPDATE TO authenticated
USING (
    EXISTS (SELECT 1 FROM perfiles_usuarios pu WHERE pu.id = auth.uid() AND pu.rol IN ('ADMIN', 'OPERADOR') AND pu.activo = true)
);

-- POLÍTICAS PARA CEREMONIAS
DROP POLICY IF EXISTS ceremonias_select_authenticated ON ceremonias;
CREATE POLICY ceremonias_select_authenticated ON ceremonias FOR SELECT TO authenticated
USING (
    EXISTS (
        SELECT 1 FROM perfiles_usuarios pu WHERE pu.id = auth.uid() AND pu.rol IN ('ADMIN', 'OPERADOR') AND pu.activo = true
    )
    OR EXISTS (
        SELECT 1 FROM clientes c WHERE c.id = ceremonias.cliente_id AND c.auth_user_id = auth.uid()
    )
);

CREATE POLICY ceremonias_insert_authenticated ON ceremonias FOR INSERT TO authenticated
WITH CHECK (
    EXISTS (SELECT 1 FROM perfiles_usuarios pu WHERE pu.id = auth.uid() AND pu.rol IN ('ADMIN', 'OPERADOR') AND pu.activo = true)
);

CREATE POLICY ceremonias_update_authenticated ON ceremonias FOR UPDATE TO authenticated
USING (
    EXISTS (SELECT 1 FROM perfiles_usuarios pu WHERE pu.id = auth.uid() AND pu.rol IN ('ADMIN', 'OPERADOR') AND pu.activo = true)
);

-- POLÍTICAS SERVICE_ROLE (Mantenidas)
DROP POLICY IF EXISTS historial_select_service_role ON historial_eventos;
CREATE POLICY historial_select_service_role ON historial_eventos FOR SELECT TO service_role USING (true);

DROP POLICY IF EXISTS clientes_write_service_role ON clientes;
CREATE POLICY clientes_write_service_role ON clientes FOR ALL TO service_role USING (true) WITH CHECK (true);

DROP POLICY IF EXISTS perfiles_usuarios_write_service_role ON perfiles_usuarios;
CREATE POLICY perfiles_usuarios_write_service_role ON perfiles_usuarios FOR ALL TO service_role USING (true) WITH CHECK (true);

DROP POLICY IF EXISTS espacios_write_service_role ON espacios;
CREATE POLICY espacios_write_service_role ON espacios FOR ALL TO service_role USING (true) WITH CHECK (true);

DROP POLICY IF EXISTS contratos_write_service_role ON contratos;
CREATE POLICY contratos_write_service_role ON contratos FOR ALL TO service_role USING (true) WITH CHECK (true);

DROP POLICY IF EXISTS ceremonias_write_service_role ON ceremonias;
CREATE POLICY ceremonias_write_service_role ON ceremonias FOR ALL TO service_role USING (true) WITH CHECK (true);

DROP POLICY IF EXISTS historial_write_service_role ON historial_eventos;
CREATE POLICY historial_write_service_role ON historial_eventos FOR ALL TO service_role USING (true) WITH CHECK (true);

GRANT SELECT ON vw_clientes_activos TO authenticated, service_role;
GRANT SELECT ON vw_panel_administrador TO authenticated, service_role;
GRANT SELECT ON vw_panel_cliente TO authenticated, service_role;
GRANT SELECT ON vw_espacios_disponibles TO authenticated, service_role;
GRANT SELECT ON vw_contratos_activos_cliente TO authenticated, service_role;
GRANT SELECT ON vw_agenda_ceremonias TO authenticated, service_role;
GRANT SELECT ON vw_ocupacion_por_sector TO authenticated, service_role;
GRANT SELECT ON vw_historial_reciente TO service_role;
GRANT SELECT ON vw_tareas_operador_hoy TO authenticated, service_role;
