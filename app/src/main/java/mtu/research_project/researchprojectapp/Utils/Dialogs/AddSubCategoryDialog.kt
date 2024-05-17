package mtu.research_project.researchprojectapp.Utils.Dialogs

import android.util.Log
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
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import mtu.research_project.researchprojectapp.AppModel.Category
import mtu.research_project.researchprojectapp.Theme.secondaryColor
import mtu.research_project.researchprojectapp.ViewModel.AppViewModel

/**
 * this is used to display a dialog when adding a new subcategory
 *
 * @param onDismiss lambda param to hide the dialog
 * @param onAddCategory lambda param to add the category
 */
@Composable
fun AddSubCategoryDialog(onDismiss: () -> Unit, onAddCategory: (Category) -> Unit, appViewModel: AppViewModel) {
    var subCategoryName by rememberSaveable { mutableStateOf("") }
    var submitWithNoName by mutableStateOf(false)

    Dialog(onDismissRequest = { onDismiss() }) {
        Column(
            modifier = Modifier
                .background(secondaryColor)
                .border(1.dp, Color.White, RoundedCornerShape(8.dp))
        ) {

            Text(
                text = "Add new sub-category",
                modifier = Modifier
                    .padding(12.dp)
            )


            TextField(
                value = subCategoryName,
                onValueChange = { subCategoryName = it },
                label = { Text(text = "Category name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            )

            if (submitWithNoName){
                Text(
                    text = "Please enter a name",
                    modifier = Modifier
                        .padding(start = 12.dp),
                    fontSize = 20.sp
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 10.dp, end = 10.dp, bottom = 5.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ){


                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF101430),
                        contentColor = Color(0xFF00B6CB)
                    )
                ) {
                    Text(text = "Cancel")
                }

                Button(
                    onClick = {
                        if (subCategoryName.isNotBlank()){
                            val newSubCategory = Category(
                                name = subCategoryName,
                            )
                            Log.d("NEW SUB CATEGORY", "$newSubCategory")
                            onAddCategory(newSubCategory)
                        }else{
                            submitWithNoName = true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF101430),
                        contentColor = Color(0xFF00B6CB)
                    )
                ){
                    Text(text = "Add+")
                }
            }
        }
    }
}