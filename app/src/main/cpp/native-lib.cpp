#include <jni.h>
#include <string>
#include <vector>
#include <string>
#include <wasmedge/wasmedge.h>


extern "C"
JNIEXPORT
jstring
Java_com_hangedfish_wasmedge_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string str = std::string("WasmEdge Version: ") + WasmEdge_VersionGet();
    return env->NewStringUTF(str.c_str());
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_hangedfish_wasmedge_MainActivity_nativeWasmFib(JNIEnv *env, jobject thiz,
                                                        jbyteArray j_wasm_bytes, jint idx) {
    jsize n_wasm_bytes = env->GetArrayLength(j_wasm_bytes);
    jbyte *wasm_bytes = env->GetByteArrayElements(j_wasm_bytes, 0);

    WasmEdge_ConfigureContext *conf = WasmEdge_ConfigureCreate();
    WasmEdge_ConfigureAddHostRegistration(conf, WasmEdge_HostRegistration_Wasi);

    WasmEdge_VMContext *vm_ctx = WasmEdge_VMCreate(conf, nullptr);
    WasmEdge_Value params[1] = {WasmEdge_ValueGenI32(idx)};
    WasmEdge_Value retval[1]{};

    const WasmEdge_String &func_name = WasmEdge_StringCreateByCString("fib");
    const WasmEdge_Result &res = WasmEdge_VMRunWasmFromBuffer(vm_ctx, (uint8_t *) wasm_bytes,
                                                              n_wasm_bytes,
                                                              func_name, params, 1,
                                                              retval, 1);
    env->ReleaseByteArrayElements(j_wasm_bytes, wasm_bytes, 0);

    WasmEdge_VMDelete(vm_ctx);
    WasmEdge_ConfigureDelete(conf);
    WasmEdge_StringDelete(func_name);

    if (!WasmEdge_ResultOK(res)) {
        return -1;
    }
    return WasmEdge_ValueGetI32(retval[0]);
}