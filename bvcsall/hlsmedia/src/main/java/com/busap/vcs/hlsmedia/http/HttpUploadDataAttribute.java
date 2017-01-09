package com.busap.vcs.hlsmedia.http;

/**
 * 上传结果用的类.
 */
public class HttpUploadDataAttribute {
    public String name;
    public String type;
    public String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
