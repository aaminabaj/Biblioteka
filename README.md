1.       UVOD
Projekat „Biblioteka“ je razvijen kao timski rad troje studenata u sklopu predmeta „Metode razvoja softvera“, sa ciljem izrade funkcionalne desktop aplikacije za podršku radu manje biblioteke.​ Primarni fokus projekta je na modeliranju domenskih klasa (knjiga, korisnik, posudba) i implementaciji korisničkog interfejsa pomoću JavaFX-a.
2.       TEHNOLOGIJE I ARHITEKTURA
Aplikacija je implementirana u programskom jeziku Java uz korištenje JavaFX biblioteke za grafički korisnički interfejs (GUI). Projekat koristi Maven za upravljanje zavisnostima i build procesom, pri čemu pom.xml sadrži konfiguraciju za JavaFX module i pokretanje aplikacije.
Osnovne karakteristike:
Razdvajanje na slojeve: model (domenske klase), kontroleri (JavaFX kontroleri vezani za FXML forme) i prikaz (FXML datoteke)
Organizacija koda u pakete unutar src/main/java, dok se FXML i resursi (ikone, stilovi) nalaze u src/main/resources
Pokretanje aplikacije ide preko glavne Java klase koja inicijalno učitava početni FXML ekran i postavlja glavnu scenu
Aplikacija koristi bazu podataka hostovanu na Aiven cloudu, gdje se trajno čuvaju svi podaci koje sistem koristi. Bazi se pristupa preko posebnog sloja za rad sa podacima (DAO), koji je zadužen za otvaranje konekcije, izvršavanje SQL upita i mapiranje rezultata u Java objekte.
Struktura baze obuhvata četiri tabele koje prate domenski model biblioteke:
Tabela za knjige - čuva osnovne informacije o svakoj knjizi 
Tabela za korisnike - čuva podatke o registrovanim korisnicima aplikacije
Tabela za posudbe - evidentira svako zaduženje knjige (član, knjiga, datumi posudbe i povrata)
Tabela za članove - čuva podatke o članovima biblioteke
Korištenjem Aiven clouda baza je fizički smještena izvan same aplikacije, pa se podaci mogu sigurno čuvati i njima pristupati sa više klijentskih instalacija JavaFX aplikacije, uz odgovarajuće pristupne podatke.
3.       FUNKCIONALNOSTI
Sistem je zamišljen kao alat za osnovno vođenje evidencije biblioteke na lokalnom računaru.​Ključne funkcionalnosti (prema strukturi UI i modela):
Registracija i login na aplikaciju
Upravljanje knjigama: unos novih knjiga, izmjena podataka, pregled liste dostupnih naslova kroz JavaFX forme i tabele, pretraga i filtriranje knjiga, generisanje PDF dokumenta
Upravljanje korisnicima: registracija i pregled članova biblioteke, evidentiranje osnovnih podataka o korisnicima, pretraga članova, generisanje PDF dokumenta
Posudbe: kreiranje posudbe između korisnika i knjige, praćenje statusa (zadužena/vraćena), rad sa datumima posudbe/povrata, pretraga i filtriranje posudbi, generisanje PDF dokumenta
4.   	ZAKLJUČAK
Projekat „Biblioteka“ predstavlja funkcionalnu JavaFX desktop aplikaciju koja demonstrira rad sa grafičkim interfejsom u Javi, višeslojnom strukturom i osnovnim poslovnim pravilima bibliotečkog sistema. Kroz timski rad članova obuhvaćeni su ključni aspekti razvoja: od modeliranja i dizajna GUI-a, do implementacije poslovne logike.
