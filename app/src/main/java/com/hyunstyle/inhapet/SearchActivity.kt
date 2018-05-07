package com.hyunstyle.inhapet

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.SpannableStringBuilder
import android.widget.AutoCompleteTextView
import android.widget.Button
import com.hyunstyle.inhapet.adapter.AutoCompleteListAdapter
import com.hyunstyle.inhapet.model.Restaurant

import io.realm.Realm
import io.realm.RealmResults

/**
 * Created by SangHyeon on 2018-05-07.
 */

class SearchActivity : AppCompatActivity() {

    private var autoCompleteTextView: AutoCompleteTextView? = null
    private var searchButton: Button? = null
    private var realm: Realm? = null
    private lateinit var items: RealmResults<Restaurant>
    private var autoCompleteAdapter: AutoCompleteListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_search)

        autoCompleteTextView = findViewById(R.id.search_auto_complete_view)
        searchButton = findViewById(R.id.search_search_button)
        val backButton = findViewById<Button>(R.id.search_back_button)
        backButton.setOnClickListener { click -> onBackPressed() }
        searchButton!!.setOnClickListener{click -> kotlin.run {

        } }

        if (realm == null) {
            realm = Realm.getInstance(Config().get(this))
            findAll()
        } else {
            findAll()
        }
    }

    private fun findAll() {
        if(autoCompleteAdapter == null) {
            items = realm!!.where<Restaurant>(Restaurant::class.java).findAllAsync() // 비동기로 찾기 종료시 addChangeListener 1회 실행
        } else {
            autoCompleteTextView!!.setAdapter(autoCompleteAdapter)
        }

        items.addChangeListener {
            result -> kotlin.run {

            val nameList = java.util.ArrayList<String>()

            result.asSequence().mapTo(nameList, {it.name.toString()})
            val spanList = ArrayList<SpannableStringBuilder>()

            nameList.asSequence().mapTo(spanList, { SpannableStringBuilder(it) })
            autoCompleteAdapter = AutoCompleteListAdapter(this, R.layout.list_item_search, spanList)

            autoCompleteTextView!!.setAdapter(autoCompleteAdapter)
        }
        }
    }
}
