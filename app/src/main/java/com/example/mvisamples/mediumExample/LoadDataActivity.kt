package com.example.mvisamples.mediumExample

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.example.mvisamples.R
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_load_data.*

class LoadDataActivity : AppCompatActivity() {

    private val compositeDisposable = CompositeDisposable()
    private lateinit var viewModel : LoadDataViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load_data)

        viewModel = ViewModelProviders.of(
            this,
            MVIPresentationViewModelFactory()
        )[LoadDataViewModel::class.java]


        viewModel.state()
            .subscribe(::render){ e ->  Log.e("test",e.message)}
            .let(compositeDisposable::add)

        getIntents()
            .let(viewModel::processIntents)
            .let(compositeDisposable::add)
    }

    private fun getIntents() : Observable<LoadDataIntentions> =
         Observable.merge(
             Observable.just(LoadDataIntentions.LoadDataFromServerIntention),
             refreshData.clicks().map { LoadDataIntentions.RefreshDataFromServerIntention },
             randomNumber.clicks().map { LoadDataIntentions.RandomNumberIntention }
        )

    private fun render(state: LoadDataState){
        when{
            state.progress -> reduceLoadingState()
            state.isFailed -> reduceFailureState()
            else -> reduceDataState(state)
        }
    }

    private fun reduceDataState(state: LoadDataState) {
        dataView.visibility = View.VISIBLE
        randomNumber.text = "${state.randomNumber}"
        if(!state.list.isNullOrEmpty()) {
            dataView.text = state.list.reduce { acc, s -> "$acc , $s" }
        }
        progressBar.visibility = View.GONE
    }

    private fun reduceFailureState() {
        dataView.visibility = View.VISIBLE
        dataView.text = getString(R.string.error_string)
        progressBar.visibility = View.GONE
    }

    private fun reduceLoadingState() {
        dataView.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }
}
