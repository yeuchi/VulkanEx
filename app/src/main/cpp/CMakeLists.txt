# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.18.1)

# Declares and names the project.

project("vulkanex")

find_package(game-activity REQUIRED CONFIG)

# build vulkan app
get_filename_component(REPO_ROOT_DIR
        ${CMAKE_SOURCE_DIR}/../../../..  ABSOLUTE)
set(COMMON_DIR ${REPO_ROOT_DIR}/common)

add_library(${CMAKE_PROJECT_NAME} SHARED
        VulkanMain.cpp
        ${COMMON_DIR}/vulkan_wrapper/vulkan_wrapper.cpp
        AndroidMain.cpp
        ${COMMON_DIR}/src/GameActivitySources.cpp)

include_directories(${COMMON_DIR}/vulkan_wrapper)
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11 -Wall \
                     -DVK_USE_PLATFORM_ANDROID_KHR")
target_link_libraries(${CMAKE_PROJECT_NAME}
        game-activity::game-activity
        log
        android)