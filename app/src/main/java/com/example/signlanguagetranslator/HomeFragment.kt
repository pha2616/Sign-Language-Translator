package com.example.signlanguagetranslator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction

class HomeFragment: Fragment() {
    var bundle: Bundle = Bundle()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = LayoutInflater.from(activity).inflate(R.layout.fragment_home,container,false)

        var translate_btn: Button = view.findViewById(R.id.translate_btn)
        var dictionary_btn: Button = view.findViewById(R.id.dictionary_btn)
        var manual_btn: Button = view.findViewById(R.id.manual_btn)

        translate_btn.setOnClickListener {
            replaceFragment(STTFragment())
        }

        dictionary_btn.setOnClickListener {
            replaceFragment(DictionaryFragment())
        }

        manual_btn.setOnClickListener {

        }

        return view
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentTransaction: FragmentTransaction = fragmentManager!!.beginTransaction()
        fragmentTransaction.replace(R.id.main_content, fragment)
        fragmentTransaction.addToBackStack(null)
        fragment.arguments = bundle
        fragmentTransaction.commit()
    }
}