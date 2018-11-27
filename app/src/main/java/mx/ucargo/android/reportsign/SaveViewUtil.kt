package mx.ucargo.android.reportsign


import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.os.Environment
import android.util.Log
import android.view.View
import java.io.*

object SaveViewUtil{

    private val rootDir = File(Environment.getExternalStorageDirectory().toString() + File.separator + "ucargo/" )

    fun takeScreenShot(view: View): Bitmap? {
        view.isDrawingCacheEnabled = true
        view.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_LOW
        view.buildDrawingCache()

        if (view.drawingCache == null) return null
        val snapshot = Bitmap.createBitmap(view.drawingCache)
        view.isDrawingCacheEnabled = false
        view.destroyDrawingCache()

        return snapshot
    }

    fun storeScreenshot(path: String, bitmap: Bitmap, onSuccess: ()->Unit, onFailure: () ->Unit) {
        var out: OutputStream? = null
        val imageFile = File(path)

        try {
            out = FileOutputStream(imageFile)
            // choose JPEG format
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
            out!!.flush()
        } catch (e: FileNotFoundException) {
            onFailure()
        } catch (e: IOException) {
            onFailure()
        } finally {

            try {
                if (out != null) {
                    out!!.close()
                    onSuccess()
                }

            } catch (exc: Exception) {
                onFailure()
            }

        }
    }

    fun deleteSign(path:String): Boolean {
        try {
            val myFile = File(path)
            if (myFile.exists()){
                myFile.delete()
                return true
            }
            else{
                return false
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            return false
        }
    }
}