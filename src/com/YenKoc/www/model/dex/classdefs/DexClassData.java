package com.YenKoc.www.model.dex.classdefs;

import com.YenKoc.www.model.ChangePoint;
import com.YenKoc.www.util.Reader;
import com.YenKoc.www.util.Trans;
import com.YenKoc.www.util.Writer;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class DexClassData {
    public ChangePoint staticFieldsSize; //静态变量的个数
    public ChangePoint instanceFieldsSize; //实例变量的个数
    public ChangePoint directMethodsSize; //直接函数的个数
    public ChangePoint virtualMethodsSize; //虚函数的个数(构造函数不是虚函数)

    public EncodedField staticFields[];  //静态变量
    public EncodedField instanceFields[]; //实例变量
    public EncodedMethod directMethods[]; //直接函数
    public EncodedMethod virtualMethods[]; //虚函数
    public DexClassData(byte[] dexBuff,int off)
    {
        Reader reader=new Reader(dexBuff,off);
        this.staticFieldsSize=new ChangePoint(ChangePoint.ULEB128,reader.getOff(),reader.readUleb128().getIntValue());
        this.instanceFieldsSize=new ChangePoint(ChangePoint.ULEB128,reader.getOff(),reader.readUleb128().getIntValue());
        this.directMethodsSize=new ChangePoint(ChangePoint.ULEB128,reader.getOff(),reader.readUleb128().getIntValue());
        this.virtualMethodsSize=new ChangePoint(ChangePoint.ULEB128,reader.getOff(),reader.readUleb128().getIntValue());
        staticFields=new EncodedField[this.staticFieldsSize.value];
        for(int i=0;i<this.staticFieldsSize.value;i++)
        {
            EncodedField field=new EncodedField(reader);
            staticFields[i]=field;
        }
        instanceFields = new EncodedField[this.instanceFieldsSize.value];
        for(int i=0; i<this.instanceFieldsSize.value; i++){
            EncodedField field = new EncodedField(reader);
            instanceFields[i] = field;
        }

        directMethods = new EncodedMethod[this.directMethodsSize.value];
        for(int i=0; i<this.directMethodsSize.value; i++){
            EncodedMethod method = new EncodedMethod(reader);
            directMethods[i] = method;
        }
        virtualMethods = new EncodedMethod[this.virtualMethodsSize.value];
        for(int i=0; i<this.virtualMethodsSize.value; i++){
            EncodedMethod field = new EncodedMethod(reader);
            virtualMethods[i] = field;
        }

    }
    public void write(byte[] dexBuff, int off) {
        Writer writer = new Writer(dexBuff, off);
        writer.writeUleb128(Trans.intToUleb128(this.staticFieldsSize.value));
        writer.writeUleb128(Trans.intToUleb128(this.instanceFieldsSize.value));
        writer.writeUleb128(Trans.intToUleb128(this.directMethodsSize.value));
        writer.writeUleb128(Trans.intToUleb128(this.virtualMethodsSize.value));

        for (int i = 0; i < this.staticFieldsSize.value; i++) {
            this.staticFields[i].write(writer);
        }
        for (int i = 0; i < this.instanceFieldsSize.value; i++) {
            this.instanceFields[i].write(writer);
        }

        for (int i = 0; i < this.directMethodsSize.value; i++) {
            this.directMethods[i].write(writer);
        }
        for (int i = 0; i < this.virtualMethodsSize.value; i++) {
            this.virtualMethods[i].write(writer);
        }
    }

    public JSONObject toJson(){
        JSONObject json = new JSONObject();
        json.put("static_fields_size", this.staticFieldsSize.value);
        json.put("instance_fields_size", this.instanceFieldsSize.value);
        json.put("direct_methods_size", this.directMethodsSize.value);
        json.put("virtual_methods_size", this.virtualMethodsSize.value);

        //json.put("static_fields", arrayJson(0));
        //json.put("instance_fields", arrayJson(1));
        json.put("direct_methods", arrayJson(2));
        json.put("virtual_methods", arrayJson(3));
        return json;
    }

    private Object arrayJson(int type){
        JSONArray jsonArray = new JSONArray();
        switch (type){
            case 0:
                for(int i=0; i<this.staticFieldsSize.value; i++){
                    JSONObject json = this.staticFields[i].toJson();
                    jsonArray.add(json);
                }
                break;
            case 1:
                for(int i=0; i<this.instanceFieldsSize.value; i++){
                    JSONObject json = this.instanceFields[i].toJson();
                    jsonArray.add(json);
                }
                break;
            case 2:
                for(int i=0; i<this.directMethodsSize.value; i++){
                    JSONObject json = this.directMethods[i].toJson();
                    jsonArray.add(json);
                }
                break;
            case 3:
                for(int i=0; i<this.virtualMethodsSize.value; i++){
                    JSONObject json = this.virtualMethods[i].toJson();
                    jsonArray.add(json);
                }
                break;
        }
        return jsonArray;
    }



}
