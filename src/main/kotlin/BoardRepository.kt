class BoardRepository {


    @JvmName("getBoardLastId1")
    private fun getBoardLastId(): Int {

        return readIntFromFile("data/board/boardLastId.txt", 0)
    }


    @JvmName("setBoardLastId1")
    private fun setBoardLastId(newId: Int){
        writeIntInFile("data/board/boardLastId.txt", newId)
    }

    private fun getBoardFromFile(filePath: String): Board? {
        val jsonStr = readStrFromFile(filePath)

        if (jsonStr.isEmpty()) {
            return null
        }

        val map = mapFromJson(jsonStr)

        val id = map["id"].toString().toInt()
        val regDate = map["regDate"].toString()
        val updateDate = map["updateDate"].toString()
        val memberId = map["memberId"].toString().toInt()
        val name = map["name"].toString()
        val code = map["code"].toString()

        return Board(id, regDate, updateDate, memberId, name, code)

    }

    fun getBoards(): List<Board>{

        val boards = mutableListOf<Board>()
        val boardLastId = getBoardLastId()

        for(id in 1 .. boardLastId){
            val board = getBoardFromFile("data/board/${id}.json")
            if(board != null){
                boards.add(board)
            }
        }

        return boards

    }



    fun isUsableName(name: String): Boolean {
        for(board in getBoards()){
            if(board.name == name){
                return false
            }
        }
        return true

    }

    fun isUsableCode(code: String): Boolean {
        for(board in getBoards()){
            if(board.code == code){
                return false
            }
        }
        return true

    }

    fun add(memberId: Int, name: String, code: String) {

        val id = getBoardLastId() + 1
        setBoardLastId(id)
        val regDate = Util.getNowDateStr()
        val updateDate = Util.getNowDateStr()

        val board = Board(id, regDate, updateDate, memberId, name, code)
        writeStrInFile("data/board/${id}.json", board.toJson())


    }

    fun getBoardById(id: Int): Board? {
        for(board in getBoards()){
            if(board.id == id){
                return board
            }
        }
        return null

    }

    fun delete(board: Board) {
        deleteFile("data/board/${board.id}.json")
    }

    fun modify(board: Board, name: String, code: String) {

        board.updateDate = Util.getNowDateStr()
        board.name = name
        board.code = code

        writeStrInFile("data/board/${board.id}.json", board.toJson())

    }

    fun getList() {



        for(board in getBoards()){
            val member = memberRepository.getMemberById(board.memberId)
            print("${board.id} / ${board.name} / ${board.code} / ${member!!.id} / ${board.regDate}")
        }
    }

    fun getBoardByCode(boardCode: String): Board? {
        for(board in getBoards()){
            if(board.code == boardCode){
                return board
            }
        }
        return null

    }


}