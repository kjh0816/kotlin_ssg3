class BoardRepository {
    fun isUsableName(name: String): Boolean {
        for(board in boards){
            if(board.name == name){
                return false
            }
        }
        return true

    }

    fun isUsableCode(code: String): Boolean {
        for(board in boards){
            if(board.code == code){
                return false
            }
        }
        return true

    }

    fun add(memberId: Int, name: String, code: String) {

        val id = ++boardLastId
        val regDate = Util.getNowDateStr()
        val updateDate = Util.getNowDateStr()

        boards.add(Board(id, regDate, updateDate, memberId, name, code))


    }

    fun getBoardById(id: Int): Board? {
        for(board in boards){
            if(board.id == id){
                return board
            }
        }
        return null

    }

    fun delete(board: Board) {
        boards.remove(board)
    }

    fun modify(board: Board, name: String, code: String) {

        board.updateDate = Util.getNowDateStr()
        board.name = name
        board.code = code

    }

    fun getList() {



        for(board in boards){
            val member = memberRepository.getMemberById(board.memberId)
            print("${board.id} / ${board.name} / ${board.code} / ${member!!.id} / ${board.regDate} /")
        }
    }

    fun getBoardByCode(boardCode: String): Board? {
        for(board in boards){
            if(board.code == boardCode){
                return board
            }
        }
        return null

    }

    var boardLastId = 0
    private val boards = mutableListOf<Board>()
}