# README

## Opis projekta
Ovaj projekat implementira jednostavan model aplikacija (`App`) i developera (`Developer`) sa funkcionalnostima za filtriranje, grupisanje i analizu podataka o aplikacijama.

## Struktura projekta
### App: 
Data class koja predstavlja pojedinacnu aplikaciju sa poljima:
  - `appName: String` - ime aplikacija
  - `category: String` - kategorija
  - `downloads: String` - broj preuzimanja
  - `rating: Double` - projecna ocjena 
  - `size: Int` - velicina aplikacije u MB

  **Napomena:**
  - `init` blok koji osigurava validaciju podataka, npr. broj preuzimanja i velicina moraju biti veci od nula, a ocjena ne smije biti negativna

### Developer: 
Data class koji predstavlja developera sa poljima:
  - `name`, `surname`, `country` - svaki je tipa String i predstavlja ime, prezime i drzavu developera
  - `listOfDevelopedApps: List<App>` - lista aplikacija koju je developer razvio

### Funkcije za obradu podataka:
  - `filterByRating` - filtrira aplikacije koje imaju ocjenu vecu od zadane, implementacija ove funkcije koristi metod filter()
  - `filterByRatingMyImplementation` - radi isto sto i funckija `filterByRating` samo se razlikuje po tome sto ne koristi vec postojeci metod filter() i lambda izraze
  - `groupByCategory` - grupise aplikacije po kategoriji i broji koliko aplikacija ima u svakoj
  - `groupByCategoryMyImplementation` - radi isto sto i funkcija `groupByCategory` samo sto ne koristi vec postojane funckije 
  - `ratingsByCategory` - racuna prosjecnu ocjenu aplikacija po kategoriji
  - `searchByName` - pronalazi aplikaciju po imenu i ispisuje rezultat
  - `searchByNameReturnApp` - pronalazi aplikaciju po imenu i vraca tu aplikaciju
  - `sortByDownloads` / `sortByDownloadsMyImplementation` - sortira podatke po broju preuzimanja u opadajucem redoslijedu
  - `findDeveloperWithMaxDownloads` - pronalazi developera cije aplikacije imaju najvise ukupnih preuzimanja
  - `findAverageRatingOfDeveloperApp` - racuna prosjecnu ocjenu svih aplikacija odredjenog developera

  **Dodatni opis za funkciju sortByDownloadsMyImplementation()**
  - implementacija koristi `selection sort` algoritam za sortiranje aplikacija po broju preuzimanja u opadajucem redoslijedu.
  - algoritam iterira kroz listu, pronalazi najveci element u preostalom dijelu liste i zamijeni ga sa elementom na trenutnoj poziciji.

   
```kotlin
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
          //Swap 
          val temp = sortedList[i]
          sortedList[i] = sortedList[maxPos]
          sortedList[maxPos] = temp
      }
      return sortedList
  }

```

  **Objasnjenje koraka**
  1. Vanjska petlja iterira kroz listu od prvog do predzadnjeg elemenata
  2. Unutrasnja petlja trazi element sa najvecim brojem preuzimanja u ostatku liste
  3. Kada se pronadje najveci element, zamijeni se sa elementom na trenutnoj poziciji
  4. Rezultat je lista sortirana po broju preuzimanja u opadajucem redoslijedu

  Referenca za selection sort:
  - <https://www.geeksforgeeks.org/dsa/selection-sort-algorithm-2/>


## Razlozi za odabir nekih postojecih funkcija

-  **`filter` i `map`** - omogucuju jednostavno filtriranje i transformaciju podataka
 - **`groupingBy` i `eachCount`** - elegantno grupisu podatke po kategoriji te zatim sa `eachCount` dobijem broj aplikacija u toj kategoriji
- **`fold`** - koristi se za sumiranje ocjena unutar kategorija zato sto omogucava agregiranje sa inicijalnom vrijednoscu
- **`firstOrNull`** - vraca mi prvi element u listi za kojeg je uslov tacan, a ako ne postoji vraca *null* sto je efikasno
- **`sumOf`** - vrati sumu vrijednosti elemenata iz liste na osnovu proslijedjene funkcije koja odredjuje koji clan se sabira

## Koristenje funkcionalnosti u Android aplikaciji
1. **`filterByRating` / `filterByRatingMyImplementation`**
  - Moze posluziti za prikazivanje popularnih aplikacija prema ocjeni
2. **`groupByCategory` / `groupByCategoryMyImplementation`**
  - zajedno sa slicnim funkcijama za grupisanje, mogu se iskoristit za pravljenje   odredjenih sekcija u aplikaciji ili kategorizovanog prikaza
3. **`ratingsByCategory`**
  - moze se korisiti za kreiranje rang lista unutar pojedinacnih kategorija
4. **`searchByName` / `searchByNameReturnApp`**
  - omogucava pretragu aplikacija po imenu, sto poboljsava korisnicko iskustvo 

## Koristenje AI alata 

U ovom projektu je koristen **ChatGPT** za:
- Generisanje testnih podataka (`listaApps` i `developerList`) 
- Savjete oko Kotlin funkcionalnostima (`fold`, `groupingBy`, ...)
- Savjete u vezi koristenja `assert()` funkcije
- Optimizaciju koda i formatiranje testova



