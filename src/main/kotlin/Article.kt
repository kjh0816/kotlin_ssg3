data class Article(
    val id: Int,

    val regDate: String,
    var updateDate: String,

    val memberId: Int,
    val boardId: Int,

    var title: String,
    var body: String
)