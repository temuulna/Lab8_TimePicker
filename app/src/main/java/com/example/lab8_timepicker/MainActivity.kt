package com.example.lab8_timepicker


import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    lateinit var ivQuestion: ImageView
    lateinit var ivAnswer: ImageView
    lateinit var ivAmPm: ImageView

    var question = 0   // 문제 번호 (0~4)
    var ampm = 0       // 0 = AM, 1 = PM

    // 문제 그림 / 정답 그림
    var drawableQuestion = intArrayOf(R.drawable.question1, R.drawable.question2, R.drawable.question3,
        R.drawable.question4, R.drawable.question5)
    var drawableAnswerAM = intArrayOf(R.drawable.answer11, R.drawable.answer12, R.drawable.answer13,
        R.drawable.answer14, R.drawable.answer15)
    var drawableAnswerPM = intArrayOf(R.drawable.answer21, R.drawable.answer22, R.drawable.answer23,
        R.drawable.answer24, R.drawable.answer25)

    // 각 문제의 정답 시간 (AM / PM-24시간제)
    var timeAnswerAM = arrayOf("9:15", "2:25", "10:35", "11:40", "3:10")
    var timeAnswerPM = arrayOf("21:15", "14:25", "22:35", "23:40", "15:10")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        ivQuestion = findViewById(R.id.question)
        ivAnswer = findViewById(R.id.answer)
        ivAmPm = findViewById(R.id.ampm)

        newQuestion()   // 첫 문제

        // 다시 시작하기 → 새 문제
        val buttonStart: Button = findViewById(R.id.buttonStart)
        buttonStart.setOnClickListener { newQuestion() }

        // 시계 불러오기 → TimePickerDialog 띄우기
        val buttonTime: Button = findViewById(R.id.buttonTime)
        buttonTime.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)     // 지금 시각으로 시작
            val minute = calendar.get(Calendar.MINUTE)

            // 시간을 고르고 확인을 누르면 실행
            val listener = TimePickerDialog.OnTimeSetListener { timePicker, selHour, selMinute ->
                val resultTime = "$selHour:$selMinute"
                // 문제의 AM/PM에 맞는 정답과 비교
                val correct = if (ampm == 0) timeAnswerAM[question] else timeAnswerPM[question]
                if (resultTime == correct) {
                    ivAnswer.isVisible = true
                    ivAnswer.setImageResource(
                        if (ampm == 0) drawableAnswerAM[question] else drawableAnswerPM[question])
                    Toast.makeText(applicationContext, "맞았습니다", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(applicationContext, "틀렸습니다", Toast.LENGTH_SHORT).show()
                }
            }

            // 마지막 true = 24시간제로 표시
            val timePickerDialog = TimePickerDialog(this, R.style.TimePickerStyle, listener, hour, minute, true)
            timePickerDialog.show()
        }
    }

    // 문제와 AM/PM을 무작위로 정하기
    fun newQuestion() {
        question = (Math.random() * 5).toInt()   // 0~4
        ampm = (Math.random() * 2).toInt()       // 0 또는 1
        ivQuestion.setImageResource(drawableQuestion[question])
        ivAmPm.setImageResource(if (ampm == 0) R.drawable.time_am else R.drawable.time_pm)
        ivAnswer.isVisible = false
    }
}