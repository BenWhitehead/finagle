package com.twitter.finagle.mux.lease.exp

import com.twitter.util.NonFatal
import java.util.logging.Level

private[lease] object GarbageCollector {
  private val log = java.util.logging.Logger.getLogger("GarbageCollector")

  val forceNewGc: () => Unit = try {
    // This is a method present in Twitter's JVMs to force
    // a minor collection.
    val meth = classOf[System].getMethod("minorGc")
    log.log(Level.INFO, "Found System.minorGc")
    () => meth.invoke(null)
  } catch {
    case exc: NoSuchMethodException =>
      log.log(
        Level.INFO,
        "Failed to resolve System.minorGc; falling "+
          "back to full GC",
        exc)
      () => System.gc()
  }
}
