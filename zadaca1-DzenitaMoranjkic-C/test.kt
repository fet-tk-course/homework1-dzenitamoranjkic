data class App (
    var appName:String,
    var category:String,
    var downloads:Int,
    var rating:Double,
    var size:Int) {
    init {
        require(downloads > 0){
            "Vrijednost za broj preuzimanja mora biti veca od nula!"
        }
        require(rating in 0.0..5.0){
            "Vrijednost za prosjecnu ocjenu mora biti izmedju 0 i 5"
        }
        require(size > 0){
            "Vrijednost za velicinu aplikacije mora biti veca od nula!"
        }
    }
    override fun toString(): String {
        var formatedString = ""
        if (downloads > 1000000) {
            val value = (downloads / 1000000.0)
            if (value % 1.0 == 0.0)
                formatedString = "${value.toInt()}M+"
            else
                formatedString = String.format("%.1fM+", value)
        }
        else if (downloads > 1000) {
            val value = (downloads / 1000.0)
            if (value % 1.0 == 0.0)
                formatedString = "${value.toInt()}K+"
            else
                formatedString = String.format("%.1fK+", value)
        }
        else {formatedString = downloads.toString()}
        return "\nApp: $appName | Category: $category  | Downloads: $formatedString | Rating: $rating | Size: ${size}MB"
    }
}

data class Developer(
    var name : String,
    var surname : String,
    var country : String,
    var listOfDevelopedApps : List<App>
)
{
    override fun toString(): String {
        return "\nDeveloper: $name $surname | Country: $country | Apps: $listOfDevelopedApps"
    }
}



fun filterByRatingMyImplementation(appList : List<App>, rating : Double) : List<App> {
    val filteredApps : ArrayList<App> = ArrayList()
    for (app in appList) {
        if (app.rating > rating)
            filteredApps.add(app)
    }
    return filteredApps
}

fun filterByRating(appList : List<App>, rating : Double) : List<App> {
    return appList.filter { app -> app.rating > rating}
}

fun groupByCategoryMyImplementation(appList : List<App>) : MutableMap<String,Int>{
    val mapCategory : MutableMap<String,Int> = mutableMapOf()
    for (app in appList) {
        if (mapCategory.contains(app.category)){
         val tempValue = mapCategory.getValue(app.category)
            mapCategory.replace(app.category, tempValue + 1)
        }else {
            mapCategory[app.category] = 1
        }
    }
    return mapCategory
}

fun groupByCategory(appList : List<App>) : Map<String,Int>{
    return appList.groupingBy { it.category }.eachCount()
}

fun ratingsByCategory(appList : List<App>) : Map<String, Double>{
    val countGroups = groupByCategory(appList)
    val map : MutableMap<String, Double> =
        appList.groupingBy { it.category }.fold(0.0){ acc, el -> acc + el.rating}.toMutableMap()

    for ((k, v) in countGroups){
        val sum = map.getOrDefault(k, 0.0)
        map[k] = sum / v
    }
    return map
}

fun sizeByCategory(appList : List<App>) : Map<String, Double>{
    val countGroups = groupByCategory(appList)
    val map : MutableMap<String, Double> =
        appList.groupingBy { it.category }.fold(0.0){ acc, el -> acc + el.size}.toMutableMap()
    for ((k, v) in countGroups){
        val sum = map.getOrDefault(k, 0.0)
        map[k] = sum / v
    }
    return map
}

fun searchByName(appList : List<App>, name: String){
    val element = appList.firstOrNull { it.appName == name }
    if(element != null)
        println(element)
    else
        println("Aplikacija $name nije pronadjena!")
}

// I used selection sort
fun sortByDownloadsMyImplementation(appList: List<App>):List<App>{
    val sortedList = appList.toMutableList()
    val n = sortedList.size
    for(i in 0 until n-1) {
        var maxPos = i
        for (j in i + 1 until n) {
            if (sortedList[j].downloads > sortedList[maxPos].downloads) {
                maxPos = j
            }
        }

        val temp = sortedList[i]
        sortedList[i] = sortedList[maxPos]
        sortedList[maxPos] = temp
    }
    return sortedList
}

fun sortByDownloads(appList : List<App>): List<App>{
    return appList.sortedByDescending { it.downloads }
}

//KOD ZA DODATNI ZADATAK

//proizvoljno dodana funkcija za efikasnije kreiranje podataka za Developer klasu
fun searchByNameReturnApp(appList : List<App>, name: String): App? {
    val element = appList.firstOrNull { it.appName == name }
    if(element != null)
        return element

    println("Aplikacija $name nije pronadjena!")
    return null
}

fun findDeveloperWithMaxDownloads(developerList: List<Developer>): Developer {
    var downloadsSum : Int
    var dev : Developer = developerList.first()
    var maxSum = dev.listOfDevelopedApps.sumOf { it.downloads }
    for(developer in developerList){
        downloadsSum = developer.listOfDevelopedApps.sumOf { it.downloads }
        if(downloadsSum > maxSum){
            maxSum = downloadsSum
            dev = developer
        }
    }
    return dev
}



fun findAverageRatingOfDeveloperApp(dev : Developer): Double {
    var sum = 0.0
    var count = 0.0
        for(app in dev.listOfDevelopedApps){
            sum += app.rating
            count++
        }
    return sum / count
}

fun developerLeaderboard(k: Int, developerList: List<Developer>) : Map<String,Pair<Int, Double>> {
    var listaNovi : List<Developer>
    listaNovi = developerList.sortedByDescending { it.listOfDevelopedApps.sumOf { it.downloads } }
    var mapDownloadsRating : MutableMap<String,Pair<Int, Double>> = mutableMapOf()
    for (dev in listaNovi){
        mapDownloadsRating[dev.name] = Pair(dev.listOfDevelopedApps.sumOf { it.downloads }, dev.listOfDevelopedApps.sumOf { it.rating })
    }
    var returnMapaNovi : Map<String, Pair<Int, Double>> = mutableMapOf()
    for (i in 0..<k){
        returnMapaNovi = mapDownloadsRating.entries.associateBy({ it.key }, { it.value })
    }

    return returnMapaNovi
} //strahota i grehota

fun developerLeaderboard2(k: Int, developerList: List<Developer>) : Unit {
    val listaNovi : List<Developer> = developerList.sortedByDescending { it -> it.listOfDevelopedApps.sumOf { it.rating } }.take(k).sortedByDescending { it -> it.listOfDevelopedApps.sumOf { it.downloads } }
    for(dev in listaNovi){
        println(dev.name + " -> " + "sum of downloads: " + dev.listOfDevelopedApps.sumOf { it.downloads } + ", sum of ratings: " + dev.listOfDevelopedApps.sumOf { it.rating })
    }
}

fun main() {

    // ---------- Generisanje testnih podataka ----------
    val listaApps = listOf(
        App("ChatMaster", "Komunikacija", 5000000, 4.5, 120),
        App("TalkZone", "Komunikacija", 2200000, 2.3, 95),
        App("QuickChat", "Komunikacija", 3100000, 1.9, 100),

        App("FitTrack", "Zdravlje i fitnes", 1200000, 4.2, 85),
        App("YogaDaily", "Zdravlje i fitnes", 3100000, 4.6, 70),
        App("CalorieWatch", "Zdravlje i fitnes", 450000, 3.8, 65),

        App("GalaxyRun", "Igrice", 10000000, 3.6, 250),
        App("PixelQuest", "Igrice", 4000000, 2.5, 230),
        App("RoboBattle", "Igrice", 1500000, 2.9, 190),

        App("BookNest", "Knjige", 5000000, 4.9, 55),
        App("StoryWorld", "Knjige", 1200000, 5.0, 60)
    )

    // ---------- Kreiranje developera ----------
    val developer1Apps = listOfNotNull(
        searchByNameReturnApp(listaApps, "ChatMaster"),
        searchByNameReturnApp(listaApps, "FitTrack"),
        searchByNameReturnApp(listaApps, "QuickChat")
    )

    val developer2Apps = listOfNotNull(
        searchByNameReturnApp(listaApps, "GalaxyRun"),
        searchByNameReturnApp(listaApps, "PixelQuest"),
        searchByNameReturnApp(listaApps, "RoboBattle")
    )

    val developer3Apps = listOfNotNull(
        searchByNameReturnApp(listaApps, "BookNest"),
        searchByNameReturnApp(listaApps, "StoryWorld"),
        searchByNameReturnApp(listaApps, "YogaDaily")
    )

    val developer1 = Developer("Marko", "Markovic", "Bosna", developer1Apps)
    val developer2 = Developer("Ana", "Ilic", "Hrvatska", developer2Apps)
    val developer3 = Developer("Petar", "Petrovic", "Srbija", developer3Apps)

    val developerList = listOf(developer1, developer2, developer3)

    developerLeaderboard2(3, developerList)

    // ====================================================
    //                     TESTOVI
    // ====================================================
/*
    println("\n========== TEST 1: Filtriranje aplikacija ==========")

    println("\n--- 1.1 filtiranje aplikacija pomocu funkcije filterByRating---")
    val ratingApps1 = 3.5
    val filteredApps1 = filterByRating(listaApps, ratingApps1)
    println("Aplikacije sa ocjenom > $ratingApps1:\n$filteredApps1")
    assert(filteredApps1.all { it.rating > ratingApps1 }) { "Filtriranje ne radi ispravno!" }

    // ----------------------------------------------------

    println("\n--- 1.2 filtiranje aplikacija pomocu funkcije filterByRatingMyImplementation---")
    val ratingApps2 = 3.5
    val filteredApps2 = filterByRatingMyImplementation(listaApps, ratingApps2)
    println("Aplikacije sa ocjenom > $ratingApps2:\n$filteredApps2")
    assert(filteredApps2.all { it.rating > ratingApps2 }) { "Filtriranje ne radi ispravno!" }

    // ----------------------------------------------------

    println("\n========== TEST 2: Grupisanje po kategoriji ==========")
    val groupedAppsByCategory = groupByCategoryMyImplementation(listaApps)
    println("Grupisane aplikacije po kategorijama:\n$groupedAppsByCategory")

    val category = "Komunikacija"
    assert(groupedAppsByCategory[category] == listaApps.count { it.category == category }) {
        "Grupisanje po kategoriji ne radi ispravno!"
    }

    // ----------------------------------------------------

    println("\n========== TEST 3: Pretraga po imenu ==========")
    val name = "BookNest"
    val searchName = searchByNameReturnApp(listaApps, name)
    searchByName(listaApps, name)
    assert(searchName?.appName == name) { "Pretraga po imenu ne radi ispravno!" }

    // ----------------------------------------------------

    println("\n========== TEST 4: Racunanje prosjeka po kategoriji ==========")
    val ratingByCategory = ratingsByCategory(listaApps)
    println("Prosjecni rating po kategorijama:\n$ratingByCategory")

    val category1 = "Igrice"
    val expectedAvg = listaApps.filter { it.category == category1 }.map { it.rating }.average()
    assert(ratingByCategory[category1] == expectedAvg) {
        "Racunanje ratinga po kategoriji nije ispravno!"
    }

    // ----------------------------------------------------

    println("\n========== TEST 5: Racunanje prosjecne velicine po kategoriji ==========")
    val sizeByCategory = sizeByCategory(listaApps)
    println("Prosjecna velicina po kategorijama:\n$sizeByCategory")

    val category2 = "Igrice"
    val expectedAvgSize = listaApps.filter { it.category == category2 }.map { it.size }.average()
    assert(sizeByCategory[category2] == expectedAvgSize) {
        "Racunanje prosjecne velicine po kategoriji nije ispravno!"
    }

    // ----------------------------------------------------

    println("\n========== TEST 6: Sortiranje liste ==========")

    val sortedList = sortByDownloadsMyImplementation(listaApps)
  //  println("Sortirana lista po broju preuzimanja u opadajucem redoslijedu:\n$sortedList")
    println("Sortirana lista po broju preuzimanja u opadajucem redoslijedu:")
    for(app in sortedList) {
        println(app)
    }
    assert(sortedList == sortByDownloads(listaApps)) {
        "Funkcija za sortiranje liste po broju preuzimanja ne radi ispravno!"
    }
    // ----------------------------------------------------

    println("\n========== TEST 7: Developer logicke provjere ==========")

    println("\n--- 7.1 Provjera developera sa najvise preuzimanja ---")
    val foundDev = findDeveloperWithMaxDownloads(developerList)
    println("Developer sa najvise preuzimanja:\n$foundDev")
    assert(foundDev.name == developer2.name) {
        "Funkcija za pronalazak developera sa max preuzimanjima ne radi ispravno!"
    }

    println("\n--- 7.2 Provjera prosjecnog ratinga developera ---")
    val foundAvDev = findAverageRatingOfDeveloperApp(developer2)
    val expectedAvgRating = developer2.listOfDevelopedApps.map { it.rating }.average()
    println("Izracunati prosjek: $foundAvDev | Ocekivani prosjek: $expectedAvgRating")
    assert(foundAvDev == expectedAvgRating) {
        "Funkcija za pronalazak prosjecnog ratinga developera ne radi ispravno!"
    }

    // ----------------------------------------------------

    println("\nSve logicke provjere su prosle uspjesno!")
    */
}
