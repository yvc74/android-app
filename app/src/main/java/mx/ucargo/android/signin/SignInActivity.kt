package mx.ucargo.android.signin

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.EditorInfo
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.sign_in_activity.*
import mx.ucargo.android.R
import mx.ucargo.android.editprofile.EditProfileActivity
import mx.ucargo.android.orderlist.OrderListActivity
import mx.ucargo.android.entity.Unauthorized
import mx.ucargo.android.signup.SignUpActivity
import javax.inject.Inject

class SignInActivity : AppCompatActivity() {
    @Inject
    lateinit var signInViewModel: SignInViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()


        setContentView(R.layout.sign_in_activity)

        passwordEditText.setOnEditorActionListener { _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_SEND -> {
                    send.invoke(passwordEditText)
                    true
                }
                else -> false
            }
        }

        signUpButton.setOnClickListener({
            startActivity(SignUpActivity.newIntent(this))
        })

        sendButton.setOnClickListener(send)

        signInViewModel.isSignIn.observe(this, signInObserver)
        signInViewModel.formError.observe(this, formErrorObserver)
    }

    private val send: (View) -> Unit = {
        signInViewModel.send(usernameEditText.text.toString(), passwordEditText.text.toString())
    }

    private val signInObserver = Observer<Boolean> {
        it?.let {
            if (it) {
                finish()
                startActivity(EditProfileActivity.newIntent(this))
            }
        }
    }

    private val formErrorObserver = Observer<Throwable> {
        it?.let {
            if (it is Unauthorized) {
                usernameEditText.error = getString(R.string.sign_in_error_forbidden)
            } else {
                Snackbar.make(coordinatorLayout, it.message
                        ?: getString(R.string.error_generic), Snackbar.LENGTH_LONG).show()
            }
        }
    }
}
