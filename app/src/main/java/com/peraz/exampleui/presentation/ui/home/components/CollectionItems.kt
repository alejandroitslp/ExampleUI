package com.peraz.exampleui.presentation.ui.home.components

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.peraz.exampleui.data.RandomCollModel
import com.peraz.exampleui.data.RetrofitInstance
import com.peraz.exampleui.presentation.ui.theme.dark_blue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

@Composable
fun CollectionItems(
    collectioName: String? = null,
    image: String? = null,
    modifier: Modifier = Modifier,
    onClick: (RandomCollModel?) -> Unit,
    scope: CoroutineScope,
    context: Context,
    id: Int?
){
    var collections: RandomCollModel? = null
    Column(modifier= Modifier.padding(10.dp)
        .height(125.dp).width(85.dp)
        .border(width = .3.dp,
            color = Color.LightGray,
            shape = RoundedCornerShape(10.dp))
        .background(color = Color.White,
            shape = RoundedCornerShape(10.dp))
        .clickable(onClick =
            {
            scope.launch(Dispatchers.IO) {
                val response = try{
                    RetrofitInstance.api.getSpecificCollection(id)
                }catch(e: IOException){
                    Toast.makeText(context, "$e" , Toast.LENGTH_LONG).show()
                    return@launch
                }
                withContext(Dispatchers.Main) {
                    collections=response.body()
                    if(collections!=null){
                        onClick(collections)
                    }
                }
            }

        }
        ),
        verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Image(painter = rememberAsyncImagePainter(image), contentDescription = null, modifier = Modifier.size(50.dp).clip(shape = CircleShape).background(color = Color.LightGray).alpha(1f).padding(5.dp))
        Spacer(modifier=Modifier.height(6.dp))
        Text(text= collectioName.toString(), color = dark_blue, fontSize = 13.sp)
    }
}


@Preview(showBackground = true)
@Composable
fun CollectionItemsPreview(){
}