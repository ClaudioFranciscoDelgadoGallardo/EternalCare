# EternalCare

Base del proyecto preparada para crecer por módulos sobre Spring Boot.

## Backend

El backend vive en `Backend/Backend` y ya queda listo para configurar Supabase por variables de entorno:

- `SUPABASE_URL`
- `SUPABASE_ANON_KEY`
- `SUPABASE_SERVICE_ROLE_KEY`
- `SERVER_PORT`

## Estructura sugerida

Cuando agregues módulos, una separación práctica es:

- `inventory`
- `bookings`
- `contracts`
- `config`
- `common`

## Nota de seguridad

No guardes claves reales dentro del repositorio. Usa variables de entorno o archivos locales ignorados por Git.