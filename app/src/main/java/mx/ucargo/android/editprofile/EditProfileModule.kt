package mx.ucargo.android.editprofile

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3Client
import dagger.Module
import dagger.Provides
import mx.ucargo.android.R
import mx.ucargo.android.R.id.navView
import mx.ucargo.android.usecase.GetAccountUseCase
import mx.ucargo.android.usecase.SendEditProfileUseCase
import mx.ucargo.android.usecase.SignOutUseCase

@Module
class EditProfileModule {
    @Provides
    fun provideEditProfileViewModelFactory(getAccountUseCase: GetAccountUseCase, sendEditProfileUseCase: SendEditProfileUseCase) =
            EditProfileViewModel.Factory(getAccountUseCase, sendEditProfileUseCase)

    @Provides
    fun provideEditProfileViewModel(activity: EditProfileActivity, factory: EditProfileViewModel.Factory): EditProfileViewModel =
            ViewModelProviders.of(activity, factory).get(EditProfileViewModel::class.java)


    @Provides
    fun provideCognitoCachingCredentialsProvider(context: Context) = CognitoCachingCredentialsProvider(
            context,
            "us-east-2:e52a46f4-3f0d-4ad9-a727-09c292a6920a",
            Regions.US_EAST_2 // Region
    )

    @Provides
    fun provideAmazonS3Client(credentialsProvider: CognitoCachingCredentialsProvider): AmazonS3Client {
        val s3Client = AmazonS3Client(credentialsProvider)
        s3Client.setRegion(Region.getRegion(Regions.US_EAST_2));

        return s3Client
    }

    @Provides
    fun provideTransferUtility(s3Client: AmazonS3Client, context: Context) = TransferUtility.builder()
            .s3Client(s3Client)
            .context(context)
            .build()


}
