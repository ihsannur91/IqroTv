package id.myindo.ecosystem.iqrotv

data class DataModel(
    val result: List<Result> = listOf()
) {
    data class Result(
        val details: List<Detail> = listOf(),
        val id: Int = 0,
        val title: String = ""
    ) {
        data class Detail(
            val title: String = "",
            val overview: String = "",
            val backdrop_path: String = "",
            val poster_path: String = ""
        )
    }
}