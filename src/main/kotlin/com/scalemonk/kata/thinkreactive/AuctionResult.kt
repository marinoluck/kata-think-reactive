package com.scalemonk.kata.thinkreactive

interface AuctionResult

data class AuctionResultNoFill(val reason:String)
data class AuctionResultWithBid(val payload:String, val bidPrice: Int)

