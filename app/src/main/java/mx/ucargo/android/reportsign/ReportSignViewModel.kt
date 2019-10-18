package mx.ucargo.android.reportsign

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.util.Log
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import mx.ucargo.android.editprofile.S3Image
import mx.ucargo.android.entity.EmptyEventPayload
import mx.ucargo.android.entity.Event
import mx.ucargo.android.orderdetails.Mappers
import mx.ucargo.android.orderdetails.OrderDetailsModel
import mx.ucargo.android.reportlock.PictureUrlPayload
import mx.ucargo.android.usecase.SendEventUseCase
import java.lang.Exception
import java.util.*
import java.util.logging.Logger


private const val bucket = "ucargo.developer.com"

class ReportSignViewModel(private val sendEventUseCase: SendEventUseCase) : ViewModel() {
    val orderStatus = MutableLiveData<OrderDetailsModel.Status>()
    val error = MutableLiveData<Throwable>()
    val s3Image = MutableLiveData<S3Image>()
    val uploadProgress = MutableLiveData<Int>()
    val errorImage = MutableLiveData<Exception>()
    val flagToSendEvent = MutableLiveData<Boolean>()

    fun beginSign(orderId: String,imageKey : String) {
        sendEventUseCase.execute(orderId, Event.ReportSign, PictureUrlPayload("https://s3.us-east-2.amazonaws.com/ucargo.developer.com/$imageKey"), {
            Log.d("com.ucargo.event", "Succesfully Send")
            orderStatus.postValue(Mappers.mapOrderDetailsModelStatus(it))
        }, {
            Log.d("com.ucargo.event", "Wrong Send")
            error.postValue(it)
        })
    }


    fun imageSelected(path: String,imageKey: String) {
        s3Image.postValue(S3Image(bucket, imageKey, path))
    }

    val transferListener: TransferListener = object : TransferListener {
        override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {
            uploadProgress.postValue((bytesCurrent.toFloat() / bytesTotal.toFloat() * 100).toInt())
        }

        override fun onStateChanged(id: Int, state: TransferState?) {
            if (TransferState.COMPLETED == state) {
                flagToSendEvent.postValue(true)
            } else if (TransferState.FAILED == state) {
                errorImage.postValue(Exception("Unknown error"))
            }
        }

        override fun onError(id: Int, ex: Exception?) {
            errorImage.postValue(ex)
        }
    }





    @Suppress("UNCHECKED_CAST")
    class Factory(private val sendEventUseCase: SendEventUseCase) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ReportSignViewModel(sendEventUseCase) as T
        }
    }
}

