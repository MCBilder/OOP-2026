# OOP-2026
Programowanie Obiektowe Java

## Spis Treści

1. [Temat 1: Podstawy OOP](#temat-1-podstawy-oop)
2. [Temat 2: Tablice](#temat-2-Tablice)
3. [Temat 3: Plytka i gleboka kopia](#temat-3-Plytka-i-gleboka-kopia)
4. [Temat 4: Listy](#temat-4-Listy)
5. [Temat 5: Pliki](#temat-4-Pliki)

---

## Temat 1: Podstawy OOP

Podstawowe koncepty Programowania Obiektowego:
- Klasy i obiekty
- Enkapsulacja
- Dziedziczenie
- Polimorfizm

### Zawartość:
- Definicje i przykłady
- Ćwiczenia praktyczne
- Kod źródłowy

---

## Temat 2: Tablice
### Tworzenie
```
Typ[] nazwa = new Typ[rozmiar];
```
##### lub
```
Typ[] nazwa = {zmienna1, zmienna 2, ....};
```
### dodawanie zmiana elementu
```
liczby[0] = 100;
```

### Klasyczny for
```
for(int i = 0; i < liczby.length; i++) {

    System.out.println(liczby[i]);
}
```
### foreach
```
for(int x : liczby) {

    System.out.println(x);
}
```
### suma elementow
```
int suma = 0;

for(int x : liczby) {

    suma += x;
}
```
### najwiekszy element
```
int max = liczby[0];

for(int x : liczby) {

    if(x > max) {

        max = x;
    }
}
```
---
### kopia tablicy
```
int[] kopia = liczby.clone();
```
---

## Temat 3: Plytka i gleboka kopia

Zaawansowane zagadnienia z OOP:
- 

### Zawartość:
-

---

## Temat 4: Listy

### Pliki a ArrayList
#### Odczyt i dopisnaie do listy
```
public class Main {

    public static void main(String[] args) {

        try {

            // lista
            ArrayList<String> lista = new ArrayList<>();

            // plik
            File plik = new File("dane.txt");

            // scanner
            Scanner scanner = new Scanner(plik);

            // odczyt pliku
            while(scanner.hasNextLine()) {

                String linia = scanner.nextLine();

                // dodanie do listy
                lista.add(linia);
            }

            scanner.close();

            // wyswietlenie listy
            for(String element : lista) {

                System.out.println(element);
            }

        } catch(Exception e) {

            e.printStackTrace();
        }
    }
}
```

#### Odczyt listy i zapisanie do pliku
```
import java.io.FileWriter;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        try {

            ArrayList<String> lista = new ArrayList<>();

            lista.add("Adam");
            lista.add("Kasia");
            lista.add("Ola");

            FileWriter writer =
                    new FileWriter("wynik.txt");

            // zapis listy
            for(String element : lista) {

                writer.write(element + "\n");
            }

            writer.close();

            System.out.println("Zapisano.");

        } catch(Exception e) {

            e.printStackTrace();
        }
    }
}
```

### Tworzenie ArrayList
```
ArrayList<TYP> nazwa = new ArrayList<>();
```
#### Dodawanie
```
lista.add(to co dodajemu);
```
#### Pobieranie elementow
get(index)
```
lista.get(0);
```
#### Rozmiar listy
```
lista.size();
```
#### Petla po liscie
```
for(String element : lista) {

    System.out.println(element);
}
```
##### lub
```
for(int i = 0; i < lista.size(); i++) {

    System.out.println(lista.get(i));
}
```
#### Usuwanie elementów
##### Po indeksie
```
lista.remove(0);
```
##### Po wartosci
```
lista.remowe("Adam");
```
#### Sprawdzanie czy element istnieje
```
lista.contains("Adam")
```
#### Czyszczenie listy
```
lista.clear();
```
#### Sortowanie listy
```
Collections.sort(lista);
```
### Łączenie list
```
lista1.addAll(lista2);
```
---
### Kopiowanie listy - BARDZO WAŻNE
```
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        ArrayList<String> lista =
                new ArrayList<>();

        lista.add("Adam");
        lista.add("Kasia");

        ArrayList<String> kopia =
                new ArrayList<>(lista);

        System.out.println(kopia);
    }
}
```
---

## Temat 5: Pliki
---
#### Najbardziej uniwersalny szablon kolokwialny
```
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        try {

            File plik = new File("dane.txt");

            Scanner scanner = new Scanner(plik);

            FileWriter writer =
                    new FileWriter("wynik.txt");

            while(scanner.hasNextLine()) {

                String linia = scanner.nextLine();

                // operacje na danych

                writer.write(linia + "\n");
            }

            scanner.close();
            writer.close();

        } catch(Exception e) {

            e.printStackTrace();
        }
    }
}
```
---
1. Otwieranie pliku
```
Scanner scanner = new Scanner(plik);
```
2. czytanie w petli
```
while(scanner.hasNextLine())
```
3. Pobieranie linii
```
String linia = scanner.nextLine();
```
4. Operacje na danych
```
System.out.println(linia);
```
##### lub
```
int liczba = Integer.parseInt(linia);
```
5. Zamykanie pliku
```
scanner.close();
```


### Odczytywanie Pliku:
- File -> wskazuje plik.
- Scanner -> czyta dane z pliku.
- while -> czyta linie po linii.

```
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        try {

            // wskazanie pliku
            File plik = new File("dane.txt");

            // otwarcie pliku do czytania
            Scanner scanner = new Scanner(plik);

            // czytanie linia po linii
            while (scanner.hasNextLine()) {

                String linia = scanner.nextLine();

                System.out.println(linia);
            }

            // zamkniecie skanera
            scanner.close();

        } catch (FileNotFoundException e) {

            System.out.println("Nie znaleziono pliku.");
            e.printStackTrace();
        }
    }
}
```
Jezeli plik ma liczby
```
while(scanner.hasNextLine()) {

    String linia = scanner.nextLine();

    // operacje na linii
}
```
### Najczestrze modyfikacje
#### Liczeni linii
```
int licznik = 0;

while(scanner.hasNextLine()) {

    scanner.nextLine();
    licznik++;
}

System.out.println(licznik);
```
#### Szukanie konkretnego slowa
```
while(scanner.hasNextLine()) {

                String linia = scanner.nextLine();

                if(linia.contains("Java")) {

                    System.out.println("Znaleziono slowo Java");
                }
            }
```
#### Wypisywanie tylko liczb wiekszych od X
```
while(scanner.hasNextLine()) {

                String linia = scanner.nextLine();

                int liczba = Integer.parseInt(linia);

                if(liczba > 50) {

                    System.out.println(liczba);
                }
            }
```
#### Sumowanie liczb z pliku
```
int suma = 0;

            while(scanner.hasNextLine()) {

                int liczba = Integer.parseInt(scanner.nextLine());

                suma += liczba;
            }

            System.out.println("Suma = " + suma);

            scanner.close();
```
#### Odczytywanie danych rozdzielonych srednikiem
```
while(scanner.hasNextLine()) {

                String linia = scanner.nextLine();

                String[] dane = linia.split(";");

                String imie = dane[0];
                int wiek = Integer.parseInt(dane[1]);

                System.out.println(imie);
                System.out.println(wiek);
            }
```
---
### Zapisywanie Pliku:
Najważniejszy uniwersalny schemat
```
FileWriter writer = new FileWriter("dane.txt");

writer.write("tekst");

writer.close();
```
- FileWriter
- write()
- close()
```
import java.io.FileWriter;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {

        try {

            FileWriter writer = new FileWriter("dane.txt");

            writer.write("Hello World");

            writer.close();

            System.out.println("Zapisano plik.");

        } catch(IOException e) {

            e.printStackTrace();
        }
    }
}
```
Zapis wielu linii
```
import java.io.FileWriter;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {

        try {

            FileWriter writer = new FileWriter("dane.txt");

            writer.write("Adam\n");
            writer.write("Kasia\n");
            writer.write("Java\n");

            writer.close();

        } catch(IOException e) {

            e.printStackTrace();
        }
    }
}
```
### Dopisywanie do pliku (BARDZO WAŻNE)
#### nadpisnaie pliku
```
new FileWriter("dane.txt");
```
#### dopisnaie do pliku
```
new FileWriter("dane.txt", true);
```
#### przyklad
```
FileWriter writer =
                    new FileWriter("dane.txt", true);

            writer.write("Nowa linia\n");

            writer.close();

            System.out.println("Dopisano.");

```
#### Zapis liczb
```
FileWriter writer =
                    new FileWriter("liczby.txt");

            int liczba = 50;

            writer.write(String.valueOf(liczba));

            writer.close();
```
Uzywamy String.valueof() zeby zamienic liczbe na Stringa.
#### Zapisanie danych odzielonych srednikiem
```
FileWriter writer =
                    new FileWriter("osoby.txt");

            String imie = "Adam";
            int wiek = 20;

            writer.write(imie + ";" + wiek + "\n");

            writer.close();

```
#### Zapis w petli
```
FileWriter writer =
                    new FileWriter("dane.txt");

            for(int i = 1; i <= 5; i++) {

                writer.write("Liczba: " + i + "\n");
            }

            writer.close();
```
---
