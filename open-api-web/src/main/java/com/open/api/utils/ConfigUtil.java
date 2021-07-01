package com.open.api.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class ConfigUtil {
    public static Map<String, String> parseMap(String value) {
        Map<String, String> res = new HashMap<>();
        value = value.trim();
        if (!StringUtils.isBlank(value)) {
            String[] keys = value.split(";");
            for (String keyV : keys) {
                if (keyV.contains(":")) {
                    String[] pair = keyV.split(":");
                    res.put(pair[0].trim(), pair[1].trim());
                }
            }
        }
        return res;
    }
}
