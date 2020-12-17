package com.YenKoc.www.model.dex.classdefs;

import com.YenKoc.www.model.ChangePoint;
import com.YenKoc.www.model.Uleb128;
import com.YenKoc.www.util.Reader;
import com.YenKoc.www.util.Trans;
import com.YenKoc.www.util.Writer;
import com.alibaba.fastjson.JSONObject;

public class EncodedMethod {
    public Uleb128 methodIdxDiff; //函数编号 对应method_ids
    public Uleb128 accessFlags;   //访问类型
    public ChangePoint codeOff;     //代码偏移 Uleb128

    public EncodedMethod(Reader reader){
        this.methodIdxDiff = reader.readUleb128();
        this.accessFlags = reader.readUleb128();
        this.codeOff = new ChangePoint(ChangePoint.ULEB128, reader.getOff(), reader.readUleb128().getIntValue());
    }

    public void write(Writer writer){
        writer.writeUleb128(this.methodIdxDiff);
        writer.writeUleb128(this.accessFlags);
        writer.writeUleb128(Trans.intToUleb128(this.codeOff.value));
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("method_idx_diff", methodIdxDiff.getIntValue());
        json.put("access_flags", accessFlags.getIntValue());
        json.put("code_off", codeOff.value);
        return json;
    }
}
