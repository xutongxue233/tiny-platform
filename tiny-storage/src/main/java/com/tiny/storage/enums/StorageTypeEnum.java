package com.tiny.storage.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 存储类型枚举
 */
@Getter
@AllArgsConstructor
public enum StorageTypeEnum {

    LOCAL("local", "本地存储"),
    ALIYUN_OSS("aliyun_oss", "阿里云OSS"),
    MINIO("minio", "MinIO"),
    AWS_S3("aws_s3", "AWS S3"),
    TENCENT_COS("tencent_cos", "腾讯云COS");

    /**
     * 存储类型代码
     */
    private final String code;

    /**
     * 存储类型描述
     */
    private final String desc;

    /**
     * 根据code获取枚举
     */
    public static StorageTypeEnum getByCode(String code) {
        if (code == null) {
            return null;
        }
        for (StorageTypeEnum type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}
