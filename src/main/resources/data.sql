-- =====================================================
-- PÓLIZAS
-- =====================================================

INSERT INTO polizas (
    tipo,
    estado,
    fecha_inicio,
    fecha_fin,
    meses_vigencia_inicial,
    canon_mensual,
    prima,
    fecha_cancelacion,
    version
)
VALUES (
    'INDIVIDUAL',
    'ACTIVA',
    DATE '2026-01-01',
    DATE '2026-12-31',
    12,
    1500000.00,
    18000000.00,
    NULL,
    0
);

INSERT INTO polizas (
    tipo,
    estado,
    fecha_inicio,
    fecha_fin,
    meses_vigencia_inicial,
    canon_mensual,
    prima,
    fecha_cancelacion,
    version
)
VALUES (
    'COLECTIVA',
    'ACTIVA',
    DATE '2026-02-01',
    DATE '2027-01-31',
    12,
    2500000.00,
    30000000.00,
    NULL,
    0
);

INSERT INTO polizas (
    tipo,
    estado,
    fecha_inicio,
    fecha_fin,
    meses_vigencia_inicial,
    canon_mensual,
    prima,
    fecha_cancelacion,
    version
)
VALUES (
    'INDIVIDUAL',
    'CANCELADA',
    DATE '2026-03-01',
    DATE '2027-02-28',
    12,
    1200000.00,
    14400000.00,
    TIMESTAMP '2026-06-15 10:30:00',
    0
);

-- =====================================================
-- RIESGOS DE LA PÓLIZA INDIVIDUAL ACTIVA
-- =====================================================

INSERT INTO riesgos (
    poliza_id,
    descripcion,
    direccion_inmueble,
    estado,
    fecha_cancelacion,
    version
)
SELECT
    id,
    'Apartamento de uso residencial',
    'Calle 100 # 15-20, Bogotá',
    'ACTIVO',
    NULL,
    0
FROM polizas
WHERE tipo = 'INDIVIDUAL'
  AND fecha_inicio = DATE '2026-01-01';

-- =====================================================
-- RIESGOS DE LA PÓLIZA COLECTIVA
-- =====================================================

INSERT INTO riesgos (
    poliza_id,
    descripcion,
    direccion_inmueble,
    estado,
    fecha_cancelacion,
    version
)
SELECT
    id,
    'Local comercial 101',
    'Carrera 7 # 72-41, Bogotá',
    'ACTIVO',
    NULL,
    0
FROM polizas
WHERE tipo = 'COLECTIVA'
  AND fecha_inicio = DATE '2026-02-01';

INSERT INTO riesgos (
    poliza_id,
    descripcion,
    direccion_inmueble,
    estado,
    fecha_cancelacion,
    version
)
SELECT
    id,
    'Local comercial 102',
    'Carrera 7 # 72-45, Bogotá',
    'ACTIVO',
    NULL,
    0
FROM polizas
WHERE tipo = 'COLECTIVA'
  AND fecha_inicio = DATE '2026-02-01';

-- =====================================================
-- RIESGO DE LA PÓLIZA CANCELADA
-- =====================================================

INSERT INTO riesgos (
    poliza_id,
    descripcion,
    direccion_inmueble,
    estado,
    fecha_cancelacion,
    version
)
SELECT
    id,
    'Casa de uso residencial',
    'Calle 25 # 30-15, Medellín',
    'CANCELADO',
    TIMESTAMP '2026-06-15 10:30:00',
    0
FROM polizas
WHERE tipo = 'INDIVIDUAL'
  AND fecha_inicio = DATE '2026-03-01';