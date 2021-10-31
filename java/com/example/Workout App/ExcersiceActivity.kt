package com.example.WorkoutApp

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_excersice.*
import kotlinx.android.synthetic.main.dialog_custom_back_confirmation.*
import java.util.*
import kotlin.collections.ArrayList

class ExcersiceActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private var restTimer: CountDownTimer? = null
    private var restProgress = 0
    private var exerciseTimer: CountDownTimer? = null
    private var exerciseProgress = 0
    private var exerciseList: ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = -1
    private var tts: TextToSpeech? = null
    private var player: MediaPlayer? = null
    private var exerciseAdapter: ExerciseStatusAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_excersice)

        setSupportActionBar(toolbar_exercise_activity)
        val actionbar = supportActionBar
        if (actionbar != null){
            actionbar.setDisplayHomeAsUpEnabled(true)
        }
        toolbar_exercise_activity.setNavigationOnClickListener {
            customDialogForBackButton()
        }
        tts = TextToSpeech(this, this)

        exerciseList = Constants.defaultExerciseList()
        setupRestView()

        setupExerciseStatusRecyclerView()
    }

    override fun onDestroy() {
        if (restTimer!=null){
            restTimer!!.cancel()
            restProgress = 0
        }

        if (exerciseTimer!=null){
            exerciseTimer!!.cancel()
            exerciseProgress = 0
        }

        if (tts!=null){
            tts!!.stop()
            tts!!.shutdown()
        }

        if (player!=null){
            player!!.stop()
        }
        super.onDestroy()
    }

    private fun setRestProgressBar(){
        progressbar.progress = restProgress
        restTimer = object : CountDownTimer(20000, 1000){

            override fun onTick(millisUntilFinished: Long) {
                restProgress++
                progressbar.progress = 20-restProgress
                tvTimer.text = (20-restProgress).toString()
            }

            override fun onFinish() {
                currentExercisePosition++
                exerciseList!![currentExercisePosition].setIsSelected(true)
                exerciseAdapter!!.notifyDataSetChanged()
                setupExerciseView()
            }
        }.start()
    }

    private fun setExerciseProgressBar(){
        progressbarExercise.progress = exerciseProgress
        exerciseTimer = object : CountDownTimer(30000, 1000){

            override fun onTick(millisUntilFinished: Long) {
                exerciseProgress++
                progressbarExercise.progress = 30-exerciseProgress
                tvExerciseTimer.text = (30-exerciseProgress).toString()
            }

            override fun onFinish() {
                if (currentExercisePosition < exerciseList?.size!!-1){
                    exerciseList!![currentExercisePosition].setIsSelected(false)
                    exerciseList!![currentExercisePosition].setIsCompleted(true)
                    exerciseAdapter!!.notifyDataSetChanged()
                    setupRestView()
                }else{
                    finish()
                    val intent = Intent(this@ExcersiceActivity, FinishActivity::class.java)
                    startActivity(intent)
                }
            }
        }.start()
    }

    private fun setupRestView(){
        try {
            player = MediaPlayer.create(applicationContext, R.raw.press_start)
            player!!.isLooping = false
            player!!.start()
        }catch (e: Exception){
            e.printStackTrace()
        }

        ll_rest_view.visibility = View.VISIBLE
        ll_exercise_view.visibility = View.GONE
        if (restTimer!=null){
            restTimer!!.cancel()
            restProgress = 0
        }
        tvNextExercise.text = exerciseList!![currentExercisePosition+1].getName()
        setRestProgressBar()
    }

    private fun setupExerciseView(){
        ll_rest_view.visibility = View.GONE
        ll_exercise_view.visibility = View.VISIBLE
        if (exerciseTimer!=null){
            exerciseTimer!!.cancel()
            exerciseProgress = 0
        }
        speakOut(exerciseList!![currentExercisePosition].getName()) // Text To Speech implementation

        setExerciseProgressBar()

        iv_image.setImageResource(exerciseList!![currentExercisePosition].getImage())
        tvExerciseName.text = exerciseList!![currentExercisePosition].getName()

    }

    override fun onInit(status: Int) { // Setup for the Text to Speech
        if (status == TextToSpeech.SUCCESS){
            val result = tts!!.setLanguage(Locale.US)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                Log.e("TTS", "The language specified is not supported!")
            }
        }else{
            Log.e("TTS", "Initialisation failed!")
        }
    }

    private fun speakOut(text: String){  // Method for the tts to speak
        tts!!.speak(text,TextToSpeech.QUEUE_FLUSH, null, "")
    }

    private fun setupExerciseStatusRecyclerView(){
        rv_exercise_status.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        exerciseAdapter = ExerciseStatusAdapter(exerciseList!!, this)
        rv_exercise_status.adapter = exerciseAdapter
    }

    private fun customDialogForBackButton(){
        val customDialog = Dialog(this)
        customDialog.setContentView(R.layout.dialog_custom_back_confirmation)
        customDialog.btn_cd_yes.setOnClickListener {
            finish()
            customDialog.dismiss()
        }
        customDialog.btn_cd_no.setOnClickListener {
            customDialog.dismiss()
        }
        customDialog.show()
    }
}