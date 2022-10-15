package br.com.gmfonseca.gamma.financials.ui.widgets.layouts

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.ActionParameters
import androidx.glance.action.clickable
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Column
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.Text
import br.com.gmfonseca.gamma.financials.R
import br.com.gmfonseca.gamma.financials.domain.model.Currency
import br.com.gmfonseca.gamma.financials.domain.model.Exchange
import br.com.gmfonseca.gamma.financials.domain.repositories.ExchangeRepository
import br.com.gmfonseca.gamma.financials.ui.widgets.ExchangeWidget
import com.google.gson.Gson
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private val EXCHANGE_RATE_KEY = stringPreferencesKey("EXCHANGE_RATE")

@Composable
fun ExchangeWidgetContent() {
    val prefs = currentState<Preferences>()
    val exchange = prefs[EXCHANGE_RATE_KEY]?.let { Gson().fromJson(it, Exchange::class.java) }

    val onRefreshClick = remember {
        actionRunCallback<RefreshActionCallback>()
    }
    Log.d("ExchangeWidgetContent", "Creating the widget")

    Column(
        modifier = GlanceModifier.background(Color.White)
    ) {

        if (exchange != null) {
            Text(text = "From")
            Text(text = "${exchange.from} $ 1")

            Text(text = "To")
            Text(text = exchange.to.toString())
            Text(text = "$ ${exchange.rate ?: "--"}")
        }

        Image(
            provider = ImageProvider(R.drawable.ic_repeat_24),
            contentDescription = "Reload",
            modifier = GlanceModifier.clickable(onRefreshClick)
        )
    }
}

@Preview
@Composable
private fun ExchangeWidgetContent_Preview() {
    ExchangeWidgetContent()
}

class RefreshActionCallback : ActionCallback, KoinComponent {
    private val gson = Gson()
    private val exchangeRepository by inject<ExchangeRepository>()

    init {
        Log.d("ExchangeWidgetContent", "Initialized action callback")
    }

    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters,
    ) {
        Log.d("ExchangeWidgetContent", "RefreshCallback -> on action")

        updateAppWidgetState(context, PreferencesGlanceStateDefinition, glanceId) {
            it.toMutablePreferences().apply {
                Log.d("ExchangeWidgetContent", "RefreshCallback -> fetching exchange")

                val key = stringPreferencesKey("EXCHANGE_RATE")
                val (from, to) = getCurrentExchange(key, gson)

                Log.d("ExchangeWidgetContent", "RefreshCallback -> fetched cached exchange")

                val exchange = exchangeRepository.findExchange(from, to)

                Log.d("ExchangeWidgetContent", "RefreshCallback -> getting new exchange")

                if (exchange != null) {
                    Log.d("ExchangeWidgetContent", "RefreshCallback -> found exchange")
                    this[key] = gson.toJson(exchange)
                } else {
                    Log.d("ExchangeWidgetContent", "RefreshCallback -> didnt find an exchange")
                }
            }
        }

        ExchangeWidget().update(context, glanceId)
    }

    private fun MutablePreferences.getCurrentExchange(
        key: Preferences.Key<String>,
        gson: Gson,
    ) = if (contains(key)) {
        gson.fromJson(this[key], Exchange::class.java)
    } else {
        Exchange(from = Currency.USD, to = Currency.BRL, rate = null)
    }
}
