package com.hidrovia.collector

import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.rxkotlin.toObservable

interface SensorService {
    fun getAllSensors(): Single<List<Sensor>>
    fun addSensorData(sensorData: SensorDataRequest): Completable
}

class Collector(private val sensorService: SensorService, private val datFileReader: DatFileReader) {
    operator fun invoke() {
        sensorService.getAllSensors()
            .flatMapObservable { it.toObservable() }
            .firstOrError()// FIXME: remove me
            .toObservable()// FIXME: remove me
            .map { sensor ->  datFileReader(sensor.filePath.replace("/var/file", "./data/level.dat"), sensor.referenceValue)
                .map { SensorDataRequest(
                    sensor.sensorId,
                    sensor.stationId,
                    sensor.name,
                    sensor.referenceValue,
                    it.rawValue,
                    it.calculatedValue,
                    it.timestamp
                ) }}
            .flatMap { it.toObservable() }
            .flatMapCompletable {
                sensorService.addSensorData(it)
            }
            .subscribe()
    }
}