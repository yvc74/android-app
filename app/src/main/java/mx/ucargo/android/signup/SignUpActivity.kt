package mx.ucargo.android.signup

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.sign_up_activity.*
import mx.ucargo.android.R
import javax.inject.Inject

class SignUpActivity : AppCompatActivity() {
    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, SignUpActivity::class.java)
        }
    }

    @Inject
    lateinit var viewModel: SignUpViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState)

        setContentView(R.layout.sign_up_activity)

        signUpButton.setOnClickListener({
            viewModel.signUp(usernameEditText.text.toString(), passwordEditText.text.toString())
        })

        viewModel.isSignUp.observe(this, Observer {
            it?.let {
                if (it) {
                    finish()
                }
            }
        })
    }
}
