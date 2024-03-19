package mtu.research_project.researchprojectapp.Utils

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material.SnackbarDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CreateNewFolder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mtu.research_project.researchprojectapp.R
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    icon: ImageVector?,
    modifier: Modifier
) {
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .size(height = 56.dp, width = 360.dp)
            .padding(top = 20.dp, start = 20.dp, end = 20.dp),
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholder,
                color = Color.Gray
            )
        },
        textStyle = TextStyle(color = Color.Black, fontSize = 18.sp),
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.textFieldColors(
            cursorColor = Color.Black,
            containerColor = Color.Gray,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        leadingIcon = {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = "search",
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp)
                        .size(24.dp, 24.dp)
                )
            }
        }
    )
}

@Composable
fun CategoryBox(text: String, onClick: () -> Unit, modifier: Modifier){

    Box (
        modifier = Modifier
            .size(10.dp, 200.dp)
            .padding(start = 20.dp, top = 30.dp, end = 20.dp)
            .clickable(onClick = onClick)
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ){
        Column (
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Icon(
                imageVector = Icons.Default.CreateNewFolder,
                contentDescription = "file image",
                tint = Color.Cyan,
                modifier = Modifier
                    .size(100.dp, 100.dp)
            )

            Text(
                text = text,
                textAlign = TextAlign.Center,
                color = Color.White,
                fontSize = 30.sp
            )
            Text(
                text = "items 0",
                textAlign = TextAlign.Center,
                color = Color.White
            )
        }
    }
}




@Composable
fun PreviewImageBtns(onClick: () -> Unit, text: String){
    Button(
        onClick = onClick ,
        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
        modifier = Modifier
            .size(120.dp, 60.dp)
            .padding(start = 24.dp)
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
            color = Color.Black,
            modifier = Modifier
                .weight(1f)
        )
    }

    Spacer(modifier = Modifier.width(16.dp))

}