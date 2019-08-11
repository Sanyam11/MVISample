package com.example.mvisamples.CounterExample

sealed class CounterIntentions{
    object IncrementIntent : CounterIntentions()
    object DecrementIntent : CounterIntentions()
}

