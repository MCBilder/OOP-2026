# OOP-2026
Programowanie Obiektowe Java

## Spis Treści

1. [Temat 1: Podstawy OOP](#temat-1-podstawy-oop)
2. [Temat 2: Tablice](#temat-2-Tablice)
3. [Temat 3: Plytka i gleboka kopia](#temat-3-Plytka-i-gleboka-kopia)
4. [Temat 4: Listy](#temat-4-Listy)
5. [Temat 4: Pliki](#temat-4-Pliki)

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

Zaawansowane zagadnienia z OOP:
- 

### Zawartość:
-

---

## Temat 3: Plytka i gleboka kopia

Zaawansowane zagadnienia z OOP:
- 

### Zawartość:
-

---

## Temat 4: Listy

Zaawansowane zagadnienia z OOP:
- 

### Zawartość:
-

---

## Temat 4: Pliki

Zaawansowane zagadnienia z OOP:
- 

### Odczytywanie Pliku:
- File -> wskazuje plik.
- Scanner -> czyta dane z pliku.
- while -> czyta linie po linii.

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
---
