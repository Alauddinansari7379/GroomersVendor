package com.groomers.groomersvendor.helper

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.BufferedSink
import okio.source
import java.io.File
import java.io.FileInputStream


class UploadRequestBody(
    private val file: File,
    private val contentType: String,
    private val context: Context
) : RequestBody() {

    override fun contentType(): MediaType? {
        return "$contentType/*".toMediaTypeOrNull()
    }

    override fun contentLength(): Long {
        return file.length()
    }

    override fun writeTo(sink: BufferedSink) {
        try {
            file.source().use { source ->
                sink.writeAll(source)
            }
        } catch (e: Exception) {
            Log.e("UploadRequestBody", "Error writing file to sink: ${e.message}", e)
        }
    }
}