CREATE TABLE IF NOT EXISTS emergency_hospital (
    id BIGSERIAL PRIMARY KEY,
    hospital_id VARCHAR(20) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(255),
    region VARCHAR(100),
    latitude DOUBLE PRECISION,
    longitude DOUBLE PRECISION,
    phone VARCHAR(30),
    emergency_phone VARCHAR(30),
    geom geometry(Point, 4326),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS emergency_status (
    id BIGSERIAL PRIMARY KEY,
    hospital_id VARCHAR(20) NOT NULL,
    available_beds INT NOT NULL DEFAULT 0,
    emergency_status VARCHAR(10) NOT NULL,
    last_updated TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_emergency_status_hospital
        FOREIGN KEY (hospital_id) REFERENCES emergency_hospital(hospital_id) ON DELETE CASCADE
);

INSERT INTO emergency_hospital (hospital_id, name, address, region, latitude, longitude, phone, emergency_phone, geom)
VALUES
    ('A1100001', '샘플 응급의료센터', '서울특별시 중구 샘플로 1', '서울특별시 중구', 37.5665, 126.9780, '02-0000-0001', '02-0000-9999', ST_SetSRID(ST_MakePoint(126.9780, 37.5665), 4326))
ON CONFLICT (hospital_id) DO NOTHING;

INSERT INTO emergency_status (hospital_id, available_beds, emergency_status)
VALUES
    ('A1100001', 12, 'GREEN')
ON CONFLICT DO NOTHING;
