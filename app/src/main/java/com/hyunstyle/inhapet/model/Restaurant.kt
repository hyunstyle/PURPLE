package com.hyunstyle.inhapet.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by sh on 2018-04-04.
 */

open class Restaurant(@PrimaryKey var id: Int? = null,
                      var isClose: Int = 1,
                      var subCategory: Int = 0,
                      var subCategoryTwo: Int = 0,
                      var soloOrGroups: Int = 0,
                      var averagePrice: Int = 0,
                      var famousMenu: String? = null,
                      var name: String? = null,
                      var phone: String? = null,
                      var location: String? = null,
                      var businessHours: String? = null,
                      var longitude: Double = 0.toDouble(),
                      var latitude: Double = 0.toDouble(),
                      var image: String? = null,
                      var likeNum: Int = 0) : RealmObject()
