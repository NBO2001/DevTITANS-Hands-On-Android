package com.example.plaintext.data

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SimulatorDatabase() {
    private val datalist = mutableListOf<String>();

    init {
        for (i in 1..100) {
            datalist.add("devtitans #$i");
        }

    }

    fun getData(): Flow<List<String>> = flow {
        delay(5000)
        emit(datalist)
    }
}