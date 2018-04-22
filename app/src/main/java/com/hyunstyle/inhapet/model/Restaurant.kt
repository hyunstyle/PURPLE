package com.hyunstyle.inhapet.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by sh on 2018-04-04.
 */

open class Restaurant(@PrimaryKey var id: String? = null,
                      var isClose: Boolean = false,
                      var subCategory: Int = 0,
                      var famousMenu: String? = null,
                      var name: String? = null,
                      var phone: String? = null,
                      var location: String? = null,
                      var businessHours: String? = null,
                      var longitude: Float = 0.toFloat(),
                      var latitude: Float = 0.toFloat()) : RealmObject()
