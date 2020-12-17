package com.YenKoc.www.model.dex;

import com.YenKoc.www.util.Reader;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import static com.YenKoc.www.util.Trans.charToInt;

public class DexProtoIds {
    class ProtoId{
        class Parameters{
            int size; //参数个数
            char typeIdxs[]; //参数列表
            public Parameters(int size,char typeIdxs[])
            {
                this.size=size;
                this.typeIdxs=typeIdxs;
            }
            public JSONObject toJson()
            {
                JSONObject jsonParam=new JSONObject();
                jsonParam.put("size",this.size);
                jsonParam.put("list",charToInt(this.typeIdxs));
                return jsonParam;
            }
        }
        int shortyIdx; //StringIds编号
        int returnTypeIdx; //typeIds编号
        int parametersOff; //参数偏移
        Parameters parameters;
        public ProtoId(int shortyIdx,int returnTypeIdx,int parametersOff,int paramSize,char[] paramTyps)
        {
                this.shortyIdx=shortyIdx;
                this.returnTypeIdx=returnTypeIdx;
                this.parametersOff=parametersOff;
                this.parameters=new Parameters(paramSize,paramTyps);
        }
        public ProtoId(int shortyIdx,int returnTypeIdx,int parametersOff)
        {
            this.shortyIdx=shortyIdx;
            this.returnTypeIdx=returnTypeIdx;
            this.parametersOff=parametersOff;
        }
    }
    ProtoId protoIds[];
    public DexProtoIds(byte[] dexBuff,int off,int size)
    {
        this.protoIds=new ProtoId[size];
        Reader reader=new Reader(dexBuff,off);
        for(int i=0;i<size;i++)
        {
            int shortIdx=reader.readUint();
            int returnTypeIdx=reader.readUint();
            int parametersOff=reader.readUint();
            ProtoId protoId;
            if(parametersOff!=0)
            {
                Reader reader1=new Reader(dexBuff,parametersOff);
                int pararmSize=reader1.readUint();
                char parmTypes[] =new char[pararmSize];
                for(int j=0;j<pararmSize;j++)
                {
                    parmTypes[j]=reader1.readUshort();
                }
                protoId=new ProtoId(shortIdx,returnTypeIdx,parametersOff,pararmSize,parmTypes);
            }else
            {
                protoId=new ProtoId(shortIdx,returnTypeIdx,parametersOff);
            }
            protoIds[i]=protoId;
        }
    }
    public JSONArray toJson()
    {
        JSONArray jsonIds = new JSONArray();
        for (int i = 0; i < protoIds.length; i++) {
            ProtoId protoId = protoIds[i];
            JSONObject jsonItem = new JSONObject();
            jsonItem.put("id", i);
            jsonItem.put("shorty_idx", protoId.shortyIdx);
            jsonItem.put("return_type_idx", protoId.returnTypeIdx);
            jsonItem.put("parameters_off", protoId.parametersOff);
            if(protoId.parametersOff != 0) {
                jsonItem.put("parameters", protoId.parameters.toJson());
            }
            jsonIds.add(i, jsonItem);
        }
        return jsonIds;
    }
}
