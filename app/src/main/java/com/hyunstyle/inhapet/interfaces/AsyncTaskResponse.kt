package com.hyunstyle.inhapet.interfaces

import org.json.JSONArray

/**
 * Created by SangHyeon on 2018-04-09.
 */
interface AsyncTaskResponse {
    fun finished(output: JSONArray)
}