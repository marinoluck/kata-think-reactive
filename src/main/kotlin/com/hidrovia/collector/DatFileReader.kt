package com.hidrovia.collector

import io.reactivex.Single
import java.io.File
import java.io.InputStream
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import kotlin.math.abs

data class SensorData(
    val timestamp: Long,
    val record: Long,
    val rawValue: Float,
    val calculatedValue: Float,
    val rawTimeStamp: String
)


class DatFileReader {

    operator fun invoke(filePath: String, referenceValue: Float): List<SensorData> {

        val datFile = File(filePath)
        if (!datFile.exists()) {
            println("Could not read data for sensor. File does not exists ($filePath)")
            return listOf()
        }
        val inputStream: InputStream = datFile.inputStream()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val sensorDataList = mutableListOf<SensorData>()

        var i = 0
        inputStream.bufferedReader()
            .forEachLine {
                i++
                if (i < 5) return@forEachLine
                val values = it.split(",")

                val rawTimestamp = values[0].replace("\"", "")
                val timestamp = LocalDateTime.parse(rawTimestamp, formatter).toInstant(ZoneOffset.UTC).toEpochMilli()
                val record = values[1].toLong()
                val rawValue = values[2].toFloat()

                val calculatedValue = rawValue.let { value ->
                    if (value < 0)
                        abs(value) + referenceValue
                    else
                        value + referenceValue
                }

                val sensorData = SensorData(timestamp, record, rawValue, calculatedValue, rawTimestamp)
                sensorDataList.add(sensorData)
            }
        return sensorDataList.toList()
    }
}