package com.example.quoto.di

import com.example.quoto.api.AuthInterceptor
import com.example.quoto.api.NoteApi
import com.example.quoto.api.UserApi
import com.example.quoto.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.annotation.Signed
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofitBuilder(): Retrofit.Builder {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
    }

    @Singleton
    @Provides
    fun provideOkHttpClint(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(authInterceptor).build()
    }

    @Provides
    @Singleton
    fun provideApi(retrofitBuilder: Retrofit.Builder): UserApi {
        return retrofitBuilder.build().create(UserApi::class.java)
    }

//    fun provideAuthRetrofit(okHttpClient: OkHttpClient): Retrofit {
//        return Retrofit.Builder()
//            .addConverterFactory(GsonConverterFactory.create())
//            .client(okHttpClient)
//            .baseUrl(BASE_URL)
//            .build()
//    }

    @Signed
    @Provides
    fun providesNoteApi(retrofitBuilder: Retrofit.Builder, okHttpClient: OkHttpClient): NoteApi {
        return retrofitBuilder.client(okHttpClient).build().create(NoteApi::class.java)
    }

}