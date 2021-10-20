package com.example.getandpostlocation

import com.google.gson.annotations.SerializedName

class Details {
    var data: List<Data>? = null

    class Data {
        @SerializedName("name")
        var name: String? = null

        @SerializedName("location")
        var location: String? = null
        constructor( name: String?,location:String?) {

            this.name = name
            this.location=location
        }
    }
}