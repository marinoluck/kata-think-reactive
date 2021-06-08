package com.hidrovia.collector

import com.hidrovia.collector.Factory.Factory
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

class Main {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {



            Observable.interval(10, TimeUnit.MINUTES)
                .doOnSubscribe {
                    doCollect()
                }
                .subscribe {
                    doCollect()
                }
            while (true) {

            }
        }

        private fun doCollect() {
            val collector = Factory.collector
            println("Starting process")
            collector()
        }
    }
}