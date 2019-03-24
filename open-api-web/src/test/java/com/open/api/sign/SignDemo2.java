package com.open.api.sign;///**
// * fshows.com
// * Copyright (C) 2013-2019 All Rights Reserved.
// */
//package com.open.api.test;
//
//import com.alibaba.fastjson.JSON;
//import com.alipay.api.AlipayApiException;
//import com.alipay.api.internal.util.AlipaySignature;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * 获取token接口中sign签名验签Demo
// * 算法说明：
// * 使用SHA256签名算法和RSA2048非对称算法加密
// *
// * @author Administrator
// */
//public class SignDemo2 {
//
//    /**
//     * 公钥
//     */
//    public static String publicKey_App2048 = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnLbS4NKB4K6lSwAruObksqJpcrs7t8w51Y38I9ix9r7WCBMiAjRxWu8DNnr2t5ya/XXoioHqyMH3oEkiqsUh3FX1pG/4N/9/hRtFDJqpkCo4Z/D76wEehY+y+k8sGEO+dO8m8bNbmfpcIGDwuPl3qVBFeyJQwPguuWd32/xtzLcYk2C/Eec25++ElHobGvLkRTHHlXeZ+v8IYCIPd058bQssnFMw7nn+Lgr2kQK/2gPQ8gXs8mi1712i/dgoo8bzw9mhzkFnWXExRqzgoXH5DfIMWnlRR4l1u2qXXPU+hIKFua2776qHREmeqPkKiHA2zoMc9LVydVDTGovJjSn00wIDAQAB";
//
//    /**
//     * 私钥
//     */
//    public static String privateKey_App2048 = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCcttLg0oHgrqVLACu45uSyomlyuzu3zDnVjfwj2LH2vtYIEyICNHFa7wM2eva3nJr9deiKgerIwfegSSKqxSHcVfWkb/g3/3+FG0UMmqmQKjhn8PvrAR6Fj7L6TywYQ7507ybxs1uZ+lwgYPC4+XepUEV7IlDA+C65Z3fb/G3MtxiTYL8R5zbn74SUehsa8uRFMceVd5n6/whgIg93TnxtCyycUzDuef4uCvaRAr/aA9DyBezyaLXvXaL92CijxvPD2aHOQWdZcTFGrOChcfkN8gxaeVFHiXW7apdc9T6EgoW5rbvvqodESZ6o+QqIcDbOgxz0tXJ1UNMai8mNKfTTAgMBAAECggEAaWS+/7oy5JMv0Pflb63Awq5dcm6fI+XyQ0ABAW6biREgyj9r0MXKMlip9XrO7/8fcTRZ1sS2zJs+WQq2iNiZBCC/Wf5/ldF7xl3nmylsverXEnhMQ+j1yOcyfArA4fS1YtjvfP+drDlmudPYMN59bl3wzHXwj0aZzdDbGBR1F4Y+CzgRObq5092acJz/foUO5SAg/+KLhmQQ28AvBhXPTgV467ER34pDHexCgAnsLsH4KVCPxsbW0eh7vaIgUjWQ/sTAfRzFW5CLJRYO1xH6mDS42dImiUJBiWiz2fK4FRrSM05QXCHkKfXb1DpMnvrNyHyCDZfshRp7UsZoxtVeaQKBgQDvWrX7JNb3Km5JncdKRq+I8lqx+d750Ffj0Ln2wTR0lahonJyF+YL4yuP5R8+sCeFRjcPWMUkZ2oSxxT6gaHRl0Z2fJXqwuCaO9NKq9C03olPmiFXM/H4knXsr1urfcRmE5sg3k0kcmtDnxoyDBBb3w8SRb8p0jqRl/NspDq9IJwKBgQCnnNj/JgCZJkWnK7JPTqnjTlavkV1Zicz89dvQ9Eq7Ff0e/BJiEi+DrndSP2v2YOZEJr0LOpIxJwaNCgea67t055/IlsBqHBu0cBtGQ7i2ykd/3Q1v+q0dEKf3C92lvwcsLRQo01gjnDMRiiSkIf0cYdxs66wgzAxh4xd9BvwNdQKBgAEVrydxNkwudAt/XIzYnykGuCSAVmNZb1yH8J/Oplc06mt28jqlM9O+z6OskKNd9BhzhQSuen9Ufy9zDmKZtpVTitxSxiiQ3RPeximiK6ZJ5QlxarogFs5BrHI5ah0THSN+DEA8OaOYjAPQ4Ygid5wt1fE2yXsXvmT77V5VQ7QXAoGALDRNdl0LY1iYnhIEIK5aV7xdWEg6GlchXMVqh50l6FlQPE+2eW7aYRwuE97uFjhQAkFFMiTsUVI9hAzVHKJ2+cnsdfZsII/xLpEyYEGUAYEvgiVGWfX+md++rITQm9nZhmkNHAdBA4M6ZLHOIAtmuYmFnKQ67RtjWJ2PoEWrS9ECgYBW6QtwiRxoz/Xes9jHRmShVcDfwq210Qubmd81uUtDy/lVOY99qCb4rNsTu3RVrtMubD9syvivDroE6NtFrDBNHAtRaGB77Ivs+WdMdhDzNwGxVfgt6h/qJoY05+NmVPe3ExtC2fs8EWnZkXPbs2qurFxdvhBjxHIQHQwV54GaZQ==";
//
//    /**
//     * 字符集
//     */
//    private static String CHARSET = "UTF-8";
//
//    public static void main(String[] args) {
//
//        /*
//         * 接口请求参数,注意  所有参数名称全部为小写
//         */
//        Map<String, String> map = new HashMap<String, String>();
//
//        //系统参数
//        map.put("appid", "123456789");
//        map.put("timestamp", "20190801050402");
//
//        /*
//         * 业务参数封装成bizcontent， 格式为json字符串
//         */
//        Map<String, Object> bizmap = new HashMap<String, Object>();
//
//        bizmap.put("deviceid", "66666666");
//
//        String bizContent = JSON.toJSONString(bizmap);
//
//        System.out.println("业务参数JSON格式字符串:" + bizContent);
//
//        map.put("bizcontent", bizContent);
//
//        try {
//            /*
//             * 拼接待签名字符串  RSA2对应的函数
//             */
//            String content = AlipaySignature.getSignCheckContentV2(map);
//
//
//            System.out.println("拼接待签名字符串:" + content);
//
//
//            /*
//             * 对数据签名  RSA2对应的函数和密钥
//             */
//            String sign = AlipaySignature.rsaSign(content, privateKey_App2048, CHARSET, "RSA2");
//
//            System.out.println("原始sign：" + sign);
//
//            map.put("sign", sign);
//
//            /*
//             * 进入验签流程
//             */
//            boolean result = AlipaySignature.rsaCheckV1(map, publicKey_App2048, "UTF-8", "RSA2");
//
//            System.out.println("验签结果：" + result);
//
//        } catch (AlipayApiException e) {
//            e.printStackTrace();
//        }
//    }
//
//}
