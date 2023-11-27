package com.zxkj.libhttp

import android.annotation.SuppressLint
import android.content.Context
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.zxkj.libhttp.config.IBaseUrlConfig
import com.zxkj.libhttp.dns.OkHttpDNS
import com.zxkj.libhttp.event.OkHttpEventListener
import com.zxkj.libhttp.interceptor.HTTPDNSInterceptor
import com.zxkj.libhttp.interceptor.NoNetworkInterceptor
import okhttp3.Cache
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitClient(private var context: Context?) {
    private lateinit var iBaseUrlConfig: IBaseUrlConfig

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var retrofitClient: RetrofitClient? = null
        private const val DEFAULT_TIME_OUT = 15
        private val sRetrofitManager: MutableMap<Int, Retrofit> = HashMap()
        fun getInstance(context: Context?): RetrofitClient {
            if (retrofitClient == null) {
                synchronized(RetrofitClient::class.java) {
                    retrofitClient = RetrofitClient(context)
                    return retrofitClient as RetrofitClient
                }
            }
            return retrofitClient as RetrofitClient
        }

    }

    fun setBaseUrlConfig(iBaseUrlConfig: IBaseUrlConfig): RetrofitClient {
        this.iBaseUrlConfig = iBaseUrlConfig
        return this
    }

    private fun createOkHttpClient(optimization: Boolean): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger.DEFAULT)
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        if (optimization) {
            return OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIME_OUT.toLong(), TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIME_OUT.toLong(), TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIME_OUT.toLong(), TimeUnit.SECONDS)
                .connectionPool(ConnectionPool(8, 10, TimeUnit.SECONDS)) //添加这两行代码
//                .sslSocketFactory(TrustAllCerts.createSSLSocketFactory()!!, TrustAllCerts())
//                .hostnameVerifier(TrustAllCerts.TrustAllHostnameVerifier())
//            .protocols(Collections.unmodifiableList(listOf(Protocol.HTTP_1_1)))
                //alibaba dns优化
                .dns(OkHttpDNS.get(context))
                .addInterceptor(HTTPDNSInterceptor(context)) //不建议用这种方式，因为大型APP 域名会比较多，假设HTTPS 的话，证书会认证失败
                .cache(context?.cacheDir?.let { Cache(it, 50 * 1024 * 1024L) })//缓存目录
                .addInterceptor(NoNetworkInterceptor(context))//无网拦截器
                .addInterceptor(httpLoggingInterceptor)
                .eventListenerFactory(OkHttpEventListener.FACTORY)
                .build()
        } else {
            //无优化版本
            return OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIME_OUT.toLong(), TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIME_OUT.toLong(), TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIME_OUT.toLong(), TimeUnit.SECONDS)
                .connectionPool(ConnectionPool(8, 10, TimeUnit.SECONDS)) //添加这两行代码
//                .sslSocketFactory(TrustAllCerts.createSSLSocketFactory()!!, TrustAllCerts())
//                .hostnameVerifier(TrustAllCerts.TrustAllHostnameVerifier())
                .addInterceptor(httpLoggingInterceptor)
                .eventListenerFactory(OkHttpEventListener.FACTORY)
                .build()
        }
    }

    fun <T> getDefault(interfaceServer: Class<T>?, hostType: Int): T {
        val retrofit = sRetrofitManager[hostType]
        return if (retrofit != null) {
            retrofit.create(interfaceServer!!)
        } else create(interfaceServer, hostType)
    }

    /**
     *
     */
    private fun <T> create(interfaceServer: Class<T>?, hostType: Int): T {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(iBaseUrlConfig.getBaseUrl(hostType))
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(createOkHttpClient(true))
            .build()
        sRetrofitManager[hostType] = retrofit
        if (interfaceServer == null) {
            throw RuntimeException("The Api InterfaceServer is null!")
        }
        return retrofit.create(interfaceServer)
    }
}