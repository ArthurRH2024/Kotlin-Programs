package com.arhsystems.bannerintsersticial

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class MainActivity : AppCompatActivity() {

    private var mInterstitialAd: InterstitialAd? = null
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Configuracion y ejecucion de BANNER
        val mAdView: AdView = findViewById(R.id.adView)
        MobileAds.initialize(this);
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        // Carga del anuncio intersticial
        loadInterstitialAd()

    }

    private fun loadInterstitialAd() {
        val adRequest = AdRequest.Builder().build()

        // Ejemplo android:value = "ca-app-pub-3940256099942544/1033173712" INTERSTICIAL
        // Real    android:value = "ca-app-pub-XXXXXXXXXXXXXXXXXXXXXXXXXXX" INTERSTICIAL

        InterstitialAd.load(this, "ca-app-pub-3940256099942544/1033173712", adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    adError?.toString()?.let { Log.d(TAG, it) }
                    mInterstitialAd = null
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    Log.d(TAG, "Anuncio Cargado.")
                    mInterstitialAd = interstitialAd
                }
            })
    }

    private var isAdShowing = false

    override fun onBackPressed() {
        if (mInterstitialAd != null && !isAdShowing) {
            isAdShowing = true // Marca que el anuncio se está mostrando

            mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    isAdShowing = false // Permite que `onBackPressed` funcione normalmente después de cerrar
                    super@MainActivity.onBackPressed() // Cierra la app
                }

                override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                    isAdShowing = false // Permite el cierre
                    super@MainActivity.onBackPressed() // Cierra la app si falla el anuncio
                }

                override fun onAdShowedFullScreenContent() {
                    mInterstitialAd = null // Elimina referencia al anuncio
                    isAdShowing = true
                }
            }

            // Muestra el anuncio intersticial
            mInterstitialAd?.show(this)
        } else if (!isAdShowing) {
            // Si no hay anuncio cargado o el anuncio ya está mostrándose, cierra normalmente
            super.onBackPressed()
        } else {

            // Si el anuncio está en pantalla, asegúrate de que también se cierre al presionar "Atrás"
            isAdShowing = false
            super.onBackPressed()
        }
    }
}