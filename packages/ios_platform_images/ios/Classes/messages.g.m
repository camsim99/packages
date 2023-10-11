// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.
// Autogenerated from Pigeon (v11.0.1), do not edit directly.
// See also: https://pub.dev/packages/pigeon

#import "messages.g.h"

#if TARGET_OS_OSX
#import <FlutterMacOS/FlutterMacOS.h>
#else
#import <Flutter/Flutter.h>
#endif

#if !__has_feature(objc_arc)
#error File requires ARC to be enabled.
#endif

static NSArray *wrapResult(id result, FlutterError *error) {
  if (error) {
    return @[
      error.code ?: [NSNull null], error.message ?: [NSNull null], error.details ?: [NSNull null]
    ];
  }
  return @[ result ?: [NSNull null] ];
}
static id GetNullableObjectAtIndex(NSArray *array, NSInteger key) {
  id result = array[key];
  return (result == [NSNull null]) ? nil : result;
}

@interface FPIPlatformImageData ()
+ (FPIPlatformImageData *)fromList:(NSArray *)list;
+ (nullable FPIPlatformImageData *)nullableFromList:(NSArray *)list;
- (NSArray *)toList;
@end

@implementation FPIPlatformImageData
+ (instancetype)makeWithData:(FlutterStandardTypedData *)data scale:(NSNumber *)scale {
  FPIPlatformImageData *pigeonResult = [[FPIPlatformImageData alloc] init];
  pigeonResult.data = data;
  pigeonResult.scale = scale;
  return pigeonResult;
}
+ (FPIPlatformImageData *)fromList:(NSArray *)list {
  FPIPlatformImageData *pigeonResult = [[FPIPlatformImageData alloc] init];
  pigeonResult.data = GetNullableObjectAtIndex(list, 0);
  NSAssert(pigeonResult.data != nil, @"");
  pigeonResult.scale = GetNullableObjectAtIndex(list, 1);
  NSAssert(pigeonResult.scale != nil, @"");
  return pigeonResult;
}
+ (nullable FPIPlatformImageData *)nullableFromList:(NSArray *)list {
  return (list) ? [FPIPlatformImageData fromList:list] : nil;
}
- (NSArray *)toList {
  return @[
    (self.data ?: [NSNull null]),
    (self.scale ?: [NSNull null]),
  ];
}
@end

@interface FPIPlatformImagesApiCodecReader : FlutterStandardReader
@end
@implementation FPIPlatformImagesApiCodecReader
- (nullable id)readValueOfType:(UInt8)type {
  switch (type) {
    case 128:
      return [FPIPlatformImageData fromList:[self readValue]];
    default:
      return [super readValueOfType:type];
  }
}
@end

@interface FPIPlatformImagesApiCodecWriter : FlutterStandardWriter
@end
@implementation FPIPlatformImagesApiCodecWriter
- (void)writeValue:(id)value {
  if ([value isKindOfClass:[FPIPlatformImageData class]]) {
    [self writeByte:128];
    [self writeValue:[value toList]];
  } else {
    [super writeValue:value];
  }
}
@end

@interface FPIPlatformImagesApiCodecReaderWriter : FlutterStandardReaderWriter
@end
@implementation FPIPlatformImagesApiCodecReaderWriter
- (FlutterStandardWriter *)writerWithData:(NSMutableData *)data {
  return [[FPIPlatformImagesApiCodecWriter alloc] initWithData:data];
}
- (FlutterStandardReader *)readerWithData:(NSData *)data {
  return [[FPIPlatformImagesApiCodecReader alloc] initWithData:data];
}
@end

NSObject<FlutterMessageCodec> *FPIPlatformImagesApiGetCodec(void) {
  static FlutterStandardMessageCodec *sSharedObject = nil;
  static dispatch_once_t sPred = 0;
  dispatch_once(&sPred, ^{
    FPIPlatformImagesApiCodecReaderWriter *readerWriter =
        [[FPIPlatformImagesApiCodecReaderWriter alloc] init];
    sSharedObject = [FlutterStandardMessageCodec codecWithReaderWriter:readerWriter];
  });
  return sSharedObject;
}

void FPIPlatformImagesApiSetup(id<FlutterBinaryMessenger> binaryMessenger,
                               NSObject<FPIPlatformImagesApi> *api) {
  /// Returns the URL for the given resource, or null if no such resource is
  /// found.
  {
    FlutterBasicMessageChannel *channel = [[FlutterBasicMessageChannel alloc]
           initWithName:@"dev.flutter.pigeon.ios_platform_images.PlatformImagesApi.resolveUrl"
        binaryMessenger:binaryMessenger
                  codec:FPIPlatformImagesApiGetCodec()];
    if (api) {
      NSCAssert([api respondsToSelector:@selector(resolveURLForResource:withExtension:error:)],
                @"FPIPlatformImagesApi api (%@) doesn't respond to "
                @"@selector(resolveURLForResource:withExtension:error:)",
                api);
      [channel setMessageHandler:^(id _Nullable message, FlutterReply callback) {
        NSArray *args = message;
        NSString *arg_resourceName = GetNullableObjectAtIndex(args, 0);
        NSString *arg_extension = GetNullableObjectAtIndex(args, 1);
        FlutterError *error;
        NSString *output = [api resolveURLForResource:arg_resourceName
                                        withExtension:arg_extension
                                                error:&error];
        callback(wrapResult(output, error));
      }];
    } else {
      [channel setMessageHandler:nil];
    }
  }
  /// Returns the data for the image resource with the given name, or null if
  /// no such resource is found.
  {
    FlutterBasicMessageChannel *channel = [[FlutterBasicMessageChannel alloc]
           initWithName:@"dev.flutter.pigeon.ios_platform_images.PlatformImagesApi.loadImage"
        binaryMessenger:binaryMessenger
                  codec:FPIPlatformImagesApiGetCodec()];
    if (api) {
      NSCAssert(
          [api respondsToSelector:@selector(loadImageWithName:error:)],
          @"FPIPlatformImagesApi api (%@) doesn't respond to @selector(loadImageWithName:error:)",
          api);
      [channel setMessageHandler:^(id _Nullable message, FlutterReply callback) {
        NSArray *args = message;
        NSString *arg_name = GetNullableObjectAtIndex(args, 0);
        FlutterError *error;
        FPIPlatformImageData *output = [api loadImageWithName:arg_name error:&error];
        callback(wrapResult(output, error));
      }];
    } else {
      [channel setMessageHandler:nil];
    }
  }
}
