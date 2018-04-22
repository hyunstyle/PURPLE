package com.hyunstyle.inhapet.interfaces

import android.view.View
import android.widget.TextView

/**
 * Created by SangHyeon on 2018-04-18.
 */
interface SurveyResponse {
    fun clicked(position: Int, view: View, text: TextView)
}