package com.geedgenetworks.model;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
public class AbstractResponse {
    public AbstractResponse() {
    }

    public String toJson() {
        return JSON.toJSONString(this, JSONWriter.Feature.WriteNullStringAsEmpty);
    }

    public String toString() {
        String var10000 = this.getClass().getName();
        return var10000 + " [ " + this.toJson() + " ]";
    }

}
