package com.debugg3r.android.testdecisions.data

class DataStoreSample : DataStoreProvider() {
    private object Holder {
        val instance = DataStoreSample()
    }
    companion object {
        val instance: DataStore by lazy { Holder.instance }
        init {
            instance.addQuestion("first question")
            instance.addQuestion("second question")
            instance.addQuestion("third question")
            instance.addQuestion("fourth question")
            instance.addQuestion("fifth question")
        }
    }

}