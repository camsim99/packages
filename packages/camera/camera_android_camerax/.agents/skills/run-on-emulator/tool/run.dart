// Copyright 2013 The Flutter Authors
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

import 'dart:io';

import 'package:path/path.dart' as p;
import 'package:run_on_emulator/run_on_emulator.dart';

/// Resolves the camera_android_camerax package root from the script URI.
Directory findPackageDir(Uri scriptUri) {
  // The script is at: .agents/skills/run-on-emulator/tool/run.dart
  final scriptDir = Directory(p.dirname(scriptUri.toFilePath()));
  return scriptDir.parent.parent.parent.parent;
}

Future<void> main(List<String> args) async {
  if (args.contains('--help') || args.contains('-h')) {
    stdout.writeln(RunOptions.usage);
    return;
  }

  final RunOptions options;
  try {
    options = RunOptions.parse(args);
  } on FormatException catch (e) {
    stderr.writeln('ERROR: ${e.message}\n');
    stderr.writeln(RunOptions.usage);
    exitCode = ExitCodes.usage;
    return;
  }

  final String packageRoot = findPackageDir(Platform.script).path;
  exitCode = await EmulatorRunner().run(options, packageRoot: packageRoot);
}
