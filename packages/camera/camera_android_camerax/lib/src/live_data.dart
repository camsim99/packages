// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

import 'package:flutter/services.dart';
import 'package:meta/meta.dart';

import 'camera_state.dart';
import 'camerax_library.g.dart';
import 'instance_manager.dart';
import 'java_object.dart';
import 'observer.dart';

/// A data holder class that can be observed.
///
/// For this wrapped class, observation can only fall within the lifecycle of the
/// Android Activity to which this plugin is attached.
///
/// See https://developer.android.com/reference/androidx/lifecycle/LiveData.
class LiveData<T> extends JavaObject {
  /// Constructs a [LiveData] that is not automatically attached to a native object.
  LiveData.detached({this.binaryMessenger, this.instanceManager})
      : _api = _LiveDataHostApiImpl(
            binaryMessenger: binaryMessenger, instanceManager: instanceManager),
        super.detached(
            binaryMessenger: binaryMessenger, instanceManager: instanceManager);

  final _LiveDataHostApiImpl _api;

  /// Receives binary data across the Flutter platform barrier.
  final BinaryMessenger? binaryMessenger;

  /// Maintains instances stored to communicate with native language objects.
  final InstanceManager? instanceManager;

  /// Adds specified [Observer] to the list of observers of this instance.
  Future<void> observe(Observer<T> observer) {
    return _api.observeFromInstances(this, observer);
  }

  /// Removes all observers of this instance.
  Future<void> removeObservers() {
    return _api.removeObserversFromInstances(this);
  }
}

/// Host API implementation of [LiveData].
class _LiveDataHostApiImpl extends LiveDataHostApi {
  /// Constructs a [_LiveDataHostApiImpl].
  ///
  /// If [binaryMessenger] the default BinaryMessenger will be used which routes to
  /// the host platform.
  ///
  /// An [instanceManager] is typically passed when a copy of an instance
  /// contained by an [InstanceManager] is being created. If left null, it
  /// will default to the global instance defined in [JavaObject].
  _LiveDataHostApiImpl({
    this.binaryMessenger,
    InstanceManager? instanceManager,
  })  : instanceManager = instanceManager ?? JavaObject.globalInstanceManager,
        super(binaryMessenger: binaryMessenger);

  /// Receives binary data across the Flutter platform barrier.
  final BinaryMessenger? binaryMessenger;

  /// Maintains instances stored to communicate with native language objects.
  final InstanceManager instanceManager;

  /// Adds specified [Observer] to the list of observers of the specified
  /// [LiveData] instance.
  Future<void> observeFromInstances(
    LiveData<dynamic> instance,
    Observer<dynamic> observer,
  ) {
    return observe(
      instanceManager.getIdentifier(instance)!,
      instanceManager.getIdentifier(observer)!,
    );
  }

  /// Removes all observers of the specified [LiveData] instance.
  Future<void> removeObserversFromInstances(
    LiveData<dynamic> instance,
  ) {
    return removeObservers(
      instanceManager.getIdentifier(instance)!,
    );
  }
}

/// Flutter API implementation for [LiveData].
///
/// This class may handle instantiating and adding Dart instances that are
/// attached to a native instance or receiving callback methods from an
/// overridden native class.
@protected
class LiveDataFlutterApiImpl implements LiveDataFlutterApi {
  /// Constructs a [LiveDataFlutterApiImpl].
  ///
  /// If [binaryMessenger] is null, the default [BinaryMessenger] will be used,
  /// which routes to the host platform.
  ///
  /// An [instanceManager] is typically passed when a copy of an instance
  /// contained by an [InstanceManager] is being created. If left null, it
  /// will default to the global instance defined in [JavaObject].
  LiveDataFlutterApiImpl({
    BinaryMessenger? binaryMessenger,
    InstanceManager? instanceManager,
  })  : _binaryMessenger = binaryMessenger,
        _instanceManager = instanceManager ?? JavaObject.globalInstanceManager;

  /// Receives binary data across the Flutter platform barrier.
  final BinaryMessenger? _binaryMessenger;

  /// Maintains instances stored to communicate with native language objects.
  final InstanceManager _instanceManager;

  @override
  void create(
    int identifier,
    LiveDataSupportedTypeData typeData,
  ) {
    switch (typeData.value) {
      case LiveDataSupportedType.cameraState:
        _instanceManager.addHostCreatedInstance(
          LiveData<CameraState>.detached(
            binaryMessenger: _binaryMessenger,
            instanceManager: _instanceManager,
          ),
          identifier,
          onCopy: (LiveData<CameraState> original) =>
              LiveData<CameraState>.detached(
            binaryMessenger: _binaryMessenger,
            instanceManager: _instanceManager,
          ),
        );
    }
  }
}