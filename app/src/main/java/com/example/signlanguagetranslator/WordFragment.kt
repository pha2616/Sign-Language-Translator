package com.example.signlanguagetranslator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment

class WordFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = LayoutInflater.from(activity).inflate(R.layout.fragment_word,container,false)
        var word = arguments?.getString("word")
        var word_img = view.findViewById<ImageView>(R.id.word_img)
        var word_text = view.findViewById<TextView>(R.id.word_text)

        if(word=="파도"){
            word_img.setImageResource(R.drawable.pado)
            word_text.setText("오른 주먹의 1·5지를 펴서 구부려 입으로 약간 기울여 올린 다음, 손등이 위로 향하게 편 왼 손등을 오른 주먹을 펴면서 바닥으로 스치며 위로 올리다가 내린다.")
        }
        else{
            word_img.setImageResource(R.drawable.arrow)
            word_text.setText("왼 주먹의 5지를 펴서 바닥이 밖으로 향하게 세워 밖으로 내밀며 동시에 오른손의 1·5지 끝을 맞대고 그 등에서 안으로 당기며 1·5지를 편 다음, 주먹을 쥐고 1지를 펴서 끝이 밖으로 향하게 하여 밖으로 내민다.")
        }
        return view
    }
}