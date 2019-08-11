package com.example.mvisamples.mediumExample

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

interface DataRepo{
    fun loadData(): Observable<List<String>>
}

class DataRepoImpl : DataRepo {
    override fun loadData(): Observable<List<String>> {
        return Observable.timer(1,TimeUnit.SECONDS,Schedulers.io())
            .map{
                listOf(
                    "First",
                    "Second",
                    "Third",
                    "Fourth",
                    "Fifth"
                )
            }
    }
}
