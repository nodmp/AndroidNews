package com.prsixe.anews.glance

import android.content.Context
import android.util.Log

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.ColorFilter
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalSize
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.CircularProgressIndicator
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.text.FontFamily
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.appwidget.lazy.items
import androidx.glance.background
import androidx.glance.color.ColorProvider
import androidx.glance.layout.Box
import androidx.glance.text.TextStyle
import com.prsixe.anews.MainActivity
import com.prsixe.anews.R
import com.prsixe.anews.WebViewActivity


private val destinationKey = ActionParameters.Key<String>(
    WebViewActivity.URL
)


class AndroidNewsGlanceAppWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            Content()
        }
    }

    override val sizeMode: SizeMode
        get() = SizeMode.Exact

}


@Composable
private fun Content() {
    val androidNewsInfo by AndroidNesRepo.currentInfo.collectAsState()
    GlanceTheme {
        when (val info = androidNewsInfo) {
            AndroidNewsInfo.Loading -> {
                Box(
                    modifier = GlanceModifier.fillMaxSize().padding(16.dp).appWidgetBackground()
                        .background(GlanceTheme.colors.background),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }

            is AndroidNewsInfo.Success -> {
                News(response = info.response, lastUpdate = info.updateTime)
            }

            AndroidNewsInfo.Error -> {
                Box(
                    modifier = GlanceModifier.fillMaxSize().padding(16.dp).appWidgetBackground()
                        .background(GlanceTheme.colors.background),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        "Data not available,Click Refresh it",
                        modifier = GlanceModifier.clickable(actionRunCallback<RefreshArticle>()),
                        style = TextStyle(
                            color = ColorProvider(
                                day = Color.Black,
                                night = Color.White
                            )
                        )
                    )
                }
            }
        }
    }

}


@Composable
fun News(response: Response,lastUpdate:String, modifier: GlanceModifier = GlanceModifier) {
    val current = LocalSize.current
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .appWidgetBackground()
            .background(GlanceTheme.colors.background),
        verticalAlignment = Alignment.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Row(modifier = GlanceModifier) {
            Image(
                provider = ImageProvider(R.drawable.sync_time),
                contentDescription = null,
                modifier = GlanceModifier.size(20.dp)
                    .clickable(actionRunCallback<RefreshArticle>()),
                colorFilter = ColorFilter.tint(
                    ColorProvider(
                        day = Color.Black,
                        night = Color.White
                    )
                )
            )
            Spacer(modifier = GlanceModifier.padding(2.dp))
            Text(
                text = if (current.width > 200.dp) lastUpdate else lastUpdate.split(" ")[1],
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = ColorProvider(day = Color.Black, night = Color.White)
                ),
                modifier = GlanceModifier.fillMaxWidth().defaultWeight()
            )
        }
        if (current.width > 200.dp) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(provider = ImageProvider(R.drawable.android), contentDescription = null)
                Text(
                    text = "Android News",
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif,
                        color = ColorProvider(day = Color.Black, night = Color.White)
                    ),
                )
            }
        }else{
            Column(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    modifier = GlanceModifier.padding(8.dp),
                    text = "Android News",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = ColorProvider(day = Color.Black, night = Color.White)
                    ),
                )
            }
        }
        LazyColumn(
            modifier = GlanceModifier
        ) {
            items(response.page.articles) { article ->
                Row(
                    modifier = GlanceModifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        modifier = GlanceModifier.clickable(
                            actionStartActivity<WebViewActivity>(
                                actionParametersOf(destinationKey to article.link)
                            )
                        ),
                        text = article.title,
                        style = TextStyle(
                            fontSize = 14.sp,
                            color = ColorProvider(day = Color.Black, night = Color.White)
                        ),
                        maxLines = 1,
                    )
                }
            }
        }
    }
}

class RefreshArticle : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters) {
        AndroidNesRepo.updateArticles()
    }
}