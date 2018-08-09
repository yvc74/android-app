package mx.ucargo.android.data.retrofit

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class GmapsApiKeyInterceptor(private val apiKey: String) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val request = originalRequest.newBuilder()
                .header("key", apiKey)
                .build()
        return chain.proceed(request)
    }
}
