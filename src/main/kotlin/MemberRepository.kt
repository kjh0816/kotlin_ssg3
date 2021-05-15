class MemberRepository {

    @JvmName("getMemberLastId1")
    private fun getMemberLastId(): Int {

        return readIntFromFile("data/member/memberLastId.txt", 0)
    }

    @JvmName("setMemberLastId1")
    private fun setMemberLastId(newId: Int){
        writeIntInFile("data/member/memberLastId.txt", newId)
    }

    private fun getMemberFromFile(filePath: String): Member? {
        val jsonStr = readStrFromFile(filePath)

        if (jsonStr.isEmpty()) {
            return null
        }

        val map = mapFromJson(jsonStr)

        val id = map["id"].toString().toInt()
        val loginId = map["loginId"].toString()
        val loginPw = map["loginPw"].toString()
        val regDate = map["regDate"].toString()
        val updateDate = map["updateDate"].toString()
        val name = map["name"].toString()
        val nickname = map["nickname"].toString()
        val cellphoneNo = map["cellphoneNo"].toString()
        val email = map["email"].toString()

        return Member(id, loginId, loginPw, regDate, updateDate, name, nickname, cellphoneNo, email)

    }

    fun getMembers(): List<Member>{

        val members = mutableListOf<Member>()
        val memberLastId = getMemberLastId()

        for(id in 1 .. memberLastId){
            val member = getMemberFromFile("data/member/${id}.json")
            if(member != null){
                members.add(member)
            }
        }

        return members

    }




    fun join(loginId: String, loginPw: String, name: String, nickname: String, cellphoneNo: String, email: String): Int {

        val id = getMemberLastId() + 1
        setMemberLastId(id)
        val regDate = Util.getNowDateStr()
        val updateDate = Util.getNowDateStr()

        val member = Member(id, loginId, loginPw, regDate, updateDate, name, nickname, cellphoneNo, email)

        writeStrInFile("data/member/${id}.json", member.toJson())

        return id
    }

    fun getMemberByLoginId(loginId: String): Member? {
        for(member in getMembers()){
            if(member.loginId == loginId){
                return member
            }
        }
        return null


    }

    fun getMemberById(memberId: Int): Member? {

        for(member in getMembers()){
            if(member.id == memberId){
                return member
            }
        }
        return null
    }



}