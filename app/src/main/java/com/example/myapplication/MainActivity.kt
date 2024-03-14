package com.example.myapplication

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.window.OnBackInvokedCallback
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImagePainter.State.Empty.painter
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.android.awaitFrame

class MainActivity : ComponentActivity() {


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val navController = rememberNavController()
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                NavHost(navController = navController, startDestination = "screen1") {
                    composable(route = "screen1") {
                        ScreenOne(onClick = {
                            Log.d("BUGBUG","Onclick")
                            navController.navigate(route = "screen2")})}
                    composable(route = "screen2") { ScreenTwo()}
                }
            }
        }

        onBackInvokedDispatcher.registerOnBackInvokedCallback(0,
            object : OnBackInvokedCallback {
                override fun onBackInvoked() {

                }
            })
    }
}


@Composable
fun ScreenOne (onClick : () -> Unit = {}) {
    Column (modifier = Modifier
        .background(Color.Yellow)
        .fillMaxSize(1f)){
        var showDialog by remember { mutableStateOf(false) }
        Button(onClick = { showDialog = !showDialog }) {
            Text(text = "Push me")
        }
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .background(color = Color.Cyan)
        ) {

        }
        BottomPane(
            modifier = Modifier.fillMaxWidth(1f)
        )
        if (showDialog)
            FeedbackDialog(onSubmit = {it ->}, onCancel = {  })

    }
}

@Preview
@Composable
fun previewFeedback() {
    FeedbackDialog(onSubmit = { it -> },
        onCancel = {})
}

@Composable
private fun FeedbackDialog(
    onSubmit : (String) -> Unit,
    onCancel : () -> Unit,
    presets  : List<String> = listOf("Not what I asked", "Poor quality", "Hallucination")
) {
    var text by remember { mutableStateOf("") }
    var selectedPretext by remember { mutableIntStateOf(2) }
    val focusRequester = FocusRequester()

    LaunchedEffect(Unit) {
        awaitFrame()
        focusRequester.requestFocus()
    }

    Box(modifier = Modifier
        .fillMaxSize(1f)
        .background(color = Color.Transparent))
    {
        Box(
            modifier = Modifier
                .fillMaxWidth(1f)
                .height(260.dp)
                .align(Alignment.BottomCenter)
                .background(color = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column {
                Text(
                    "Tell us more",
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                OutlinedTextField(value = text,
                    onValueChange = { text = it },
                    modifier = Modifier
                        .padding(top = 4.dp, bottom = 8.dp, start = 4.dp, end = 4.dp)
                        .fillMaxHeight(1f)
                        .fillMaxWidth(1f)
                        .weight(1f)
                        .focusRequester(focusRequester = focusRequester)
                        .align(Alignment.CenterHorizontally),
                    maxLines = 4,
                    minLines = 4,
                    placeholder = { Text(text = "Please share more with us",
                        color = MaterialTheme.colorScheme.onBackground) }
                )
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(start = 16.dp)
                ) {
                    itemsIndexed(presets)

                    { index: Int, text: String ->
                        val backgroundColor =
                            if (index == selectedPretext) Color.Gray else Color.Transparent
                        Text(text = text,
                            modifier = Modifier
                                .border(
                                    width = 1.dp, color = Color(0xff3c3f41),
                                    shape = RoundedCornerShape(percent = 50)
                                )
                                .background(
                                    color = backgroundColor,
                                    shape = RoundedCornerShape(percent = 50)
                                )
                                .padding(start = 8.dp, end = 8.dp, top = 4.dp, bottom = 4.dp)
                                .clickable {
                                    if (index == selectedPretext)
                                        selectedPretext = -1
                                    else
                                        selectedPretext = index
                                })
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }

                Row(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Button(onClick = {
                        onSubmit(text)
                        Log.d("BUGBUG", "on submit clicked")
                    }) {
                        Text(text = "Share feedbak")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        onCancel()
                        Log.d("BUGBUG", "on cancel clicked")
                    }) {
                        Text(text = "Cancel")
                    }
                }
            }
        }
    }
}



@Composable
fun BottomPane (
    modifier: Modifier = Modifier
) {
    var mode : PaneMode by remember { mutableStateOf(PaneMode.MINIMIZED) }

    var paneHeight : Dp = 56.dp
    var paneBackgroundColor : Color = Color(0xff333333)
    var paneShape : Shape = RoundedCornerShape(0.dp)
    val onChangeMode : (newMode : PaneMode) -> Unit = {
        mode = it
    }

    val isKeyboardOpen by keyboardAsState()

    when (mode) {
        PaneMode.MINIMIZED -> {
            paneHeight = 56.dp
            paneBackgroundColor = Color.Black
            paneShape = RoundedCornerShape(0.dp)
        }

        else -> {
            paneHeight = 160.dp
            paneBackgroundColor = Color(0xff333333)
            paneShape = RoundedCornerShape(topStart =  16.dp, topEnd = 16.dp)
        }
    }

    Box(
        modifier =
        modifier
            .background(color = Color(0xff333333), shape = paneShape)
            .clip(paneShape)
            .animateContentSize { initialValue, targetValue -> }
            .height(paneHeight)
    ) {
        when (mode) {
            PaneMode.MINIMIZED -> {
                MinimalPane(modifier = Modifier.align(Alignment.Center),
                    onTextClick = onChangeMode)
            }

            PaneMode.VOICE -> {

                VoiceInputPane()
            }
            PaneMode.TEXT -> {
                TextInputPane(paneBackgroundColor, onCancel = {
                    mode = PaneMode.MINIMIZED
                })
            }
        }
    }
}

@Composable
private fun VoiceInputPane() {
    val infiniteAnimation = rememberInfiniteTransition()
    val rotation by infiniteAnimation.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = Easing { fraction -> fraction * 360 }),
            repeatMode = RepeatMode.Restart
        )
    )

    Box(
        modifier = Modifier
            .fillMaxWidth(1f)
            .fillMaxHeight(1f)
    ) {
        Text(
            text = "this is where you dictate",
            modifier = Modifier.padding(16.dp),
            color = Color(0xff5f605f),
            fontSize = 28.sp,
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .width(48.dp)
                .height(48.dp)
        ) {

            Image(
                painter = painterResource(id = R.drawable.mic_anim),
                modifier = Modifier.rotate(rotation),
                contentDescription = ""
            )
            Icon(
                painter = painterResource(id = R.drawable.baseline_mic_24),
                modifier = Modifier
                    .align(Alignment.Center),
                contentDescription = ""
            )
        }

    }
}

@Composable
private fun TextInputPane(
    paneBackgroundColor: Color,
    onCancel: () -> Unit,
) {

    var text by remember { mutableStateOf("") }

    val focusRequester = remember { FocusRequester() }

    Box(modifier = Modifier
        .fillMaxHeight(1f)
        .fillMaxWidth(1f))
    {
        TextField(
            value = text,
            placeholder = {
                Text(
                    text = "Make my photo shine!",
                )
            },
            onValueChange = { newText: String -> text = newText },
            modifier = Modifier
                .background(color = paneBackgroundColor)
                .focusRequester(focusRequester),
            colors =
            TextFieldDefaults.colors(
                focusedContainerColor = paneBackgroundColor,
                unfocusedContainerColor = paneBackgroundColor,
                disabledContainerColor = paneBackgroundColor,
                focusedIndicatorColor = paneBackgroundColor,
                unfocusedIndicatorColor = paneBackgroundColor
            ),
        )

        IconButton(
            onClick = { onCancel() },
            modifier = Modifier.Companion
                .align(Alignment.TopEnd)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.close_24px),

                tint = Color.White,
                contentDescription = ""
            )
        }

        LaunchedEffect(focusRequester) {
            awaitFrame()
            focusRequester.requestFocus()
        }
    }
}

@Composable
fun keyboardAsState(): State<Boolean> {
    val isImeVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0
    return rememberUpdatedState(isImeVisible)
}

@Composable
private fun MinimalPane(
    modifier : Modifier = Modifier,
    onTextClick : (PaneMode) -> Unit)
{

    Box(
        modifier = modifier
            .border(
                width = 2.dp, color = Color(0xffa8c7fa),
                shape = RoundedCornerShape(percent = 50)
            )/*.background(color= Color.Cyan)*/
            .fillMaxWidth(0.9f)
            .fillMaxHeight(1f)
            .clip(shape = RoundedCornerShape(percent = 50))
            .padding(start = 12.dp, end = 4.dp),
        /*verticalAlignment = Alignment.CenterVertically*/
    ) {

        val containerColor = MaterialTheme.colorScheme.surface // Color(0xFF333333)

        Text(
            text = "Add a photo, type or talk to edit",
            color = Color(0xff8e918f),
            fontSize = 14.sp,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .clickable {
                    Log.d("BUGBUG", "Text was clicked")
                    onTextClick(PaneMode.TEXT)
                }
        )

        //Mic and image button
        Row(
            modifier = Modifier
                .background(
                    color = Color(0xFFd7c4a0),
                    shape = RoundedCornerShape(percent = 50)
                )
                .align(Alignment.CenterEnd)
        ) {
            //mic button
            IconButton(onClick = { onTextClick(PaneMode.VOICE) }) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_mic_24),
                    contentDescription = "",
                    tint = Color.Black,
                )
            }

            //add image button
            IconButton(onClick = { }) {
                Icon(
                    painter = painterResource(id = R.drawable.add_circle_24px),
                    contentDescription = "",
                    tint = Color.Black,
                )
            }
        }
    }
}

@Composable
fun ScreenTwo() {
    Box(modifier = Modifier
        .background(Color.Cyan)
        .fillMaxSize(1f)) {

        Text(text = "This is screen 2", modifier = Modifier.align(Alignment.Center))
    }
}


@Preview
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
//        Greeting("Android")
        ScreenOne {

        }
    }
}

enum class PaneMode {
    MINIMIZED,
    VOICE,
    TEXT
}