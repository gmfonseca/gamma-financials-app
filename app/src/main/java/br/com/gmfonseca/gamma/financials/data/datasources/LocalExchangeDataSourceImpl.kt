package br.com.gmfonseca.gamma.financials.data.datasources

import br.com.gmfonseca.gamma.financials.domain.model.Currency
import br.com.gmfonseca.gamma.financials.domain.model.Exchange

class LocalExchangeDataSourceImpl : ExchangeDataSource {

    private val exchanges = mapOf(
        Currency.USD to mapOf(
            Currency.BRL to 5.204403,
            Currency.EUR to 1.032668,
        ),
        Currency.BRL to mapOf(
            Currency.USD to 0.192145,
        )
    )

    override fun findExchange(from: Currency, to: Currency): Exchange? {
        val rate = findRate(from, to)
            ?: findReverseRate(from, to)
            ?: return null

        return Exchange(from, to, rate)
    }

    private fun findReverseRate(from: Currency, to: Currency) =
        findRate(to, from)?.let { 1 / it }

    private fun findRate(from: Currency, to: Currency) = exchanges[from]?.get(to)
}
