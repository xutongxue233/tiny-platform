-- ============================================
-- Tiny Platform - Full Installation Script
-- Version: 1.0.0
-- Description: Install all modules in correct order
-- ============================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ============================================
-- 00-core: Core Module (Required)
-- ============================================
-- Core schema
SOURCE 00-core/schema.sql;
-- Core data
SOURCE 00-core/data.sql;

-- ============================================
-- 01-monitor: Monitor Module (Optional)
-- ============================================
-- Monitor schema
SOURCE 01-monitor/schema.sql;
-- Monitor data
SOURCE 01-monitor/data.sql;

-- ============================================
-- 02-storage: Storage Module (Optional)
-- ============================================
-- Storage schema
SOURCE 02-storage/schema.sql;
-- Storage data
SOURCE 02-storage/data.sql;

-- ============================================
-- 03-generator: Generator Module (Optional)
-- ============================================
-- Generator schema
SOURCE 03-generator/schema.sql;
-- Generator data
SOURCE 03-generator/data.sql;

-- ============================================
-- 04-notice: Notice Module (Optional)
-- ============================================
-- Notice schema
SOURCE 04-notice/schema.sql;
-- Notice data
SOURCE 04-notice/data.sql;

-- ============================================
-- 05-message: Message Module (Optional)
-- ============================================
-- Message schema
SOURCE 05-message/schema.sql;
-- Message data
SOURCE 05-message/data.sql;

SET FOREIGN_KEY_CHECKS = 1;

-- ============================================
-- Installation Complete
-- ============================================
SELECT 'Tiny Platform database installation completed!' AS message;
