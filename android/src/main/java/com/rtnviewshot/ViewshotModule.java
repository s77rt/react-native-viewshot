package com.rtnviewshot;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.view.View;
import androidx.annotation.NonNull;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.UIManager;
import com.facebook.react.bridge.UiThreadUtil;
import com.facebook.react.uimanager.UIManagerHelper;
import com.facebook.react.uimanager.common.UIManagerType;
import com.rtnviewshot.NativeRTNViewshotSpec;
import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ViewshotModule extends NativeRTNViewshotSpec {

  public static String NAME = "RTNViewshot";

  private final ReactApplicationContext reactContext;
  private final ExecutorService executorService =
      Executors.newCachedThreadPool();

  ViewshotModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @Override
  @NonNull
  public String getName() {
    return NAME;
  }

  @Override
  public void capture(double nodeHandle, Promise promise) {
    int reactTag = (int)nodeHandle;
    UIManager uiManager =
        UIManagerHelper.getUIManager(reactContext, UIManagerType.FABRIC);
    UiThreadUtil.runOnUiThread(new Runnable() {
      public void run() {
        View reactView = uiManager.resolveView(reactTag);
        if (reactView == null) {
          promise.reject("View", "View not found");
          return;
        }

        Bitmap bitmap =
            Bitmap.createBitmap(reactView.getWidth(), reactView.getHeight(),
                                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        reactView.draw(canvas);

        executorService.execute(new Runnable() {
          public void run() {
            File file;
            try {
              file = File.createTempFile(NAME, ".jpeg");
            } catch (Throwable e) {
              promise.reject("File", "File creation failed", e);
              return;
            }
            file.deleteOnExit();

            boolean compressSuccess;
            try {
              FileOutputStream fileOutputStream = new FileOutputStream(file);
              compressSuccess = bitmap.compress(Bitmap.CompressFormat.JPEG, 100,
                                                fileOutputStream);
              fileOutputStream.close();
            } catch (Throwable e) {
              promise.reject("File", "Could not open output stream", e);
              return;
            }

            if (!compressSuccess) {
              promise.reject("File", "File write failed");
              return;
            }

            promise.resolve(Uri.fromFile(file).toString());
          }
        });
      }
    });
  }
}
