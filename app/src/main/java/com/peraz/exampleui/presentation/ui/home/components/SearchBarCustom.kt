package com.peraz.exampleui.presentation.ui.home.components


import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.peraz.exampleui.R
import com.peraz.exampleui.data.remote.ColProductModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarCustom(
    modifier: Modifier, products: SnapshotStateList<ColProductModel>,
    onClick: (Int) -> Unit
) {
    var oltxtfield by remember { mutableStateOf("") }
    var textFieldState by remember{mutableStateOf(false)}
    var scope = rememberCoroutineScope()
    val onActiveChange = {}

    DockedSearchBar(
        inputField = {
            SearchBarDefaults.InputField(
                query = oltxtfield,
                onQueryChange = { oltxtfield=it},
                onSearch = { textFieldState=!textFieldState},
                expanded = textFieldState,
                onExpandedChange = {onActiveChange},
                leadingIcon = {
                    if (textFieldState==false){
                        Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.height(20.dp).clickable{
                            textFieldState=true
                        }) {
                            Icon(painterResource(R.drawable.zoomfilled), contentDescription = null, modifier= Modifier.size(45.dp))
                            Text(text="Buscar", modifier = Modifier.fillMaxSize().wrapContentHeight(align = Alignment.CenterVertically), fontSize = 22.sp)
                        }
                    }
                },
                trailingIcon = {
                    Icon(painterResource(R.drawable.baseline_arrow_back_24), contentDescription = null, modifier= Modifier.size(40.dp).clickable{
                        textFieldState=false
                    })
                },
            )
        },
        expanded = textFieldState,
        onExpandedChange = {onActiveChange},
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp)
            .clip(shape = RoundedCornerShape(10.dp)) //Este da forma redondeada a la parte expandida
            ,
    ){
        LazyColumn {
            items(products.size){
                item->
                if (products[item].name.startsWith(oltxtfield, ignoreCase = true)){
                    Text(text = products[item].name, modifier = Modifier.clickable{
                        onClick(products[item].id)
                    }.fillMaxWidth())
                }
            }
        }
    }



}

@Preview
@Composable
fun SearchBarCustomPreview(){
}