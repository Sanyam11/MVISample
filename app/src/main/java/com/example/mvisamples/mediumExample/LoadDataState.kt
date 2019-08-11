package com.example.mvisamples.mediumExample

data class LoadDataState (
    val progress: Boolean,
    val isFailed: Boolean,
    val list : List<String>,
    val randomNumber: Int
) {
     companion object{
         val INITIAL_STATE = LoadDataState(
             progress = false,
             isFailed = false,
             list = emptyList(),
             randomNumber = 0
         )
     }
}
