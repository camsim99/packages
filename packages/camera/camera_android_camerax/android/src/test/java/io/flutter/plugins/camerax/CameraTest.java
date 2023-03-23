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

import androidx.camera.core.Camera;
import androidx.camera.core.CameraInfo;
import io.flutter.plugin.common.BinaryMessenger;
import java.util.Objects;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class CameraTest {
  @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

  @Mock public BinaryMessenger mockBinaryMessenger;
  @Mock public Camera camera;

  InstanceManager testInstanceManager;

  @Before
  public void setUp() {
    testInstanceManager = InstanceManager.open(identifier -> {});
  }

  @After
  public void tearDown() {
    testInstanceManager.close();
  }

  @Test
  public void getCameraInfo_makesCallToGetCameraInfo() {
    final CameraHostApiImpl cameraHostApiImpl =
        new CameraHostApiImpl(mockBinaryMessenger, testInstanceManager);
    final Long cameraIdentifier = 65L;
    CameraInfo mockCameraInfo = mock(CameraInfo.class);

    testInstanceManager.addDartCreatedInstance(camera, cameraIdentifier);
    Long mockCameraInfoIdentifier = testInstanceManager.addHostCreatedInstance(mockCameraInfo);

    when(camera.getCameraInfo()).thenReturn(mockCameraInfo);

    assertEquals(cameraHostApiImpl.getCameraInfo(cameraIdentifier), mockCameraInfoIdentifier);
  }

  @Test
  public void flutterApiCreate_makesCallToCreateInstance() {
    final CameraFlutterApiImpl spyFlutterApi =
        spy(new CameraFlutterApiImpl(mockBinaryMessenger, testInstanceManager));

    spyFlutterApi.create(camera, reply -> {});

    final long identifier =
        Objects.requireNonNull(testInstanceManager.getIdentifierForStrongReference(camera));
    verify(spyFlutterApi).create(eq(identifier), any());
  }
}
