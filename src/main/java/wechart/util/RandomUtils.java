package wechart.util;

import java.util.Random;

/**
 * @author <a href="mailto:Administrator@gtmap.cn">Administrator</a>
 * @version 1.0, 2017/8/4
 * @description
 */
public class RandomUtils {
    //生成8位数的 字母 大小随机 当做授权码
    public static String randomUtil() {

        Random r = new Random();

        String code = "";

        for (int i = 0; i < 8; ++i) {
            int temp = r.nextInt(52);
            char x = (char) (temp < 26 ? temp + 97 : (temp % 26) + 65);
            code += x;
        }

        return code;
    }

    public static String getNumberAsId(long rang) {
        return Math.round(Math.random()*Math.pow(10, rang))+"";
    }

}