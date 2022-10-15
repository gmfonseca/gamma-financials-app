package br.com.gmfonseca.gamma.financials

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.gmfonseca.gamma.financials.data.datasources.LocalExchangeDataSourceImpl
import br.com.gmfonseca.gamma.financials.data.repositories.ExchangeRepositoryImpl
import br.com.gmfonseca.gamma.financials.ui.components.CurrencyDropdown
import br.com.gmfonseca.gamma.financials.ui.components.LabeledTextField
import br.com.gmfonseca.gamma.financials.ui.theme.MyApplicationTheme
import br.com.gmfonseca.gamma.financials.ui.viewmodels.MainViewModel
import org.koin.android.ext.android.inject
import java.text.DecimalFormat

class MainActivity : ComponentActivity() {
    private val viewModel by inject<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Greeting(viewModel)
            }
        }
    }
}

@Composable
fun Greeting(viewModel: MainViewModel) {
    val fromMenuState = remember { mutableStateOf(false) }
    val toMenuState = remember { mutableStateOf(false) }
    val state by viewModel.uiState.collectAsState()
    val numberFormatter = DecimalFormat("0.00")

    val value = numberFormatter.format(state.value)
    val result = state.result?.let { numberFormatter.format(it) } ?: "--"

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        color = MaterialTheme.colorScheme.background
    ) {
        Column {
            Text(
                text = "Exchange",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row {
                CurrencyDropdown(
                    text = "From",
                    selectedCurrency = state.fromCurrency,
                    menuState = fromMenuState,
                    onOptionClick = { new ->
                        val to = state.toCurrency.takeIf { it != new } ?: state.fromCurrency

                        viewModel.loadExchange(new, to)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                )

                CurrencyDropdown(
                    text = "To",
                    selectedCurrency = state.toCurrency,
                    menuState = toMenuState,
                    onOptionClick = { new ->
                        val from = state.fromCurrency.takeIf { it != new } ?: state.toCurrency

                        viewModel.loadExchange(from, new)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp),
                )
            }
            LabeledTextField(
                label = "Amount",
                value = state.valueTxt,
                onValueChange = viewModel::convert,
                keyboardOptions = KeyboardOptions(autoCorrect = false,
                    keyboardType = KeyboardType.Number),
                modifier = Modifier.padding(vertical = 16.dp),
                singleLine = true
            )

            Card(modifier = Modifier
                .padding(vertical = 16.dp)
                .fillMaxWidth()) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(8.dp)
                ) {

                    Text(text = "$value ${state.fromCurrency} =",
                        modifier = Modifier.padding(8.dp))
                    Text(
                        text = "$result ${state.toCurrency}",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    state.exchange?.let {
                        if (state.value != 1.0) {
                            Text(text = "Rate 1 ${state.fromCurrency} = ${numberFormatter.format(it.rate)} ${state.toCurrency}",
                                modifier = Modifier.padding(bottom = 8.dp))
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        Greeting(
            MainViewModel(
                ExchangeRepositoryImpl(
                    LocalExchangeDataSourceImpl()
                )
            )
        )
    }
}
