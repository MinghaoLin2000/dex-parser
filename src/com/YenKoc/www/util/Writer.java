package com.YenKoc.www.util;

import com.YenKoc.www.model.Uleb128;

import static com.YenKoc.www.model.Const.UINT_LEN;
import static com.YenKoc.www.model.Const.USHORT_LEN;

public class Writer {
    private byte[] buffer;
    private int offset=0;
    public Writer(byte[] buffer,int off)
    {
        this.buffer=buffer;
        this.offset=off;
    }
    public void setStream(byte[] buffer)
    {
        this.buffer=buffer;
    }
    public void setOffset(int offset)
    {
        this.offset=offset;
    }
    public void replace(byte[] replacement,int len)
    {
        for(int i=0;i<len;i++)
        {
            this.buffer[i+offset]=replacement[i];

        }
        this.offset+=len;
    }
    public void writeUint(int val)
    {
        for(int i=0;i<UINT_LEN;i++)
        {
            buffer[offset+i]=(byte) ((val>>(8*i))&0xff);
        }
        this.offset+=UINT_LEN;
    }
    public void writeUshort(int val)
    {
        for(int i=0;i<USHORT_LEN;i++)
        {
            buffer[offset+i]=(byte)((val>>(8*i))&0xff);
        }
        this.offset=USHORT_LEN;
    }
    public void writeUleb128(Uleb128 val)
    {
        byte realVal[]=val.getOrigValue();
        int len=val.getLength();
        replace(realVal,len);
    }
}
