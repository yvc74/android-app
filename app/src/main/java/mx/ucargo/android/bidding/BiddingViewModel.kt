package mx.ucargo.android.bidding


import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import mx.ucargo.android.data.retrofit.BiddingDataModel

class BiddingViewModel : ViewModel {
    val biddings = MutableLiveData<List<BiddingDataModel>>()

    constructor() {

        val biddings = ArrayList<BiddingDataModel>()
        biddings.add(BiddingDataModel("Mexico DF", "Veracruz","Importacion","2 Semanas"))
        biddings.add(BiddingDataModel("Mexico DF", "Veracruz","Importacion","2 Semanas"))
        biddings.add(BiddingDataModel("Mexico DF", "Veracruz","Importacion","2 Semanas"))
        biddings.add(BiddingDataModel("Mexico DF", "Veracruz","Importacion","2 Semanas"))
        biddings.add(BiddingDataModel("Mexico DF", "Veracruz","Importacion","2 Semanas"))
        biddings.add(BiddingDataModel("Mexico DF", "Veracruz","Importacion","2 Semanas"))
        biddings.add(BiddingDataModel("Mexico DF", "Veracruz","Importacion","2 Semanas"))
        biddings.add(BiddingDataModel("Mexico DF", "Veracruz","Importacion","2 Semanas"))
        biddings.add(BiddingDataModel("Mexico DF", "Veracruz","Importacion","2 Semanas"))
        biddings.add(BiddingDataModel("Mexico DF", "Veracruz","Importacion","2 Semanas"))


        this.biddings.value = biddings

    }


    class Factory : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return BiddingViewModel() as T
        }

    }
}
