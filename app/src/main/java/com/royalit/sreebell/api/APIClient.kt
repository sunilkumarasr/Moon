package com.royalit.sreebell.api


import com.google.gson.GsonBuilder
import com.royalit.sreebell.utils.Constants.BASE_URL
import com.royalit.sreebell.utils.Constants.BASE_URL_PHOTOS
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit

/**
 * Created by sucharitha  on 8/01/2022.
 */
class APIClient {

    companion object {
        var loggingInterceptor = HttpLoggingInterceptor().also {
            it.level = HttpLoggingInterceptor.Level.BODY
        }
        //private const val BASE_URL = "https://sreebellkart.com/api/"


        //val basic = "Basic " + Base64.encodeToString(ConfigData.appBasicAuth.toByteArray(), Base64.NO_WRAP)
        private var okHttpClient =  OkHttpClient.Builder()
            /*.addInterceptor(Interceptor { chain ->
                val originRequest = chain.request()

                val newRquest = originRequest.newBuilder()
                    // .addHeader("Accept", "Application/JSON")
                    .header("Authorization", basic)
                    .header(
                        "x-api-key",
                        ConfigData.appXApiKey
                    )
                    .header("Cache-Control","no-cache")
                    //   .header("Access-Token", "${}")
                    .build()
                chain.proceed(newRquest)
            })*/
            .cache(null)
            .addInterceptor(loggingInterceptor)
            .callTimeout(2, TimeUnit.MINUTES)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()


        var retofit: Retrofit? = null

        val client: Retrofit
            get() {

                if (retofit == null) {
                    val gson = GsonBuilder()

                    retofit = Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .client(okHttpClient)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                }
                return retofit!!
            }



        /*fun client(): Retrofit {
            return Retrofit.Builder()
                //.baseUrl(ConfigData.BASE_URL)
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }*/

    }

    internal class NullOnEmptyConverterFactory : Converter.Factory() {
        fun responseBody(
            type: Type?,
            annotations: Array<Annotation?>?,
            retrofit: Retrofit
        ): Any {
            val delegate: Converter<ResponseBody, *> =
                retrofit.nextResponseBodyConverter<Any>(this, type, annotations)
            return object {
                fun convert(body: ResponseBody): Any? {
                    return if (body.contentLength() == 0L) null else delegate.convert(body)
                }
            }
        }
    }

}
