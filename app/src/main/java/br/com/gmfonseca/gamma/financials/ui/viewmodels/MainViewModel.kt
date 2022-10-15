package br.com.gmfonseca.gamma.financials.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.gmfonseca.gamma.financials.domain.model.Currency
import br.com.gmfonseca.gamma.financials.domain.model.Exchange
import br.com.gmfonseca.gamma.financials.domain.repositories.ExchangeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val exchangeRepository: ExchangeRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> get() = _uiState

    init {
        loadExchange(uiState.value.fromCurrency, uiState.value.toCurrency)
    }

    fun loadExchange(fromCurrency: Currency, toCurrency: Currency) {
        viewModelScope.launch {
            val state = uiState.value

            if (state.exchange == null || state.fromCurrency != fromCurrency || state.toCurrency != toCurrency) {

                val exchange = if (state.exchange != null && fromCurrency == state.toCurrency) {
                    Exchange(fromCurrency, toCurrency, 1 / (state.exchange.rate ?: 1.0))
                } else {
                    _uiState.emit(
                        uiState.value.copy(
                            fromCurrency = fromCurrency,
                            toCurrency = toCurrency,
                            exchange = null,
                            result = null
                        )
                    )

                    exchangeRepository.findExchange(fromCurrency, toCurrency)
                }
                _uiState.emit(
                    UiState(
                        fromCurrency = fromCurrency,
                        toCurrency = toCurrency,
                        exchange = exchange,
                        result = internalConvert(state.value, exchange)
                    )
                )
            }
        }
    }

    fun convert(valueTxt: String) {
        viewModelScope.launch {
            val state = uiState.value
            val input = valueTxt.trim().replace("\n", "")
            val value = try {
                input.toDouble()
            } catch (t: NumberFormatException) {
                0.0
            }
            _uiState.emit(
                state.copy(
                    valueTxt = input,
                    value = value,
                    result = internalConvert(value, state.exchange)
                )
            )
        }
    }

    private fun internalConvert(value: Double, exchange: Exchange?): Double? =
        exchange?.run { (rate ?: 0.0) * value }

    data class UiState(
        val fromCurrency: Currency = Currency.USD,
        val toCurrency: Currency = Currency.BRL,
        val valueTxt: String = "1.0",
        val value: Double = 1.0,
        val exchange: Exchange? = null,
        val result: Double? = null,
    )
}
