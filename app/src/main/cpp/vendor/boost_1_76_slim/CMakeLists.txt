cmake_minimum_required(VERSION 3.10.2)

project(boost)

add_library(Boost_boost INTERFACE)
add_library(Boost::boost ALIAS Boost_boost)
target_include_directories(Boost_boost INTERFACE ${CMAKE_CURRENT_SOURCE_DIR})