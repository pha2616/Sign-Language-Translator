package com.example.signlanguagetranslator

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.*
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import com.example.signlanguagetranslator.utils.AudioWriterPCM
import com.naver.speech.clientapi.*
import java.lang.ref.WeakReference

class STTFragment: Fragment() {
    private var CLIENT_ID = "346755u9ep"
    private lateinit var naverRecognizer: NaverRecognizer
    private lateinit var handler: RecognitionHandler
    private lateinit var txtResult: TextView
    private lateinit var btnStart: Button
    private lateinit var result: String
    private lateinit var writer: AudioWriterPCM

    private fun handleMessage(msg: Message){
        when(msg.what){
            //음성 인식을 시작할 준비가 완료된 경ㅇ
            R.id.clientReady->{
                txtResult.setText("Connected")
                writer = AudioWriterPCM(Environment.getExternalStorageDirectory().absolutePath+"/NaverSpeechTest")
                writer.open("Test")
            }
            //현재 음성 인식이 진행되고 있는 경우
            R.id.audioRecording->{
                writer.write(msg.obj as ShortArray?)
            }
            //처리가 되고 있는 도중에 결과를 받은 경우
            R.id.partialResult->{
                result = msg.obj as String
                txtResult.setText(result)
            }
            //최종 인식이 완료되면 유사 결과를 모두 보여줌
            R.id.finalResult->{
                var speechRecognitionResult: SpeechRecognitionResult = msg.obj as SpeechRecognitionResult
                var results = speechRecognitionResult.results
                var strBuf = StringBuilder()
                for(result in results){
                    strBuf.append(result)
                    strBuf.append("\n")
                }
                result = strBuf.toString()
                txtResult.setText(result)
            }
            //인식 오류가 발생하는 경우
            R.id.recognitionError->{
                if(writer!=null){
                    writer.close()
                }
                result="Error code : " + msg.obj.toString()
                txtResult.setText(result)
                btnStart.setText(R.string.str_start)
                btnStart.isEnabled = true
            }
            //음성 인식 비활성화 상태인 경우
            R.id.clientInactive->{
                if(writer!=null){
                    writer.close()
                }
                btnStart.setText(R.string.str_start)
                btnStart.isEnabled=true
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = LayoutInflater.from(activity).inflate(R.layout.fragment_stt,container,false)
        txtResult = view.findViewById(R.id.txt_result)
        btnStart = view.findViewById(R.id.btn_start)
        handler = RecognitionHandler(this)
        naverRecognizer = NaverRecognizer(requireContext(), handler, CLIENT_ID)

        btnStart.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    var permissionResult = checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO)
                    if(permissionResult == PackageManager.PERMISSION_DENIED){
                        if(shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO)){
                            var dialog = AlertDialog.Builder(requireContext())
                            dialog.setTitle("권한이 필요합니다")
                                .setMessage("이 기능을 사용하기 위해서는 권한이 필요합니다. 계속 하시겠습니까?")
                                .setPositiveButton("네", DialogInterface.OnClickListener{
                                    dialog: DialogInterface?, which: Int ->
                                    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                                        requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO), 1000)
                                    }
                                })
                                .setNegativeButton("아니오", DialogInterface.OnClickListener {
                                        dialog: DialogInterface?, which: Int ->
                                    Toast.makeText(requireContext(), "기능을 취소했습니다.", Toast.LENGTH_SHORT).show()
                                }).create().show()
                        }
                        else{
                            requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO),1000)
                        }
                    }
                    //권한이 있는 경우
                    else{
                        //음성 인식 기능을 처리합니다.
                        if(!naverRecognizer.getSpeechRecognizer().isRunning()){
                            result = ""
                            txtResult.setText("Connecting...")
                            btnStart.setText(R.string.str_stop)
                            naverRecognizer.recognize()
                        }
                        else{
                            Log.d("Tag", "stop and wait Final Result")
                            btnStart.isEnabled = true
                            naverRecognizer.getSpeechRecognizer().stop()
                        }
                    }
                }
                else{
                    if(!naverRecognizer.getSpeechRecognizer().isRunning()){
                        result = ""
                        txtResult.setText("Connecting...")
                        btnStart.setText(R.string.str_stop)
                        naverRecognizer.recognize()
                    }
                    else{
                        Log.d("Tag","stop and wait Final Result")
                        btnStart.isEnabled = true
                        naverRecognizer.getSpeechRecognizer().stop()
                    }
                }
            }
        })

        return view
    }

    override fun onStart() {
        super.onStart()
        naverRecognizer.getSpeechRecognizer().initialize()
    }

    override fun onResume() {
        super.onResume()
        result = ""
        txtResult.setText("")
        btnStart.setText(R.string.str_start)
        btnStart.isEnabled = true
    }

    override fun onStop() {
        super.onStop()
        naverRecognizer.getSpeechRecognizer().release()
    }

    class RecognitionHandler(fragment: STTFragment): Handler() {
        private lateinit var mFragment: WeakReference<STTFragment>
        init {
            mFragment = WeakReference<STTFragment>(fragment)
        }

        override fun handleMessage(msg: Message) {
            var fragment = mFragment.get()
            if(fragment != null){
                fragment.handleMessage(msg)
            }
        }
    }

    class NaverRecognizer(context: Context, handler: Handler, clientId: String): SpeechRecognitionListener{
        private lateinit var mHandler: Handler
        private lateinit var mRecognizer: SpeechRecognizer

        init {
            mHandler = handler
            try{
                mRecognizer = SpeechRecognizer(context, clientId)
            } catch (e: SpeechRecognitionException){
                e.printStackTrace()
            }
            mRecognizer.setSpeechRecognitionListener(this)
        }

        fun getSpeechRecognizer(): SpeechRecognizer{
            return mRecognizer
        }

        fun recognize(){
            try{
                mRecognizer.recognize(SpeechConfig(SpeechConfig.LanguageType.KOREAN,
                SpeechConfig.EndPointDetectType.AUTO))
            } catch(e: SpeechRecognitionException){
                e.printStackTrace()
            }
        }

        override fun onResult(finalResult: SpeechRecognitionResult?) {
            var msg = Message.obtain(mHandler, R.id.finalResult, finalResult)
            msg.sendToTarget()
        }

        override fun onReady() {
            var msg = Message.obtain(mHandler, R.id.clientReady)
            msg.sendToTarget()
        }

        override fun onEndPointDetected() {
            Log.d("Point","Event occurred : EndPointDetected")
        }

        override fun onPartialResult(partialResult: String?) {
            var msg = Message.obtain(mHandler, R.id.partialResult, partialResult)
            msg.sendToTarget()
        }

        override fun onInactive() {
            var msg = Message.obtain(mHandler, R.id.clientInactive)
            msg.sendToTarget()
        }

        override fun onRecord(speech: ShortArray?) {
            var msg = Message.obtain(mHandler, R.id.audioRecording, speech)
            msg.sendToTarget()
        }

        override fun onError(errorCode: Int) {
            var msg = Message.obtain(mHandler, R.id.recognitionError, errorCode)
            msg.sendToTarget()
        }

        override fun onEndPointDetectTypeSelected(epdType: SpeechConfig.EndPointDetectType?) {
            var msg = Message.obtain(mHandler, R.id.endPointDetectTypeSelected, epdType)
            msg.sendToTarget()
        }

    }
}