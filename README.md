## jni常见的使用
#### 1. 访问java属性。
```java
1. native接口
public class JniNatice {
    public String key = "andnux";
    static {
        System.loadLibrary("native");
    }
    public native String accessField();
}
2. c++实现
extern "C" JNIEXPORT jstring JNICALL
Java_top_andnux_jnidemo_JniNatice_accessField(JNIEnv *env, jobject instance) {
    jclass jclazz = env->GetObjectClass(instance);
    jfieldID fieldID = env->GetFieldID(jclazz, "key", "Ljava/lang/String;");
    jstring jstring1 = static_cast<jstring>(env->GetObjectField(instance, fieldID));
    char *cstr = const_cast<char *>(env->GetStringUTFChars(jstring1, JNI_FALSE));
    char text[30] = "key ";
    strcat(text,cstr);
    jstring  jstring2 = env->NewStringUTF(text);
    env->SetObjectField(instance,fieldID,jstring2);
    return jstring2;
}
```
#### 2. 访问java静态属性。
```java
1. native接口
public class JniNatice {
    public static String key2 = "andnux";
    static {
        System.loadLibrary("native");
    }
    public native String accessStaticField();
}
2. c++实现
extern "C"
JNIEXPORT jstring JNICALL
Java_top_andnux_jnidemo_JniNatice_accessStaticField(JNIEnv *env, jobject instance) {
    jclass jclazz = env->GetObjectClass(instance);
    jfieldID fieldID = env->GetStaticFieldID(jclazz, "key2", "Ljava/lang/String;");
    jstring jstring1 = static_cast<jstring>(env->GetStaticObjectField(jclazz, fieldID));
    char *cstr = const_cast<char *>(env->GetStringUTFChars(jstring1, JNI_FALSE));
    char text[30] = "key2 ";
    strcat(text,cstr);
    jstring  jstring2 = env->NewStringUTF(text);
    env->SetStaticObjectField(jclazz,fieldID,jstring2);
    return jstring2;
}
```
#### 3. 访问java方法。
```java
1. native接口
public class JniNatice {
    static {
        System.loadLibrary("native");
    }
    public int randomInt() {
        return new Random().nextInt();
    }
    public native int accessMethod();
}
2. c++实现
extern "C"
JNIEXPORT jint JNICALL
Java_top_andnux_jnidemo_JniNatice_accessMethod(JNIEnv *env, jobject instance) {
    jclass jclazz = env->GetObjectClass(instance);
    jmethodID jmethodID1 = env->GetMethodID(jclazz, "randomInt", "()I");
    jint value = env->CallIntMethod(instance, jmethodID1);
    return value;
}
```

#### 4. 访问java静态方法。
```java
1. native接口
public class JniNatice {
    static {
        System.loadLibrary("native");
    }
    public static String randomUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
    public native int accessStaticMethod();
}
2. c++实现
extern "C"
JNIEXPORT jint JNICALL
Java_top_andnux_jnidemo_JniNatice_accessStaticMethod(JNIEnv *env, jobject instance) {
    jclass jclazz = env->GetObjectClass(instance);
    jmethodID jmethodID1 = env->GetStaticMethodID(jclazz, "randomUUID", "()Ljava/lang/String;");
    jstring jstring1 = static_cast<jstring>(env->CallStaticObjectMethod(jclazz, jmethodID1));
    return jstring1;
}
```

#### 5. 访问java构造方法。
```java
1. native接口
public class JniNatice {
    static {
        System.loadLibrary("native");
    }
    public native long accessConstructor();
}
2. c++实现
extern "C"
JNIEXPORT jlong JNICALL
Java_top_andnux_jnidemo_JniNatice_accessConstructor(JNIEnv *env, jobject instance) {
    jclass jclazz = env->FindClass("java/util/Date");
    jmethodID jmethodID1 = env->GetMethodID(jclazz, "<init>", "()V");
    jobject jobject1 = env->NewObject(jclazz, jmethodID1);
    jmethodID jmethodID2 = env->GetMethodID(jclazz, "getTime", "()J");
    jlong  jlong1 = env->CallLongMethod(jobject1,jmethodID2);
    return jlong1;
}
```

#### 6. 访问java父类得方法。
```java
1. A类
public class A {
    public void a() {
        Log.e("A", "aaaaaaaaa");
    }
}
2.AA类
public class AA extends A {
    @Override
    public void a() {
        Log.e("A", "AAAAAAAAAA");
    }
}
3. native接口
public class JniNatice {
    static {
        System.loadLibrary("native");
    }
     public native void accessNonvirtualMethod();
}
4. c++实现
extern "C"
JNIEXPORT void JNICALL
Java_top_andnux_jnidemo_JniNatice_accessNonvirtualMethod(JNIEnv *env, jobject instance) {
    jclass jclazz = env->GetObjectClass(instance);
    jfieldID jfieldID1 = env->GetFieldID(jclazz, "a", "Ltop/andnux/jnidemo/A;");
    jobject jobject1 = env->GetObjectField(instance, jfieldID1);
    jclass  jclass1 = env->FindClass("top/andnux/jnidemo/A");
    jmethodID  jmethodID1 = env->GetMethodID(jclass1,"a","()V");
    env->CallNonvirtualVoidMethod(jobject1,jclass1,jmethodID1);
}
```
#### 7. 解决字符串乱码问题。
```java
1. native接口
public class JniNatice {
    static {
        System.loadLibrary("native");
    }
    public native long accessConstructor();
}
2. c++实现
extern "C"
JNIEXPORT jstring JNICALL
Java_top_andnux_jnidemo_JniNatice_chineseChars(JNIEnv *env, jobject instance) {
    char *c_str = const_cast<char *>("张春林");
    jclass jclass1 = env->FindClass("java/lang/String");
    jmethodID jmethodID1 = env->GetMethodID(jclass1, "<init>", "([BLjava/lang/String;)V");
    jbyteArray jbyteArray1 = env->NewByteArray(static_cast<jsize>(strlen(c_str)));
    env->SetByteArrayRegion(jbyteArray1, 0, static_cast<jsize>(strlen(c_str)),
                            reinterpret_cast<const jbyte *>(c_str));
    jstring charsetName = env->NewStringUTF("UTF-8");
    return static_cast<jstring>(env->NewObject(jclass1, jmethodID1,
                                               jbyteArray1, charsetName));
//    return env->NewStringUTF(c_str);
}
```
#### 8. 访问JAVA数组。
```java
1. native接口
public class JniNatice {
    static {
        System.loadLibrary("native");
    }
    public native void  qsort(int [] datas);
}
2. c++实现
extern "C"
JNIEXPORT void JNICALL
Java_top_andnux_jnidemo_JniNatice_qsort(JNIEnv *env, jobject instance, jintArray datas_) {
    jint *datas = env->GetIntArrayElements(datas_, NULL);
    jint len =(env->GetArrayLength(datas_));
    qsort(datas, static_cast<size_t>(len), sizeof(int),
          reinterpret_cast<int (*)(const void *, const void *)>(comparator));
    env->ReleaseIntArrayElements(datas_, datas, 0);
}
```
#### 9. 返回JAVA数组。
```java
1. native接口
public class JniNatice {
    static {
        System.loadLibrary("native");
    }
    public native int[] getArray();
}
2. c++实现
extern "C"
JNIEXPORT jintArray JNICALL
Java_top_andnux_jnidemo_JniNatice_getArray(JNIEnv *env, jobject instance) {
    jintArray array = env->NewIntArray(10);
    jint len = env->GetArrayLength(array);
    jint *elems = env->GetIntArrayElements(array, JNI_FALSE);
    for (int i = 0; i < len; ++i) {
        elems[i] = i;
    }
    env->ReleaseIntArrayElements(array, elems, 0);
    return array;
}
```