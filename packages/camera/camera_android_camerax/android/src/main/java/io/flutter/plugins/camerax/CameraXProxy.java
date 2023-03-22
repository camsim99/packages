// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package io.flutter.plugins.camerax;

import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.view.Surface;
import androidx.annotation.NonNull;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.Preview;
import io.flutter.plugin.common.BinaryMessenger;
import java.io.File;

/** Utility class used to create CameraX-related objects primarily for testing purposes. */
public class CameraXProxy {
  /** Creates a builder for a {@link CameraSelector}. */
  public CameraSelector.Builder createCameraSelectorBuilder() {
    return new CameraSelector.Builder();
  }

  /** Creates an instance of {@link CameraPermissionsManager}. */
  public CameraPermissionsManager createCameraPermissionsManager() {
    return new CameraPermissionsManager();
  }

  /** Creates an instance of the {@link DeviceOrientationManager}. */
  public DeviceOrientationManager createDeviceOrientationManager(
      @NonNull Activity activity,
      @NonNull Boolean isFrontFacing,
      @NonNull int sensorOrientation,
      @NonNull DeviceOrientationManager.DeviceOrientationChangeCallback callback) {
    return new DeviceOrientationManager(activity, isFrontFacing, sensorOrientation, callback);
  }

  /** Creates a builder for an instance of the {@link Preview} use case. */
  public Preview.Builder createPreviewBuilder() {
    return new Preview.Builder();
  }

  /** Creates a {@link Surface} instance from the specified {@link SurfaceTexture}. */
  public Surface createSurface(@NonNull SurfaceTexture surfaceTexture) {
    return new Surface(surfaceTexture);
  }

  /**
   * Creates an instance of the {@link SystemServicesFlutterApiImpl}.
   *
   * <p>Included in this class to utilize the callback methods it provides, e.g. {@code
   * onCameraError(String)}.
   */
  public SystemServicesFlutterApiImpl createSystemServicesFlutterApiImpl(
      @NonNull BinaryMessenger binaryMessenger) {
    return new SystemServicesFlutterApiImpl(binaryMessenger);
  }

  /** Creates a builder for an instance of the {@link ImageCapture} use case. */
  public ImageCapture.Builder createImageCaptureBuilder() {
    return new ImageCapture.Builder();
  }

  /**
   * Creates an {@link ImageCapture.OutputFileOptions} to configure where to save a captured image.
   */
  public ImageCapture.OutputFileOptions createImageCaptureOutputFileOptions(@NonNull File file) {
    return new ImageCapture.OutputFileOptions.Builder(file).build();
  }

  /** Creates an instance of the {@link LiveCameraStateFlutterApiImpl}. */
  public LiveCameraStateFlutterApiImpl createLiveCameraStateFlutterApiImpl(
      @NonNull BinaryMessenger binaryMessenger, @NonNull InstanceManager instanceManager) {
    return new LiveCameraStateFlutterApiImpl(binaryMessenger, instanceManager);
  }
}
