#import "RTNViewshot.h"

#import <React/RCTUtils.h>

@implementation RTNViewshot

RCT_EXPORT_MODULE()

@synthesize viewRegistry_DEPRECATED = _viewRegistry_DEPRECATED;

- (void)capture:(double)nodeHandle
        resolve:(RCTPromiseResolveBlock)resolve
         reject:(RCTPromiseRejectBlock)reject {
  NSNumber *reactTag = [NSNumber numberWithDouble:nodeHandle];

  dispatch_async(dispatch_get_main_queue(), ^{
    UIView *reactView = [self.viewRegistry_DEPRECATED viewForReactTag:reactTag];
    if (!reactView) {
      reject(@"View", @"View not found", nil);
      return;
    }

    UIGraphicsImageRendererFormat *rendererFormat =
        [UIGraphicsImageRendererFormat defaultFormat];
    UIGraphicsImageRenderer *renderer =
        [[UIGraphicsImageRenderer alloc] initWithBounds:reactView.bounds
                                                 format:rendererFormat];
    __block BOOL snapshotSuccess;
    UIImage *image = [renderer
        imageWithActions:^(
            __unused UIGraphicsImageRendererContext *rendererContext) {
          snapshotSuccess = [reactView drawViewHierarchyInRect:reactView.bounds
                                            afterScreenUpdates:YES];
        }];

    if (!snapshotSuccess) {
      reject(@"Snapshot", @"Snapshot failed", nil);
      return;
    }

    dispatch_async(
        dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
          NSError *fileError;
          NSString *filePath = RCTTempFilePath(@"png", &fileError);
          if (fileError) {
            reject(@"File", @"File creation failed", fileError);
            return;
          }

          NSData *imageData = UIImagePNGRepresentation(image);

          BOOL writeSuccess = [imageData writeToFile:filePath atomically:YES];
          if (!writeSuccess) {
            reject(@"File", @"File write failed", nil);
            return;
          }

          resolve([[NSURL fileURLWithPath:filePath] absoluteString]);
        });
  });
}

- (std::shared_ptr<facebook::react::TurboModule>)getTurboModule:
    (const facebook::react::ObjCTurboModule::InitParams &)params {
  return std::make_shared<facebook::react::NativeRTNViewshotSpecJSI>(params);
}

@end