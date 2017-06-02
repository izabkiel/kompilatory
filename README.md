# kompilatory
Stan projektu: 
Dodałam klasę do tworzenia SATa z pliku, jest ona teraz domyślnie wyłowywana w mainie. Dla instruktora ograniczenia są w pliku instruktorzy.csv natomiast dla uczniów w pliku students.csv. Dane zapisywane w tych plikach:
jako separator między polami jest średnik
data w formacie "yyyy-MM-dd HH:mm:ss"
nie można używać polskich znaków 
nie ma wgl sprawdzania poprawności wprowadzonych danych np. Koniec lekcji może być przed jej początkiem
	W pliku z ograniczeniami dla uczniów dla jednego ucznia 	insruktor powinien być taki sam
Zmaina intów na date, nie zostało to jeszcze zmienione w GUI dlatego dodawanie ograniczeń zostało tam na razie zakomentowane. 

Do zrobienia:
W GUI zmiana intów na Date
Dorobienie GUI na dodawanie plików z kompa 
Przetestowanie na większej ilości danych (ja robiłam na razie tylko na 2 instruktorach I 2 uczniach). Nie jestem pewna czy na pewno dobrze zmienilam warunki dla dat mogłam pomieszać after I before dlateczo trzeba to dokładnie przeanalizować.
Jest problem z tym jak do ucznia dodamy instruktora, który nie istnieje tzreba to sprawdzić
Dodać instruktora dla konkretnego ograniczenia a nie dla ucznia 
Zapisanie wynkiu sata do pliku i wyświetlanie wyniku
