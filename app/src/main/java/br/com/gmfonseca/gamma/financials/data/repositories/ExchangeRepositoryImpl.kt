package br.com.gmfonseca.gamma.financials.data.repositories

import br.com.gmfonseca.gamma.financials.data.datasources.ExchangeDataSource
import br.com.gmfonseca.gamma.financials.domain.model.Currency
import br.com.gmfonseca.gamma.financials.domain.model.Exchange
import br.com.gmfonseca.gamma.financials.domain.repositories.ExchangeRepository

class ExchangeRepositoryImpl(
    private val localDatasource: ExchangeDataSource,
) : ExchangeRepository {

    override fun findExchange(from: Currency, to: Currency): Exchange? {
        return localDatasource.findExchange(from, to)
    }
}
