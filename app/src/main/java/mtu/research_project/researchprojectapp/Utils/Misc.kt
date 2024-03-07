package mtu.research_project.researchprojectapp.Utils

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import mtu.research_project.researchprojectapp.Theme.secondaryColor

@Composable
fun CustomButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = secondaryColor),
        modifier = modifier
    ) {

        Text(
            text = text,
            textAlign = TextAlign.Center,
            color = Color.Black,
            modifier = Modifier
                .weight(1f)
                .padding(start = 32.dp)
        )
    }
}