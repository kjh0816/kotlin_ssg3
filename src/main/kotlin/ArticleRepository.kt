class ArticleRepository {


    @JvmName("getArticleLastId1")
    private fun getArticleLastId(): Int {

        return readIntFromFile("data/article/articleLastId.txt", 0)
    }



    @JvmName("setArticleLastId1")
    private fun setArticleLastId(newId: Int){
        writeIntInFile("data/article/articleLastId.txt", newId)
    }

    private fun getArticleFromFile(filePath: String): Article? {
        val jsonStr = readStrFromFile(filePath)

        if (jsonStr.isEmpty()) {
            return null
        }

        val map = mapFromJson(jsonStr)

        val id = map["id"].toString().toInt()
        val regDate = map["regDate"].toString()
        val updateDate = map["updateDate"].toString()
        val memberId = map["memberId"].toString().toInt()
        val boardId = map["boardId"].toString().toInt()
        val title = map["title"].toString()
        val body = map["body"].toString()

        return Article(id, regDate, updateDate, memberId, boardId, title, body)

    }

    @JvmName("getArticles1")
    fun getArticles(): MutableList<Article>{

        val articles = mutableListOf<Article>()
        val articleLastId = getArticleLastId()

        for(id in 1 .. articleLastId){
            val article = getArticleFromFile("data/article/${id}.json")
            if(article != null){
                articles.add(article)
            }
        }

        return articles

    }


    fun write(memberId: Int, boardId: Int, title: String, body: String): Int {

        val id = getArticleLastId() + 1
        setArticleLastId(id)
        val regDate = Util.getNowDateStr()
        val updateDate = Util.getNowDateStr()

        val article = Article(id, updateDate, regDate, memberId, boardId, title , body)
        writeStrInFile("data/article/${article.id}.json", article.toJson())

        return id

    }

    fun getArticleById(id: Int): Article? {
        for(article in getArticles()){
            if(article.id == id){
                return article
            }
        }
        return null

    }

    fun delete(article: Article) {
        deleteFile("data/article/${article.id}.json")
    }

    fun modify(article: Article, title: String, body: String) {

        article.updateDate = Util.getNowDateStr()
        article.title = title
        article.body = body

        writeStrInFile("data/article/${article.id}.json", article.toJson())
    }

    fun getFilteredArticles(searchKeyword: String, boardCode: String, page: Int, itemCountInAPage: Int): List<Article> {
        val filtered1Articles = getSearchKeywordFilteredArticles(getArticles(), searchKeyword, boardCode)
        val filtered2Articles = getPageFilteredArticles(filtered1Articles, page, itemCountInAPage)

        return filtered2Articles

    }



    private fun getSearchKeywordFilteredArticles(
        articles: MutableList<Article>,
        searchKeyword: String,
        boardCode: String
    ): List<Article> {

        val filteredArticles = mutableListOf<Article>()

        val board = boardRepository.getBoardByCode(boardCode)
        var boardId = 0
        if(board != null){
            boardId = board.id
        }
        if(searchKeyword.isNotEmpty() && boardId != 0){
            for(article in articles){
                if(article.title.contains(searchKeyword) && article.boardId == boardId){
                    filteredArticles.add(article)
                }
            }
            return filteredArticles
        }
        else if(searchKeyword.isNotEmpty() && boardId == 0){
            for(article in articles){
                if(article.title.contains(searchKeyword)){
                    filteredArticles.add(article)
                }
            }
            return filteredArticles
        }
        else if(searchKeyword.isEmpty() && boardId != 0){
            for(article in articles){
                if(article.boardId == boardId){
                    filteredArticles.add(article)
                }
            }
            return filteredArticles
        }else{
            return articles
        }

    }

    private fun getPageFilteredArticles(filtered1Articles: List<Article>, page: Int, itemCountInAPage: Int): List<Article> {


        val filteredArticles = mutableListOf<Article>()

        val offsetCount = (page - 1) * itemCountInAPage

        val startIndex = filtered1Articles.lastIndex - offsetCount
        var endIndex = startIndex - (itemCountInAPage - 1)

        if(endIndex < 0){
            endIndex = 0
        }

        for(i in startIndex downTo endIndex){
            filteredArticles.add(filtered1Articles[i])
        }

        return filteredArticles


    }


}