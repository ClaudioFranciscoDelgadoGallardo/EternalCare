

SET search_path TO public;

CREATE OR REPLACE FUNCTION public.fn_touch_row()
RETURNS TRIGGER
LANGUAGE plpgsql
AS $$
BEGIN
    NEW.updated_at := NOW();
    NEW.version := COALESCE(OLD.version, 0) + 1;
    RETURN NEW;
END;
$$;

CREATE OR REPLACE FUNCTION public.fn_normalize_cliente()
RETURNS TRIGGER
LANGUAGE plpgsql
AS $$
BEGIN
    NEW.rut := UPPER(TRIM(NEW.rut));
    NEW.nombres := TRIM(NEW.nombres);
    NEW.apellidos := TRIM(NEW.apellidos);

    IF NEW.rut !~ '^[0-9]{7,8}-[0-9K]$' THEN
        RAISE EXCEPTION 'RUT invalido: % (formato esperado 12345678-9 o 12345678-K)', NEW.rut;
    END IF;

    IF NEW.nombres IS NULL OR NEW.nombres = '' THEN
        RAISE EXCEPTION 'nombres es obligatorio';
    END IF;

    IF NEW.apellidos IS NULL OR NEW.apellidos = '' THEN
        RAISE EXCEPTION 'apellidos es obligatorio';
    END IF;

    RETURN NEW;
END;
$$;

CREATE OR REPLACE FUNCTION public.fn_validar_espacio()
RETURNS TRIGGER
LANGUAGE plpgsql
AS $$
BEGIN
    NEW.sector := TRIM(NEW.sector);
    NEW.tipo := TRIM(NEW.tipo);

    IF NEW.sector IS NULL OR NEW.sector = '' THEN
        RAISE EXCEPTION 'sector es obligatorio';
    END IF;

    IF NEW.tipo IS NULL OR NEW.tipo = '' THEN
        RAISE EXCEPTION 'tipo es obligatorio';
    END IF;

    IF NEW.capacidad IS NULL OR NEW.capacidad < 1 THEN
        NEW.capacidad := 1;
    END IF;

    RETURN NEW;
END;
$$;

CREATE OR REPLACE FUNCTION public.fn_sync_contrato_cliente()
RETURNS TRIGGER
LANGUAGE plpgsql
AS $$
DECLARE
    v_rut_cliente VARCHAR(12);
BEGIN
    IF NEW.cliente_id IS NOT NULL THEN
        SELECT c.rut
          INTO v_rut_cliente
          FROM clientes c
         WHERE c.id = NEW.cliente_id;

        IF v_rut_cliente IS NULL THEN
            RAISE EXCEPTION 'Cliente % no existe', NEW.cliente_id;
        END IF;

        IF NEW.rut_cliente IS NULL OR TRIM(NEW.rut_cliente) = '' THEN
            NEW.rut_cliente := v_rut_cliente;
        ELSIF UPPER(TRIM(NEW.rut_cliente)) <> v_rut_cliente THEN
            RAISE EXCEPTION 'El rut_cliente no coincide con el cliente_id asociado';
        END IF;
    ELSIF NEW.rut_cliente IS NOT NULL THEN
        NEW.rut_cliente := UPPER(TRIM(NEW.rut_cliente));

        IF NEW.rut_cliente !~ '^[0-9]{7,8}-[0-9K]$' THEN
            RAISE EXCEPTION 'RUT invalido: % (formato esperado 12345678-9 o 12345678-K)', NEW.rut_cliente;
        END IF;

        SELECT c.id
          INTO NEW.cliente_id
          FROM clientes c
         WHERE c.rut = NEW.rut_cliente
         LIMIT 1;
    ELSE
        RAISE EXCEPTION 'Se requiere rut_cliente o cliente_id';
    END IF;

    IF NEW.monto_mensual <= 0 THEN
        RAISE EXCEPTION 'monto_mensual debe ser mayor que 0';
    END IF;

    IF NEW.fecha_termino IS NOT NULL AND NEW.fecha_termino < NEW.fecha_inicio THEN
        RAISE EXCEPTION 'fecha_termino no puede ser menor que fecha_inicio';
    END IF;

    RETURN NEW;
END;
$$;

CREATE OR REPLACE FUNCTION public.fn_normalize_and_validate_contrato()
RETURNS TRIGGER
LANGUAGE plpgsql
AS $$
BEGIN
    IF NEW.monto_mensual <= 0 THEN
        RAISE EXCEPTION 'monto_mensual debe ser mayor que 0';
    END IF;

    IF NEW.fecha_termino IS NOT NULL AND NEW.fecha_termino < NEW.fecha_inicio THEN
        RAISE EXCEPTION 'fecha_termino no puede ser menor que fecha_inicio';
    END IF;

    RETURN NEW;
END;
$$;

CREATE OR REPLACE FUNCTION public.fn_sync_espacio_estado_por_contrato()
RETURNS TRIGGER
LANGUAGE plpgsql
AS $$
DECLARE
    v_activos BIGINT;
    v_ceremonias_activas BIGINT;
    v_espacio_id BIGINT;
BEGIN
    v_espacio_id := COALESCE(NEW.espacio_id, OLD.espacio_id);

    IF TG_OP IN ('INSERT', 'UPDATE') THEN
        IF NEW.estado = 'ACTIVO' THEN
            -- MODIFICACIÓN CRÍTICA: No sobreescribir si está en mantenimiento
            UPDATE espacios
               SET estado = 'ocupado'
             WHERE id = v_espacio_id AND estado <> 'mantenimiento';
        END IF;

        IF TG_OP = 'UPDATE' AND OLD.estado = 'ACTIVO' AND NEW.estado IN ('TERMINADO', 'CANCELADO', 'PAUSADO') THEN
            SELECT COUNT(*)
              INTO v_activos
              FROM contratos c
             WHERE c.espacio_id = OLD.espacio_id
               AND c.estado = 'ACTIVO'
               AND c.id <> NEW.id;

            IF v_activos = 0 THEN
                SELECT COUNT(*)
                  INTO v_ceremonias_activas
                  FROM ceremonias ce
                 WHERE ce.espacio_id = OLD.espacio_id
                   AND ce.estado IN ('PROGRAMADA', 'CONFIRMADA');

                UPDATE espacios
                   SET estado = CASE WHEN v_ceremonias_activas > 0 THEN 'reservado' ELSE 'disponible' END
                 WHERE id = OLD.espacio_id
                   AND estado <> 'mantenimiento';
            END IF;
        END IF;
    ELSIF TG_OP = 'DELETE' THEN
        SELECT COUNT(*)
          INTO v_activos
          FROM contratos c
         WHERE c.espacio_id = OLD.espacio_id
           AND c.estado = 'ACTIVO';

        IF v_activos = 0 THEN
            SELECT COUNT(*)
              INTO v_ceremonias_activas
              FROM ceremonias ce
             WHERE ce.espacio_id = OLD.espacio_id
               AND ce.estado IN ('PROGRAMADA', 'CONFIRMADA');

            UPDATE espacios
               SET estado = CASE WHEN v_ceremonias_activas > 0 THEN 'reservado' ELSE 'disponible' END
             WHERE id = OLD.espacio_id
               AND estado <> 'mantenimiento';
        END IF;
    END IF;

    RETURN COALESCE(NEW, OLD);
END;
$$;

CREATE OR REPLACE FUNCTION public.fn_prevent_delete_espacio_with_active_contract()
RETURNS TRIGGER
LANGUAGE plpgsql
AS $$
DECLARE
    v_count BIGINT;
BEGIN
    SELECT COUNT(*)
      INTO v_count
      FROM contratos
     WHERE espacio_id = OLD.id
       AND estado = 'ACTIVO';

    IF v_count > 0 THEN
        RAISE EXCEPTION 'No se puede eliminar espacio %: tiene contratos activos', OLD.id;
    END IF;

    RETURN OLD;
END;
$$;

CREATE OR REPLACE FUNCTION public.fn_validate_ceremonia()
RETURNS TRIGGER
LANGUAGE plpgsql
AS $$
DECLARE
    v_contrato_estado public.estado_contrato_enum;
    v_contrato_cliente_id BIGINT;
    v_contrato_espacio_id BIGINT;
    v_estado_espacio public.estado_espacio_enum;
BEGIN
    NEW.ubicacion := TRIM(NEW.ubicacion);

    IF NEW.ubicacion IS NULL OR NEW.ubicacion = '' THEN
        RAISE EXCEPTION 'ubicacion no puede ser vacia';
    END IF;

    IF NEW.contrato_id IS NOT NULL THEN
        SELECT c.estado, c.cliente_id, c.espacio_id
          INTO v_contrato_estado, v_contrato_cliente_id, v_contrato_espacio_id
          FROM contratos c
         WHERE c.id = NEW.contrato_id;

        IF v_contrato_estado IS NULL THEN
            RAISE EXCEPTION 'Contrato % no existe', NEW.contrato_id;
        END IF;

        IF NEW.cliente_id IS NULL THEN
            NEW.cliente_id := v_contrato_cliente_id;
        ELSIF v_contrato_cliente_id IS NOT NULL AND NEW.cliente_id <> v_contrato_cliente_id THEN
            RAISE EXCEPTION 'La ceremonia no coincide con el cliente del contrato asociado';
        END IF;

        IF NEW.espacio_id IS NULL THEN
            NEW.espacio_id := v_contrato_espacio_id;
        ELSIF v_contrato_espacio_id IS NOT NULL AND NEW.espacio_id <> v_contrato_espacio_id THEN
            RAISE EXCEPTION 'La ceremonia no coincide con el espacio del contrato asociado';
        END IF;

        IF v_contrato_estado <> 'ACTIVO' THEN
            RAISE EXCEPTION 'Contrato % no esta ACTIVO y no permite nuevas ceremonias', NEW.contrato_id;
        END IF;
    END IF;

    IF NEW.espacio_id IS NOT NULL THEN
        SELECT e.estado
          INTO v_estado_espacio
          FROM espacios e
         WHERE e.id = NEW.espacio_id;

        IF v_estado_espacio IS NULL THEN
            RAISE EXCEPTION 'Espacio % no existe', NEW.espacio_id;
        END IF;

        IF v_estado_espacio IN ('mantenimiento', 'bloqueado') THEN
            RAISE EXCEPTION 'El espacio % no esta disponible para ceremonias', NEW.espacio_id;
        END IF;
    END IF;

    IF NEW.estado IN ('PROGRAMADA', 'CONFIRMADA') AND NEW.fecha_hora < NOW() THEN
        RAISE EXCEPTION 'No se puede programar/confirmar ceremonia en fecha pasada';
    END IF;

    RETURN NEW;
END;
$$;

CREATE OR REPLACE FUNCTION public.fn_sync_espacio_estado_por_ceremonia()
RETURNS TRIGGER
LANGUAGE plpgsql
AS $$
DECLARE
    v_ceremonias_activas BIGINT;
    v_contratos_activos BIGINT;
    v_espacio_id BIGINT;
    v_exclude_id BIGINT;
BEGIN
    IF TG_OP = 'DELETE' THEN
        v_espacio_id := OLD.espacio_id;
        v_exclude_id := OLD.id;
    ELSE
        v_espacio_id := NEW.espacio_id;
        v_exclude_id := NEW.id;
    END IF;

    IF v_espacio_id IS NULL THEN
        RETURN COALESCE(NEW, OLD);
    END IF;

    IF TG_OP <> 'DELETE' AND NEW.estado IN ('PROGRAMADA', 'CONFIRMADA') THEN
        UPDATE espacios
           SET estado = CASE WHEN estado = 'mantenimiento' THEN estado ELSE 'reservado' END
         WHERE id = v_espacio_id;
    ELSE
        SELECT COUNT(*)
          INTO v_contratos_activos
          FROM contratos c
         WHERE c.espacio_id = v_espacio_id
           AND c.estado = 'ACTIVO';

        SELECT COUNT(*)
          INTO v_ceremonias_activas
          FROM ceremonias ce
         WHERE ce.espacio_id = v_espacio_id
           AND ce.estado IN ('PROGRAMADA', 'CONFIRMADA')
           AND ce.id <> v_exclude_id;

        UPDATE espacios
           SET estado = CASE
                            WHEN estado = 'mantenimiento' THEN estado
                            WHEN v_contratos_activos > 0 THEN 'ocupado'
                            WHEN v_ceremonias_activas > 0 THEN 'reservado'
                            ELSE 'disponible'
                        END
         WHERE id = v_espacio_id;
    END IF;

    RETURN COALESCE(NEW, OLD);
END;
$$;

CREATE OR REPLACE FUNCTION public.fn_registrar_historial()
RETURNS TRIGGER
LANGUAGE plpgsql
AS $$
DECLARE
    v_registro_id TEXT;
    v_detalle JSONB;
BEGIN
    IF TG_OP = 'DELETE' THEN
        v_registro_id := OLD.id::text;
        v_detalle := to_jsonb(OLD);
    ELSE
        v_registro_id := NEW.id::text;
        v_detalle := to_jsonb(NEW);
    END IF;

    INSERT INTO historial_eventos (entidad, registro_id, accion, detalle, usuario_id)
    VALUES (TG_TABLE_NAME, v_registro_id, TG_OP, v_detalle, NULL);

    RETURN COALESCE(NEW, OLD);
END;
$$;
