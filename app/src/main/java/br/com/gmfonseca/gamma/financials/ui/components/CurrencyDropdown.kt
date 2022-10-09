package br.com.gmfonseca.gamma.financials.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import br.com.gmfonseca.gamma.financials.domain.model.Currency
import br.com.gmfonseca.gamma.financials.ui.theme.MyApplicationTheme


@Composable
fun CurrencyDropdown(
    text: String,
    selectedCurrency: Currency,
    menuState: MutableState<Boolean>,
    onOptionClick: (Currency) -> Unit,
    modifier: Modifier = Modifier,
) {
    val (showMenu, setShowMenu) = menuState

    Box(modifier) {
        LabeledTextField(
            label = text,
            value = selectedCurrency.name,
            onValueChange = {},
            enabled = false,
            onClick = { setShowMenu(true) },
        )
        DropdownMenu(
            expanded = showMenu,
            onDismissRequest = { setShowMenu(false) },
        ) {
            Currency.values()
                .forEach {
                    val enabled = it != selectedCurrency

                    DropdownMenuItem(
                        onClick = {
                            onOptionClick(it)
                            setShowMenu(false)
                        },
                        enabled = enabled
                    ) {
                        Text(
                            it.name,
                            color = if (enabled) Color.Black else Color.LightGray
                        )
                    }
                }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        val menuState = remember { mutableStateOf(true) }

        CurrencyDropdown(
            "From",
            Currency.USD,
            menuState,
            {},
            Modifier
        )
    }
}
