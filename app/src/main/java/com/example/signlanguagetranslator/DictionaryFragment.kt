package com.example.signlanguagetranslator

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ListView
import androidx.fragment.app.Fragment

class DictionaryFragment: Fragment() {
    private lateinit var listView: ListView
    private lateinit var editSearch: EditText
    private lateinit var searchAdapter: SearchAdapter
    private var data: MutableList<SearchList> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = LayoutInflater.from(activity).inflate(R.layout.fragment_dictionary,container,false)
        var searchArray = resources.getStringArray(R.array.city_array)
        editSearch = view!!.findViewById(R.id.edit_search)
        listView = view!!.findViewById(R.id.listview_search)
        insert(searchArray)
        searchAdapter = SearchAdapter(data, view!!.context)
        listView.adapter = searchAdapter
        editSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                var text: String = p0.toString()
                data.clear()
                for(i in searchArray.indices){
                    if(searchArray[i].contains(text)){
                        var searchlist = SearchList(searchArray[i])
                        data.add(searchlist)
                    }
                }
                searchAdapter = SearchAdapter(data, view!!.context)
                listView.adapter = searchAdapter
            }
        })

        return view
    }

    fun search(charText: String){
        //clear

        if(charText.length == 0){

        }
        else{

        }
    }

    fun insert(searchArray: Array<String>){
        for(element in searchArray){
            var text = element
            var searchlist = SearchList(text)
            data.add(searchlist)
        }
    }
}