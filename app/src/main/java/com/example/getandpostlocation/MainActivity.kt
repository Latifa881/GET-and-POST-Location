package com.example.getandpostlocation

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    lateinit var edName: EditText
    lateinit var edLocation: EditText
    lateinit var btSave: Button
    lateinit var edNameGetLocation: EditText
    lateinit var btGetLocation: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        edName = findViewById(R.id.edName)
        edLocation = findViewById(R.id.edLocation)
        btSave = findViewById(R.id.btSave)
        edNameGetLocation = findViewById(R.id.edNameGetLocation)
        btGetLocation = findViewById(R.id.btGetLocation)
        btSave.setOnClickListener {
            val name = edName.text.toString()
            val location = edLocation.text.toString()
            if (name.isNotEmpty() && location.isNotEmpty()) {
                addData(name, location)
            } else {
                Toast.makeText(this, "Enter a name and location", Toast.LENGTH_SHORT).show()
            }
        }
        btGetLocation.setOnClickListener {
            val searchName = edNameGetLocation.text.toString()

            if (searchName.isNotEmpty()) {
                getAllData(searchName)
            } else {
                Toast.makeText(this@MainActivity, "Enter a name", Toast.LENGTH_SHORT).show()

            }
            edNameGetLocation.text.clear()
        }

    }

    fun getAllData(name: String) {
        val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)
        var location = ""
        var flag=true
        val progressDialog = ProgressDialog(this@MainActivity)
        progressDialog.setMessage("Please wait")
        progressDialog.show()
        if (apiInterface != null) {

            apiInterface.getDetails().enqueue(object : Callback<ArrayList<Details.Data>> {
                override fun onResponse(
                    call: Call<ArrayList<Details.Data>>,
                    response: Response<ArrayList<Details.Data>>
                ) {
                    Log.d("TAG", response.code().toString() + "")
                    val resultArray=response.body()!!
                    for (data in resultArray) {
                        if (data.name.toString().lowercase().equals(name.lowercase())) {
                            location = data.location!!
                            Log.d("location", location+ "")
                            Toast.makeText(
                                this@MainActivity,
                                "location:$location",
                                Toast.LENGTH_SHORT
                            ).show()
                            flag=false

                           findViewById<TextView>(R.id.tvResults).setText("$name lives in $location")
                            progressDialog.dismiss()
                            break
                        }
                        if(flag){
                            progressDialog.dismiss()
                            findViewById<TextView>(R.id.tvResults).setText("No result found for $name")
                        }
                    }

                }

                override fun onFailure(call: Call<ArrayList<Details.Data>>, t: Throwable) {
                    call.cancel()
                }
            })
        }


    }

    fun addData(name: String, location: String) {

        val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)
        if (apiInterface != null) {
            apiInterface.addDetails(Details.Data(name, location))
                //apiInterface.addNames(etName.text.toString())
                .enqueue(object : Callback<Details.Data> {
                    override fun onResponse(
                        call: Call<Details.Data>,
                        response: Response<Details.Data>
                    ) {
                        Toast.makeText(
                            this@MainActivity,
                            "$name & $location are added successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        edName.text.clear()
                        edLocation.text.clear()
                    }

                    override fun onFailure(call: Call<Details.Data>, t: Throwable) {
                        Toast.makeText(
                            this@MainActivity,
                            "failed to add",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.e("Failed oops", t.toString())

                        call.cancel()
                    }
                })

        }
    }

}