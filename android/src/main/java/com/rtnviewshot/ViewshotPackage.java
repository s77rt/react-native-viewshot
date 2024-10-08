package com.rtnviewshot;

import androidx.annotation.Nullable;
import com.facebook.react.TurboReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.module.model.ReactModuleInfo;
import com.facebook.react.module.model.ReactModuleInfoProvider;
import java.util.HashMap;
import java.util.Map;

public class ViewshotPackage extends TurboReactPackage {

  @Nullable
  @Override
  public NativeModule getModule(String name,
                                ReactApplicationContext reactContext) {
    if (name.equals(ViewshotModule.NAME)) {
      return new ViewshotModule(reactContext);
    } else {
      return null;
    }
  }

  @Override
  public ReactModuleInfoProvider getReactModuleInfoProvider() {
    return () -> {
      final Map<String, ReactModuleInfo> moduleInfos = new HashMap<>();
      moduleInfos.put(ViewshotModule.NAME,
                      new ReactModuleInfo(ViewshotModule.NAME,
                                          ViewshotModule.NAME,
                                          false, // canOverrideExistingModule
                                          false, // needsEagerInit
                                          true,  // hasConstants
                                          false, // isCxxModule
                                          true   // isTurboModule
                                          ));
      return moduleInfos;
    };
  }
}
