package mtu.research_project.researchprojectapp.Utils

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreateNewFolder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mtu.research_project.researchprojectapp.Theme.secondaryColor
import mtu.research_project.researchprojectapp.ViewModel.AppViewModel

/**
 * a custom texttfield to be used across the application
 *
 * @param value the typed text
 * @param onValueChange lambda param
 * @param placeholder the placeholder text
 * @param icon icon to be displayed
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: () -> String?,
    icon: ImageVector?,
    modifier: Modifier
) {
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 20.dp)
            .size(height = 56.dp, width = 360.dp),
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholder() ?: "",
                color = Color.Black
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

/**
 *  used to display categories
 *
 *  @param text name of category
 *  @param onClick lambda for click function
 *  @param fileCount number of items within
 */
@Composable
fun CategoryBox(text: String, onClick: () -> Unit, modifier: Modifier, fileCount: Int){

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
                text = "items $fileCount",
                textAlign = TextAlign.Center,
                color = Color.White
            )
        }
    }
}


/**
 * Btns to be used on image preview screen
 *
 * @param onClick lambda from click function
 * @param text the buttons text
 */
@Composable
fun PreviewImageBtns(onClick: () -> Unit, text: String){
    Button(
        onClick = onClick ,
        colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
        modifier = Modifier
            .size(140.dp, 60.dp)
    ) {
        Text(
            text = text,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            color = secondaryColor,
            modifier = Modifier
                .weight(1f)
        )
    }
}

/**
 * a custom text field to be used for the filtered text field
 *
 * @param value the typed text
 * @param onValueChange lambda param
 * @param placeholder the placeholder text
 * @param icon icon to be displayed
 * @param appViewModel the apps viewmodel
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilteredCustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    icon: ImageVector?,
    appViewModel: AppViewModel,
    modifier: Modifier
) {
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 20.dp)
            .size(height = 56.dp, width = 360.dp),
        value = value,
        onValueChange = {
            onValueChange(it)
            appViewModel.setSearchQuery(it)
        },
        placeholder = {
            Text(
                text = placeholder,
                color = Color.Black
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