package com.hidrovia.collector

import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.*


class HttpSensorsService(private val sensorsAPI: SensorsAPI) : SensorService {
    override fun getAllSensors(): Single<List<Sensor>> {
        return sensorsAPI.getAllSensors()
            .flatMap {
                Single.create<List<Sensor>> { emitter ->
                    if (it.isSuccessful) {
                        it.body()
                            ?.let { emitter.onSuccess(it.sensors) }
                            ?: emitter.onSuccess(
                                // Empty body response
                                listOf()
                            )
                    } else {
                        println("error getting sensors ${it.message()}")
                        emitter.onSuccess(
                            // Empty body response
                            listOf()
                        )
                    }
                }
            }
    }

    override fun addSensorData(sensorData: SensorDataRequest): Single<Response<SensorDataRequest>> {
//        return return Single.just(Response.success(sensorData))
        return sensorsAPI.addSensorData(sensorData).doAfterSuccess { println("record sent ${it.body()}") }
    }

    override fun addMultipleSensorData(sensorData: List<SensorDataRequest>): Single<Response<SensorMultipleDataResponse>> {
//        return return Single.just(Response.success(SensorMultipleDataResponse("success")))
        return sensorsAPI.addMultipleSensorData(SensorMultipleDataRequest(sensorData)).doAfterSuccess { println("record sent ${it.body()}") }
    }
}

const val token = "Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjAwM2Y4OGI3LWYzYjctNDA4Yi05YWM3LTM5MGQzYjY4MTk2NSIsInJvbGUiOjEsImlhdCI6MTYyMDUwMTY4MX0.lP5OyJhXUMODTccB_d-e8AL15RzwoSNoO_zRfCX79ZMN97EYZzhMzsWm-V1WINa6s46L4J-RUjOBNsgYMiX3EQ"

interface SensorsAPI {
    @Headers("Authorization: $token")
    @GET("/api/sensors")
    fun getAllSensors(): Single<Response<SensorsResponse>>


    @Headers("Authorization: $token")
    @POST("/api/sensors/data")
    fun addSensorData(
        @Body body: SensorDataRequest
    ): Single<Response<SensorDataRequest>>

    @Headers("Authorization: $token")
    @POST("/api/sensors/multiple-data")
    fun addMultipleSensorData(
        @Body body: SensorMultipleDataRequest
    ): Single<Response<SensorMultipleDataResponse>>
}

data class SensorsResponse(
    val sensors: List<Sensor>,
    val total: Int
)

data class SensorDataRequest(
    val sensorId: String,
    val stationId: String,
    val sensorName: String,
    val referenceValueName: Float,
    val rawValue: Float,
    val calculatedValue: Float,
    val valueDate: Long
)

data class SensorMultipleDataRequest (val data: List<SensorDataRequest>)

data class SensorMultipleDataResponse (val message: String)
