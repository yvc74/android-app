package mx.ucargo.android.editprofile

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import mx.ucargo.android.entity.Account
import mx.ucargo.android.usecase.GetAccountUseCase
import mx.ucargo.android.usecase.SendEditProfileUseCase
import java.lang.Exception

private const val bucket = "ucargo.developer.com"

class EditProfileViewModel(private val getAccountUseCase: GetAccountUseCase,
                           private val sendEditProfileUseCase: SendEditProfileUseCase) : ViewModel() {
    val profile = MutableLiveData<Profile>()
    val s3Image = MutableLiveData<S3Image>()
    val uploadProgress = MutableLiveData<Int>()
    val error = MutableLiveData<Exception>()

    init {
        getAccountUseCase.execute({
            profile.postValue(it.toProfile())
        })
    }

    fun imageSelected(path: String) {
        getAccountUseCase.execute({
            s3Image.postValue(S3Image(bucket, "${it.driverid}.png", path))
        })
    }

    val transferListener: TransferListener = object : TransferListener {
        override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {
            uploadProgress.postValue((bytesCurrent.toFloat() / bytesTotal.toFloat() * 100).toInt())
        }

        override fun onStateChanged(id: Int, state: TransferState?) {
            if (TransferState.COMPLETED == state) {
                getAccountUseCase.execute({
                    it.picture = it.driverid + ".png"

                    sendEditProfileUseCase.execute(it, {
                        profile.postValue(it.toProfile())
                    })
                })
            } else if (TransferState.FAILED == state) {
                error.postValue(Exception("Unknown error"))
            }
        }

        override fun onError(id: Int, ex: Exception?) {
            error.postValue(ex)
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val getAccountUseCase: GetAccountUseCase,
                  private val editProfileUseCase: SendEditProfileUseCase) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return EditProfileViewModel(getAccountUseCase, editProfileUseCase) as T
        }
    }
}

private fun Account.toProfile() = Profile(
        name = name,
        username = username,
        email = email,
        picture = picture,
        score = score,
        phone = phone
)
