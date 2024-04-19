package com.fun.port;

public class FormatUtil {
    //    增加CRC校验码
    private static final int POLY = 0xA001;

    public static String getCrcStr(String dataStr) {
        byte[] data = FormatUtil.hexStringToBytes(dataStr);
        byte[] dataWithCrc = addCrc(data);
        return FormatUtil.bytesToHexString(dataWithCrc);
    }

    public static byte[] addCrc(byte[] data) {
        int crc = getCrc(data, 0, data.length);
        byte[] result = new byte[data.length + 2];
        System.arraycopy(data, 0, result, 0, data.length);
        result[result.length - 2] = (byte) (crc & 0xFF);
        result[result.length - 1] = (byte) ((crc >> 8) & 0xFF);
        return result;
    }

    private static int getCrc(byte[] data, int offset, int length) {
        int crc = 0xFFFF;
        for (int i = offset; i < offset + length; i++) {
            crc ^= (data[i] & 0xFF);

            for (int j = 0; j < 8; j++) {
                if ((crc & 0x0001) != 0) {
                    crc = (crc >> 1) ^ POLY;
                } else {
                    crc = crc >> 1;
                }
            }
        }

        return crc;
    }

    public static int crc16(byte[] snd, int num) {
        int i, j;
        int c, crc = 0xFFFF;
        for (i = 0; i < num; i++) {
            c = snd[i] & 0x00FF;
            crc ^= c;
            for (j = 0; j < 8; j++) {
                if ((crc & 0x0001) != 0) {
                    crc = (crc >> 1) ^ 0xA001;
                } else {
                    crc >>= 1;
                }
            }
        }
        return crc;
    }
    public static String getHex4fromInt(int number) {
        String hexString = Integer.toHexString(number).toUpperCase();
        while(hexString.length() < 4)
        {
            hexString = "0" + hexString;
        }
        if(hexString.length()>4){
            hexString = hexString.substring(hexString.length()-4);
        }
        return hexString;
    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder builder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        String hv;
        for (int i = 0; i < src.length; i++) {
            // 以十六进制（基数 16）无符号整数形式返回一个整数参数的字符串表示形式，并转换为大写
            hv = Integer.toHexString(src[i] & 0xFF).toUpperCase();
            if (hv.length() < 2) {
                builder.append(0);
            }
            builder.append(hv);
        }

        return builder.toString();
    }

    public static String bytesToHexString(byte[] src,int length) {
        StringBuilder builder = new StringBuilder();
        if (src == null || src.length <= 0 || length>src.length) {
            return null;
        }
        String hv;
        int min = Math.min(src.length, length);
        for (int i = 0; i < min; i++) {
            // 以十六进制（基数 16）无符号整数形式返回一个整数参数的字符串表示形式，并转换为大写
            hv = Integer.toHexString(src[i] & 0xFF).toUpperCase();
            if (hv.length() < 2) {
                builder.append(0);
            }
            builder.append(hv);
        }

        return builder.toString();
    }

    /**
     * 将Hex String转换为Byte数组
     *
     * @param hexString the hex string
     * @return the byte [ ]
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString==null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toLowerCase();
        final byte[] byteArray = new byte[hexString.length() >> 1];
        int index = 0;
        for (int i = 0; i < hexString.length(); i++) {
            if (index  > hexString.length() - 1) {
                return byteArray;
            }
            byte highDit = (byte) (Character.digit(hexString.charAt(index), 16) & 0xFF);
            byte lowDit = (byte) (Character.digit(hexString.charAt(index + 1), 16) & 0xFF);
            byteArray[i] = (byte) (highDit << 4 | lowDit);
            index += 2;
        }
        return byteArray;
    }
}