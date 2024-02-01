// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

/// Affect the quality of video recording and image capture:
///
/// A preset is treated as a target resolution, and exact values are not
/// guaranteed. Platform implementations may fall back to a higher or lower
/// resolution if the specific preset is not available.
enum ResolutionPreset {
  /// 352x288 on iOS, ~240p (320x240) on Android and Web
  low,

  /// ~480p (640x480 on iOS, 720x480 on Android and Web)
  medium,

  /// ~720p (1280x720)
  high,

  /// ~1080p (1920x1080)
  veryHigh,

  /// ~2160p (3840x2160 on Android and iOS, 4096x2160 on Web)
  ultraHigh,

  /// The highest resolution available.
  max,
}
