package com.YenKoc.www.model.dex.classdefs;

import com.YenKoc.www.model.ChangePoint;
import com.YenKoc.www.model.Uleb128;
import com.YenKoc.www.model.dex.DexFileParse;
import com.YenKoc.www.util.Reader;
import com.YenKoc.www.util.Writer;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sun.corba.se.spi.presentation.rmi.PresentationManager;

public class DexClassDefs {
    public class ClassDef {
        public int          classIdx;       //class类型，对应type_ids
        public int          accessFlags;    //访问类型，enum
        public int          superclassIdx;  //supperclass类型，对应type_ids
        public int          interfacesOff;  //接口偏移，对应type_list
        public int          sourceFileIdx;  //源文件名，对应string_ids
        public int          annotationsOff; //class注解，位置位于data区，对应annotation_direcotry_item
        public ChangePoint   classDataOff;   //class具体用到的数据，位于data区，格式为class_data_item,描述class的field,method,method执行代码
        public ChangePoint   staticValueOff; //位于data区，格式为encoded_array_item

        public StaticValues staticValues;  // classDataOff不为0时存在
        public DexClassData    classData;     // staticValueOff不为0存在

        public ClassDef(int classIdx, int accessFlags,
                        int superclassIdx, int interfacesOff,
                        int sourceFileidx, int annotationsOff,
                        ChangePoint classDataOff, ChangePoint staticValueOff) {
            this.classIdx = classIdx;
            this.accessFlags = accessFlags;
            this.superclassIdx = superclassIdx;
            this.interfacesOff = interfacesOff;
            this.sourceFileIdx = sourceFileidx;
            this.annotationsOff = annotationsOff;
            this.classDataOff = classDataOff;
            this.staticValueOff = staticValueOff;
        }

        public void setClassData(DexClassData classData){
            this.classData = classData;
        }

        public void setStaticValue(StaticValues staticValues){
            this.staticValues = staticValues;
        }
    }

    int      offset; //偏移位置
    int      size;   //大小

    public ClassDef classDefs[];

    public DexClassDefs(byte[] dexBuff, int off, int size) {
        this.offset = off;
        this.size = size;

        Reader reader = new Reader(dexBuff, off);
        classDefs = new ClassDef[size];
        for (int i = 0; i < size; i++) {
            int classIdx = reader.readUint();
            int accessFlags = reader.readUint();
            int superclassIdx = reader.readUint();
            int interfacesOff = reader.readUint();
            int sourcFileIdx = reader.readUint();
            int annotationOff = reader.readUint();

            ChangePoint classDataOff = new ChangePoint(ChangePoint.UINT, reader.getOff(), reader.readUint());
            ChangePoint staticValueOff = new ChangePoint(ChangePoint.UINT, reader.getOff(), reader.readUint());

            ClassDef classDef = new ClassDef(
                    classIdx, accessFlags,
                    superclassIdx, interfacesOff,
                    sourcFileIdx, annotationOff,
                    classDataOff, staticValueOff);

            if(staticValueOff.value != 0){
                Reader reader1 = new Reader(dexBuff, staticValueOff.value);
                Uleb128 staticSize = reader1.readUleb128();
                StaticValues staticValues = new StaticValues(staticSize);
                classDef.setStaticValue(staticValues);
            }

            if(classDataOff.value != 0){
                classDef.setClassData(new DexClassData(dexBuff, classDataOff.value));
            }
            classDefs[i] = classDef;
        }
    }

    public void write(byte[] dexBuff){
        Writer writer = new Writer(dexBuff, offset);
        for(int i=0; i<size; i++){
            ClassDef classDef = classDefs[i];
            writer.writeUint(classDef.classIdx);
            writer.writeUint(classDef.accessFlags);
            writer.writeUint(classDef.superclassIdx);
            writer.writeUint(classDef.interfacesOff);
            writer.writeUint(classDef.sourceFileIdx);
            writer.writeUint(classDef.annotationsOff);

            writer.writeUint(classDef.classDataOff.value);
            if(classDef.classDataOff.value != 0){
                classDef.classData.write(dexBuff, classDef.classDataOff.value);
            }

            writer.writeUint(classDef.staticValueOff.value);
            if(classDef.staticValueOff.value != 0){
                //暂时不做处理
            }
        }
    }


    public JSONArray toJson(DexFileParse dexFile){
        JSONArray jsonDefs = new JSONArray();
        for(int i = 0; i< classDefs.length; i++){
            ClassDef classDef = classDefs[i];
            JSONObject jsonItem = new JSONObject();
            jsonItem.put("class_idx", dexFile.typeIds.getString(dexFile, classDef.classIdx));
            jsonItem.put("access_flags", classDef.accessFlags);
            jsonItem.put("superclass_idx", dexFile.typeIds.getString(dexFile, classDef.superclassIdx));
            jsonItem.put("interfaces_off", classDef.interfacesOff);
            jsonItem.put("source_file_idx", dexFile.stringIds.getData(classDef.sourceFileIdx));
            jsonItem.put("annotations_off", classDef.annotationsOff);
            jsonItem.put("class_data_off", classDef.classDataOff.value);
            jsonItem.put("static_values_off", classDef.staticValueOff.value);
            if(classDef.classDataOff.value != 0) {
                jsonItem.put("class_data", classDef.classData.toJson());
            }
            if(classDef.staticValueOff.value != 0) {
                jsonItem.put("static_values", classDef.staticValues.toJson());
            }
            jsonDefs.add(i, jsonItem);
        }
        return jsonDefs;
    }
}