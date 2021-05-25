package com.hidrovia.collector

data class Sensor (
    val sensorId: String,
    val stationId: String,
    val name: String,
    val filePath: String,
    val idLoggerNet: String,
    val referenceValue: Float,
    val refreshIntervalMilliseconds: Long
)
