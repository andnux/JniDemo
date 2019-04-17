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
#### 10. 局部引用。
```java
1. native接口
public class JniNatice {
    static {
        System.loadLibrary("native");
    }
    public native void localReference();
}
2. c++实现
extern "C"
JNIEXPORT void JNICALL
Java_top_andnux_jnidemo_JniNatice_localReference(JNIEnv *env, jobject instance) {
    for (int i = 0; i < 100; ++i) {
        jclass jclass1 = env->FindClass("java/util/Date");
        jmethodID jmethodID1 = env->GetMethodID(jclass1, "<init>", "()V");
        jobject jobject1 = env->NewObject(jclass1, jmethodID1);
        env->DeleteLocalRef(jobject1);
    }
}
```
#### 11. 全局引用(若全局引用类似)。
> **若全局引用：**<br>
> 节省内存，在内存不足的时候回收。<br>
> 可以引用一个不常用的对象，如果为空，临时创建。<br>
> 创建：NewWeakGlobalRef<br>
> 销毁：DeleteWeakGlobalRef<br>
```java
1. native接口
public class JniNatice {
    static {
        System.loadLibrary("native");
    }

    public native void createGlobalReference();

    public native String getGlobalReference();

    public native void releaseGlobalReference();
}
2. c++实现
static jstring globalString;
extern "C"
JNIEXPORT void JNICALL
Java_top_andnux_jnidemo_JniNatice_createGlobalReference(JNIEnv *env, jobject instance) {
    jstring glob = env->NewStringUTF("哈哈哈哈");
    globalString = static_cast<jstring>(env->NewGlobalRef(glob));
}

extern "C"
JNIEXPORT jstring JNICALL
Java_top_andnux_jnidemo_JniNatice_getGlobalReference(JNIEnv *env, jobject instance) {
    return globalString;
}

extern "C"
JNIEXPORT void JNICALL
Java_top_andnux_jnidemo_JniNatice_releaseGlobalReference(JNIEnv *env, jobject instance) {
    env->DeleteGlobalRef(globalString);
}
```
#### 12. 异常处理。
```java
1. native接口
public class JniNatice {
    static {
        System.loadLibrary("native");
    }
    public native String exception();
}
2. c++实现
extern "C"
JNIEXPORT jstring JNICALL
Java_top_andnux_jnidemo_JniNatice_exception(JNIEnv *env, jobject instance) {
    jclass jclazz = env->GetObjectClass(instance);
    jfieldID fieldID = env->GetFieldID(jclazz, "key2", "Ljava/lang/String;");
    jthrowable jthrowable1 = env->ExceptionOccurred();
    if (jthrowable1 != NULL) {
        env->ExceptionClear();
        fieldID = env->GetFieldID(jclazz, "key", "Ljava/lang/String;");
    }
    jstring jstring1 = static_cast<jstring>(env->GetObjectField(instance, fieldID));
    char *ptr = (char *) env->GetStringChars(jstring1, JNI_FALSE);
    if (strcmp(ptr, "andnux") != 0) {
        jclass jclass1 = env->FindClass("java/lang/IllegalArgumentException");
        env->ThrowNew(jclass1, "参数错误");
    }
    return jstring1;
}
```
#### 13. 附上签名。
| 基本类型 | 属性签名 | 方法签名 |
| -------- | -------- | -------- |
|void | V | () |
|boolean | Z | (Z)V |
|byte | B |  (B)V |
|char | C | (C)V |
|short | S | (S)V |
|int | I | (I)V |
|long | J | (J)V |
|float | F | (F)V |
|double | D | (D)V |
 > **引用类型的描述符:**<br>
 一般引用类型则为 L + 该类型类描述符 + ;<br>
  (注意，这儿的分号“；”只得是JNI的一部分，而不是我们汉语中的分段，下同)
 ```java
 int[ ]     其描述符为[I
 float[ ]   其描述符为[F
 String[ ]  其描述符为[Ljava/lang/String;
 Object[ ]类型的域描述符为[Ljava/lang/Object;
 ```