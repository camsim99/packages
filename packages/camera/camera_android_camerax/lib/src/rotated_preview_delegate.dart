// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

import 'dart:async';

import 'package:flutter/services.dart';
import 'package:flutter/widgets.dart';
import 'package:meta/meta.dart';

import 'camerax_proxy.dart';
import 'image_reader_rotated_preview.dart';
import 'surface_texture_rotated_preview.dart';

/// Widget that rotates the camera preview to be upright according to the
/// current user interface orientation based on whether or not the Impeller
/// backend used handles crop and rotation of Surfaces correctly.
@internal
final class RotatedPreviewDelegate extends StatelessWidget {
  /// Creates [RotatedPreviewDelegate] that will build the correctly
  /// rotated preview widget depending on whether or not the Impeller
  /// backend handles crop and rotation.
  const RotatedPreviewDelegate(
      {super.key,
      required this.handlesCropAndRotation,
      required this.initialDeviceOrientation,
      required this.initialDefaultDisplayRotation,
      required this.deviceOrientationStream,
      required this.sensorOrientationDegrees,
      required this.cameraIsFrontFacing,
      required this.cameraXProxy,
      required this.child});

  /// Whether or not the Android surface producer automatically handles
  /// correcting the rotation of camera previews for the device this plugin
  /// runs on.
  final bool handlesCropAndRotation;

  /// The initial orientation of the device when the camera is created.
  final DeviceOrientation initialDeviceOrientation;

  /// The initial rotation of the Android default display when the camera is created,
  /// in terms of a Surface rotation constant.
  final int initialDefaultDisplayRotation;

  /// Stream of changes to the device orientation.
  final Stream<DeviceOrientation> deviceOrientationStream;

  /// The orienation of the camera sensor in degrees.
  final double sensorOrientationDegrees;

  /// Whether or not the camera is front facing.
  final bool cameraIsFrontFacing;

  /// Proxy for calling into CameraX library on the native Android side of the plugin.
  ///
  /// Instance required to check the current rotation of the default Android display.
  final CameraXProxy cameraXProxy;

  /// The camera preview [Widget] to rotate.
  final Widget child;

  @override
  Widget build(BuildContext context) {
    if (handlesCropAndRotation) {
      return SurfaceTextureRotatedPreview(initialDeviceOrientation,
          initialDefaultDisplayRotation, deviceOrientationStream, cameraXProxy,
          child: child);
    }

    if (cameraIsFrontFacing) {
      return ImageReaderRotatedPreview.frontFacingCamera(
        initialDeviceOrientation,
        initialDefaultDisplayRotation,
        deviceOrientationStream,
        sensorOrientationDegrees,
        cameraXProxy,
        child: child,
      );
    } else {
      return ImageReaderRotatedPreview.backFacingCamera(
        initialDeviceOrientation,
        initialDefaultDisplayRotation,
        deviceOrientationStream,
        sensorOrientationDegrees,
        cameraXProxy,
        child: child,
      );
    }
  }
}
