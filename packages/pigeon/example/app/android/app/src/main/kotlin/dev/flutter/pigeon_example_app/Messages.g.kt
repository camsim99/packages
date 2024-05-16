// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.
// Autogenerated from Pigeon, do not edit directly.
// See also: https://pub.dev/packages/pigeon
@file:Suppress("UNCHECKED_CAST", "ArrayInDataClass")

import android.util.Log
import io.flutter.plugin.common.BasicMessageChannel
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MessageCodec
import io.flutter.plugin.common.StandardMessageCodec
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer

private fun wrapResult(result: Any?): List<Any?> {
  return listOf(result)
}

private fun wrapError(exception: Throwable): List<Any?> {
  return if (exception is FlutterError) {
    listOf(exception.code, exception.message, exception.details)
  } else {
    listOf(
        exception.javaClass.simpleName,
        exception.toString(),
        "Cause: " + exception.cause + ", Stacktrace: " + Log.getStackTraceString(exception))
  }
}

private fun createConnectionError(channelName: String): FlutterError {
  return FlutterError(
      "channel-error", "Unable to establish connection on channel: '$channelName'.", "")
}

/**
 * Error class for passing custom error details to Flutter via a thrown PlatformException.
 *
 * @property code The error code.
 * @property message The error message.
 * @property details The error details. Must be a datatype supported by the api codec.
 */
class FlutterError(
    val code: String,
    override val message: String? = null,
    val details: Any? = null
) : Throwable()

enum class Code(val raw: Int) {
  ONE(0),
  TWO(1);

  companion object {
    fun ofRaw(raw: Int): Code? {
      return values().firstOrNull { it.raw == raw }
    }
  }
}

/** Generated class from Pigeon that represents data sent in messages. */
data class MessageData(
    val name: String? = null,
    val description: String? = null,
    val code: Code,
    val data: Map<String?, String?>
) {
  companion object {
    @Suppress("LocalVariableName")
    fun fromList(__pigeon_list: List<Any?>): MessageData {
      val name = __pigeon_list[0] as String?
      val description = __pigeon_list[1] as String?
      val code = Code.ofRaw(__pigeon_list[2] as Int)!!
      val data = __pigeon_list[3] as Map<String?, String?>
      return MessageData(name, description, code, data)
    }
  }

  fun toList(): List<Any?> {
    return listOf<Any?>(
        name,
        description,
        code.raw,
        data,
    )
  }
}

private object ExampleHostApiCodec : StandardMessageCodec() {
  override fun readValueOfType(type: Byte, buffer: ByteBuffer): Any? {
    return when (type) {
      128.toByte() -> {
        return (readValue(buffer) as? List<Any?>)?.let { MessageData.fromList(it) }
      }
      else -> super.readValueOfType(type, buffer)
    }
  }

  override fun writeValue(stream: ByteArrayOutputStream, value: Any?) {
    when (value) {
      is MessageData -> {
        stream.write(128)
        writeValue(stream, value.toList())
      }
      else -> super.writeValue(stream, value)
    }
  }
}

/** Generated interface from Pigeon that represents a handler of messages from Flutter. */
interface ExampleHostApi {
  fun getHostLanguage(): String

  fun add(a: Long, b: Long): Long

  fun sendMessage(message: MessageData, callback: (Result<Boolean>) -> Unit)

  companion object {
    /** The codec used by ExampleHostApi. */
    val codec: MessageCodec<Any?> by lazy { ExampleHostApiCodec }
    /** Sets up an instance of `ExampleHostApi` to handle messages through the `binaryMessenger`. */
    fun setUp(
        binaryMessenger: BinaryMessenger,
        api: ExampleHostApi?,
        messageChannelSuffix: String = ""
    ) {
      val separatedMessageChannelSuffix =
          if (messageChannelSuffix.isNotEmpty()) ".$messageChannelSuffix" else ""
      run {
        val channel =
            BasicMessageChannel<Any?>(
                binaryMessenger,
                "dev.flutter.pigeon.pigeon_example_package.ExampleHostApi.getHostLanguage$separatedMessageChannelSuffix",
                codec)
        if (api != null) {
          channel.setMessageHandler { _, reply ->
            val wrapped: List<Any?> =
                try {
                  listOf<Any?>(api.getHostLanguage())
                } catch (exception: Throwable) {
                  wrapError(exception)
                }
            reply.reply(wrapped)
          }
        } else {
          channel.setMessageHandler(null)
        }
      }
      run {
        val channel =
            BasicMessageChannel<Any?>(
                binaryMessenger,
                "dev.flutter.pigeon.pigeon_example_package.ExampleHostApi.add$separatedMessageChannelSuffix",
                codec)
        if (api != null) {
          channel.setMessageHandler { message, reply ->
            val args = message as List<Any?>
            val aArg = args[0].let { num -> if (num is Int) num.toLong() else num as Long }
            val bArg = args[1].let { num -> if (num is Int) num.toLong() else num as Long }
            val wrapped: List<Any?> =
                try {
                  listOf<Any?>(api.add(aArg, bArg))
                } catch (exception: Throwable) {
                  wrapError(exception)
                }
            reply.reply(wrapped)
          }
        } else {
          channel.setMessageHandler(null)
        }
      }
      run {
        val channel =
            BasicMessageChannel<Any?>(
                binaryMessenger,
                "dev.flutter.pigeon.pigeon_example_package.ExampleHostApi.sendMessage$separatedMessageChannelSuffix",
                codec)
        if (api != null) {
          channel.setMessageHandler { message, reply ->
            val args = message as List<Any?>
            val messageArg = args[0] as MessageData
            api.sendMessage(messageArg) { result: Result<Boolean> ->
              val error = result.exceptionOrNull()
              if (error != null) {
                reply.reply(wrapError(error))
              } else {
                val data = result.getOrNull()
                reply.reply(wrapResult(data))
              }
            }
          }
        } else {
          channel.setMessageHandler(null)
        }
      }
    }
  }
}
/** Generated class from Pigeon that represents Flutter messages that can be called from Kotlin. */
class MessageFlutterApi(
    private val binaryMessenger: BinaryMessenger,
    private val messageChannelSuffix: String = ""
) {
  companion object {
    /** The codec used by MessageFlutterApi. */
    val codec: MessageCodec<Any?> by lazy { StandardMessageCodec() }
  }

  fun flutterMethod(aStringArg: String?, callback: (Result<String>) -> Unit) {
    val separatedMessageChannelSuffix =
        if (messageChannelSuffix.isNotEmpty()) ".$messageChannelSuffix" else ""
    val channelName =
        "dev.flutter.pigeon.pigeon_example_package.MessageFlutterApi.flutterMethod$separatedMessageChannelSuffix"
    val channel = BasicMessageChannel<Any?>(binaryMessenger, channelName, codec)
    channel.send(listOf(aStringArg)) {
      if (it is List<*>) {
        if (it.size > 1) {
          callback(Result.failure(FlutterError(it[0] as String, it[1] as String, it[2] as String?)))
        } else if (it[0] == null) {
          callback(
              Result.failure(
                  FlutterError(
                      "null-error",
                      "Flutter api returned null value for non-null return value.",
                      "")))
        } else {
          val output = it[0] as String
          callback(Result.success(output))
        }
      } else {
        callback(Result.failure(createConnectionError(channelName)))
      }
    }
  }
}
