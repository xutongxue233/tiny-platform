package com.tiny.common.utils;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.service.Config;
import org.lionsoul.ip2region.service.Ip2Region;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * IP地址工具类
 * 基于ip2region实现离线IP地址定位，支持IPv4和IPv6
 */
@Slf4j
public class IpUtil {

    private static Ip2Region ip2Region;

    static {
        try {
            // 加载IPv4数据库
            ClassPathResource v4Resource = new ClassPathResource("ip2region_v4.xdb");
            InputStream v4InputStream = v4Resource.getInputStream();
            Path v4TempFile = Files.createTempFile("ip2region_v4", ".xdb");
            Files.copy(v4InputStream, v4TempFile, StandardCopyOption.REPLACE_EXISTING);
            v4InputStream.close();

            // 加载IPv6数据库
            ClassPathResource v6Resource = new ClassPathResource("ip2region_v6.xdb");
            InputStream v6InputStream = v6Resource.getInputStream();
            Path v6TempFile = Files.createTempFile("ip2region_v6", ".xdb");
            Files.copy(v6InputStream, v6TempFile, StandardCopyOption.REPLACE_EXISTING);
            v6InputStream.close();

            // 创建IPv4配置
            Config v4Config = Config.custom()
                    .setCachePolicy(Config.BufferCache)
                    .setXdbPath(v4TempFile.toString())
                    .asV4();

            // 创建IPv6配置
            Config v6Config = Config.custom()
                    .setCachePolicy(Config.BufferCache)
                    .setXdbPath(v6TempFile.toString())
                    .asV6();

            // 创建双协议查询实例
            ip2Region = Ip2Region.create(v4Config, v6Config);
            log.info("ip2region数据库加载成功(IPv4+IPv6)");
        } catch (Exception e) {
            log.error("ip2region数据库加载失败", e);
        }
    }

    /**
     * 根据IP获取地址
     *
     * @param ip IP地址
     * @return 地址信息
     */
    public static String getRegion(String ip) {
        if (StrUtil.isBlank(ip)) {
            return "未知";
        }
        // 本地IP
        if ("127.0.0.1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip)) {
            return "内网IP";
        }
        // 内网IP段
        if (isInternalIp(ip)) {
            return "内网IP";
        }
        if (ip2Region == null) {
            return "未知";
        }
        try {
            // 返回格式: 国家|区域|省份|城市|ISP
            String region = ip2Region.search(ip);
            return formatRegion(region);
        } catch (Exception e) {
            log.error("IP地址解析失败: {}", ip, e);
            return "未知";
        }
    }

    /**
     * 格式化地区信息
     * 原格式: 国家|区域|省份|城市|ISP
     * 返回: 省份 城市
     */
    private static String formatRegion(String region) {
        if (StrUtil.isBlank(region)) {
            return "未知";
        }
        String[] parts = region.split("\\|");
        if (parts.length < 4) {
            return region;
        }
        String country = parts[0];
        String province = parts[2];
        String city = parts[3];

        // 国外IP直接返回国家
        if (!"中国".equals(country) && !"0".equals(country)) {
            return country;
        }

        StringBuilder sb = new StringBuilder();
        if (!"0".equals(province)) {
            sb.append(province);
        }
        if (!"0".equals(city) && !city.equals(province)) {
            if (sb.length() > 0) {
                sb.append(" ");
            }
            sb.append(city);
        }
        return sb.length() > 0 ? sb.toString() : "未知";
    }

    /**
     * 判断是否为内网IP
     */
    private static boolean isInternalIp(String ip) {
        if (StrUtil.isBlank(ip)) {
            return false;
        }
        try {
            String[] parts = ip.split("\\.");
            if (parts.length != 4) {
                return false;
            }
            int first = Integer.parseInt(parts[0]);
            int second = Integer.parseInt(parts[1]);

            // 10.x.x.x
            if (first == 10) {
                return true;
            }
            // 172.16.x.x - 172.31.x.x
            if (first == 172 && second >= 16 && second <= 31) {
                return true;
            }
            // 192.168.x.x
            if (first == 192 && second == 168) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
