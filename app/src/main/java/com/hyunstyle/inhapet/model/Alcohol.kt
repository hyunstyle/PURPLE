package com.hyunstyle.inhapet.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by SangHyeon on 2018-05-07.
 */

open class Alcohol(@PrimaryKey var id: Int? = null,
                      var isClose: Boolean = false,
                   var ton: Int = 0,
                   var ttw: Int = 0,
                   var tth: Int = 0,
                   var sno: Int = 0,
                   var snt: Int = 0,
                   var mon: Int = 0,
                   var mtw: Int = 0,
                   var mth: Int = 0,
                      var famousMenu: String? = null,
                      var name: String? = null,
                      var phone: String? = null,
                      var location: String? = null,
                      var businessHours: String? = null,
                      var longitude: Double = 0.toDouble(),
                      var latitude: Double = 0.toDouble(),
                   var image: String? = null,
                   var likeNum: String? = null) : RealmObject()