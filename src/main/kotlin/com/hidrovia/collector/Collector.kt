package com.hidrovia.collector

import io.reactivex.Single
import io.reactivex.rxkotlin.toObservable
import retrofit2.Response

interface SensorService {
    fun getAllSensors(): Single<List<Sensor>>
    fun addSensorData(sensorData: SensorDataRequest): Single<Response<SensorDataRequest>>
    fun addMultipleSensorData(sensorData: List<SensorDataRequest>): Single<Response<SensorMultipleDataResponse>>
}

class Collector(private val sensorService: SensorService, private val datFileReader: DatFileReader) {
    operator fun invoke() {
        sensorService.getAllSensors()
            .flatMapObservable { it.toObservable() }
            .map { sensor ->
                datFileReader(getFilePath(sensor), sensor.referenceValue, sensor.lastValueReadDate)
                    .map {
                        SensorDataRequest(
                            sensor.sensorId,
                            sensor.stationId,
                            sensor.name,
                            sensor.referenceValue,
                            it.rawValue,
                            it.calculatedValue,
                            it.timestamp
                        )
                    }
            }
            .flatMap { it.toObservable() }
            .buffer(100)
            .flatMap {
                sensorService.addMultipleSensorData(it).toObservable()
            }
            .subscribe()
    }

    private fun getFilePath(sensor: Sensor) = sensor.filePath

//    private fun getFilePath(sensor: Sensor) = sensor.filePath.replace(
//        "C:\\Campbellsci\\LoggerNet\\",
//        "/Users/lucasmarino/Development/projects/hidrovia/data/"
//    )
}