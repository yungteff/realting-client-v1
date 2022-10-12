package org.necrotic.client.cache

import org.necrotic.client.bzip.BZIPDecompressor
import org.necrotic.client.io.ByteBuffer
import java.io.BufferedInputStream
import java.io.DataInputStream
import java.io.File
import java.io.FileInputStream
import java.util.*

class Archive(data: ByteArray) {
    private val payload: ByteArray
    private val anIntArray730: IntArray
    private val indiceTable: IntArray
    private val dataSize: Int
    private val hashTable: IntArray
    private val isCompressed: Boolean
    private val sizeTable: IntArray

    init {
        var buffer = ByteBuffer(data)
        val decompressed = buffer.readTribyte()
        val compressed = buffer.readTribyte()
        isCompressed = decompressed != compressed
        if (isCompressed) {
            val tmp = ByteArray(decompressed)
            BZIPDecompressor.decompress(tmp, decompressed, data, compressed, 6)
            payload = tmp
            buffer = ByteBuffer(payload)
        } else {
            payload = data
        }
        dataSize = buffer.readUnsignedShort()
        hashTable = IntArray(dataSize)
        sizeTable = IntArray(dataSize)
        anIntArray730 = IntArray(dataSize)
        indiceTable = IntArray(dataSize)
        var position = buffer.position + dataSize * 10
        for (i in 0 until dataSize) {
            hashTable[i] = buffer.readIntLittleEndian()
            sizeTable[i] = buffer.readTribyte()
            anIntArray730[i] = buffer.readTribyte()
            indiceTable[i] = position
            position += anIntArray730[i]
        }
    }

    operator fun get(name: String): ByteArray? {
        var data: ByteArray? = null
        val hash = getHash(name)
        for (i in 0 until dataSize) {
            if (hashTable[i] == hash) {
                data = ByteArray(sizeTable[i])
                if (!isCompressed) {
                    BZIPDecompressor.decompress(data, sizeTable[i], payload, anIntArray730[i], indiceTable[i])
                } else {
                    System.arraycopy(payload, indiceTable[i], data, 0, sizeTable[i])
                }
                return data
            }
        }
        return null
    }

    private fun getHash(name: String): Int {
        var hashName = name
        var hash = 0
        hashName = hashName.uppercase(Locale.getDefault())
        for (element in hashName) {
            hash = hash * 61 + element.code - 32
        }
        return hash
    }

    companion object {
        @JvmStatic
        fun ReadFile(s: String): ByteArray? {
            try {
                val file = File(s)
                val i = file.length().toInt()
                val abyte0 = ByteArray(i)
                val datainputstream = DataInputStream(BufferedInputStream(FileInputStream(s)))
                datainputstream.readFully(abyte0, 0, i)
                datainputstream.close()
                //TotalRead++;
                return abyte0
            } catch (exception: Exception) {
            }
            return null
        }
    }
}