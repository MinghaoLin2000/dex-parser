package com.YenKoc.www.model.dex;

import com.YenKoc.www.util.Reader;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class DexFieldIds {
    class FieldId{
        char classIdx; //类名type_ids的index
        char typeIdx; //类型的type_ids的index
        int nameIdx; //字段名称string_ids的index
        public FieldId(char classIdx,char typeIdx,int nameIdx)
        {
            this.classIdx=classIdx;
            this.typeIdx=typeIdx;
            this.nameIdx=nameIdx;
        }
    }
    FieldId fieldIds[];
    public DexFieldIds(byte[] dexBuff, int off, int size)
    {
        fieldIds=new FieldId[size];
        Reader reader=new Reader(dexBuff,off);
        for(int i=0;i<size;i++)
        {
            char classIdx=reader.readUshort();
            char typeIdx=reader.readUshort();
            int nameIdx=reader.readUint();
            FieldId fieldId=new FieldId(classIdx,typeIdx,nameIdx);
            fieldIds[i]=fieldId;
        }
    }
    //对应的字符串都直接打印出来，并存到json中
    public JSONArray toJson(DexFileParse dexFileParse)
    {
        JSONArray jsonIds = new JSONArray();

        for(int i = 0; i< fieldIds.length; i++){
            FieldId fieldId = fieldIds[i];
            JSONObject jsonItem = new JSONObject();
            jsonItem.put("class_idx", dexFileParse.typeIds.getString(dexFileParse, (int)fieldId.classIdx));
            jsonItem.put("type_idx", dexFileParse.typeIds.getString(dexFileParse, (int)fieldId.typeIdx));
            jsonItem.put("name_idx", dexFileParse.stringIds.getData(fieldId.nameIdx));
            jsonIds.add(i, jsonItem);
        }
        return jsonIds;
    }
}
