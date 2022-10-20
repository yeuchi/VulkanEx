# VulkanEx
Android Vulkan exercises

### Ground Zero - Tutorial 5 : Triangle

Created a new Kotlin project that utilizes Google sample project triangle<sup>[0]</sup>. \
!! No creativity what-so-ever !! Just figuring out configurations \
Took a few days to read introductory documentation, test samples and watch youtube tutorials. \
<img width="250" src="https://user-images.githubusercontent.com/1282659/189745147-1e47e469-4134-48b5-af1c-11ce5218306b.jpg">

### OFF Loader (Work in progress)

Load OFF file on phone's document or download directory \
See assets directory for sample OFF files. \
<img width="250" src="https://user-images.githubusercontent.com/1282659/189754965-c111eda9-f798-4e6b-9f1f-f9670b84a0b1.jpg">
<img width="250" src="https://user-images.githubusercontent.com/1282659/187953254-39a2b07b-988c-4258-bbba-dd67c02b67e4.png">

Sucessful load right_triangle.off file. \
<img width="250" src="https://user-images.githubusercontent.com/1282659/189752061-4abca822-a390-45b9-8005-f42fe5160bb6.jpg">

# bash_profile
Notice some special dependencies for C++ build. 

export ANDROID_HOME=/Users/you/Library/Android/sdk \
export PATH=$PATH:$ANDROID_HOME/cmake/3.18.1/bin \
export PATH=$PATH:$ANDROID_HOME/tools \
export PATH=$PATH:$ANDROID_HOME/bin \
export PATH=$PATH:$ANDROID_HOME/tools/bin \
export PATH=$PATH:$ANDROID_HOME/tools:$ANDROID_HOME/platform-tools \
export PATH=$PATH:$ANDROID_HOME/cmake/3.18.1/bin \
export PATH=$PATH:$ANDROID_HOME/cmdline-tools/latest/bin \
export ANDROID_NDK=/Users/ctyeung/Library/Android/sdk/ndk/20.0.5594570 \
export ANDROID_SDK=${ANDROID_HOME} \
export JAVA_HOME=/Users/you/Library/Java/JavaVirtualMachines/openjdk-18.0.2.1/Contents/Home \

# References

0. Google Samples - Android Vulkan Samples Tutorial 5, Github \
https://github.com/googlesamples/android-vulkan-tutorials/tree/master/tutorial05_triangle \
https://github.com/googlesamples/android-vulkan-tutorials

1. Get started with GameActivity \
https://developer.android.com/games/agdk/game-activity/get-started

2. OFF File Format, Wiki \
https://en.wikipedia.org/wiki/OFF_(file_format)
