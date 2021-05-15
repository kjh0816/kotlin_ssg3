class ArticleController {
    fun write(req: Req) {
        if(loginedMember == null){
            println("로그인 후 이용해주세요.")
            return
        }


        println("게시판 번호를 선택해주세요.")
        boardRepository.getList()
        val boardId = readLineTrim().toInt()
        val board = boardRepository.getBoardById(boardId)
        if(board == null){
            println("${boardId}번 게시판은 존재하지 않습니다.")
            return
        }
        println("제목을 입력해주세요.")
        val title = readLineTrim()
        println("내용을 입력해주세요.")
        val body = readLineTrim()

        val memberId = loginedMember!!.id

        val id = articleRepository.write(memberId, board.id, title, body)
        println("${id}번 게시물이 추가되었습니다.")

    }

    fun delete(req: Req) {
        if(loginedMember == null){
            println("로그인 후 이용해주세요.")
            return
        }
        val id = req.getIntParam("id", 0)
        if(id == 0){
            println("게시물 번호를 숫자로 입력해주세요.")
            return
        }
        val article = articleRepository.getArticleById(id)
        if(article == null){
            println("${id}번 게시물은 존재하지 않습니다.")
            return
        }
        if(article.memberId != loginedMember!!.id){
            println("권한이 없습니다.")
            return
        }

        articleRepository.delete(article)
        println("${id}번 게시물이 삭제되었습니다.")

    }

    fun modify(req: Req) {

        if(loginedMember == null){
            println("로그인 후 이용해주세요.")
            return
        }
        val id = req.getIntParam("id", 0)
        if(id == 0){
            println("게시물 번호를 숫자로 입력해주세요.")
            return
        }
        val article = articleRepository.getArticleById(id)
        if(article == null){
            println("${id}번 게시물은 존재하지 않습니다.")
            return
        }
        if(article.memberId != loginedMember!!.id){
            println("권한이 없습니다.")
            return
        }

        println("게시판 번호를 선택해주세요.")
        boardRepository.getList()
        val boardId = readLineTrim().toInt()
        val board = boardRepository.getBoardById(boardId)
        if(board == null){
            println("${boardId}번 게시판은 존재하지 않습니다.")
            return
        }
        println("제목을 입력해주세요.")
        val title = readLineTrim()
        println("내용을 입력해주세요.")
        val body = readLineTrim()



        articleRepository.modify(article, title, body)
        println("${id}번 게시물이 수정되었습니다.")
    }

    fun detail(req: Req) {
        val id = req.getIntParam("id", 0)
        if(id == 0){
            println("게시물 번호를 숫자로 입력해주세요.")
            return
        }
        val article = articleRepository.getArticleById(id)
        if(article == null){
            println("${id}번 게시물은 존재하지 않습니다.")
            return
        }

        val member = memberRepository.getMemberById(article.memberId)
        val board = boardRepository.getBoardById(article.boardId)

        println("번호       : ${article.id}")
        println("제목       : ${article.title}")
        println("내용       : ${article.body}")
        println("작성자      : ${member!!.nickname}")
        println("게시판      : ${board!!.name}")
        println("최초 생성날짜: ${article.regDate}")
        println("수정날짜    : ${article.updateDate}")


    }

    fun list(req: Req) {

        val searchKeyword = req.getStrParam("searchKeyword", "")
        val page = req.getIntParam("id",1)
        val boardCode = req.getStrParam("boardCode", "")

        val itemCountInAPage = 10

        val filteredArticles = articleRepository.getFilteredArticles(searchKeyword, boardCode, page, itemCountInAPage)

        println("번호 / 제목 / 내용 / 작성자 / 게시판 / 수정날짜")
        for(article in filteredArticles){
            val member = memberRepository.getMemberById(article.memberId)
            val board = boardRepository.getBoardById(article.boardId)

            println("${article.id} / ${article.title} / ${article.body} / ${member!!.nickname} / ${board!!.name} / ${article.updateDate}")



        }


    }
}