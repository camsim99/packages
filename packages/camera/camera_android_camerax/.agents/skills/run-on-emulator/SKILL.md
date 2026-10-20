---
name: run-on-emulator
description: Launches an Android emulator with the Android CLI and runs the camera_android_camerax example app or its integration tests (example/integration_test). Use this skill whenever the user asks to run the integration tests, run or launch the example app, test on an emulator, or "manually verify" a change; whenever the pre-push-skill reaches its integration test step; whenever an implementation plan has a verification step (including "Manual Verification") that can be exercised on an emulator; and whenever writing the verification section of an implementation plan, so emulator checks are planned as automated agent steps instead of manual steps for the user.
metadata:
  internal: true
---
# Run on Emulator

This skill boots an Android emulator and runs the `camera_android_camerax`
example app or integration tests on it. All environment checks are performed
by a bundled, deterministic script ([tool/run.dart](tool/run.dart)) that
stops at the first problem with a distinct exit code.

> [!IMPORTANT]
> CI does not run this package's integration tests on emulators (it is listed
> in `script/configs/exclude_integration_android_emulator.yaml`), so running
> them locally is the only emulator coverage a change gets before review.

## When NOT to use the emulator

The emulator only provides generic emulated cameras (`emulated`,
`virtualscene`, or a webcam). It **cannot** verify behavior that depends on
specific hardware, for example:

- Flash or torch output.
- Real lens characteristics: optical zoom ranges, focus distance, multiple
  physical back cameras, or camera extensions (HDR, night, bokeh).
- Device-specific sensor orientations, OEM camera quirks, or performance.

If the behavior under test depends on hardware like this, tell the user that
it requires a physical device and **do not** claim it was verified. You may
still use the `app` command to confirm the change does not crash or regress
the emulator's camera flow, as long as you report that limitation.

## Instructions

Run all commands from the `camera_android_camerax` package root. Always use the
script; do not start emulators or run `flutter test` by hand to work around a
script failure, because that bypasses the checks that keep runs reliable
(e.g. pre-granting camera permissions, which otherwise hang the tests
indefinitely).

| Goal | Command |
| --- | --- |
| Check the environment only | `dart run .agents/skills/run-on-emulator/tool/run.dart check` |
| Boot an emulator | `dart run .agents/skills/run-on-emulator/tool/run.dart emulator` |
| Build, install, and launch the example app | `dart run .agents/skills/run-on-emulator/tool/run.dart app` |
| Run all integration tests | `dart run .agents/skills/run-on-emulator/tool/run.dart integration-test` |
| Run matching integration tests only | `dart run .agents/skills/run-on-emulator/tool/run.dart integration-test --plain-name="Video capture"` |

Each command performs, in order, and stops at the first failure:

1. **Android CLI check**: `android` must be on the PATH and must be the Android
   CLI (not the legacy SDK tool).
2. **Tool check**: `adb` (from the SDK reported by `android info`, falling back
   to the PATH) and, for `app`/`integration-test`, `flutter`.
3. **Emulator**: reuses a running emulator, otherwise starts the first AVD
   from `android emulator list` (or `--avd=<id>`) with
   `android emulator start`, then waits for `sys.boot_completed`. For
   `app`/`integration-test`, the AVD must have at least one camera.
4. **Run**: builds a debug APK, installs it with all runtime permissions
   granted (`adb install -g`), then launches the app or runs `flutter test`.

Other options: `--avd=<id>`, `--target=<path relative to example/>`,
`--boot-timeout=<sec>`, `--test-timeout=<sec>`, `--stop-emulator` (stops the
emulator afterwards only if the script started it), `--allow-no-camera`.
Run with `--help` for details.

The emulator is left running by default so later runs are fast. The full
integration test suite can take more than 10 minutes, so run it in the
background and wait for it to complete rather than polling.

### Handling the Results

The last line of output is always `RESULT: SUCCESS ...` or
`RESULT: FAILED (exit code N)`, preceded by an `ERROR:` line on failure.

| Exit code | Meaning | What to do |
| --- | --- | --- |
| 0 | Success | Report the result (see below). |
| 2 | Android CLI missing or not the Android CLI | **Stop.** Warn the user and relay the install instructions from the output. Do not try other ways to start an emulator. |
| 3 | `adb` or `flutter` missing | **Stop.** Warn the user and relay the fix from the output. |
| 4 | No usable AVD (none exist, `--avd` not found, or no camera) | **Stop.** Relay the suggested fix (e.g. `android emulator create medium_phone`, or editing the AVD's camera config) and ask the user before creating or modifying AVDs. |
| 5 | Emulator failed to start or boot | **Stop.** Report the error. Suggest `android emulator start --cold <avd>` or freeing disk/RAM. |
| 6 | Example app failed to build, install, or launch | Report the build/install error or crash. For crashes, read `adb -s <serial> logcat -d -b crash`. |
| 7 | Integration tests failed or timed out | Report each failing test and its error from the output. Re-run each failing test once with `--plain-name` to tell flakes (common right after a cold boot) from real failures, and report both results. On a timeout, capture the screen (below) to find what is blocking the app. |
| 64 | Invalid arguments | Fix the command and re-run. |

## Writing Verification Steps in Implementation Plans

When writing an implementation plan, write emulator-observable verification as
steps **the agent performs** with this skill, not as steps for the user. Never
write "the user should run the emulator", "launch the app and confirm...", or
"manually run the integration tests". For example:

```markdown
## Verification Plan

### Automated (performed by the agent)
- Dart/native unit tests: ...
- Integration tests: run `run-on-emulator integration-test` (add a new
  integration test for <behavior> and run it with `--plain-name`).
- Emulator check: run `run-on-emulator app`, tap <control>, and capture a
  screenshot showing <expected result>.

### Requires a physical device (performed by the user)
- <Only hardware-dependent checks, e.g. flash firing, with the reason.>
```

Leave out the physical-device section entirely if nothing needs real hardware.

## Verifying a Change in the App

Use this workflow when an implementation plan has a verification step
(including one labeled "Manual Verification"), or the user asks for one, and
the behavior can be observed on an emulator. Do it yourself rather than
asking the user to:

1. Run the `app` command. Note the `serial=` value in the `RESULT:` line.
2. Inspect and drive the app with the Android CLI and `adb`:
   - Screenshot: `android screen capture --annotate -o <artifact dir>/scratch/<name>.png`,
     then view the PNG to check the UI. This command has no device option, so
     make sure the emulator is the only connected device (`adb devices`).
   - UI tree: `android layout --device=<serial> --pretty`.
   - Input: `adb -s <serial> shell input tap <x> <y>` (coordinates from the
     annotated screenshot or layout tree).
   - Logs: `adb -s <serial> logcat -d | grep -i -E "camera|flutter"`.
3. If verifying the change needs code in the example app that is not on the
   default screen, prefer adding an integration test that exercises it and
   running it with `integration-test --plain-name=<test name>`, so the
   verification is repeatable.
4. Report what you did, what you observed (embed the screenshots), and
   anything you could not verify on an emulator.
