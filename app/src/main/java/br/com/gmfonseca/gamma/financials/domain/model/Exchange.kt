package br.com.gmfonseca.gamma.financials.domain.model

data class Exchange(
    val from: Currency,
    val to: Currency,
    val rate: Double
)
