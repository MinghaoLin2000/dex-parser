package com.YenKoc.www.model.dex;

import com.YenKoc.www.util.Reader;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class DexMethodIds {
    class MethodId{
        char classIdx; //所属类type_ids的index
        char protoIdx; //类型proto_ids的index
        int nameIdx; //名称string_ids的index
        public MethodId(char classIdx,char protoIdx,int nameIdx)
        {
            this.classIdx=classIdx;
            this.protoIdx=protoIdx;
            this.nameIdx=nameIdx;
        }
    }
    MethodId methodIds[];
    public DexMethodIds(byte[] dexBuff,int off,int size)
    {
        methodIds=new MethodId[size];
        Reader reader=new Reader(dexBuff,off);
        for(int i=0;i<size;i++)
        {
            char classIdx=reader.readUshort();
            char protoIdx=reader.readUshort();
            int nameIdx=reader.readUint();
            MethodId methodId=new MethodId(classIdx,protoIdx,nameIdx);
            methodIds[i]=methodId;
        }
    }
    public JSONArray toJson(){
        JSONArray jsonIds = new JSONArray();

        for(int i = 0; i< methodIds.length; i++){
            MethodId fieldId = methodIds[i];
            JSONObject jsonItem = new JSONObject();
            jsonItem.put("class_idx", (int)fieldId.classIdx);
            jsonItem.put("proto_idx", (int)fieldId.protoIdx);
            jsonItem.put("name_idx", fieldId.nameIdx);
            jsonIds.add(i, jsonItem);
        }
        return jsonIds;
    }

}
