package com.open.api.web.bo;


import lombok.Data;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

@Data
public class Test2BO implements Serializable {
    private static final long serialVersionUID = -1L;

    @NotBlank(message = "username 不能为空！")
    private String username;
    @NotBlank(message = "password 不能为空！")
    private String password;
    
    @Override
    public String toString() {
        return ReflectionToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}