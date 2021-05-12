package com.scalemonk.kata.thinkreactive

interface Tracker {
    /**
     * sends an event with an [eventName] and optional [parameters] to the analytics server
     */
    fun sendEvent(eventName:String, parameters: Map<String, String> = mapOf())
}