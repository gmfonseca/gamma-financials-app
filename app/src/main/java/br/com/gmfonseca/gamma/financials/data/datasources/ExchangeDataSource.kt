package br.com.gmfonseca.gamma.financials.data.datasources

import br.com.gmfonseca.gamma.financials.domain.model.Currency
import br.com.gmfonseca.gamma.financials.domain.model.Exchange

interface ExchangeDataSource {
    fun findExchange(from: Currency, to: Currency): Exchange?
}
