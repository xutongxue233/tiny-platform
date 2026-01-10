package com.tiny.core.xss;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HtmlUtil;

/**
 * XSS过滤工具类
 */
public class XssUtil {

    /**
     * 过滤XSS攻击代码
     *
     * @param value 原始值
     * @return 过滤后的值
     */
    public static String clean(String value) {
        if (StrUtil.isBlank(value)) {
            return value;
        }
        // 使用Hutool的HTML转义功能
        return HtmlUtil.cleanHtmlTag(value);
    }

    /**
     * HTML实体编码
     *
     * @param value 原始值
     * @return 编码后的值
     */
    public static String escape(String value) {
        if (StrUtil.isBlank(value)) {
            return value;
        }
        return HtmlUtil.escape(value);
    }

    /**
     * 过滤SQL注入和XSS攻击
     *
     * @param value 原始值
     * @return 过滤后的值
     */
    public static String filter(String value) {
        if (StrUtil.isBlank(value)) {
            return value;
        }
        // 移除脚本标签
        value = value.replaceAll("(?i)<script[^>]*>.*?</script>", "");
        // 移除事件处理器
        value = value.replaceAll("(?i)\\s+on\\w+\\s*=\\s*(['\"])[^'\"]*\\1", "");
        value = value.replaceAll("(?i)\\s+on\\w+\\s*=\\s*[^\\s>]+", "");
        // 移除javascript:协议
        value = value.replaceAll("(?i)javascript\\s*:", "");
        // 移除vbscript:协议
        value = value.replaceAll("(?i)vbscript\\s*:", "");
        // 移除expression
        value = value.replaceAll("(?i)expression\\s*\\(", "");
        // HTML实体编码
        return HtmlUtil.escape(value);
    }
}
