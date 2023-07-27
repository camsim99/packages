
// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package io.flutter.plugins.camerax;

import android.util.Size;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.camera.video.FallbackStrategy;
import androidx.camera.video.Quality;
import androidx.camera.video.QualitySelector;
import io.flutter.plugins.camerax.GeneratedCameraXLibrary.QualityConstraint;
import io.flutter.plugins.camerax.GeneratedCameraXLibrary.QualitySelectorHostApi;
import io.flutter.plugins.camerax.GeneratedCameraXLibrary.ResolutionInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Host API implementation for {@link QualitySelector}.
 *
 * <p>This class may handle instantiating and adding native object instances that are attached to a
 * Dart instance or handle method calls on the associated native class or an instance of the class.
 */
public class QualitySelectorHostApiImpl implements QualitySelectorHostApi {
  private final InstanceManager instanceManager;

  private final QualitySelectorProxy proxy;

  /** Proxy for constructors and static method of {@link QualitySelector}. */
  @VisibleForTesting
  public static class QualitySelectorProxy {

    /** Creates an instance of {@link QualitySelector}. */
    public QualitySelector create(
        @NonNull List<Long> qualityIndexList, @Nullable FallbackStrategy fallbackStrategy) {
      List<Quality> qualityList = new ArrayList<Quality>();
      for (Long qualityIndex : qualityIndexList) {
        qualityList.add(getQualityConstant(qualityIndex));
      }

      if (qualityList.size() == 1) {
        Quality quality = qualityList.get(0);
        if (fallbackStrategy == null) {
          return QualitySelector.from(quality);
        }
        return QualitySelector.from(quality, fallbackStrategy);
      }

      if (fallbackStrategy == null) {
        return QualitySelector.fromOrderedList(qualityList);
      }
      return QualitySelector.fromOrderedList(qualityList);
    }

    private Quality getQualityConstant(@NonNull Long qualityIndex) {
      QualityConstraint quality = QualityConstraint.values()[qualityIndex.intValue()];
      return getCameraXQualityFromQuality(quality);
    }
  }

  /**
   * Constructs a {@link QualitySelectorHostApiImpl}.
   *
   * @param instanceManager maintains instances stored to communicate with attached Dart objects
   */
  public QualitySelectorHostApiImpl(@NonNull InstanceManager instanceManager) {

    this(instanceManager, new QualitySelectorProxy());
  }

  /**
   * Constructs a {@link QualitySelectorHostApiImpl}.
   *
   * @param instanceManager maintains instances stored to communicate with attached Dart objects
   * @param proxy proxy for constructors and static method of {@link QualitySelector}
   */
  @VisibleForTesting
  QualitySelectorHostApiImpl(
      @NonNull InstanceManager instanceManager, @NonNull QualitySelectorProxy proxy) {
    this.instanceManager = instanceManager;
    this.proxy = proxy;
  }

  @Override
  public void create(
      @NonNull Long identifier,
      @NonNull List<Long> qualityList,
      @Nullable Long fallbackStrategyIdentifier) {

    instanceManager.addDartCreatedInstance(
        proxy.create(
            qualityList,
            fallbackStrategyIdentifier == null
                ? null
                : Objects.requireNonNull(instanceManager.getInstance(fallbackStrategyIdentifier))),
        identifier);
  }

  @Override
  public ResolutionInfo getResolution(
      @NonNull Long cameraInfoIdentifier, @NonNull QualityConstraint quality) {
    final Size result =
        QualitySelector.getResolution(
            Objects.requireNonNull(instanceManager.getInstance(cameraInfoIdentifier)),
            getCameraXQualityFromQuality(quality));

    return new ResolutionInfo.Builder()
        .setWidth(Long.valueOf(result.getWidth()))
        .setHeight(Long.valueOf(result.getHeight()))
        .build();
  }

  private QualitySelector getQualitySelectorInstance(@NonNull Long identifier) {
    return Objects.requireNonNull(instanceManager.getInstance(identifier));
  }

  public static Quality getCameraXQualityFromQuality(QualityConstraint quality) {
    switch (quality) {
      case SD:
        return Quality.SD;
      case HD:
        return Quality.HD;
      case FHD:
        return Quality.FHD;
      case UHD:
        return Quality.UHD;
      case LOWEST:
        return Quality.LOWEST;
      case HIGHEST:
        return Quality.HIGHEST;
    }
    throw new IllegalArgumentException("whoops");
  }
}