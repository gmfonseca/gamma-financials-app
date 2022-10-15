package br.com.gmfonseca.gamma.financials

import android.app.Application
import br.com.gmfonseca.gamma.financials.data.datasources.ExchangeDataSource
import br.com.gmfonseca.gamma.financials.data.datasources.LocalExchangeDataSourceImpl
import br.com.gmfonseca.gamma.financials.data.repositories.ExchangeRepositoryImpl
import br.com.gmfonseca.gamma.financials.domain.repositories.ExchangeRepository
import br.com.gmfonseca.gamma.financials.ui.viewmodels.MainViewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class FinancialsApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            modules(
                module {
                    factory { MainViewModel(get()) }
                    factory<ExchangeRepository> { ExchangeRepositoryImpl(get()) }
                    single<ExchangeDataSource> { LocalExchangeDataSourceImpl() }
                }
            )
        }
    }

}
