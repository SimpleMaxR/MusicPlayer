package com.example.musicplayer

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Pause
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.musicplayer.ui.theme.MusicPlayerTheme

class MainActivity : ComponentActivity() {
    private lateinit var networkChangeReceiver: NetworkChangeReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MusicPlayerScreen()
        }
        networkChangeReceiver = NetworkChangeReceiver(this)
        networkChangeReceiver.startMonitoring()
    }

    override fun onDestroy() {
        super.onDestroy()
        networkChangeReceiver.stopMonitoring()
    }
}

@Composable
fun MusicPlayerScreen(){
    var status by remember {
        mutableStateOf("Status:Stopped")
    }
    val context = LocalContext.current
    var currentCover by remember { mutableStateOf(R.drawable.dream_cover) } // 当前专辑封面

    Box(modifier = Modifier) {
        Image(
            painter = painterResource(id = R.drawable.ocean),
            contentDescription = "shows the ocean, water is blue",
            modifier = Modifier
                .fillMaxSize()
                .blur(radius = 16.dp),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            // 专辑封面
            Image(
                painter = painterResource(id = currentCover),
                contentDescription = "The song's cover image",
                modifier = Modifier,
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(8.dp))

            //状态信息
            Text(text = status, style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.ExtraBold
            ))
            Spacer(modifier = Modifier.height(32.dp))

            // 操作按钮
            Row(modifier = Modifier) {
                IconButton(onClick = {
                    context.startService(Intent(context, MusicService::class.java).apply { action = "PLAY" })
                    status = "Status: Playing"
                },
                    modifier = Modifier.background(MaterialTheme.colorScheme.background, shape = CircleShape)) {
                    Icon(imageVector = Icons.Outlined.PlayArrow, contentDescription = "")
                }
                Spacer(modifier = Modifier.width(16.dp))
                IconButton(onClick = {
                    context.startService(Intent(context, MusicService::class.java).apply { action = "PAUSE" })
                    status = "Paused"
                },
                    modifier = Modifier.background(MaterialTheme.colorScheme.background, shape = CircleShape)) {
                    Icon(imageVector = Icons.Outlined.Pause, contentDescription = "")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 下一首按钮
            Button(onClick = {
                context.startService(Intent(context, MusicService::class.java).apply { action = "NEXT" })
                status = "Playing"
                // 更新专辑封面资源 ID
                if (currentCover == R.drawable.dream_cover) {
                    currentCover = R.drawable.sweet_cover
                } else {
                    currentCover = R.drawable.dream_cover
                }
            }, modifier = Modifier
                    .clip(RoundedCornerShape(0.dp)
                )) {
                Text(text = "Next")
            }
            Spacer(modifier = Modifier.height(16.dp))

        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MusicPlayerTheme {
        MusicPlayerScreen()
    }
}