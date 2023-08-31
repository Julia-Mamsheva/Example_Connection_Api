package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myapplication.Adapter.AdapterCharacter
import com.example.myapplication.Adapter.ApiRequests
import com.example.myapplication.Model.ApiModel
import com.example.myapplication.Model.Result
import com.example.myapplication.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Math.abs
import java.util.*

class MainActivity : AppCompatActivity(), AdapterCharacter.Listener, GestureDetector.OnGestureListener {
    lateinit var gestureDetector: GestureDetector //Объект



    var UserName : String = "User Name"
    private val BASE_URL = "https://rickandmortyapi.com"
    private val TAG = "MainActivity"

    lateinit var binding: ActivityMainBinding
    private val adapterCharacter = AdapterCharacter(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        gestureDetector = GestureDetector(this, this)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.userName.text = UserName


        getData()
    }
    private fun init(data: ApiModel) {
        with(binding) {
            listViewCharacter.layoutManager = GridLayoutManager(this@MainActivity, 1)
            listViewCharacter.adapter = adapterCharacter

            val listCharacter: List<Result> = data.results
            if (listCharacter.isNotEmpty()) {
                for (element in listCharacter) {
                    adapterCharacter.addResult(element)
                }
                progressBarLoad.visibility = View.INVISIBLE
            }
            imgSetting.setOnClickListener()
            {
               //открытие по клику
            }
        }
    }

    private fun getData() {
        val api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiRequests::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = api.getCharacter().awaitResponse()
                if (response.isSuccessful) {
                    val data = response.body()!!
                    runOnUiThread { init(data) }
                    Log.d(TAG, data.toString())
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.d(TAG, e.toString())
                }
            }
        }
    }



    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return if(gestureDetector.onTouchEvent(event!!)){
            true
        }else{
            return super.onTouchEvent(event)
        }
    }
    override fun onDown(e: MotionEvent): Boolean {
        return false
    }

    override fun onShowPress(e: MotionEvent) {
    }

    override fun onSingleTapUp(e: MotionEvent): Boolean {
        return false
    }

    override fun onScroll(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
        return false
    }

    override fun onLongPress(e: MotionEvent) {

    }

    override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
        val diffY = e2.y - e1.y
        val diffX = e2.x - e1.x
        if(abs(diffX)>abs(diffY)){
            if(abs(diffX)>100 && abs(velocityX) > 100){
                if(diffX<0){
                    Toast.makeText(this,"Смахиваем влево",Toast.LENGTH_LONG).show()
                }
            }
        }
        return true
    }

    override fun OnClick(result: Result) {
        TODO("Not yet implemented")
    }
}