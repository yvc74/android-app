package mx.ucargo.android.editprofile

import android.Manifest
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.telephony.PhoneNumberUtils
import android.view.View
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.features.ReturnMode
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.driver_profile_activity.*
import kotlinx.android.synthetic.main.toolbar.*
import mx.ucargo.android.R
import mx.ucargo.android.app.drawerMenuOnBackPressed
import mx.ucargo.android.app.setUpDrawer
import mx.ucargo.android.app.setUpMenuHeader
import java.io.File
import java.util.*
import javax.inject.Inject

class EditProfileActivity : AppCompatActivity(), PermissionListener {
    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, EditProfileActivity::class.java)
        }
    }

    @Inject
    lateinit var transferUtility: TransferUtility

    @Inject
    lateinit var editProfileViewModel: EditProfileViewModel



    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState)
        setContentView(R.layout.driver_profile_activity)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayUseLogoEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setUpDrawer(drawerLayout,5)

        changeImageProfileButoon.setOnClickListener(changeImageProfileButtonListener)

        editProfileViewModel.profile.observe(this, profileObserver)
        editProfileViewModel.s3Image.observe(this, s3ImageObserver)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private val profileObserver = Observer<Profile> {
        it?.let {
            setUpMenuHeader(it)
            nameTextView.text = it.name.toUpperCase()
            userEmailTextView.text = it.email
            usernameTextView.text = it.username.toUpperCase()
            driverScoreTextView.numStars = it.score
            driverScoreTextView.rating = it.score.toFloat()
            userPhoneNumberTextView.setText(PhoneNumberUtils.formatNumber(it.phone, Locale.getDefault().country))

            loadProfileImage(it.picture)
        }
    }

    private val changeImageProfileButtonListener: (View) -> Unit = {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(this)
                .check()
    }

    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
        launchCamera()
    }

    override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken?) {
        AlertDialog.Builder(this@EditProfileActivity)
                .setTitle(R.string.storage_permission_rationale_title)
                .setMessage(R.string.storage_permition_rationale_message)
                .setNegativeButton(android.R.string.cancel,
                        { dialog, _ ->
                            dialog.dismiss()
                            token?.cancelPermissionRequest()
                        })
                .setPositiveButton(android.R.string.ok,
                        { dialog, _ ->
                            dialog.dismiss()
                            token?.continuePermissionRequest()
                        })
                .setOnDismissListener({ token?.cancelPermissionRequest() })
                .show()
    }

    override fun onPermissionDenied(response: PermissionDeniedResponse?) {
        Snackbar.make(mainContainer!!, R.string.storage_permission_denied_message, Snackbar.LENGTH_LONG).show()
    }

    private fun launchCamera() {
        ImagePicker.create(this)
                .returnMode(ReturnMode.ALL) // set whether pick and / or camera action should return immediate result or not.
                .single() // single mode
                .showCamera(true) // show camera or not (true by default)
                .start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (!ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data)
            return
        }

        val image = ImagePicker.getFirstImageOrNull(data)
        editProfileViewModel.imageSelected(image.path)
    }

    private val s3ImageObserver = Observer<S3Image> {
        it?.let {
            val uploadObserver = transferUtility.upload(it.bucket, it.key, File(it.path))
            uploadObserver.setTransferListener(editProfileViewModel.transferListener)
        }
    }

    private fun loadProfileImage(url: String) {
        val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .placeholder(R.drawable.ic_usuario_default_image_profile)
                .circleCrop()

        Glide.with(this)
                .load(url)
                .apply(requestOptions)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        progressBar.setVisibility(View.GONE);
                        return false
                    }

                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        progressBar.setVisibility(View.GONE);
                        return false
                    }

                })
                .into(profileImageView)
    }

    override fun onBackPressed() {
        drawerMenuOnBackPressed {
            super.onBackPressed()
        }
    }
}
