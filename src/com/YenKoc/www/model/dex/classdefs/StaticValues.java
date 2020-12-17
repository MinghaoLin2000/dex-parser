package com.YenKoc.www.model.dex.classdefs;

import com.YenKoc.www.model.Uleb128;
import com.alibaba.fastjson.JSONObject;

public class StaticValues {
    class StaticValue {

    }

    Uleb128 size; //静态变量个数
    StaticValue staticValue[];//静态变量信息
    public StaticValues(Uleb128 size)
    {
        this.size=size;
        this.staticValue=new StaticValue[this.size.getIntValue()];
    }
    public JSONObject toJson()
    {
        JSONObject jsonStaticValues = new JSONObject();
        jsonStaticValues.put("size", size.getIntValue());
        return jsonStaticValues;
    }
}
