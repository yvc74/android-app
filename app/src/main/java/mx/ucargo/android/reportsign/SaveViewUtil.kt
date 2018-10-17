package mx.ucargo.android.reportsign


import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream

import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.os.Environment
import android.view.View

object SaveViewUtil{

    private val rootDir = File(Environment.getExternalStorageDirectory().toString() + File.separator + "ucargo/" )

    /** Save picture to file  */
    fun saveScreen(view: View,image_Name:String): Boolean {
        //determine if SDCARD is available
        if (Environment.MEDIA_MOUNTED != Environment.getExternalStorageState()) {
            return false
        }
        if (!rootDir.exists()) {
            rootDir.mkdir()
        }
        view.isDrawingCacheEnabled = true
        view.buildDrawingCache()
        var bitmap: Bitmap? = view.drawingCache
        try {
            bitmap!!.compress(CompressFormat.JPEG, 100, FileOutputStream(File(rootDir, image_Name)))
            return true
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            return false
        } finally {
            view.isDrawingCacheEnabled = false
            bitmap = null
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