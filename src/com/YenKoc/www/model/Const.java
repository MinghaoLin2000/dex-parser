package com.YenKoc.www.model;

public class Const {
    //dex文件的魔数
    public static final int MAGIC_LEN=0x00008;
    //checksum这个字段在文件中的偏移
    public static final int CHECKSUM_OFF=0x0008;
    public static final int CHECKSUM_LEN=0x0004;
    //signature这个字段在文件中的偏移以及大小
    public static final int SIGNATURE_OFF=0x000C;
    public static final int SIGNATURE_LEN=0x14;
    //常见属性的大小
    public static final int UINT_LEN=0x0004;
    public static final int USHORT_LEN=0x002;
    //MAP_OFF在文件中的偏移
    public static final int MAP_OFF_OFF=MAGIC_LEN+CHECKSUM_LEN+SIGNATURE_LEN+UINT_LEN*5;
    //文件大小字段在文件中的偏移
    public static final int FILE_SIZE_OFF=MAGIC_LEN+CHECKSUM_LEN+SIGNATURE_LEN;

    public static final int MAP_ITEM_LEN=0x000C;
}
