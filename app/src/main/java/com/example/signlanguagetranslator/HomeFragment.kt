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

        var timetable_btn: Button = view.findViewById(R.id.translate_btn)
        var reservation_btn: Button = view.findViewById(R.id.dictionary_btn)
        var qr_btn: Button = view.findViewById(R.id.manual_btn)

        bundle.putString("nickname",arguments!!.getString("nickname"))

        timetable_btn.setOnClickListener {

        }

        reservation_btn.setOnClickListener {

        }

        qr_btn.setOnClickListener {

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