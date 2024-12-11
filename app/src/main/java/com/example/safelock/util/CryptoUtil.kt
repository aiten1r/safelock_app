package com.example.safelock.util

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

object CryptoUtil {

    private const val KEY_ALIAS = "userKeyAlias"
    private const val TRANSFORMATION = "AES/GCM/NoPadding"
    private const val ANDROID_KEYSTORE = "AndroidKeyStore"

    // Получение ключа из Android Keystore или генерация нового
    private fun getKey(): SecretKey {
        val keyStore = java.security.KeyStore.getInstance(ANDROID_KEYSTORE).apply { load(null) }
        return keyStore.getKey(KEY_ALIAS, null) as? SecretKey ?: generateKey()
    }

    private fun generateKey(): SecretKey {
        val keyGenerator =
            KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE)
        keyGenerator.init(
            KeyGenParameterSpec.Builder(
                KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .build()
        )
        return keyGenerator.generateKey()
    }

    fun encrypt(data: String): Pair<ByteArray, ByteArray> {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, getKey())
        val iv = cipher.iv // Initialization Vector
        val encryptedData = cipher.doFinal(data.toByteArray())
        return Pair(iv, encryptedData)
    }

    fun decrypt(iv: ByteArray, data: ByteArray): String {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        val spec = GCMParameterSpec(128, iv)
        cipher.init(Cipher.DECRYPT_MODE, getKey(), spec)
        return String(cipher.doFinal(data))
    }
}