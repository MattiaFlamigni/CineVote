package com.example.cinevote.screens.settings


import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.example.cinevote.R
import com.example.cinevote.screens.settings.theme.model.Theme

@Composable
fun ThemeScreen(
    state: ThemeState,
    onThemeSelected: (Theme) -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
            .selectableGroup(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(R.string.select_theme),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp),
            color = MaterialTheme.colorScheme.primary
        )

        Theme.entries.forEach { theme ->
            val isSelected = theme == state.theme
            val backgroundColor by animateColorAsState(
                targetValue = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
            )
            val contentColor by animateColorAsState(
                targetValue = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
            )
            val icon: Painter = when (theme) {
                Theme.Light -> painterResource(R.drawable.light_mode_24dp_5f6368_fill0_wght400_grad0_opsz24)
                Theme.Dark -> painterResource(R.drawable.dark_mode_24dp_5f6368_fill0_wght400_grad0_opsz24)
                Theme.System -> painterResource(R.drawable.contrast_24dp_5f6368_fill0_wght400_grad0_opsz24)
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .selectable(
                        selected = isSelected,
                        onClick = { onThemeSelected(theme) },
                        role = Role.RadioButton
                    ),
                colors = CardDefaults.cardColors(
                    containerColor = backgroundColor,
                    contentColor = contentColor
                ),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
                    Icon(
                        painter = icon,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = stringResource(
                            when (theme) {
                                Theme.Light -> R.string.theme_light
                                Theme.Dark -> R.string.theme_dark
                                Theme.System -> R.string.theme_system
                            }
                        ),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

