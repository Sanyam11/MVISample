package com.example.mvisamples.mediumExample

sealed class LoadDataIntentions {
    object LoadDataFromServerIntention : LoadDataIntentions()
    object RefreshDataFromServerIntention : LoadDataIntentions()
    object RandomNumberIntention : LoadDataIntentions()
    object GetLastStateIntention : LoadDataIntentions()
}
