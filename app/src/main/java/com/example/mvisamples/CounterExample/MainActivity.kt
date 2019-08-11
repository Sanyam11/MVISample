package com.example.mvisamples.CounterExample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.mvisamples.R
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getIntents()
            .scan(CounterState(0),::reduce)
            .subscribe(::render)
            .let(compositeDisposable::add)
    }

    private fun getIntents():Observable<CounterIntentions> =
        Observable.merge(
            listOf<Observable<CounterIntentions>>(
                minusButton.clicks().map { CounterIntentions.DecrementIntent },
                plusButton.clicks().map { CounterIntentions.IncrementIntent }
            )
        )

    private fun reduce(
        oldState: CounterState ,
        intent: CounterIntentions
    ):CounterState {
        return when(intent){
            CounterIntentions.IncrementIntent -> oldState.copy(counter = oldState.counter + 1)
            CounterIntentions.DecrementIntent -> oldState.copy(counter = oldState.counter - 1)
        }
    }

    private fun render(state: CounterState){
        counter.text = state.counter.toString()
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }
}
