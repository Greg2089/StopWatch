package com.hfad.stopwatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import com.hfad.stopwatch.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    /*используем 3 свойства для управления секундомером:
    1) Свойство stopwatch содержит ссылку на Chronometer.
    2) Свойство running записывает, запущен ли секундомер. Мы установим значение true при нажатии кнопки Пуск
    и false, когда пользователь нажмет кнопку Пауза.
    3) Свойство offset используется для отображения правильного времени на секундомере,
    если секундомер был приостановлен и перезапущен. Без этого секундомер будет отображать неправильное время.*/

    //добавляем binding property
    private lateinit var binding: ActivityMainBinding

    var running = false
    var offset: Long = 0

    /*добавляем ключи (key) типа String для работы c Bundle.
     Используем три константы в качестве имен для любых значений, которые мы добавим в Bundle*/
    val OFFSET_KEY = "offset"
    val RUNNING_KEY = "running"
    val BASE_KEY = "base"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        /**Восстанавливаем предыдущее состояние*/
        if (savedInstanceState != null) {
            offset = savedInstanceState.getLong(OFFSET_KEY)
            running = savedInstanceState.getBoolean(RUNNING_KEY)
            if (running) {
                binding.stopwatch.base = savedInstanceState.getLong(BASE_KEY)
                binding.stopwatch.start()
            } else setBaseTime()
        }


        //кнопка startButton запускакет секундомер, если он не запущен

        binding.buttonStart.setOnClickListener {
            if (!running) {
                setBaseTime()
                binding.stopwatch.start()
                running = true
            }
        }
        //кнопка pauseButton ставит на паузу секундомер, есди он запущен

        binding.buttonPause.setOnClickListener {
            if (running) {
                saveOffset()//сохраняет время на секундомере
                binding.stopwatch.stop()
                running = false
            }
        }
        //кнопка resetButton устанавливает Свойство offset = 0

        binding.buttonReset.setOnClickListener {
            offset = 0
            setBaseTime()//устанавливаем секундомер назад на 0
        }
    }

    /**  Сохраняем состояние базовых свойств*/
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putLong(OFFSET_KEY, offset)
        outState.putBoolean(RUNNING_KEY, running)
        outState.putLong(BASE_KEY, binding.stopwatch.base)
        super.onSaveInstanceState(outState)
    }

    override fun onPause() {
        super.onPause()
        if (running) {
            saveOffset()
            binding.stopwatch.stop()
        }
    }

    override fun onResume() {
        super.onResume()
        if (running) {
            setBaseTime()
            binding.stopwatch.start()
            offset = 0
        }
    }

    //обновляем stopwatch.base, учитывая любое смещение (offset)
    fun setBaseTime() {
       binding.stopwatch.base = SystemClock.elapsedRealtime() - offset
    }

    //записываем смещение
    fun saveOffset() {
        offset = SystemClock.elapsedRealtime() - binding.stopwatch.base
    }


}