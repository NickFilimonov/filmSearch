package com.practicum.filmsearch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.properties.Delegates.notNull

class MainActivity : AppCompatActivity() {

//    companion object {
//        const val SEARCH_USER_INPUT = "SEARCH_USER_INPUT"
//    }
//
//    private var searchQuery by notNull<String>()
//
//    override fun onSaveInstanceState(outState: Bundle) {
//        searchQuery = queryInput.text.toString()
//        super.onSaveInstanceState(outState)
//        outState.putString(SEARCH_USER_INPUT, searchQuery)
//    }
    //    private fun clearButtonVisibility(s: CharSequence?): Int {
//        return if (s.isNullOrEmpty()) {
//            View.GONE
//        } else {
//            View.VISIBLE
//        }
//    }


    private val IMDbBaseUrl = "https://imdb-api.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(IMDbBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    private val IMDbService = retrofit.create(IMDbApi::class.java)

    private lateinit var searchButton: Button
    private lateinit var queryInput: EditText
    private lateinit var rvSearchFilm: RecyclerView
    private lateinit var placeholderMessage: TextView

    private val films = ArrayList<Film>() // инициализируем список, который будет хранить найденные города
    private val adapter = FilmsApapter() // инициализируем адаптер списка

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        queryInput = findViewById(R.id.edit_text)
        searchButton = findViewById(R.id.search_button)
        rvSearchFilm = findViewById(R.id.rv_search_film)
        placeholderMessage = findViewById(R.id.error_text)

        adapter.films = films // связываем адаптер со списком, который будет хранить найденные города
        rvSearchFilm.adapter = adapter // связываем адаптер и recyclerview, для отображения ответа сервера на экране

        searchButton.setOnClickListener {
            if (queryInput.text.isNotEmpty()) { // проверяем, чтобы edittext не был пустым
                IMDbService.getFilms(queryInput.text.toString()).enqueue(object : Callback<FilmsResponce> { // вызываем метод getFilms() у IMDbService, и передаем туда текст из edittext
                    override fun onResponse(call: Call<FilmsResponce>,                                      // метод enqueue() передает наш запрос на сервер (метод retrofit)
                                            response: Response<FilmsResponce>) { // метод onResponse() вызывается, когда сервер дал ответе(response) на запрос
                        if (response.code() == 200) { // code() вызывает код http-статуса, 200 - успех, запрос корректен, сервер вернул ответ
                            films.clear() // clear() отчищвет recyclerview от предъидущего списка, без этой строчки отображение нового списка не происходит
                            if (response.body()?.results?.isNotEmpty() == true) { // если ответ(response)  в виде объекта, который указали в Call (body() возвращает) не пустой
                                films.addAll(response.body()?.results!!) // добавляем все найденные фильмы в спсиок addAll() для отображения на экране
                                adapter.notifyDataSetChanged() // уведомляем адаптер об изменении набора данных, перерисовавается весь набор, это не оптимально
                            }
                            if (films.isEmpty()) { // если список фильмов, отображающий результат поиска пуст..
                                showMessage(getString(R.string.nothing_found), "") // передаем в showMessage() параметр text - "ничего не найдено"
                            } else {
                                showMessage("", "") // если сервер вернул какие-то элементы (нашел фильмы), ничего не передаем в showMessage()
                            }
                        } else {
                            showMessage(getString(R.string.something_went_wrong), response.code().toString()) // передаем в showMessage() параметр text - "что-то пошло не так, и параметр additionalMessage - код http-статуса"
                        }
                    }

                    override fun onFailure(call: Call<FilmsResponce>, t: Throwable) { // метод вызывается если не получилось установить соединение с сервером
                        showMessage(getString(R.string.something_went_wrong), t.message.toString()) // передаем в showMessage() параметр text - "что-то пошло не так, и параметр additionalMessage - код http-статуса"
                    }                                                                               // и параметр additionalMessage - message (строка сообщения) из базового класса Throwable"

                })
            }
        }
    }

    private fun showMessage(text: String, additionalMessage: String) {
        if (text.isNotEmpty()) { // если text ошибки не пустой
            placeholderMessage.visibility = View.VISIBLE // делает сообщение об ошибке видимым
            films.clear() // список фильмов убираем, т.к. он может перекрывать сообщение об ошибке
            adapter.notifyDataSetChanged() // уведомляем адаптер об изменении набора данных?
            placeholderMessage.text = text // передаем в view соответсвующий текст ошибки
            if (additionalMessage.isNotEmpty()) { // если не пустой additionalMessage
                Toast.makeText(applicationContext, additionalMessage, Toast.LENGTH_LONG) // показываем тост, всплывающее окошко с оповещением об ошибке, с кодом http-статуса
                    .show()
            }
        } else {
            placeholderMessage.visibility = View.GONE // делает сообщение об ошибке невидимым
        }
    }
}




//        clearButton.setOnClickListener{
//            queryInput.setText("")


//        val searchTextWatcher = object : TextWatcher {
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                clearButton.visibility = clearButtonVisibility(s)
//            }
//
//            override fun afterTextChanged(s: Editable?) {}
//        }
//
//        queryInput.addTextChangedListener(searchTextWatcher)


