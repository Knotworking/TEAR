package com.knotworking.data

import android.content.Context
import android.content.SharedPreferences
import com.knotworking.data.api.*
import com.knotworking.data.db.TearDatabaseProvider
import com.knotworking.data.location.LocationRepositoryImpl
import com.knotworking.data.location.SharedLocationManager
import com.knotworking.data.route.LocalRouteDataSource
import com.knotworking.data.route.RouteDataSource
import com.knotworking.data.words.LocalWordDataSource
import com.knotworking.data.words.WordDataSource
import com.knotworking.data.words.WordRepositoryImpl
import com.knotworking.domain.api.ApiRepository
import com.knotworking.domain.example.WordRepository
import com.knotworking.domain.location.LocationRepository
import kotlinx.coroutines.GlobalScope
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val baseDataModule = module {
    single { TearDatabaseProvider.buildDatabase(context = get()) }
    single<SharedPreferences> {
        val context: Context = get()
        context.getSharedPreferences("USER_PREFS", Context.MODE_PRIVATE)
    }
}

val wordpressApiModule = module {
    single { provideWordpressApi(retrofit = get()) }
    single { provideRetrofit(okHttpClient = get(), url = "https://benhikes.eu/wp-json/") }
    single<TokenDataSource> { SharedPrefsTokenDataSource(sharedPrefs = get()) }
    single { AuthInterceptor(tokenDataSource = get()) }
    single { provideOkHttpClient(authInterceptor = get()) }
    single<ApiRepository> { ApiRepositoryImpl(wordpressApi = get(), tokenDataSource = get()) }
}

internal fun provideRetrofit(okHttpClient: OkHttpClient, url: String): Retrofit {
    return Retrofit.Builder()
        .baseUrl(url)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

internal fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
    val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    return OkHttpClient.Builder()
        .connectTimeout(30L, TimeUnit.SECONDS)
        .readTimeout(30L, TimeUnit.SECONDS)
        .addInterceptor(authInterceptor)
        .addInterceptor(httpLoggingInterceptor)
        .build()
}

internal fun provideWordpressApi(retrofit: Retrofit): WordpressApi =
    retrofit.create(WordpressApi::class.java)

val exampleDataModule = module {
    single<WordDataSource> { LocalWordDataSource() }
    single<WordRepository> { WordRepositoryImpl(wordDataSource = get()) }
}

val locationDataModule = module {
    single { SharedLocationManager(context = androidContext(), externalScope = GlobalScope) }
    single<LocationRepository> {
        LocationRepositoryImpl(
            sharedLocationManager = get(),
            routeDataSource = get()
        )
    }
}

val routeDataModule = module {

    // we might need to add the room lib to App-Module to allow this
//    fun provideKmMarkerDao(database: TearDatabase): KmMarkerDao {
//        return database.kmMarkerDao()
//    }

    single<RouteDataSource> { LocalRouteDataSource(database = get()) }
}