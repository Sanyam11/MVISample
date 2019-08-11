package com.example.mvisamples.mediumExample

import io.reactivex.Observable.just
import io.reactivex.observers.TestObserver
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito

class LoadDataViewModelTest {

    @Mock
    lateinit var repo: DataRepo
    lateinit var actionProcessor: LoadDataActionProcessor
    lateinit var viewModel: LoadDataViewModel
    lateinit var testObserver: TestObserver<LoadDataState>

    val mockData = listOf<String>("First","Second","Third")


    @Test
    fun `When LoadDataIntent succeseds , it should return data`(){
        repo.loadData()
            .let(Mockito::`when`)
            .thenReturn(mockData.let(::just))

        LoadDataIntentions.LoadDataFromServerIntention
            .let(::just)
            .cast(LoadDataIntentions::class.java)
            .let(viewModel::processIntents)

//        testObserver.values()
//            .let(::println)

        testObserver.assertValueAt(0,LoadDataState::progress)
        testObserver.assertValueAt(1){ !it.progress }
        testObserver.assertValueAt(1){ it.list == mockData}
    }


}
