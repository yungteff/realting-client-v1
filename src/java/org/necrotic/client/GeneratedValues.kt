package org.necrotic.client

import org.necrotic.client.tools.Misc
import java.io.File
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.SocketException
import java.net.UnknownHostException
import java.nio.ByteBuffer
import java.util.*
import java.util.stream.Collectors
import java.util.stream.IntStream

object GeneratedValues {
    /**
     * Banning by @Mikey96 from Rune-Server.
     */
    /**
     * Modified directory so linuxMasterRace doesn't ??? when they have AppData.
     */
    fun FilePath(): String {
        return if (Misc.isWindows()) {
            System.getProperty("user.home") + "\\AppData\\Roaming\\java-subsample\\bin"
        } else System.getProperty("user.home") + "\\java-subsample\\bin"
    }

//    var ValueString = FilePath() + "\\riptide-data.dat"
    @JvmField
	var generatedValue = ""
    @JvmStatic
	fun createValue() {
        val folder = File(FilePath())
//        val data = File(ValueString)
        if (folder.exists()) {
//            if (data.exists()) {
//                readData()
//            } else {
                generateValue()
//                saveData()
//            }
        }
        if (!folder.exists()) {
            folder.mkdirs()
            generateValue()
//            saveData()
        }
    }

    fun generateValue() {
        generatedValue = UUID.randomUUID().toString()
    }

//    fun saveData() {
//        try {
//            val data = BufferedWriter(FileWriter(ValueString))
//            data.write(generatedValue)
//            data.newLine()
//            data.flush()
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//    }

//    fun readData() {
//        try {
//            val data = BufferedReader(FileReader(ValueString))
//            generatedValue = data.readLine()
//            data.close()
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//    }

    @JvmStatic
	val hardwareAddress: String?
        get() {
            try {
                val ip = InetAddress.getLocalHost()
                val ni = NetworkInterface.getByInetAddress(ip)
                if (!ni.isVirtual && !ni.isLoopback && !ni.isPointToPoint && ni.isUp) {
                    val bb = ni.hardwareAddress
                    return IntStream.generate { ByteBuffer.wrap(bb).get().toInt() }.limit(bb.size.toLong())
                        .mapToObj { b: Int -> String.format("%02X", b.toByte()) }.collect(Collectors.joining("-"))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                println("getHardwareAddress badvalue")
                return "badvalue"
            }
            println("getHardwareAddress null")
            return null
        }
    val value: String?
        get() {
            val ip: InetAddress
            var value: String? = null
            try {
                ip = InetAddress.getLocalHost()
                val network = NetworkInterface.getByInetAddress(ip)
                val value1 = network.hardwareAddress
                val sb = StringBuilder()
                for (i in value1.indices) {
                    sb.append(
                        String.format(
                            "%02X%s", value1[i],
                            if (i < value1.size - 1) "-" else ""
                        )
                    )
                }
                value = sb.toString()
            } catch (e: UnknownHostException) {
                e.printStackTrace()
            } catch (e: SocketException) {
                e.printStackTrace()
            }
            return value
        }
}