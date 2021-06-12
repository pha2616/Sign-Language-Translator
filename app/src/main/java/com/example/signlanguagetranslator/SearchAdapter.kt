package com.example.signlanguagetranslator

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView

class SearchAdapter(list: List<SearchList>, context: Context): BaseAdapter() {
    private lateinit var context: Context
    private lateinit var list: List<SearchList>
    private lateinit var inflate: LayoutInflater
    private var bundle = Bundle()

    init {
        this.list = list
        this.context = context
        this.inflate = LayoutInflater.from(context)
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View? {
        var convertView = inflate.inflate(R.layout.search_list, null)
        var item_btn = convertView!!.findViewById<Button>(R.id.search_item)
        item_btn.text = list[p0].search
        item_btn.setOnClickListener {
            bundle.putString("word",item_btn.text.toString())
            replaceFragment(WordFragment())
        }
        return convertView
    }

    override fun getItem(p0: Int): Any? {
        return null
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return list.size
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentTransaction: FragmentTransaction = (context as AppCompatActivity).supportFragmentManager!!.beginTransaction()
        fragmentTransaction.replace(R.id.main_content, fragment)
        fragment.arguments = bundle
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}
