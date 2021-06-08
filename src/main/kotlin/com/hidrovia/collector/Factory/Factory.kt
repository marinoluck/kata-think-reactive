package com.hidrovia.collector.Factory

import com.hidrovia.collector.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object Factory {
    val collector by lazy {
        Collector(sensorService, datFileReader)
    }

    private val sensorService: SensorService by lazy {
        HttpSensorsService(sensorsApi)
    }

    private val datFileReader: DatFileReader by lazy {
        DatFileReader()
    }

    private val sensorsApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://hidrovia-api-stg.herokuapp.com")
//            .baseUrl("http://localhost:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(createHTTPClient())
            .build()
            .create(SensorsAPI::class.java)
    }

    private fun createHTTPClient(): OkHttpClient =
        OkHttpClient.Builder()
            .callTimeout(Constants.DEFAULT_HTTP_TIMEOUT_IN_MILLIS, TimeUnit.MILLISECONDS)
            .build()

}

object Constants {
    const val DEFAULT_HTTP_TIMEOUT_IN_MILLIS = 60 * 1000L // 60 seconds
}