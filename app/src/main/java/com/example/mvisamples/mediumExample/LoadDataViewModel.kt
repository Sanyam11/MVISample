package com.example.mvisamples.mediumExample

import android.arch.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject

class LoadDataViewModel(repo : DataRepo) : ViewModel(){

    private val actionProcessor = LoadDataActionProcessor(repo)
    private val intentsSubject = PublishSubject.create<LoadDataIntentions>()
    private val states = PublishSubject.create<LoadDataState>()

    init {
        intentsSubject
            .scan(::intentFilter)
            .map(::mapToActions)
            .compose(actionProcessor.actionProcessor)
            .scan(LoadDataState.INITIAL_STATE, ::reduceToNewState)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(states)
    }

    fun processIntents(intents: Observable<LoadDataIntentions>):Disposable =
        intents.subscribe(intentsSubject::onNext)

    fun state(): Observable<LoadDataState> = states.hide()

    override fun onCleared() {
        intentsSubject.onComplete()
        states.onComplete()
        super.onCleared()
    }

    private fun intentFilter(
        initialIntentions: LoadDataIntentions ,
        newIntentions: LoadDataIntentions
    ):LoadDataIntentions{
        return if(newIntentions is LoadDataIntentions.LoadDataFromServerIntention){
            LoadDataIntentions.GetLastStateIntention
        }else {
            newIntentions
        }
    }

    private fun mapToActions(intent: LoadDataIntentions): LoadDataActionClass {
        return when(intent){
            LoadDataIntentions.LoadDataFromServerIntention -> LoadDataActionClass.LoadDataAction
            LoadDataIntentions.RefreshDataFromServerIntention -> LoadDataActionClass.LoadDataAction
            LoadDataIntentions.RandomNumberIntention -> LoadDataActionClass.RandomNumberAction
            LoadDataIntentions.GetLastStateIntention -> LoadDataActionClass.GetLastStateAction
        }
    }

    private fun reduceToNewState(oldState: LoadDataState, result: LoadDataResult): LoadDataState {
        return when (result) {
            LoadDataResult.FailureStateResult -> oldState.copy(progress = false,isFailed = true)
            LoadDataResult.LoadingStateResult -> oldState.copy(progress = true,isFailed = false)
            is LoadDataResult.DataStateResult -> oldState.copy(progress = false,isFailed = false,list = result.list)
            is LoadDataResult.RandomNumberStateResult -> oldState.copy(randomNumber = result.number)
            LoadDataResult.GetLastStateResult -> oldState
        }
    }
}

sealed class LoadDataResult{
    data class DataStateResult(val list : List<String>) : LoadDataResult()
    data class RandomNumberStateResult(val number : Int) : LoadDataResult()
    object LoadingStateResult : LoadDataResult()
    object FailureStateResult : LoadDataResult()
    object GetLastStateResult : LoadDataResult()
}
sealed class LoadDataActionClass{
    object LoadDataAction : LoadDataActionClass()
    object RandomNumberAction : LoadDataActionClass()
    object GetLastStateAction : LoadDataActionClass()
}
