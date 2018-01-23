package mx.ucargo.android.signin

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.sign_in_activity.*
import mx.ucargo.android.R
import mx.ucargo.android.bidding.BiddingActivity
import mx.ucargo.android.signup.SignUpActivity
import javax.inject.Inject

class SignInActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModel: SignInViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState)

        setContentView(R.layout.sign_in_activity)

        signUpButton.setOnClickListener({
            startActivity(SignUpActivity.newIntent(this))
        })

        signInButton.setOnClickListener {
            viewModel.signIn(userNameEditText.text.toString(), passwordEditText.text.toString())
        }

        viewModel.isSignIn.observe(this, Observer {
            it?.let {
                if (it) {
                    finish()
                    startActivity(BiddingActivity.newIntent(this))
                }
            }
        })
    }
}
