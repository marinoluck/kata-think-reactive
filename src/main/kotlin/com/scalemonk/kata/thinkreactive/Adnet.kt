package com.scalemonk.kata.thinkreactive

import io.reactivex.Completable
import io.reactivex.Single

interface Adnet {

    /**
     * initializes the adnet for the given [appId]
     * @return a Completable when the initialization is done. If anything goes wrong (network error, internal errors, etc) it emits an error
     */
    fun initialize(appId: String): Completable

    /**
     * Request a bid for the given [deviceId]
     * @return a Single emitting the [AuctionResult]. If anything goes wrong (network error, internal errors, etc) it emits an error
     */
    fun requestBid(deviceId: String): Single<AuctionResult>
}