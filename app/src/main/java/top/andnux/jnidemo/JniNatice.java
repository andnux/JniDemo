package top.andnux.jnidemo;

import java.util.UUID;

public class JniNatice {

    public A a = new AA();

    public String key = "andnux";

    public static String key2 = "andnux";

    static {
        System.loadLibrary("native");
    }

    public static String randomUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public native String accessField();

    public native String accessStaticField();

    public native int accessMethod();

    public native String accessStaticMethod();

    public native long accessConstructor();

    public native void accessNonvirtualMethod();

    public native String chineseChars();

    public native void qsort(int[] datas);

    public native int[] getArray();
}
