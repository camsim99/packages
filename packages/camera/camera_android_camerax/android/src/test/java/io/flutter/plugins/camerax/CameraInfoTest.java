// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package io.flutter.plugins.camerax;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.camera.core.CameraInfo;
import androidx.camera.core.CameraState;
import androidx.lifecycle.LiveData;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugins.camerax.GeneratedCameraXLibrary.LiveDataSupportedType;
import java.util.Objects;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class CameraInfoTest {
  @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

  @Mock public CameraInfo mockCameraInfo;
  @Mock public BinaryMessenger mockBinaryMessenger;

  InstanceManager testInstanceManager;

  @Before
  public void setUp() {
    testInstanceManager = InstanceManager.create(identifier -> {});
  }

  @After
  public void tearDown() {
    testInstanceManager.stopFinalizationListener();
  }

  @Test
  public void getSensorRotationDegrees_makesCallToRetrieveSensorRotationDegrees() {
    final CameraInfoHostApiImpl cameraInfoHostApi =
        new CameraInfoHostApiImpl(mockBinaryMessenger, testInstanceManager);

    testInstanceManager.addDartCreatedInstance(mockCameraInfo, 1);

    when(mockCameraInfo.getSensorRotationDegrees()).thenReturn(90);

    assertEquals((long) cameraInfoHostApi.getSensorRotationDegrees(1L), 90L);
    verify(mockCameraInfo).getSensorRotationDegrees();
  }

  @Test
  public void getLiveCameraState_makesCallToRetrieveLiveCameraState() {
    final CameraInfoHostApiImpl cameraInfoHostApiImpl =
        new CameraInfoHostApiImpl(mockBinaryMessenger, testInstanceManager);
    final LiveDataFlutterApiWrapper mockLiveDataFlutterApiWrapper =
        mock(LiveDataFlutterApiWrapper.class);
    final Long mockCameraInfoIdentifier = 27L;
    @SuppressWarnings("unchecked")
    final LiveData<CameraState> mockLiveCameraState = (LiveData<CameraState>) mock(LiveData.class);

    testInstanceManager.addDartCreatedInstance(mockCameraInfo, mockCameraInfoIdentifier);
    cameraInfoHostApiImpl.liveDataFlutterApiWrapper = mockLiveDataFlutterApiWrapper;
    when(mockCameraInfo.getCameraState()).thenReturn(mockLiveCameraState);

    final Long liveCameraStateIdentifier =
        cameraInfoHostApiImpl.getLiveCameraState(mockCameraInfoIdentifier);

    verify(mockLiveDataFlutterApiWrapper)
        .create(eq(mockLiveCameraState), eq(LiveDataSupportedType.CAMERA_STATE), any());
    assertEquals(
        liveCameraStateIdentifier,
        testInstanceManager.getIdentifierForStrongReference(mockLiveCameraState));
  }

  @Test
  public void flutterApi_makesCallToDartToCreateInstance() {
    final CameraInfoFlutterApiImpl spyFlutterApi =
        spy(new CameraInfoFlutterApiImpl(mockBinaryMessenger, testInstanceManager));

    spyFlutterApi.create(mockCameraInfo, reply -> {});

    final long identifier =
        Objects.requireNonNull(testInstanceManager.getIdentifierForStrongReference(mockCameraInfo));
    verify(spyFlutterApi).create(eq(identifier), any());
  }
}
