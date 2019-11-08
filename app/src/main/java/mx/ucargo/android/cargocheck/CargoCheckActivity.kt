package mx.ucargo.android.cargocheck

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.CompoundButton
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.cargo_check_activity.*
import mx.ucargo.android.R

class CargoCheckActivity : AppCompatActivity() {

    companion object {
        const val ORDER_ID = "ORDER_ID"
        fun newIntent(context: Context, orderId: String): Intent {
            val intent = Intent(context, CargoCheckActivity::class.java)
            intent.putExtra(ORDER_ID, orderId)
            return intent

        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cargo_check_activity)

        withoutincidentscheckbox.setOnCheckedChangeListener(showLockButtonPicture)
        withincidentscheckbox.setOnCheckedChangeListener(showEditTextInicidents)
        continuebutton.setOnClickListener(senddetails)
    }

    private val showLockButtonPicture = CompoundButton.OnCheckedChangeListener { compoundButton: CompoundButton, stateCheckBox: Boolean ->

        if (compoundButton.isChecked) {
            withincidentscheckbox.isChecked = false;
            incidentEdittext.visibility = View.GONE;
        }
    }


    private val showEditTextInicidents = CompoundButton.OnCheckedChangeListener { compoundButton: CompoundButton, stateCheckBox: Boolean ->

        if (compoundButton.isChecked) {
            withoutincidentscheckbox.isChecked = false;
            incidentEdittext.visibility = View.VISIBLE;
        }
        else{
            incidentEdittext.visibility = View.GONE;
        }
    }

    private val senddetails : (View) -> Unit = {
        if(withoutincidentscheckbox.isChecked){
            finish()
        }
        else if(withincidentscheckbox.isChecked){
            finish()
        }
    }

}