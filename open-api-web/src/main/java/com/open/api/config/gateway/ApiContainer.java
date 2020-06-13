package com.open.api.config.gateway;

import com.open.api.model.ApiModel;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * Api 初始化容器
 */
@Service
public class ApiContainer extends HashMap<String, ApiModel> {
}
