cmake_minimum_required(VERSION 3.18.1)

project("wasmedge_android_app")

set(CMAKE_MODULE_PATH "${CMAKE_SOURCE_DIR}/cmake; ${CMAKE_MODULE_PATH}")
add_subdirectory(vendor/boost_1_76_slim)

set(SPDLOG_BUILD_SHARED OFF CACHE BOOL "Build shared library" FORCE)
add_subdirectory(vendor/spdlog)

set(WASMEDGE_SOURCE_DIR vendor/WasmEdge)
set(WASMEDGE_BUILD_AOT_RUNTIME OFF CACHE BOOL "" FORCE)
set(WASMEDGE_BUILD_SHARED_LIB ON CACHE BOOL "" FORCE)
set(WASMEDGE_BUILD_STATIC_LIB OFF CACHE BOOL "" FORCE)
set(WASMEDGE_FORCE_DISABLE_LTO ON CACHE BOOL "" FORCE)  # fast fixed problem for `-fuse-ld=gold`, we use lld.
if (CMAKE_GENERATOR STREQUAL Ninja)
    set(CMAKE_JOB_POOLS "link=2")
    set(CMAKE_JOB_POOL_LINK link)
endif()
add_subdirectory(${WASMEDGE_SOURCE_DIR})
include_directories(${WasmEdge_BINARY_DIR}/include/api)

add_library(
        wasmedge_android_app
        SHARED

        native-lib.cpp)


target_link_libraries(
        wasmedge_android_app
        log
        wasmedge_c_shared
#        wasmedge_c_static
)