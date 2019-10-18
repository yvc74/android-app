package mx.ucargo.android.reportlock

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import mx.ucargo.android.editprofile.S3Image
import mx.ucargo.android.entity.Event
import mx.ucargo.android.orderdetails.Mappers
import mx.ucargo.android.orderdetails.OrderDetailsModel
import mx.ucargo.android.usecase.SendEventUseCase
import java.lang.Exception
import java.util.*

private const val bucket = "ucargo.developer.com"

class ReportLockViewModel (private val sendEventUseCase: SendEventUseCase) : ViewModel() {
    val orderStatus = MutableLiveData<OrderDetailsModel.Status>()
    val error = MutableLiveData<Throwable>()
    val s3Image = MutableLiveData<S3Image>()
    val uploadProgress = MutableLiveData<Int>()
    val flagToSendEvent = MutableLiveData<Boolean>()
    val errorImage = MutableLiveData<Exception>()


    fun reportLock(orderId: String,imageKey : String) {
        sendEventUseCase.execute(orderId, Event.ReportLock, PictureUrlPayload("https://s3.us-east-2.amazonaws.com/ucargo.developer.com/$imageKey"), {
            orderStatus.postValue(Mappers.mapOrderDetailsModelStatus(it))
        }, {
            error.postValue(it)
        })
    }


    fun imageSelected(path: String) {
        s3Image.postValue(S3Image(bucket, "${UUID.randomUUID()}.png", path))
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
            return ReportLockViewModel(sendEventUseCase) as T
        }
    }
}
