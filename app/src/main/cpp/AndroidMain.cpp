// Copyright 2016 Google Inc. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
#include <android/log.h>
#include "VulkanMain.hpp"

int numOfVertices = 0;
jfloat *listVertices;
jint *listFaces;

// Process the next main command.
void handle_cmd(android_app* app, int32_t cmd) {
  switch (cmd) {
    case APP_CMD_INIT_WINDOW:
      // The window is being shown, get it ready.
      InitVulkan(app, numOfVertices, listVertices);
      break;
    case APP_CMD_TERM_WINDOW:
      // The window is being hidden or closed, clean it up.
      DeleteVulkan();
      break;
    default:
      __android_log_print(ANDROID_LOG_INFO, "Vulkan Tutorials",
                          "event not handled: %d", cmd);
  }
}

void android_main(struct android_app* app) {

  // Set the callback to process system events
  app->onAppCmd = handle_cmd;

  // Used to poll the events in the main loop
  int events;
  android_poll_source* source;

  // Main loop
  do {
    if (ALooper_pollAll(IsVulkanReady() ? 1 : 0, nullptr,
                        &events, (void**)&source) >= 0) {
      if (source != NULL) source->process(app, source);
    }

    // render if vulkan is ready
    if (IsVulkanReady()) {
      VulkanDrawFrame();
    }
  } while (app->destroyRequested == 0);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_ctyeung_vulkanex_OffDecoderActivity_loadJNI(JNIEnv *env, jobject thiz,
                                                     jint num_of_verticies,
                                                     jfloatArray vertices,
                                                     jint num_of_faces,
                                                     jintArray faces) {

    listVertices = env->GetFloatArrayElements(vertices, NULL);
    listFaces = env->GetIntArrayElements(faces, NULL);

    if( listVertices != NULL &&
            listFaces != NULL &&
        num_of_verticies > 0) {
        numOfVertices = num_of_verticies;

        /*
         *  TODO Force a reload
         */
    }
}