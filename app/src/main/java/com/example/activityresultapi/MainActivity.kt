package com.example.activityresultapi

import android.Manifest
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import coil.compose.rememberImagePainter
import com.example.activityresultapi.ui.theme.ActivityResultApiTheme
import com.example.activityresultapi.ui.theme.Purple500

class MainActivity : ComponentActivity() {

    private val permission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) openCamera()
        }

    private val takePictureLauncher = registerForActivityResult(TakePhotoContract()) {
        setPicture(it)
    }

    private val uriData: MutableLiveData<Uri> = MutableLiveData(Uri.EMPTY)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ActivityResultApiTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    PictureTaker(::onTakePictureClicked, uriData)
                }
            }
        }
    }

    private fun onTakePictureClicked() {
        permission.launch(Manifest.permission.CAMERA)
    }

    private fun openCamera() {
        takePictureLauncher.launch(getExternalFilesDir(Environment.DIRECTORY_PICTURES))
    }

    private fun setPicture(uri: Uri) {
        uriData.postValue(uri)
    }
}

@Composable
fun PictureTaker(
    onButtonClicked: () -> Unit,
    uriData: MutableLiveData<Uri>,
) {
    val uriState = remember { uriData }

    Surface(color = MaterialTheme.colors.background) {
        Column {
            Image(
                painter = rememberImagePainter(data = uriState.observeAsState().value),
                contentDescription = "",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp,16.dp)
                    .aspectRatio(0.75f)
                    .align(Alignment.CenterHorizontally)
            )
            Box(
                Modifier.fillMaxWidth().padding(0.dp, 50.dp)
            ) {
                OutlinedButton(
                    onClick = onButtonClicked,
                    shape = CircleShape,
                    border = BorderStroke(1.dp, Purple500),
                    modifier = Modifier
                        .size(50.dp)
                        .align(Alignment.Center),
                    colors = ButtonDefaults.outlinedButtonColors(
                        backgroundColor = Purple500,
                        contentColor = Color.White
                    ),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_baseline_photo_camera_24),
                        contentDescription = ""
                    )
                }
            }
        }
    }
}