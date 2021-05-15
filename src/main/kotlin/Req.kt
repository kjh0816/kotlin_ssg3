class Req(command: String) {


    val actionPath: String

    val paramMap: Map<String, String>

    init {


        val commandBits = command.split("?", limit=2)

        actionPath = commandBits[0].trim()

        val queryStr = if(commandBits.lastIndex == 1 && commandBits[1].isNotEmpty()){
            commandBits[1].trim()
        }else{
            ""
        }

        paramMap = if(queryStr.isEmpty()){
            mapOf()
        }else{
            // /article/list?page=2&searchKeyword=제목

            val queryStrBits = queryStr.split("&")



            val paramMapTemp = mutableMapOf<String, String>()

            for(queryStrBit in queryStrBits){

                val keyValueBits = queryStrBit.split("=", limit=2)
                val key = keyValueBits[0].trim()
                val value = if(keyValueBits.lastIndex == 1 && keyValueBits[1].isNotEmpty()){
                    keyValueBits[1].trim()
                }else{
                    ""
                }
                if(value.isNotEmpty()){
                    paramMapTemp[key] = value.trim()
                }
            }
            paramMapTemp
        }
    }



    fun getStrParam(name: String, default: String): String{
        return paramMap[name]?: default
    }
    fun getIntParam(name: String, default: Int): Int{
        return if (paramMap[name] != null){
            try{
                paramMap[name]!!.toInt()
            }
            catch(e: NumberFormatException){
                default
            }

        }else{
            default
        }
    }


}