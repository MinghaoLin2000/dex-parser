package com.YenKoc.www.util;
import com.YenKoc.www.model.Uleb128;
import org.omg.CORBA.ORB;

import static com.YenKoc.www.model.Const.UINT_LEN;
import static com.YenKoc.www.model.Const.USHORT_LEN;
public class Reader {
    private byte[] buffer;
    private int offset=0;
    public Reader(byte[] buffer,int off)
    {
        this.buffer=buffer;
        this.offset=off;
    }
    public void setBuffer(byte[] buffer)
    {
        this.buffer=buffer;
    }
    public void setOff(int off)
    {
        this.offset=off;
    }
    public int getOff()
    {
        return this.offset;
    }
    public void reset(byte[] buffer,int off)
    {
        this.buffer=buffer;
        this.offset=off;
    }
    public byte[] subdex(int len)
    {
        byte[] sub=new byte[len];
        for(int i=0;i<len;i++)
        {
            sub[i]=buffer[i+offset];
        }
        offset+=len;
        return sub;
    }
    public int readUint()
    {
        int value=0;
        for(int i=0;i<UINT_LEN;i++)
        {
            int seg=buffer[offset+i];
            if(seg<0)
            {
                seg=256+seg;
            }
            value+=seg<<(8*i);
        }
        offset+=USHORT_LEN;
        return value;
    }
    public char readUshort()
    {
        char value=0;
        for(int i=0;i<USHORT_LEN;i++)
        {
            int seg=buffer[offset+i];
            if(seg<0) {
                seg = 256 + seg;
            }
            value+=seg<<(8*i);
        }
        offset+=USHORT_LEN;
        return value;
    }
    public Uleb128 readUleb128()
    {
        int value=0;
        int length=0;
        byte[] origValue=new byte[4];
        boolean flag;
        do{
            flag=false;
            byte seg=buffer[offset];
            if((seg&0x80)==0x80)
            {
                flag=true;
            }
            seg=(byte) (seg&0x7F);
            value+=seg<<(7*length);
            origValue[length]=buffer[offset];
            length++;
            offset++;
        }while (flag);
        origValue=BufferUtil.subdex(origValue,0,length);
        return new Uleb128(origValue,value,length);



    }
}
