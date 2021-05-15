import java.io.File
import java.text.SimpleDateFormat

fun readLineTrim() = readLine()!!.trim()

object Util {

    fun getNowDateStr(): String {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

        return format.format(System.currentTimeMillis())
    }


}

fun mapFromJson(jsonStr: String): Map<String, Any>{

    val map = mutableMapOf<String, Any>()


    var jsonStr = jsonStr.drop(1)
    jsonStr = jsonStr.dropLast(1)

    val jsonStrBits = jsonStr.split(",\r\n")

    for(jsonStrBit in jsonStrBits){
        val keyValueBits = jsonStrBit.split(":", limit=2)
        val key = keyValueBits[0].trim().drop(1).dropLast(1)
        val value = keyValueBits[1].trim()

        // String , Int, Double, email, boolean
        when{
            value == "true" -> {
                map[key] = true
            }
            value == "false" -> {
                map[key] = false
            }
            value.startsWith("\"") -> {
                map[key] = value.drop(1).dropLast(1)
            }
            value.contains("@") && value.contains(".") -> {
                map[key] = value.drop(1).dropLast(1)
            }
            !value.contains("@") && value.contains(".") -> {
                map[key] = value.toDouble()
            }
            else -> {
                map[key] = value.toInt()
            }

        }

    }
    return map

}

fun readStrFromFile(filePath: String): String{
    if(!File(filePath).isFile){
        return ""
    }
    return File(filePath).readText(Charsets.UTF_8)
}

fun writeStrInFile(filePath: String, content: String){
    File(filePath).parentFile.mkdirs()
    File(filePath).writeText(content)
}

fun readIntFromFile(filePath: String, default: Int): Int{
    val content = readStrFromFile(filePath)

    if(content.isEmpty()){
        return default
    }

    return content.toInt()
}

fun writeIntInFile(filePath: String, content: Int){
    File(filePath).parentFile.mkdirs()
    File(filePath).writeText(content.toString())
}
fun deleteFile(filePath: String){

    File(filePath).delete()
}