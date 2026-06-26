
SET search_path TO public;

DROP TRIGGER IF EXISTS trg_touch_clientes ON clientes;
CREATE TRIGGER trg_touch_clientes
BEFORE UPDATE ON clientes
FOR EACH ROW
EXECUTE FUNCTION fn_touch_row();

DROP TRIGGER IF EXISTS trg_touch_perfiles_usuarios ON perfiles_usuarios;
CREATE TRIGGER trg_touch_perfiles_usuarios
BEFORE UPDATE ON perfiles_usuarios
FOR EACH ROW
EXECUTE FUNCTION fn_touch_row();

DROP TRIGGER IF EXISTS trg_touch_espacios ON espacios;
CREATE TRIGGER trg_touch_espacios
BEFORE UPDATE ON espacios
FOR EACH ROW
EXECUTE FUNCTION fn_touch_row();

DROP TRIGGER IF EXISTS trg_validar_espacio ON espacios;
CREATE TRIGGER trg_validar_espacio
BEFORE INSERT OR UPDATE ON espacios
FOR EACH ROW
EXECUTE FUNCTION fn_validar_espacio();

DROP TRIGGER IF EXISTS trg_touch_contratos ON contratos;
CREATE TRIGGER trg_touch_contratos
BEFORE UPDATE ON contratos
FOR EACH ROW
EXECUTE FUNCTION fn_touch_row();

DROP TRIGGER IF EXISTS trg_touch_clientes_historial ON clientes;

DROP TRIGGER IF EXISTS trg_touch_ceremonias ON ceremonias;
CREATE TRIGGER trg_touch_ceremonias
BEFORE UPDATE ON ceremonias
FOR EACH ROW
EXECUTE FUNCTION fn_touch_row();

DROP TRIGGER IF EXISTS trg_touch_historial_eventos ON historial_eventos;

DROP TRIGGER IF EXISTS trg_normalize_cliente ON clientes;
CREATE TRIGGER trg_normalize_cliente
BEFORE INSERT OR UPDATE ON clientes
FOR EACH ROW
EXECUTE FUNCTION fn_normalize_cliente();

DROP TRIGGER IF EXISTS trg_sync_contrato_cliente ON contratos;
CREATE TRIGGER trg_sync_contrato_cliente
BEFORE INSERT OR UPDATE ON contratos
FOR EACH ROW
EXECUTE FUNCTION fn_sync_contrato_cliente();

DROP TRIGGER IF EXISTS trg_validate_contrato ON contratos;
CREATE TRIGGER trg_validate_contrato
BEFORE INSERT OR UPDATE ON contratos
FOR EACH ROW
EXECUTE FUNCTION fn_normalize_and_validate_contrato();

DROP TRIGGER IF EXISTS trg_sync_espacio_estado_por_contrato ON contratos;
CREATE TRIGGER trg_sync_espacio_estado_por_contrato
AFTER INSERT OR UPDATE OF estado, espacio_id OR DELETE ON contratos
FOR EACH ROW
EXECUTE FUNCTION fn_sync_espacio_estado_por_contrato();

DROP TRIGGER IF EXISTS trg_validate_ceremonia ON ceremonias;
CREATE TRIGGER trg_validate_ceremonia
BEFORE INSERT OR UPDATE ON ceremonias
FOR EACH ROW
EXECUTE FUNCTION fn_validate_ceremonia();

DROP TRIGGER IF EXISTS trg_sync_espacio_estado_por_ceremonia ON ceremonias;
CREATE TRIGGER trg_sync_espacio_estado_por_ceremonia
AFTER INSERT OR UPDATE OF estado, espacio_id OR DELETE ON ceremonias
FOR EACH ROW
EXECUTE FUNCTION fn_sync_espacio_estado_por_ceremonia();

DROP TRIGGER IF EXISTS trg_prevent_delete_espacio_with_active_contract ON espacios;
CREATE TRIGGER trg_prevent_delete_espacio_with_active_contract
BEFORE DELETE ON espacios
FOR EACH ROW
EXECUTE FUNCTION fn_prevent_delete_espacio_with_active_contract();

DROP TRIGGER IF EXISTS trg_historial_clientes ON clientes;
CREATE TRIGGER trg_historial_clientes
AFTER INSERT OR UPDATE OR DELETE ON clientes
FOR EACH ROW
EXECUTE FUNCTION fn_registrar_historial();

DROP TRIGGER IF EXISTS trg_historial_perfiles_usuarios ON perfiles_usuarios;
CREATE TRIGGER trg_historial_perfiles_usuarios
AFTER INSERT OR UPDATE OR DELETE ON perfiles_usuarios
FOR EACH ROW
EXECUTE FUNCTION fn_registrar_historial();

DROP TRIGGER IF EXISTS trg_historial_espacios ON espacios;
CREATE TRIGGER trg_historial_espacios
AFTER INSERT OR UPDATE OR DELETE ON espacios
FOR EACH ROW
EXECUTE FUNCTION fn_registrar_historial();

DROP TRIGGER IF EXISTS trg_historial_contratos ON contratos;
CREATE TRIGGER trg_historial_contratos
AFTER INSERT OR UPDATE OR DELETE ON contratos
FOR EACH ROW
EXECUTE FUNCTION fn_registrar_historial();

DROP TRIGGER IF EXISTS trg_historial_ceremonias ON ceremonias;
CREATE TRIGGER trg_historial_ceremonias
AFTER INSERT OR UPDATE OR DELETE ON ceremonias
FOR EACH ROW
EXECUTE FUNCTION fn_registrar_historial();

