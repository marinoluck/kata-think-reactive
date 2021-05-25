package com.hidrovia.collector

import com.hidrovia.collector.Factory.Factory

class Main {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {

            val collector = Factory.collector
            collector()
//            val collectorBatA = DatFileReader("./data/batA.dat", 0f)
//            val collectorLevel = DatFileReader("./data/level.dat", 1.08f)
//            collectorLevel()

        }
    }
}