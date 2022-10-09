package br.com.gmfonseca.gamma.financials.data.repositories

import br.com.gmfonseca.gamma.financials.data.datasources.ExchangeDataSource
import br.com.gmfonseca.gamma.financials.data.datasources.LocalExchangeDataSourceImpl
import br.com.gmfonseca.gamma.financials.domain.model.Currency
import br.com.gmfonseca.gamma.financials.domain.model.Exchange
import br.com.gmfonseca.gamma.financials.domain.repositories.CurrencyRepository

class ExchangeRepositoryImpl(
    private val localDatasource: ExchangeDataSource = LocalExchangeDataSourceImpl(),
//    private val remoteDatasource: ExchangeDataSource = TODO()
): CurrencyRepository {

    override fun findExchange(from: Currency, to: Currency): Exchange? {
        return localDatasource.findExchange(from, to)
    }
}
