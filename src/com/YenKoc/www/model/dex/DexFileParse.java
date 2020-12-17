package com.YenKoc.www.model.dex;
import com.YenKoc.www.model.dex.classdefs.DexClassDefs;
import com.alibaba.fastjson.JSONObject;
import static com.YenKoc.www.util.BufferUtil.subdex;


public class DexFileParse {
    //dex文件头的大小一般都为0x70
    public static final int HEADER_LEN=0x70;
    //头部
    public DexHeader header;
    //字符串索引区
    public DexStringIds stringIds;
    //类型索引区
    public DexTypeIds typeIds;
    //函数原型索引区
    public DexProtoIds protoIds;
    //字段索引区
    public DexFieldIds fieldIds;
    //方法索引区
    public DexMethodIds methodIds;
    //类信息
    public DexClassDefs classDefs;
    //dex文件的末端
    public DexMapList mapList;

    //读取dex文件信息
    public void readInfomation(byte[] dexBuff)
    {
        //read header
        byte[] headerbs = subdex(dexBuff, 0, HEADER_LEN);
        header = new DexHeader(headerbs);

        //read string_ids
        stringIds = new DexStringIds(dexBuff, header.stringIdsOff, header.stringIdsSize);

        //read type_ids
        typeIds = new DexTypeIds(dexBuff, header.typeIdsOff, header.typeIdsSize);

        //read proto_ids
        protoIds = new DexProtoIds(dexBuff, header.protoIdsOff, header.protoIdsSize);

        //read field_ids
        fieldIds = new DexFieldIds(dexBuff, header.fieldIdsOff, header.fieldIdsSize);

        //read method_ids
        methodIds = new DexMethodIds(dexBuff, header.methodIdsOff, header.methodIdsSize);

        //read class_defs
        classDefs = new DexClassDefs(dexBuff, header.classDefsOff, header.classDefsSize);

        //read map_list
        mapList = new DexMapList(dexBuff, header.mapOff);
    }

    public void write(){

    }

    public String toJsonStr(){
        JSONObject jsonDex = new JSONObject();
        jsonDex.put("header", header.toJson());
        jsonDex.put("string_ids", stringIds.toJson());
        //jsonDex.put("type_ids", typeIds.toJson());
        //jsonDex.put("proto_ids", protoIds.toJson());
        //jsonDex.put("field_ids", fieldIds.toJson(this));
        //jsonDex.put("method_ids", methodIds.toJson());
        jsonDex.put("class_def", classDefs.toJson(this));
        //jsonDex.put("map_list", mapList.toJson());
        return jsonDex.toJSONString();
    }
}