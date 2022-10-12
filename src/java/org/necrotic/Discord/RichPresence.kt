//package org.necrotic.Discord
//
//import club.minnced.discord.rpc.DiscordRPC
//import club.minnced.discord.rpc.DiscordRichPresence
//import club.minnced.discord.rpc.DiscordEventHandlers
//import java.lang.Runnable
//import java.lang.InterruptedException
//
///**
// * @author Arham 4
// */
//class RichPresence {
//    private val CLIENT_ID = "652638363366457344"
//    private var lib: DiscordRPC? = null
//    private var presence: DiscordRichPresence? = null
//    fun initiate() {
//        lib = DiscordRPC.INSTANCE
//        val handlers = DiscordEventHandlers()
//        lib!!.Discord_Initialize(CLIENT_ID, handlers, true, "")
//        presence = DiscordRichPresence()
//        presence!!.startTimestamp = System.currentTimeMillis() / 1000
//        presence!!.largeImageKey = "k3"
//        presence!!.smallImageKey = "k3small"
//        updatePresence()
//        Thread({
//            while (!Thread.currentThread().isInterrupted) {
//                lib!!.Discord_RunCallbacks()
//                try {
//                    Thread.sleep(2000)
//                } catch (ignored: InterruptedException) {
//                }
//            }
//        }, "RPC-Callback-Handler").start()
//    }
//
//    fun presenceIsNull(): Boolean {
//        return presence == null
//    }
//
//    fun updateDetails(details: String?) {
//        presence!!.details = details
//        updatePresence()
//    }
//
//    fun updateState(state: String?) {
//        presence!!.state = state
//        updatePresence()
//    }
//
//    fun updateSmallImageKey(key: String?) {
//        presence!!.smallImageKey = key
//        updatePresence()
//    }
//
//    private fun updatePresence() {
//        lib!!.Discord_UpdatePresence(presence)
//    }
//}