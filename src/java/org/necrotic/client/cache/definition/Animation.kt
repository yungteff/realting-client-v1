package org.necrotic.client.cache.definition

import org.necrotic.client.FrameReader
import org.necrotic.client.Signlink
import org.necrotic.client.cache.Archive
import org.necrotic.client.cache.DataType
import org.necrotic.client.io.ByteBuffer

class Animation internal constructor() {
    fun getFrameLength(i: Int): Int {
        if (i > delays.size) return 1
        var j = delays[i]
        if (j == 0) {
            val reader = FrameReader.forId(frameIDs[i])
            if (reader != null) {
                delays[i] = reader.displayLength
                j = delays[i]
            }
        }
        if (j == 0) j = 1
        return j
    }

    fun readValues(stream: ByteBuffer) {
        do {
            val i = stream.readUnsignedByte()
            if (i == 0) break
            when (i) {
                1 -> {
                    frameCount = stream.readUnsignedShort()
                    frameIDs = IntArray(frameCount)
                    frameIDs2 = IntArray(frameCount)
                    delays = IntArray(frameCount)
                    for (i_ in 0 until frameCount) {
                        frameIDs[i_] = stream.readInt()
                        frameIDs2[i_] = -1
                    }
                    for (i_ in 0 until frameCount) delays[i_] = stream.readUnsignedByte()
                }
                2 -> loopDelay = stream.readUnsignedShort()
                3 -> {
                    val k = stream.readUnsignedByte()
                    animationFlowControl = IntArray(k + 1)
                    for (l in 0 until k) animationFlowControl!![l] = stream.readUnsignedByte()
                    animationFlowControl!![k] = 0x98967f
                }
                4 -> oneSquareAnimation = true
                5 -> forcedPriority = stream.readUnsignedByte()
                6 -> leftHandItem = stream.readUnsignedShort()
                7 -> rightHandItem = stream.readUnsignedShort()
                8 -> frameStep = stream.readUnsignedByte()
                9 -> resetWhenWalk = stream.readUnsignedByte()
                10 -> priority = stream.readUnsignedByte()
                11 -> delayType = stream.readUnsignedByte()
                13 -> {
                    val len = stream.readUnsignedByte()
                    for (k in 0 until len) {
                        stream.read24BitInt()
                    }
                }
                else -> println("Unrecognized seq.dat config code: $i")
            }
        } while (true)
        if (frameCount == 0) {
            frameCount = 1
            frameIDs = IntArray(1)
            frameIDs[0] = -1
            frameIDs2 = IntArray(1)
            frameIDs2[0] = -1
            delays = IntArray(1)
            delays[0] = -1
        }
        if (resetWhenWalk == -1) resetWhenWalk = if (animationFlowControl != null) 2 else 0
        if (priority == -1) {
            if (animationFlowControl != null) {
                priority = 2
                return
            }
            priority = 0
        }
    }

    fun readValuesOsrs(stream: ByteBuffer) {
        do {
            val i = stream.readUnsignedByte()
            if (i == 0) break
            when (i) {
                1 -> {
                    frameCount = stream.readUnsignedShort()
                    frameIDs = IntArray(frameCount)
                    frameIDs2 = IntArray(frameCount)
                    delays = IntArray(frameCount)
                    for (i_ in 0 until frameCount) delays[i_] = stream.readUnsignedShort()
                    for (i_ in 0 until frameCount) {
                        frameIDs[i_] = stream.readUnsignedShort()
                        frameIDs2[i_] = -1
                    }
                    for (i_ in 0 until frameCount) {
                        frameIDs[i_] += stream.readUnsignedShort() shl 16
                        frameIDs2[i_] = -1
                    }
                }
                2 -> loopDelay = stream.readUnsignedShort()
                3 -> {
                    val k = stream.readUnsignedByte()
                    animationFlowControl = IntArray(k + 1)
                    for (l in 0 until k) animationFlowControl!![l] = stream.readUnsignedByte()
                    animationFlowControl!![k] = 0x98967f
                }
                4 -> oneSquareAnimation = true
                5 -> forcedPriority = stream.readUnsignedByte()
                6 -> leftHandItem = stream.readUnsignedShort()
                7 -> rightHandItem = stream.readUnsignedShort()
                8 -> frameStep = stream.readUnsignedByte()
                9 -> resetWhenWalk = stream.readUnsignedByte()
                10 -> priority = stream.readUnsignedByte()
                11 -> delayType = stream.readUnsignedByte()
                12 -> {
                    val var3 = stream.readUnsignedByte()
                    for (var4 in 0 until var3) {
                        stream.readUnsignedShort()
                    }
                    for (var4 in 0 until var3) {
                        stream.readUnsignedShort()
                    }
                }
                13 -> {
                    val len = stream.readUnsignedByte()
                    for (k in 0 until len) {
                        stream.read24BitInt()
                    }
                }
            }
            //else
            //System.out.println("Unrecognized seq.dat config code: "+i);
        } while (true)
        if (frameCount == 0) {
            frameCount = 1
            frameIDs = IntArray(1)
            frameIDs[0] = -1
            frameIDs2 = IntArray(1)
            frameIDs2[0] = -1
            delays = IntArray(1)
            delays[0] = -1
        }
        if (resetWhenWalk == -1) resetWhenWalk = if (animationFlowControl != null) 2 else 0
        if (priority == -1) {
            if (animationFlowControl != null) {
                priority = 2
                return
            }
            priority = 0
        }
    }

    var dataType: DataType? = null

    @JvmField
    var frameCount = 0
    lateinit var frameIDs: IntArray
    lateinit var frameIDs2: IntArray
    lateinit var delays: IntArray

    @JvmField
    var loopDelay: Int

    @JvmField
    var animationFlowControl: IntArray? = null

    @JvmField
    var oneSquareAnimation: Boolean

    @JvmField
    var forcedPriority: Int

    @JvmField
    var leftHandItem: Int

    @JvmField
    var rightHandItem: Int

    @JvmField
    var frameStep: Int

    @JvmField
    var resetWhenWalk: Int

    @JvmField
    var priority: Int

    @JvmField
    var delayType: Int

    init {
        loopDelay = -1
        oneSquareAnimation = false
        forcedPriority = 5
        leftHandItem = -1
        rightHandItem = -1
        frameStep = 99
        resetWhenWalk = -1
        priority = -1
        delayType = 2
    }

    companion object {
        @JvmStatic
        fun unpackConfig(streamLoader: Archive) {
            val stream1 = ByteBuffer(streamLoader["seq.dat"])
            val stream2 = ByteBuffer(Archive.ReadFile(Signlink.getCacheDirectory() + "/osrs/seq.dat"))
            val length1 = stream1.readUnsignedShort()
            val length2 = stream2.readUnsignedShort()
            if (cache == null) {
                cache = arrayOfNulls(length1 + length2 + 30000)
            }
            for (j in 0 until length1) {
                if (cache!![j] == null) {
                    cache!![j] = Animation()
                }
                cache!![j]!!.dataType = DataType.OSRS
                cache!![j]!!.readValues(stream1)
                AnimationCustom.do667(cache, j)
            }
            for (j in 0 until length2) {
                val index = j + 30000
                if (cache!![index] == null) {
                    cache!![index] = Animation()
                }
                cache!![index]!!.dataType = DataType.OSRS
                cache!![index]!!.readValuesOsrs(stream2)
                for (frameIndex in cache!![index]!!.frameIDs.indices) {
                    if (cache!![index]!!.frameIDs[frameIndex] > -1) {
                        cache!![index]!!.frameIDs[frameIndex] += 10000
                    }
                }
                for (frameIndex in cache!![index]!!.frameIDs2.indices) {
                    if (cache!![index]!!.frameIDs2[frameIndex] > -1) {
                        cache!![index]!!.frameIDs2[frameIndex] += 10000
                    }
                }
            }

            /*for (int index = 0; index < cache.length; index++) {
			if (cache[index] == null) {
				cache[index] = new Animation();
				cache[index].frameCount = 1;
				cache[index].frameIDs = new int[1];
				cache[index].frameIDs[0] = -1;
				cache[index].frameIDs2 = new int[1];
				cache[index].frameIDs2[0] = -1;
				cache[index].delays = new int[1];
				cache[index].delays[0] = -1;
			}
		}*/println("Loaded $length1 667 animations.")
            println("Loaded $length2 OSRS animations.")
        }

        @JvmStatic
        fun nullLoader() {
            cache = null
        }

        @JvmField
        var cache: Array<Animation?>? = null
        var anInt367 = 0
    }
}