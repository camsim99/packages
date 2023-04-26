// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package io.flutter.plugins.camerax;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import androidx.camera.core.resolutionselector.AspectRatioStrategy;
import androidx.camera.core.resolutionselector.ResolutionSelector;
import androidx.camera.core.resolutionselector.ResolutionStrategy;
import io.flutter.plugin.common.BinaryMessenger;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class ResolutionSelectorTest {
  @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
  @Mock public ResolutionSelector mockResolutionSelector;
  @Mock public BinaryMessenger mockBinaryMessenger;
  @Mock public ResolutionSelectorHostApiImpl.ResolutionSelectorProxy mockProxy;

  InstanceManager instanceManager;

  @Before
  public void setUp() {
    instanceManager = InstanceManager.create(identifier -> {});
  }

  @After
  public void tearDown() {
    instanceManager.stopFinalizationListener();
  }

  @Test
  public void hostApiCreate() {
    final ResolutionStrategy mockResolutionStrategy = mock(ResolutionStrategy.class);
    final long resolutionStrategyIdentifier = 14;
    instanceManager.addDartCreatedInstance(mockResolutionStrategy, resolutionStrategyIdentifier);

    final AspectRatioStrategy mockAspectRatioStrategy = mock(AspectRatioStrategy.class);
    final long aspectRatioStrategyIdentifier = 15;
    instanceManager.addDartCreatedInstance(mockAspectRatioStrategy, aspectRatioStrategyIdentifier);

    when(mockProxy.create(mockResolutionStrategy, mockAspectRatioStrategy))
        .thenReturn(mockResolutionSelector);
    final ResolutionSelectorHostApiImpl hostApi =
        new ResolutionSelectorHostApiImpl(mockBinaryMessenger, instanceManager, mockProxy);

    final long instanceIdentifier = 0;
    hostApi.create(instanceIdentifier, resolutionStrategyIdentifier, aspectRatioStrategyIdentifier);

    assertEquals(instanceManager.getInstance(instanceIdentifier), mockResolutionSelector);
  }
}
