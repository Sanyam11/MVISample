package com.example.mvisamples.mediumExample

import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import kotlin.random.Random

class LoadDataActionProcessor (repo: DataRepo){
    private val loadDataActionProcessor = ObservableTransformer<LoadDataActionClass,LoadDataResult>{ action ->
        action.switchMap {
            repo.loadData()
                .map { LoadDataResult.DataStateResult(it) }
                .cast( LoadDataResult::class.java)
                .onErrorReturn { LoadDataResult.FailureStateResult }
                .startWith(LoadDataResult.LoadingStateResult)
        }
    }

    private val randomNumberActionProcessor =
        ObservableTransformer<LoadDataActionClass.RandomNumberAction, LoadDataResult.RandomNumberStateResult> { action ->
            action.map { LoadDataResult.RandomNumberStateResult(Random.nextInt()) }
        }

    val actionProcessor = ObservableTransformer<LoadDataActionClass,LoadDataResult> { action ->
        action.publish { actionSource ->
            Observable.merge(
                    actionSource.ofType(LoadDataActionClass.LoadDataAction::class.java).compose(loadDataActionProcessor),
                    actionSource.ofType(LoadDataActionClass.RandomNumberAction::class.java).compose(randomNumberActionProcessor),
                    actionSource.ofType(LoadDataActionClass.GetLastStateAction::class.java).map { LoadDataResult.GetLastStateResult }
            )
        }
    }
}
