package com.tiny.generator.core;

import java.util.Set;

/**
 * 代码生成器常量
 */
public class GenConstants {

    /**
     * 基础实体字段列表
     */
    public static final Set<String> BASE_ENTITY_FIELDS = Set.of(
            "create_by", "create_time", "update_by", "update_time", "del_flag", "remark"
    );

    /**
     * 需要排除的字段
     */
    public static final Set<String> COLUMN_NAME_NOT_INSERT = Set.of(
            "create_by", "create_time", "update_by", "update_time", "del_flag"
    );

    /**
     * 需要排除的编辑字段
     */
    public static final Set<String> COLUMN_NAME_NOT_EDIT = Set.of(
            "create_by", "create_time", "update_by", "update_time", "del_flag"
    );

    /**
     * 需要排除的列表字段
     */
    public static final Set<String> COLUMN_NAME_NOT_LIST = Set.of(
            "del_flag", "remark"
    );

    /**
     * 需要排除的查询字段
     */
    public static final Set<String> COLUMN_NAME_NOT_QUERY = Set.of(
            "create_by", "update_by", "del_flag", "remark"
    );

    /**
     * 文本类型
     */
    public static final String HTML_INPUT = "input";
    public static final String HTML_TEXTAREA = "textarea";
    public static final String HTML_SELECT = "select";
    public static final String HTML_RADIO = "radio";
    public static final String HTML_CHECKBOX = "checkbox";
    public static final String HTML_DATETIME = "datetime";
    public static final String HTML_DATE = "date";
    public static final String HTML_NUMBER = "number";
    public static final String HTML_UPLOAD = "upload";
    public static final String HTML_IMAGE_UPLOAD = "imageUpload";
    public static final String HTML_EDITOR = "editor";
    public static final String HTML_TREE_SELECT = "treeSelect";

    /**
     * 查询类型
     */
    public static final String QUERY_EQ = "EQ";
    public static final String QUERY_NE = "NE";
    public static final String QUERY_GT = "GT";
    public static final String QUERY_GTE = "GTE";
    public static final String QUERY_LT = "LT";
    public static final String QUERY_LTE = "LTE";
    public static final String QUERY_LIKE = "LIKE";
    public static final String QUERY_LIKE_LEFT = "LIKE_LEFT";
    public static final String QUERY_LIKE_RIGHT = "LIKE_RIGHT";
    public static final String QUERY_BETWEEN = "BETWEEN";
    public static final String QUERY_IN = "IN";

    /**
     * 生成类型
     */
    public static final String GEN_TYPE_ZIP = "0";
    public static final String GEN_TYPE_PATH = "1";

    /**
     * 单表
     */
    public static final String FE_TYPE_SINGLE = "1";

    /**
     * 主从表
     */
    public static final String FE_TYPE_MASTER_SUB = "2";

    private GenConstants() {
    }
}
