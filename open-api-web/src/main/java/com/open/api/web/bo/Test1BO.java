package com.open.api.web.bo;

import lombok.Data;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Data
public class Test1BO implements Serializable {
    private static final long serialVersionUID = -1L;

    @Valid
    @NotEmpty(message = "集合不为空！")
    @Size(min = 1, message = "最小为{min}")
    private List<Item> itemList;

    @Data
    public static class Item {
        @NotBlank(message = "username 不能为空！")
        private String username;

        @NotBlank(message = "password 不能为空！")
        private String password;

        @NotBlank(message = "realName 不能为空！")
        private String realName;

    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}