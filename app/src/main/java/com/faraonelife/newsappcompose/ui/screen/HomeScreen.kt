package com.faraonelife.newsappcompose.ui.screen

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource

import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.faraonelife.newsappcompose.R
import com.faraonelife.newsappcompose.getDisplayDate
import com.faraonelife.newsappcompose.models.Article
import com.faraonelife.newsappcompose.states.HomeState
import com.faraonelife.newsappcompose.states.NewsCategory
import com.faraonelife.newsappcompose.viewmodels.HomeVM



@Composable
fun HomeScreen(vm: HomeVM) {
    val context = LocalContext.current
    val state by vm.state.collectAsState()

    HomeScreen(state = state, onSearchQuery = vm::loadNews, onNewsOrgClick = {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("https://newsapi.org")
        }
        context.startActivity(intent)
    }, onTabSelected = { category, index ->
        vm.loadHeadLines(category, index)
    }, onNewsClick = { news ->
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(news.url)
        }
        context.startActivity(intent)
    })
}

@Composable
private fun HomeScreen(
    state: HomeState,
    onSearchQuery: (String) -> Unit,
    onNewsOrgClick: () -> Unit,
    onTabSelected: (NewsCategory, Int) -> Unit,
    onNewsClick: (Article) -> Unit
) {
    Scaffold(topBar = {
        TopBar(
            state = state, onSearchQuery = onSearchQuery, onTabSelected = onTabSelected
        )
    }, ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding), Alignment.Center
        ) {
            when {
                state.isLoading.not() && state.error != null -> {
                    Failure(error = state.error ?: "Something went wrong")
                }
                state.isLoading -> {
                    CircularProgressIndicator()
                }
                else -> {
                    NewsListing(news = state.news, onNewsClick)
                }
            }
        }
    }
}

@Composable
private fun TopBar(state: HomeState, onSearchQuery: (String) -> Unit, onTabSelected: (NewsCategory, Int) -> Unit) {
    var text by remember { mutableStateOf("") }

    LaunchedEffect(text) {
        onSearchQuery(text)
    }

    Column(
        Modifier
            .fillMaxWidth()
            .padding(top = 5.dp)
    ) {
        TextField(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .clip(RoundedCornerShape(8.dp))
            .height(56.dp), value = text, onValueChange = {
            text = it
        }, leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon", tint = Color.Gray)
        }, trailingIcon = {
            if (text.isNotEmpty()) {
                Icon(modifier = Modifier.clickable {
                    text = ""
                }, imageVector = Icons.Default.Clear, contentDescription = "Clear Icon", tint = Color.Gray)
            }
        }, colors = TextFieldDefaults.textFieldColors(
            unfocusedIndicatorColor = Color.Transparent, focusedIndicatorColor = Color.Transparent
        ), label = {
            Text(text = "Search News")
        })

        NewsCategoriesTab(
            showTabs = state.showNewCategoryTabs,
            newsCategory = state.newsCategories,
            selectedTabRowIndex = state.selectedCategoryIndex,
            onTabSelected = onTabSelected
        )
    }
}

@Composable
private fun Failure(error: String) {
    Text(text = error)
}

@Composable
private fun NewsCategoriesTab(
    showTabs: Boolean, newsCategory: List<NewsCategory>, selectedTabRowIndex: Int, onTabSelected: (NewsCategory, Int) -> Unit
) {
    if (showTabs && newsCategory.isNotEmpty())
        ScrollableTabRow(
            selectedTabIndex = selectedTabRowIndex, edgePadding = 0.dp, backgroundColor = Color.Transparent
        ) {
            newsCategory.forEachIndexed { index, newsCategory ->
                Tab(modifier = Modifier.height(50.dp), selected = selectedTabRowIndex == index, onClick = {
                    if (selectedTabRowIndex != index) {
                        onTabSelected(newsCategory, index)
                    }
                }, text = { Text(text = newsCategory.displayName) })
            }
        }
}

@Composable
private fun NewsListing(news: List<Article>, onNewsClick: (Article) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        itemsIndexed(news) { index, item ->
            NewsItem(news = item, onNewsClick = onNewsClick)
        }
    }
}

@Composable
private fun NewsItem(news: Article, onNewsClick: (Article) -> Unit) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onNewsClick(news) }) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
        ) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop,
                model = news.urlToImage,
                placeholder = painterResource(id = R.drawable.placeholder1),
                error = painterResource(id = R.drawable.placeholder1),
                contentDescription = ""
            )
        }
        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 5.dp, bottom = 5.dp), Arrangement.SpaceBetween
        ) {
            Text(text = news.author ?: "", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
            Text(text = news.getDisplayDate(), fontSize = 12.sp, fontWeight = FontWeight.Normal)
        }
        if (news.title != null) {
            Text(text = news.title, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
    }
}

