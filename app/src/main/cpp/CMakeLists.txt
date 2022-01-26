cmake_minimum_required(VERSION 3.18.1)

project("wasmedge_android_app")


set(WASMEDGE_SOURCE_DIR vendor/WasmEdgeAndroid)
set(WASMEDGE_BUILD_AOT_RUNTIME OFF CACHE BOOL "" FORCE)
add_subdirectory(${WASMEDGE_SOURCE_DIR})
#add_subdirectory(${WASMEDGE_SOURCE_DIR}/include)
#include_directories(${WASMEDGE_SOURCE_DIR}/include/api)
include_directories(${WasmEdge_BINARY_DIR}/include/api)

add_library(
        wasmedge_android_app
        SHARED

        native-lib.cpp)


target_link_libraries(
        wasmedge_android_app
        log
        wasmedge_c_shared
)

#add_dependencies(
#        wasmedge_android_app
#        wasmedge_c_shared
#)