package br.com.gmfonseca.gamma.financials.data.datasources

import br.com.gmfonseca.gamma.financials.domain.model.Currency
import br.com.gmfonseca.gamma.financials.domain.model.Exchange

class LocalExchangeDataSourceImpl : ExchangeDataSource {

    private val exchanges = mapOf(
        Currency.USD to mapOf(
            Currency.BRL to 5.20184
        ),
        Currency.BRL to mapOf(
            Currency.USD to 0.19224
        )
    )

    override fun findExchange(from: Currency, to: Currency): Exchange? {
        return exchanges[from]?.get(to)?.let { Exchange(from, to, it) }
    }
}
