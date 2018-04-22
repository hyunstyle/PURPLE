package com.hyunstyle.inhapet

import android.content.Context
import android.util.Log
import io.realm.RealmConfiguration

/**
 * Created by sh on 2018-04-06.
 */
open class Config {
    fun get(context: Context): RealmConfiguration {
        return RealmConfiguration.Builder()
                .name(context.getString(R.string.data_folder))
                .encryptionKey(getKey(context).toByteArray())
                .schemaVersion(1L)
                .deleteRealmIfMigrationNeeded()
                .build()
    }
    private fun getKey(context: Context) : String {
        val sharedPrefs = context.getSharedPreferences(
                context.getString(R.string.first_execution), Context.MODE_PRIVATE)
        return sharedPrefs.getString(context.getString(R.string.ks), null)
    }


}