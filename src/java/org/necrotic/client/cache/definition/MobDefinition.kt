package org.necrotic.client.cache.definition

import org.necrotic.Configuration
import org.necrotic.client.Client
import org.necrotic.client.FrameReader
import org.necrotic.client.List
import org.necrotic.client.Signlink
import org.necrotic.client.cache.Archive
import org.necrotic.client.cache.Archive.Companion.ReadFile
import org.necrotic.client.cache.DataType
import org.necrotic.client.graphics.rsinterface.Settings
import org.necrotic.client.io.ByteBuffer
import org.necrotic.client.tools.FieldGenerator
import org.necrotic.client.tools.TempWriter
import org.necrotic.client.world.Model
import java.util.stream.IntStream

class MobDefinition private constructor() {
    fun copy(id: Int) {
        val other = Companion[id]
        changedModelColours = other!!.changedModelColours!!.clone()
        childrenIDs = other.childrenIDs!!.clone()
        combatLevel = other.combatLevel
        configChild = other.configChild
        degreesToTurn = other.degreesToTurn
        description = other.description
        dialogueModels = other.dialogueModels
        disableRightClick = false
        drawYellowDotOnMap = other.drawYellowDotOnMap
        headIcon = other.headIcon
        modelLightning = other.modelLightning
        modelShadowing = other.modelShadowing
        npcModels = other.npcModels!!.clone()
        originalModelColours = other.originalModelColours!!.clone()
        standAnimation = other.standAnimation
        varBitChild = other.varBitChild
        visibilityOrRendering = other.visibilityOrRendering
        walkAnimation = other.walkAnimation
        walkingBackwardsAnimation = other.walkingBackwardsAnimation
        walkLeftAnimation = other.walkLeftAnimation
        walkRightAnimation = other.walkRightAnimation
    }

    @JvmField
    var actions: Array<String?>? = null

    @JvmField
    var adjustVertextPointsXOrY: Int

    @JvmField
    var adjustVertextPointZ: Int

    @JvmField
    var changedModelColours: IntArray? = null

    @JvmField
    var childrenIDs: IntArray? = null

    @JvmField
    var combatLevel: Int

    @JvmField
    var configChild: Int

    @JvmField
    var degreesToTurn: Int

    lateinit var description: ByteArray
    private var dialogueModels: IntArray? = null

    @JvmField
    var disableRightClick: Boolean

    @JvmField
    var drawYellowDotOnMap: Boolean

    @JvmField
    var headIcon: Int

    @JvmField
    var modelLightning = 0

    @JvmField
    var modelShadowing = 0

    @JvmField
    var name: String? = null

    @JvmField
    var npcModels: IntArray? = null

    @JvmField
    var npcSizeInSquares: Byte

    @JvmField
    var originalModelColours: IntArray? = null

    @JvmField
    var standAnimation: Int

    @JvmField
    var id: Int

    @JvmField
    var varBitChild: Int

    @JvmField
    var visibilityOrRendering: Boolean

    @JvmField
    var walkAnimation: Int

    @JvmField
    var walkingBackwardsAnimation: Int

    @JvmField
    var walkLeftAnimation: Int

    @JvmField
    var walkRightAnimation: Int

    @JvmField
    var dataType: DataType? = null

    init {
        walkRightAnimation = -1
        varBitChild = -1
        walkingBackwardsAnimation = -1
        configChild = -1
        combatLevel = -1
        walkAnimation = -1
        npcSizeInSquares = 1
        headIcon = -1
        standAnimation = -1
        id = -1
        degreesToTurn = 32
        walkLeftAnimation = -1
        disableRightClick = true
        adjustVertextPointZ = 128
        drawYellowDotOnMap = true
        adjustVertextPointsXOrY = 128
        visibilityOrRendering = false
    }

    fun method160(): Model? {
        if (childrenIDs != null) {
            val definition = method161()
            return definition?.method160()
        }
        if (dialogueModels == null) {
            return null
        }
        var flag1 = false
        for (i in dialogueModels!!.indices) {
            if (!Model.method463(dialogueModels!![i], dataType)) {
                flag1 = true
            }
        }
        if (flag1) {
            return null
        }
        val aclass30_sub2_sub4_sub6s = arrayOfNulls<Model>(
            dialogueModels!!.size
        )
        for (j in dialogueModels!!.indices) {
            aclass30_sub2_sub4_sub6s[j] = Model.fetchModel(dialogueModels!![j], dataType)
        }
        val model: Model?
        model = if (aclass30_sub2_sub4_sub6s.size == 1) {
            aclass30_sub2_sub4_sub6s[0]
        } else {
            Model(aclass30_sub2_sub4_sub6s.size, aclass30_sub2_sub4_sub6s)
        }
        if (originalModelColours != null) {
            for (k in originalModelColours!!.indices) {
                model!!.method476(originalModelColours!![k], changedModelColours!![k])
            }
        }
        return model
    }

    fun method161(): MobDefinition? {
        var j = -1
        try {
            if (varBitChild != -1) {
                val varBit = VarBit.getCache(dataType)[varBitChild]
                val k = varBit.configId
                val l = varBit.lsb
                val i1 = varBit.msb
                val j1 = Client.anIntArray1232[i1 - l]
                // System.out.println("k: " + k + " l: " + l);
                j = clientInstance!!.variousSettings[k] shr l and j1
            } else if (configChild != -1) {
                j = clientInstance!!.variousSettings[configChild]
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return if (j < 0 || j >= childrenIDs!!.size || childrenIDs!![j] == -1) {
            null
        } else {
            Companion[childrenIDs!![j]]
        }
    }

    fun method164(j: Int, frame: Int, ai: IntArray?, nextFrame: Int, cycle1: Int, cycle2: Int): Model? {
        if (childrenIDs != null) {
            val entityDef = method161()
            return entityDef?.method164(j, frame, ai, nextFrame, cycle1, cycle2)
        }
        var model = mruNodes!!.insertFromCache(id.toLong()) as Model?
        if (model == null) {
            var flag = false
            for (i1 in npcModels!!.indices) {
                if (!Model.method463(npcModels!![i1], dataType)) {
                    flag = true
                }
            }
            if (flag) {
                return null
            }
            val aclass30_sub2_sub4_sub6s = arrayOfNulls<Model>(
                npcModels!!.size
            )
            for (j1 in npcModels!!.indices) {
                aclass30_sub2_sub4_sub6s[j1] = Model.fetchModel(npcModels!![j1], dataType)
            }
            model = if (aclass30_sub2_sub4_sub6s.size == 1) {
                aclass30_sub2_sub4_sub6s[0]
            } else {
                Model(aclass30_sub2_sub4_sub6s.size, aclass30_sub2_sub4_sub6s)
            }
            if (originalModelColours != null) {
                for (k1 in originalModelColours!!.indices) {
                    model?.method476(originalModelColours!![k1], changedModelColours!![k1])
                }
            }
            model?.createBones()
            model?.light(84 + modelLightning, 1000 + modelShadowing, -90, -580, -90, true)
            mruNodes!!.removeFromCache(model, id.toLong())
        }
        val model_1 = Model.EMPTY_MODEL
        model_1.method464(model, FrameReader.isNullFrame(frame) and FrameReader.isNullFrame(j))
        /*
		if (frame != -1 && j != -1) {
			model_1.method471(ai, j, frame);
		} else if (frame != -1 && !Configuration.TWEENING_ENABLED) {
			model_1.applyTransform(frame);
		} else if (frame != -1 && nextFrame != -1 && Configuration.TWEENING_ENABLED) {
			model_1.interpolateFrames(frame, nextFrame, cycle1, cycle2);
		}*/if (frame != -1 && j != -1) model_1.method471(
            ai, j, frame
        ) else if (frame != -1 && nextFrame != -1 && Settings.get(
                Settings.Data.TWEENING
            )
        ) model_1.interpolateFrames(
            frame, nextFrame, cycle1, cycle2
        ) else if (frame != -1) model_1.applyTransform(frame)
        if (adjustVertextPointsXOrY != 128 || adjustVertextPointZ != 128) {
            model_1.scaleT(adjustVertextPointsXOrY, adjustVertextPointsXOrY, adjustVertextPointZ)
        }
        model_1.method466()
        model_1.face_skin = null
        model_1.vertex_skin = null
        if (npcSizeInSquares.toInt() == 1) {
            model_1.renders_inside_tile = true
        }
        return model_1
    }

    private fun doReadValues(buffer: ByteBuffer?) {
        if (dataType == DataType.OSRS) {
            readValuesOsrs(buffer)
        } else {
            readValues(buffer)
        }
    }

    private fun readValues(buffer: ByteBuffer?) {
        do {
            val opcode = buffer!!.readUnsignedByte()
            if (opcode == 0) {
                return
            }
            if (opcode == 1) {
                val j = buffer.readUnsignedByte()
                npcModels = IntArray(j)
                for (j1 in 0 until j) {
                    npcModels!![j1] = buffer.readUnsignedShort()
                }
            } else if (opcode == 2) {
                name = buffer.readString()
            } else if (opcode == 3) {
                description = buffer.readBytes()
            } else if (opcode == 12) {
                npcSizeInSquares = buffer.readtSignedByte()
            } else if (opcode == 13) {
                standAnimation = buffer.readUnsignedShort()
            } else if (opcode == 14) {
                walkAnimation = buffer.readUnsignedShort()
            } else if (opcode == 17) {
                walkAnimation = buffer.readUnsignedShort()
                walkingBackwardsAnimation = buffer.readUnsignedShort()
                walkLeftAnimation = buffer.readUnsignedShort()
                walkRightAnimation = buffer.readUnsignedShort()
                if (walkAnimation == 65535) {
                    walkAnimation = -1
                }
                if (walkingBackwardsAnimation == 65535) {
                    walkingBackwardsAnimation = -1
                }
                if (walkLeftAnimation == 65535) {
                    walkLeftAnimation = -1
                }
                if (walkRightAnimation == 65535) {
                    walkRightAnimation = -1
                }
            } else if (opcode >= 30 && opcode < 40) {
                if (actions == null) {
                    actions = arrayOfNulls(5)
                }
                actions!![opcode - 30] = buffer.readString()
                if (actions!![opcode - 30].equals("hidden", ignoreCase = true)) {
                    actions!![opcode - 30] = null
                }
            } else if (opcode == 40) {
                val length = buffer.readUnsignedByte()
                changedModelColours = IntArray(length)
                originalModelColours = IntArray(length)
                for (i in 0 until length) {
                    originalModelColours!![i] = buffer.readUnsignedShort()
                    changedModelColours!![i] = buffer.readUnsignedShort()
                }
            } else if (opcode == 60) {
                val length = buffer.readUnsignedByte()
                dialogueModels = IntArray(length)
                for (i in 0 until length) {
                    dialogueModels!![i] = buffer.readUnsignedShort()
                }
            } else if (opcode == 90) {
                buffer.readUnsignedShort()
            } else if (opcode == 91) {
                buffer.readUnsignedShort()
            } else if (opcode == 92) {
                buffer.readUnsignedShort()
            } else if (opcode == 93) {
                drawYellowDotOnMap = false
            } else if (opcode == 95) {
                combatLevel = buffer.readUnsignedShort()
            } else if (opcode == 97) {
                adjustVertextPointsXOrY = buffer.readUnsignedShort()
            } else if (opcode == 98) {
                adjustVertextPointZ = buffer.readUnsignedShort()
            } else if (opcode == 99) {
                visibilityOrRendering = true
            } else if (opcode == 100) {
                modelLightning = buffer.readtSignedByte().toInt()
            } else if (opcode == 101) {
                modelShadowing = buffer.readtSignedByte() * 5
            } else if (opcode == 102) {
                headIcon = buffer.readUnsignedShort()
            } else if (opcode == 103) {
                degreesToTurn = buffer.readUnsignedShort()
            } else if (opcode == 106) {
                varBitChild = buffer.readUnsignedShort()
                if (varBitChild == 65535) {
                    varBitChild = -1
                }
                configChild = buffer.readUnsignedShort()
                if (configChild == 65535) {
                    configChild = -1
                }
                val length = buffer.readUnsignedByte()
                childrenIDs = IntArray(length + 1)
                for (i in 0..length) {
                    childrenIDs!![i] = buffer.readUnsignedShort()
                    if (childrenIDs!![i] == 65535) {
                        childrenIDs!![i] = -1
                    }
                }
            } else if (opcode == 107) {
                disableRightClick = false
            }
        } while (true)
    }

    private fun readValuesOsrs(buffer: ByteBuffer?) {
        do {
            val opcode = buffer!!.readUnsignedByte()
            if (opcode == 0) {
                return
            }
            if (opcode == 1) {
                val j = buffer.readUnsignedByte()
                npcModels = IntArray(j)
                for (j1 in 0 until j) {
                    npcModels!![j1] = buffer.readUnsignedShort()
                }
            } else if (opcode == 2) {
                name = buffer.readString()
            } else if (opcode == 3) {
                description = buffer.readBytes()
            } else if (opcode == 12) {
                npcSizeInSquares = buffer.readtSignedByte()
            } else if (opcode == 13) {
                standAnimation = buffer.readUnsignedShort()
            } else if (opcode == 14) {
                walkAnimation = buffer.readUnsignedShort()
            } else if (opcode == 17) {
                walkAnimation = buffer.readUnsignedShort()
                walkingBackwardsAnimation = buffer.readUnsignedShort()
                walkLeftAnimation = buffer.readUnsignedShort()
                walkRightAnimation = buffer.readUnsignedShort()
                if (walkAnimation == 65535) {
                    walkAnimation = -1
                }
                if (walkingBackwardsAnimation == 65535) {
                    walkingBackwardsAnimation = -1
                }
                if (walkLeftAnimation == 65535) {
                    walkLeftAnimation = -1
                }
                if (walkRightAnimation == 65535) {
                    walkRightAnimation = -1
                }
            } else if (opcode >= 30 && opcode < 40) {
                if (actions == null) {
                    actions = arrayOfNulls(5)
                }
                actions!![opcode - 30] = buffer.readString()
                if (actions!![opcode - 30].equals("hidden", ignoreCase = true)) {
                    actions!![opcode - 30] = null
                }
            } else if (opcode == 40) {
                val length = buffer.readUnsignedByte()
                changedModelColours = IntArray(length)
                originalModelColours = IntArray(length)
                for (i in 0 until length) {
                    originalModelColours!![i] = buffer.readUnsignedShort()
                    changedModelColours!![i] = buffer.readUnsignedShort()
                }
            } else if (opcode == 60) {
                val length = buffer.readUnsignedByte()
                dialogueModels = IntArray(length)
                for (i in 0 until length) {
                    dialogueModels!![i] = buffer.readUnsignedShort()
                }
            } else if (opcode == 90) {
                buffer.readUnsignedShort()
            } else if (opcode == 91) {
                buffer.readUnsignedShort()
            } else if (opcode == 92) {
                buffer.readUnsignedShort()
            } else if (opcode == 93) {
                drawYellowDotOnMap = false
            } else if (opcode == 95) {
                combatLevel = buffer.readUnsignedShort()
            } else if (opcode == 97) {
                adjustVertextPointsXOrY = buffer.readUnsignedShort()
            } else if (opcode == 98) {
                adjustVertextPointZ = buffer.readUnsignedShort()
            } else if (opcode == 99) {
                visibilityOrRendering = true
            } else if (opcode == 100) {
                modelLightning = buffer.readtSignedByte().toInt()
            } else if (opcode == 101) {
                modelShadowing = buffer.readtSignedByte() * 5
            } else if (opcode == 102) {
                headIcon = buffer.readUnsignedShort()
            } else if (opcode == 103) {
                degreesToTurn = buffer.readUnsignedShort()
            } else if (opcode == 106) {
                varBitChild = buffer.readUnsignedShort()
                if (varBitChild == 65535) {
                    varBitChild = -1
                }
                configChild = buffer.readUnsignedShort()
                if (configChild == 65535) {
                    configChild = -1
                }
                val length = buffer.readUnsignedByte()
                childrenIDs = IntArray(length + 1)
                for (i in 0..length) {
                    childrenIDs!![i] = buffer.readUnsignedShort()
                    if (childrenIDs!![i] == 65535) {
                        childrenIDs!![i] = -1
                    }
                }
            } else if (opcode == 107) {
                disableRightClick = false
            } else if (opcode == 118) {
                buffer.readUnsignedShort()
                buffer.readUnsignedShort()
                buffer.readUnsignedShort()
                val length = buffer.readUnsignedByte()
                for (index in 0..length) {
                    buffer.readUnsignedShort()
                }
            } else if (opcode == 249) {
                val length = buffer.readUnsignedByte()
                for (i in 0 until length) {
                    val isString = buffer.readUnsignedByte() == 1
                    buffer.read24BitInt()
                    if (isString) {
                        buffer.readString()
                    } else {
                        buffer.readInt()
                    }
                }
            } else {
                //System.err.println("Unknown npc opcode: " + opcode);
            }
        } while (true)
    }

    companion object {
        private var cache: Array<MobDefinition?>? = null
        private var cacheIndex = 0

        @JvmField
        var clientInstance: Client? = null

        @JvmField
        var mruNodes: List? = List(80)
        private var buffer: Array<ByteBuffer?>? = arrayOfNulls(2)
        private var streamIndices: Array<IntArray?>? = arrayOfNulls(2)

        @JvmStatic
        fun load(archive: Archive) {
            var index = 0
            buffer!![index] = ByteBuffer(archive["npc.dat"])
            var stream = ByteBuffer(archive["npc.idx"])
            var totalNPCs = stream.readUnsignedShort()
            streamIndices!![index] = IntArray(totalNPCs)
            var position = 2
            for (i in 0 until totalNPCs) {
                streamIndices!![index]!![i] = position
                position += stream.readUnsignedShort()
            }
            println("Loaded $totalNPCs 667 npcs.")
            index = 1
            buffer!![index] = ByteBuffer(ReadFile(Signlink.getCacheDirectory() + "/osrs/npc.dat"))
            stream = ByteBuffer(ReadFile(Signlink.getCacheDirectory() + "/osrs/npc.idx"))
            totalNPCs = stream.readUnsignedShort()
            streamIndices!![index] = IntArray(totalNPCs)
            position = 2
            for (i in 0 until totalNPCs) {
                streamIndices!![index]!![i] = position
                position += stream.readUnsignedShort()
            }
            cache = arrayOfNulls(20)
            for (i in 0..19) {
                cache!![i] = MobDefinition()
            }
            println("Loaded $totalNPCs OSRS npcs.")
            if (Configuration.developerMode) {
                val writer = TempWriter("npc_ids")
                IntStream.range(0, 100000).forEach { id: Int ->
                    try {
                        val def = Companion[id]
                        writer.writeLine(def!!.id.toString() + " " + def.name)
                    } catch (e: Exception) {
                    }
                }
                writer.close()
                val writer2 = TempWriter("npc_fields")
                val generator = FieldGenerator { line: String? -> writer2.writeLine(line) }
                IntStream.range(0, 100000).forEach { id: Int ->
                    try {
                        val definition = Companion[id]
                        generator.add(definition!!.name, id, definition.dataType == DataType.OSRS)
                    } catch (e: Exception) {
                    }
                }
                writer2.close()
            }
        }

        @JvmStatic
        operator fun get(id: Int): MobDefinition? {
            var id = id
            for (i in 0..19) {
                if (cache!![i]!!.id == id) {
                    return cache!![i]
                }
            }
            cacheIndex = (cacheIndex + 1) % 20
            cache!![cacheIndex] = MobDefinition()
            val definition = cache!![cacheIndex]
            var index = 0
            if (id >= 30000) {
                id -= 30000
                index = 1
                definition!!.dataType = DataType.OSRS
            } else {
                definition!!.dataType = DataType.STANDARD
            }
            buffer!![index]!!.position = streamIndices!![index]!![id]
            definition.id = id + if (index == 1) 30000 else 0
            definition.doReadValues(buffer!![index])
            if (definition.dataType == DataType.STANDARD) {
                MobDefinitionCustom.do667(definition)
            } else {
                if (definition.standAnimation != -1) definition.standAnimation += 30000
                if (definition.walkAnimation != -1) definition.walkAnimation += 30000
                if (definition.walkingBackwardsAnimation != -1) definition.walkingBackwardsAnimation += 30000
                if (definition.walkLeftAnimation != -1) definition.walkLeftAnimation += 30000
                if (definition.walkRightAnimation != -1) definition.walkRightAnimation += 30000
            }
            return definition
        }

        @JvmStatic
        fun nullify() {
            mruNodes = null
            streamIndices = null
            cache = null
            buffer = null
        }

        @JvmStatic
        fun printDefinitionsForId(mobId: Int) {
            /*Print out Grain*/
            val dump = Companion[mobId]
            if (dump!!.name != null) {
                println("Dumping: " + dump.name)
            } else {
                println("MobDefinition.get($mobId).name == null")
            }
            println("combatlevel: " + dump.combatLevel)
            println("id: " + dump.id)
            if (dump.npcModels != null) {
                for (i in dump.npcModels!!.indices) {
                    println("npcModels[" + i + "]: " + dump.npcModels!![i])
                }
            }
            if (dump.actions != null) {
                for (i in dump.actions!!.indices) {
                    println("Action[" + i + "]: " + dump.actions!![i])
                }
            }
            println("degreesToTurn: " + dump.degreesToTurn)
            println("headIcon: " + dump.headIcon)
            println("npcSizeInSquares: " + dump.npcSizeInSquares)
            println("standAnimation: " + dump.standAnimation)
            println("walkAnimation: " + dump.walkAnimation)
            println("walkingBackwardsAnimation: " + dump.walkingBackwardsAnimation)
            println("walkLeftAnimation: " + dump.walkLeftAnimation)
            println("walkRightAnimation: " + dump.walkRightAnimation)
            if (dump.originalModelColours != null) {
                for (i in dump.originalModelColours!!.indices) {
                    println("originalModelColours[" + i + "]: " + dump.originalModelColours!![i])
                }
            }
            if (dump.changedModelColours != null) {
                for (i in dump.changedModelColours!!.indices) {
                    println("changedModelColours[" + i + "]: " + dump.changedModelColours!![i])
                }
            }
            if (dump.childrenIDs != null) {
                for (i in dump.childrenIDs!!.indices) {
                    println("childrenIDs[" + i + "]: " + dump.changedModelColours!![i])
                }
            }
            println("configChild: " + dump.configChild)
            println("varBitChild: " + dump.varBitChild)
            println("modelLightning: " + dump.modelLightning)
            println("modelShadowing: " + dump.modelShadowing)
            println("drawYellowDotOnMap: " + dump.drawYellowDotOnMap)
            println("disableRightClick: " + dump.disableRightClick)
            println("visibilityOrRendering: " + dump.visibilityOrRendering)
        }
    }
}