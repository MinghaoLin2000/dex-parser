package com.YenKoc.www.model.dex;

import com.YenKoc.www.util.Reader;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class DexTypeIds {
    class TypeId{
        int descriptorIdx; //对应stringIds的下标
        public TypeId(int descriptorIdx)
        {
            this.descriptorIdx=descriptorIdx;
        }
    }
    TypeId typeIds[];
    public DexTypeIds(byte[] dexBuff,int off,int size)
    {
        this.typeIds=new TypeId[size];
        Reader reader=new Reader(dexBuff,off);
        for(int i=0;i<size;i++)
        {
            int idx=reader.readUint();
            TypeId typeId=new TypeId(idx);
            typeIds[i]=typeId;
        }
    }
    //获取type对应的类型
    public String getString(DexFileParse dexFileParse,int id)
    {
        String data=dexFileParse.stringIds.getData(id);
        return data;
    }
    public JSONArray toJson() {
        JSONArray jsonIds = new JSONArray();

        for (int i = 0; i < typeIds.length; i++) {
            TypeId typeId = typeIds[i];
            JSONObject jsonItem = new JSONObject();
            jsonItem.put("id", i);
            jsonItem.put("descriptor_idx", typeId.descriptorIdx);
            //jsonItem.put("data", new String(typeId.data));

            jsonIds.add(i, jsonItem);
        }
        return jsonIds;
    }


}
