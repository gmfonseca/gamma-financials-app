package br.com.gmfonseca.gamma.financials.ui.widgets

import androidx.compose.runtime.Composable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition
import br.com.gmfonseca.gamma.financials.ui.widgets.layouts.ExchangeWidgetContent
import org.koin.core.component.KoinComponent

class ExchangeWidget : GlanceAppWidget(), KoinComponent {
    override val stateDefinition: GlanceStateDefinition<*> = PreferencesGlanceStateDefinition

    @Composable
    override fun Content() {
        ExchangeWidgetContent()
    }
}

class ExchangeWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = ExchangeWidget()
}
