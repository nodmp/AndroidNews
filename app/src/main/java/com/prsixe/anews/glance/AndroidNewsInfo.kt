package com.prsixe.anews.glance

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


sealed interface AndroidNewsInfo {
    data class Success(val response: Response, val updateTime: String) : AndroidNewsInfo {}
    object Error : AndroidNewsInfo {}
    object Loading : AndroidNewsInfo {}
}


@Serializable
data class Response(
    @SerialName("data") val page: Page,
    val errorCode: Int,
    val errorMsg: String
)

@Serializable
data class Page(
    val curPage: Int,
    @SerialName("datas") val articles: List<Article>,
    val offset: Int,
    val over: Boolean,
    val pageCount: Int,
    val size: Int,
    val total: Int,
)

@Serializable
data class Article(
    val adminAdd: Boolean,
    val apkLink: String,
    val audit: Int,
    val author: String,
    val canEdit: Boolean,
    val chapterId: Int,
    val chapterName: String,
    val collect: Boolean,
    val courseId: Int,
    val desc: String,
    val descMd: String,
    val envelopePic: String,
    val fresh: Boolean,
    val host: String,
    val id: Int,
    val isAdminAdd: Boolean,
    val link: String,
    val niceDate: String,
    val niceShareDate: String,
    val origin: String,
    val prefix: String,
    val projectLink: String,
    val publishTime: Long,
    val realSuperChapterId: Int,
    val selfVisible: Int,
    val shareDate: Long,
    val shareUser: String,
    val superChapterId: Int,
    val superChapterName: String,
    val tags: List<Tag>,
    val title: String,
    val type: Int,
    val userId: Int,
    val visible: Int,
    val zan: Int
)

@Serializable
data class Tag(
    val name: String,
    val url: String
)
