package com.edgedevstudio.todolistapp_2.Async

import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * Created by Olorunleke Opeyemi on 23/01/2019.
 **/
class AppExecutors {

    val diskIO: Executor

    private constructor(diskIO: Executor) {
        this.diskIO = diskIO
    }

    companion object {
        private var sIntance :AppExecutors? = null

        fun getInstance() : AppExecutors {
            if (sIntance == null){
                //create a new instance of sInstance
                sIntance = AppExecutors(Executors.newSingleThreadExecutor())
            }
            return sIntance!!
        }
    }
}