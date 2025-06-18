package com.asledgehammer.langpack.core.util

import java.io.*
import java.net.URL

/**
 * **ResourceUtil** houses utilities from Bukkit for global resource
 * management.
 *
 * @author Bukkit team (Modified by Jab)
 */
object ResourceUtil {

  /**
   * (Modified method from Bukkit to write global files)
   *
   * @param path The path inside the JAR file.
   * @param replace If set to true, write the resource to a file, even
   *     if one already exists.
   */
  fun saveResource(path: String, classLoader: ClassLoader?, replace: Boolean = false) {
    require(path.isNotEmpty()) { "ResourcePath cannot be empty." }
    var resourcePath2 = path
    resourcePath2 = resourcePath2.replace('\\', '/')
    val inputStream: InputStream = getResource(resourcePath2, classLoader) ?: return
    val outFile = File(resourcePath2)
    val lastIndex = resourcePath2.lastIndexOf('/')
    val outDir = File(resourcePath2.substring(0, if (lastIndex >= 0) lastIndex else 0))
    if (!outDir.exists()) outDir.mkdirs()
    try {
      if (!outFile.exists() || replace) {
        val out: OutputStream = FileOutputStream(outFile)
        val buf = ByteArray(1024)
        var len: Int
        while (inputStream.read(buf).also { len = it } > 0) {
          out.write(buf, 0, len)
        }
        out.close()
        inputStream.close()
      }
    } catch (ex: IOException) {
      System.err.println("Could not save ${outFile.name} to $outFile")
    }
  }

  /**
   * (Modified method from Bukkit to write global files)
   *
   * @param path The path inside the JAR file.
   * @return Returns the input stream for the resource in the JAR file.
   *     If the file isn't found, null is returned.
   */
  private fun getResource(path: String, loader: ClassLoader?): InputStream? {
    return try {
      val classLoader = loader ?: this::class.java.classLoader
      val url: URL = classLoader.getResource(path) ?: return null
      val connection = url.openConnection()
      connection.useCaches = false
      connection.getInputStream()
    } catch (ex: IOException) {
      null
    }
  }
}
