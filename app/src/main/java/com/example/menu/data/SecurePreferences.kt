package com.example.menu.data

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import androidx.security.crypto.MasterKeys

interface SecurePreferences {

    var sharedPreferences: SharedPreferences?

    fun requireContext(): Context

    fun securePreferences(): SharedPreferences {
        val masterKeyAlias: MasterKey = MasterKey.Builder(requireContext())
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()

        if (sharedPreferences == null){
            sharedPreferences = EncryptedSharedPreferences.create(
                requireContext(),
                "secret_shared_prefs",
                masterKeyAlias,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        }

        return sharedPreferences as SharedPreferences
    }
}