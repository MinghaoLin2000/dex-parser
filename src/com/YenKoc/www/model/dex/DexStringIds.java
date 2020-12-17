package com.YenKoc.www.model.dex;

import com.YenKoc.www.model.Uleb128;
import com.YenKoc.www.util.Reader;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import static com.YenKoc.www.util.BufferUtil.getUleb128;
import static com.YenKoc.www.util.BufferUtil.getUtf8;

public class DexStringIds {
    class StringId{
        int dataOff;  //字符串的偏移位置
        Uleb128 utf16Size; //字符串长度
        byte data[];
        public StringId(int dataOff,Uleb128 uleb128,byte[] data)
        {
            this.dataOff=dataOff;
            this.utf16Size=uleb128;
            this.data=data;
        }
    }
    StringId stringIds[];
    public DexStringIds(byte[] dexBuff,int off,int size)
    {
        this.stringIds=new StringId[size];
        Reader reader=new Reader(dexBuff,off);

        for (int i = 0; i < size; i++) {
            int dataOff = reader.readUint();
            Uleb128 utf16Size = getUleb128(dexBuff, dataOff);
            byte[] data = getUtf8(dexBuff, dataOff + utf16Size.getLength(), utf16Size.getIntValue()); //读取utf-8 类型数据
            StringId stringId = new StringId(dataOff, utf16Size, data);
            stringIds[i] = stringId;
        }
    }
    public String getData(int id)
    {
        return new String(stringIds[id].data);
    }
    public JSONArray toJson() {
        JSONArray jsonIds = new JSONArray();
        for (int i = 0; i < stringIds.length; i++) {
            StringId stringId = stringIds[i];
            JSONObject jsonData = new JSONObject();
            jsonData.put("utf16_size", stringId.utf16Size.getIntValue());
            jsonData.put("data", new String(stringId.data));

            JSONObject jsonItem = new JSONObject();
            jsonItem.put("id", i);
            jsonItem.put("data_off", stringId.dataOff);
            jsonItem.put("data", jsonData);

            jsonIds.add(i, jsonItem);
        }
        return jsonIds;
    }
}
