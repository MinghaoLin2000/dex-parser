package com.YenKoc.www.model;

public class Uleb128 {
    byte[] origVlaue;
    int intValue;
    int length;
    public Uleb128(byte[] origVlaue,int intValue,int length)
    {
        this.origVlaue=origVlaue;
        this.intValue=intValue;
        this.length=length;
    }
    public int getLength()
    {
        return this.length;
    }
    public int getIntValue()
    {
        return this.intValue;
    }
    public byte[] getOrigValue()
    {
        return this.origVlaue;
    }
}
