// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package io.flutter.plugins.camerax;

import androidx.annotation.NonNull;
import androidx.camera.core.CameraInfo;
import androidx.camera.core.ExposureState;
import androidx.camera.core.ZoomState;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugins.camerax.GeneratedCameraXLibrary.CameraInfoHostApi;
import java.util.Objects;

public class CameraInfoHostApiImpl implements CameraInfoHostApi {
  private final BinaryMessenger binaryMessenger;
  private final InstanceManager instanceManager;

  public CameraInfoHostApiImpl(BinaryMessenger binaryMessenger, InstanceManager instanceManager) {
    this.binaryMessenger = binaryMessenger;
    this.instanceManager = instanceManager;
  }

  @Override
  public Long getSensorRotationDegrees(@NonNull Long identifier) {
    CameraInfo cameraInfo =
        (CameraInfo) Objects.requireNonNull(instanceManager.getInstance(identifier));
    return Long.valueOf(cameraInfo.getSensorRotationDegrees());
  }

  /**
   * Retrieves the {@link ExposureState} of the {@link CameraInfo} with the specified identifier.
   */
  @Override
  public Long getExposureState(@NonNull Long identifier) {
    CameraInfo cameraInfo =
        (CameraInfo) Objects.requireNonNull(instanceManager.getInstance(identifier));
    ExposureState exposureState = cameraInfo.getExposureState();

    ExposureStateFlutterApiImpl exposureStateFlutterApiImpl =
        new ExposureStateFlutterApiImpl(binaryMessenger, instanceManager);
    exposureStateFlutterApiImpl.create(exposureState, result -> {});

    return instanceManager.getIdentifierForStrongReference(exposureState);
  }

  /**
   * Retrieves the current {@link ZoomState} value of the {@link CameraInfo} with the specified
   * identifier.
   */
  @Override
  public Long getZoomState(@NonNull Long identifier) {
    CameraInfo cameraInfo =
        (CameraInfo) Objects.requireNonNull(instanceManager.getInstance(identifier));
    // Retrieves the current value of the LiveData<ZoomState>.
    ZoomState zoomState = cameraInfo.getZoomState().getValue();

    ZoomStateFlutterApiImpl zoomStateFlutterApiImpl =
        new ZoomStateFlutterApiImpl(binaryMessenger, instanceManager);
    zoomStateFlutterApiImpl.create(zoomState, result -> {});

    return instanceManager.getIdentifierForStrongReference(zoomState);
  }
}
