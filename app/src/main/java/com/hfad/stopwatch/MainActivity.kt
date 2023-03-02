package com.hfad.stopwatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.os.SystemClock
import android.widget.Button
import android.widget.Chronometer

class MainActivity : AppCompatActivity() {
    /*используем 3 свойства для управления секундомером:
    1) Свойство stopwatch содержит ссылку на Chronometer.
    2) Свойство running записывает, запущен ли секундомер. Мы установим значение true при нажатии кнопки Пуск
    и false, когда пользователь нажмет кнопку Пауза.
    3) Свойство offset используется для отображения правильного времени на секундомере,
    если секундомер был приостановлен и перезапущен. Без этого секундомер будет отображать неправильное время.*/
    lateinit var stopwatch: Chronometer
    var running = false
    var offset: Long = 0

    /*добавляем ключи (key) типа String для работы c Bundle.
     Используем три константы в качестве имен для любых значений, которые мы добавим в Bundle*/
    val OFFSET_KEY = "offset"
    val RUNNING_KEY = "running"
    val BASE_KEY = "base"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        stopwatch = findViewById<Chronometer>(R.id.stopwatch)//получаем ссылку на Chronometer

        /**Восстанавливаем предыдущее состояние*/
        if (savedInstanceState != null){
            offset = savedInstanceState.getLong(OFFSET_KEY)
            running = savedInstanceState.getBoolean(RUNNING_KEY)
            if (running){
                stopwatch.base = savedInstanceState.getLong(BASE_KEY)
                stopwatch.start()
            }else setBaseTime()
        }


        //кнопка startButton запускакет секундомер, если он не запущен
        val startButton = findViewById<Button>(R.id.start_button)
        startButton.setOnClickListener {
            if (!running) {
                setBaseTime()
                stopwatch.start()
                running = true
            }
        }
        //кнопка pauseButton ставит на паузу секундомер, есди он запущен
        var pauseButton = findViewById<Button>(R.id.button_pause)
        pauseButton.setOnClickListener {
            if (running) {
                saveOffset()//сохраняет время на секундомере
                stopwatch.stop()
                running = false
            }
        }
        //кнопка resetButton устанавливает Свойство offset = 0
        var resetButton = findViewById<Button>(R.id.button_reset)
        resetButton.setOnClickListener {
            offset = 0
            setBaseTime()//устанавливаем секундомер назад на 0
        }
    }

    /**  Сохраняем состояние базовых свойств*/
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putLong(OFFSET_KEY, offset)
        outState.putBoolean(RUNNING_KEY, running)
        outState.putLong(BASE_KEY, stopwatch.base)
        super.onSaveInstanceState(outState)
    }

    //обновляем stopwatch.base, учитывая любое смещение (offset)
    fun setBaseTime() {
        stopwatch.base = SystemClock.elapsedRealtime() - offset
    }

    //записываем смещение
    fun saveOffset() {
        offset = SystemClock.elapsedRealtime() - stopwatch.base
    }


}