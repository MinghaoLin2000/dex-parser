package com.YenKoc.www.model.dex.classdefs;

import com.YenKoc.www.model.Uleb128;
import com.YenKoc.www.util.Reader;
import com.YenKoc.www.util.Writer;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class EncodedField {
    Uleb128 fieldIdxDiff;//属性编号 对应field_ids;
    Uleb128 accessFlags; //访问权限

    public EncodedField(Reader reader){
        this.fieldIdxDiff=reader.readUleb128();
        this.accessFlags=reader.readUleb128();
    }
    public void write(Writer writer)
    {
        writer.writeUleb128(this.fieldIdxDiff);
        writer.writeUleb128(this.accessFlags);
    }
    public JSONObject toJson()
    {
        JSONObject json=new JSONObject();
        json.put("field_idx_diff",fieldIdxDiff.getIntValue());
        json.put("access_flags",accessFlags.getIntValue());
        return json;
    }
}
