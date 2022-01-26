#include <jni.h>
#include <string>
#include <wasmedge/wasmedge.h>


extern "C"
JNIEXPORT
jstring
Java_com_hangedfish_wasmedge_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string str = std::string("WasmEdge Version: ") + WasmEdge_VersionGet();
    return env->NewStringUTF(str.c_str());
}