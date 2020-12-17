package com.YenKoc.www.util;

import com.YenKoc.www.model.Uleb128;

public class Trans {
    // 二进制转16进制
    public static String binToHex(byte[] data) {
        // Create Hex String
        StringBuffer hexString = new StringBuffer();
        // 字节数组转换为 十六进制 数
        for (int i = 0; i < data.length; i++) {
            String shaHex = Integer.toHexString(data[i] & 0xFF);
            if (shaHex.length() < 2) {
                hexString.append(0);
            }
            hexString.append(shaHex);
        }
        return hexString.toString().toUpperCase();
    }

    //二进制转16进制
    public static String binToHex_Lit(byte[] data) {
        // Create Hex String
        StringBuffer hexString = new StringBuffer();
        // 字节数组转换为 十六进制 数
        for (int i = data.length - 1; i >= 0; i--) {
            String shaHex = Integer.toHexString(data[i] & 0xFF);
            if (shaHex.length() < 2) {
                hexString.append(0);
            }
            hexString.append(shaHex);
        }
        return hexString.toString().toUpperCase();
    }

    //int转16进制
    public static String intToHex(int integer) {
        byte[] bin = intToBin(integer);
        return binToHex(bin);
    }

    public static byte[] intToBin_Lit(int integer){
        byte[] bin = new byte[]{
                (byte) ((integer >> 0) & 0xFF),
                (byte) ((integer >> 8) & 0xFF),
                (byte) ((integer >> 16) & 0xFF),
                (byte) ((integer >> 24) & 0xFF)
        };
        return bin;
    }

    //int 转 二进制
    public static byte[] intToBin(int integer) {
        byte[] bin = new byte[]{
                (byte) ((integer >> 24) & 0xFF),
                (byte) ((integer >> 16) & 0xFF),
                (byte) ((integer >> 8) & 0xFF),
                (byte) (integer & 0xFF)
        };
        return bin;
    }

    //二进制转字符串
    public static String binToStr(byte[] data) {
        StringBuffer sb = new StringBuffer();
        for (byte b : data) {
            String z = b == 0 ? "." : new String(new byte[]{b});
            sb.append(z);
        }
        return sb.toString();
    }

    //Ushort 转 int
    public static int[] charToInt(char[] data) {
        int[] targe = new int[data.length];
        for (int i = 0; i < data.length; i++) {
            targe[i] = (int) data[i];
        }
        return targe;
    }

    //Lcc/gnaix/xx -> cc.gnaix.xx
    public static String pathToPackages(String path) {
        String packageName = path.replaceAll("/", ".");
        packageName = packageName.startsWith("L") ? packageName.substring(1, packageName.length() - 1) : packageName;
        return packageName;
    }

    //hackPoint 转 二进制
    public static byte[] hackpToBin(HackPoint point) {
        ByteBuffer bb = ByteBuffer.allocate(4 * 3);
        bb.put(intToBin_Lit(point.type));
        bb.put(intToBin_Lit(point.offset));
        bb.put(intToBin_Lit(point.value));
        return bb.array();
    }

    //二进制转 hackPoint
    public static HackPoint[] binToHackP(byte[] buff) {
        int remainder = buff.length % 12;
        if (remainder != 0) {     //数据有误无法读取
            log("warning", "hackinfo.length % 12 != 0");
            System.exit(0);
        }
        int count = buff.length / 12;
        HackPoint[] hackPoints = new HackPoint[count];
        Reader reader = new Reader(buff, 0);
        for (int i = 0; i < count; i++) {
            int type = reader.readUint();
            int offset = reader.readUint();
            int value = reader.readUint();
            HackPoint hackPoint = new HackPoint(type, offset, value);
            hackPoints[i] = hackPoint;
        }
        return hackPoints;
    }

    public static Uleb128 intToUleb128(int value) {
        byte[] origValue = new byte[]{0x00, 0x00, 0x00, 0x00}; //int 最大长度为4
        int intValue = value;
        int length = 0;
        for (int i = 0; i < origValue.length; i++) {
            length = i + 1; //最少长度为1
            origValue[i] = (byte) (value & 0x7F); //获取低7位的值
            if (value > (0x7F)) {
                origValue[i] |= 0x80; //高位为1 加上去
            }
            value = value >> 7;
            if (value <= 0) break;
        }
        origValue = BufferUtil.subdex(origValue, 0, length);
        Uleb128 uleb128 = new Uleb128(origValue, intValue, length);
        return uleb128;
    }
}
