-- Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/
CREATE DATABASE IF NOT EXISTS zhuatech_imagesearch DEFAULT CHARACTER SET utf8mb4;
USE zhuatech_imagesearch;
CREATE TABLE image_asset (id BIGINT PRIMARY KEY AUTO_INCREMENT, asset_code VARCHAR(40) UNIQUE NOT NULL, title VARCHAR(160) NOT NULL, source_name VARCHAR(120), category VARCHAR(60), object_key VARCHAR(500) NOT NULL, authorized BOOLEAN NOT NULL DEFAULT FALSE, created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP);
CREATE TABLE image_embedding (id BIGINT PRIMARY KEY AUTO_INCREMENT, asset_id BIGINT NOT NULL, model_code VARCHAR(100) NOT NULL, vector_ref VARCHAR(500) NOT NULL, tags JSON, created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, INDEX idx_embedding_asset(asset_id));
CREATE TABLE search_audit (id BIGINT PRIMARY KEY AUTO_INCREMENT, query_text VARCHAR(500), result_count INT NOT NULL, only_authorized BOOLEAN NOT NULL, created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP);
