package com.YenKoc.www.model;

import java.util.Calendar;

public class ChangePoint implements Cloneable {
    public static final int UINT=0x01;
    public static final int USHORT=0x02;
    public static final int ULEB128=0x03;

    public int type; //数据类型
    public int offset;//偏移地址
    public int value; //原始值

    public ChangePoint(int type,int offset,int val)
    {
        this.type=type;
        this.offset=offset;
        this.value=val;
    }

    public ChangePoint clone()
    {
        ChangePoint cp=null;
        try{
            cp=(ChangePoint)super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return cp;
    }

}
