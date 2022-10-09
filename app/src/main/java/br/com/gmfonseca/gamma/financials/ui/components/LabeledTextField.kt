package br.com.gmfonseca.gamma.financials.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LabeledTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    singleLine: Boolean = false,
    isRequired: Boolean = false,
    enabled: Boolean = true,
    onClick: () -> Unit = {},
) {
    val (isError, setIsError) = remember { mutableStateOf(false) }

    Surface(onClick = onClick) {
        OutlinedTextField(
            value = value,
            enabled = enabled,
            label = {
                Text(
                    text = buildAnnotatedString {
                        append(label)
                        if (isRequired) withStyle(style = SpanStyle(color = Color(0xFFFF0000))) {
                            append("*")
                        }
                    },
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            },
            isError = isError,
            onValueChange = { onValueChange(it); if (isRequired) setIsError(it.isBlank()) },
            placeholder = { Text(text = "Type here...") },
            singleLine = singleLine,
            keyboardOptions = keyboardOptions,
            modifier = Modifier
                .fillMaxWidth()
                .then(modifier),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LabeledTextFieldPreview() {
    LabeledTextField("label", "value", {})
}
