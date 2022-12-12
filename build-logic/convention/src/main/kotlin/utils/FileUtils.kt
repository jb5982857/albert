package utils

import org.codehaus.groovy.runtime.DefaultGroovyMethodsSupport
import java.io.ByteArrayOutputStream
import java.io.File

fun File.eachFileRecurse(callback: (File) -> Unit) {
    val files = listFiles()
    if (files != null) {
        val var5 = files.size
        for (var6 in 0 until var5) {
            val file = files[var6]
            if (file.isDirectory) {
                file.eachFileRecurse(callback)
            } else {
                callback(file)
            }
        }
    }
}

fun File.getBytes(): ByteArray? {
    val `is` = inputStream()
    val answer = ByteArrayOutputStream()
    val byteBuffer = ByteArray(8192)
    var nbByteRead: Int
    try {
        while (`is`.read(byteBuffer).also { nbByteRead = it } != -1) {
            answer.write(byteBuffer, 0, nbByteRead)
        }
    } finally {
        DefaultGroovyMethodsSupport.closeWithWarning(`is`)
    }
    return answer.toByteArray()
}