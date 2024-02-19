package mtu.research_project.researchprojectapp.Utils

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import mtu.research_project.researchprojectapp.Theme.secondaryColor
import mtu.research_project.researchprojectapp.AppModel.Category


@Composable
fun AddCategoryDialog(onDismiss: () -> Unit, onAddCategory: (Category) -> Unit) {
    var categoryName by rememberSaveable { mutableStateOf("") }

    Dialog(onDismissRequest = { onDismiss() }) {
        Column(
            modifier = Modifier
                .background(secondaryColor)
                .border(1.dp, Color.White, RoundedCornerShape(8.dp))
        ) {

            Text(text = "Add new category")


            TextField(
                value = categoryName,
                onValueChange = { categoryName = it },
                label = { Text(text = "Category name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.End
            ){
                Button(
                    onClick = {
                              if (categoryName.isNotBlank()){
                                  val newCategory = Category(categoryName)
                                  onAddCategory(newCategory)
                              }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF101430),
                        contentColor = Color(0xFF00B6CB)
                    )
                ){
                    Text(text = "Add+")
                }

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF101430),
                        contentColor = Color(0xFF00B6CB)
                    )
                ) {
                    Text(text = "Cancel")
                }
            }
        }
    }
}