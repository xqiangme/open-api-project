package com.open.api.enums;

/**
 * @author 码农猿
 * @version SupplierExceptionEnum.java, v 0.1 2019-02-27 10:38
 */
public enum SupplierExceptionEnum implements EnumInterface {

    SUPPLIER_CATEGORY_NOT_EXIST("SUPPLIER_CATEGORY_NOT_EXIST", "供应商分类编码: {0} 不存在"),

    ;


    SupplierExceptionEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private String code;

    private String msg;

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }

}
