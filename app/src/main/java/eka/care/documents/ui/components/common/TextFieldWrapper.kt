package eka.care.documents.ui.components.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eka.ui.theme.EkaTheme
import eka.care.documents.ui.R

@Composable
fun TextFieldWrapper(
    modifier: Modifier = Modifier,
    value: String = "",
    placeholder: String = "",
    label: String = "",
    onChange: (String) -> Unit = {},
    leadingIcon: Int? = null,
    trailingIcon: Int? = null,
    supportingText: String = " ",
    isError: Boolean = false,
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    colors: TextFieldColors? = null,
    required: Boolean = false
) {
    OutlinedTextField(
        singleLine = true,
        modifier = modifier,
        onValueChange = onChange,
        keyboardOptions = keyboardOptions,
        colors = (colors ?: OutlinedTextFieldDefaults.colors(
            focusedBorderColor = EkaTheme.colors.primary,
            unfocusedBorderColor = EkaTheme.colors.outline,
            errorBorderColor = EkaTheme.colors.error,
            focusedLabelColor = EkaTheme.colors.primary
        )),
        value = value,
        enabled = enabled,
        isError = isError,
        supportingText = if (supportingText.isNotEmpty()) ({
            Text(
                text = supportingText,
                style = EkaTheme.typography.labelMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }) else null,
        trailingIcon = if (trailingIcon != null) ({
            Icon(
                painter = painterResource(id = trailingIcon),
                contentDescription = "Trailing Icon",
                modifier = Modifier.size(16.dp),
            )
        }) else null,
        label = if (label.isNotEmpty()) ({
            Row(
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = label,
                    maxLines = 1,
                )
                if (required) {
                    Text(text = "*", color = EkaTheme.colors.error)
                }
            }
        }) else null,
        leadingIcon = if (leadingIcon != null) ({
            Icon(
                painter = painterResource(id = leadingIcon),
                contentDescription = "Leading Icon",
                modifier = Modifier.size(16.dp),
            )
        }) else null,
        placeholder = if (placeholder.isNotEmpty()) ({
            Text(
                text = placeholder,
                style = EkaTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }) else null
    )
}

@Preview(showBackground = true, widthDp = 720)
@Composable
fun TextFieldWrapperPreview() {
    Column {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TextFieldWrapper(
                value = "",
                label = "Label",
                placeholder = "Placeholder",
                leadingIcon = R.drawable.ic_camera_solid,
                trailingIcon = R.drawable.ic_camera_viewfinder_regular,
            )

            TextFieldWrapper(
                value = "",
                label = "Label",
                placeholder = "Placeholder",
                supportingText = "Supporting Text",
                leadingIcon = R.drawable.ic_camera_solid,
                trailingIcon = R.drawable.ic_camera_viewfinder_regular,
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TextFieldWrapper(
                value = "",
                enabled = false,
                label = "Label",
                placeholder = "Placeholder",
                supportingText = "Supporting Text",
                leadingIcon = R.drawable.ic_camera_solid,
                trailingIcon = R.drawable.ic_camera_viewfinder_regular,
            )

            TextFieldWrapper(
                value = "",
                isError = true,
                label = "Label",
                placeholder = "Placeholder",
                supportingText = "Supporting Text",
                leadingIcon = R.drawable.ic_camera_solid,
                trailingIcon = R.drawable.ic_camera_viewfinder_regular,
            )
        }
    }
}