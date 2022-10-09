package br.com.gmfonseca.gamma.financials.domain.repositories

import br.com.gmfonseca.gamma.financials.domain.model.Currency
import br.com.gmfonseca.gamma.financials.domain.model.Exchange

interface CurrencyRepository {
    fun findExchange(from: Currency, to: Currency): Exchange?
}
