# Lego Mindstorms - Gruppe 6

#### 15.11.2021
An diesem Termin wurde zum ersten Mal der gesamte Parcours-Abschnitt des Linenfolgens bewältigt.
Sowohl Lücken, rechte Winkel und Hinternisse werden nun erfolgreich befahren.

Der Linienfolger implementiert eine Mischung aus Regler und Zustandsautomat.
Im *normalen* Modus folgt der Linienfolger nur den Meldungen des Reglers. Sobald aber eine zu starke Kurve oder Lücke erkannt wird, springt der Roboter in einen anderen Zustand und versucht die Linie zuerst in der Nähe wiederzufinden. Ist dies nicht möglich, wird ein Stück weiter gerade aus gefahren und dort weiter nach einer Linie gesucht.

Außerdem wurde der Parcours-Abschnitt der Brücke teilweise bewältigt. Der Roboter kann die Brücke hoch fahren, eine Linkskurve machen, der Brücke folgen, Links wieder runterfahren. Einzig das Treffen der schmalen Lücke am Ende der Brücke ist noch nicht implementiert.

Zudem arbeiteten wir auch an dem Parcours-Abschnitts des Box-Bewegens. Diese Funktionalität war jedoch noch nicht einsatzbereit und erfordert weitere Ändrungen. 

#### 22.11.2021
An diesem Termin waren zum ersten Mal alle Abschnitte (zumindest in einer vereinfachten Form) implementiert.

Hauptsächlich haben wir uns mit dem Verschieben der Box beschäftigt. Hierzu muss der Roboter zunächst durch den langen Durchgang fahren und danach die Box suchen und in die Ecke schieben.
Für das Durchqueren des Durchgangs haben wir einen Algorithmus implementiert, mit dem sich der Roboter parallel zur Wand ausrichtet, dann ein klein wenig von der Wand entfernt und parallel zur Wand soweit vor fährt, bis er die Distanz erreicht hat, um die Box in der Mitte des Raumes zu suchen.
Dann dreht sich der Roboter auf der Stelle und misst dabei den Abstand am Ultraschallsensor. Danach dreht er sich wieder zurück und fährt ein kleines Stück weiter.
Falls er die Box nicht gesehen hat, wiederholt er das immer wieder.
Falls er die Box gesehen hat, schiebt er sie an die gegenüberliegende Wand, fährt dann auf die rechte Seite der Box und schiebt die Box dann in die richtige Ecke.
Anschließend begibt sich der Roboter zur Startlinie für den Brückenabschnitt.
Hierfür haben wir heute 2 Ansätze ausprobiert:
- Der Roboter fährt eine Linkskurve rückwärts und fährt dann vorwärts, Zeiten fest
- Der Roboter dreht um und fährt bis zur Wand. Anschließend setzt er zurück und fährt zur blauen Linie.

Heute wurde auch eine Strategie implementiert, mit der es der Roboter durch den Durchgang am Ende von dem Brückenabschnitt schafft. Dazu setzt der Roboter, wenn der Touch-Sensor betätigt wird, ein Stück zurück dreht sich in die richtige Richtung und versucht erneut durch den Durchgang zu fahren.

Außerdem haben wir eine sehr einfachen Strategie für den letzten Abschnitt ausprobiert.
Dazu dreht sich der Roboter wenn der Touch-Sensor betätigt wird und fährt dann weiter.
Die Strategie ist uns zu langsam um wir haben vor am nächsten Termin andere Strategien zu testen.

Um einschätzen zu können, wie gut unsere Lösungen für den Linienfolger und den Brückenquerer sind, haben wir bei beiden die Zeit gemessen, die der Roboter benötigt hat.
Hierbei haben wir 3:22 Minuten für den Linienfolger und 52 Sekunden für den Brückenabschnitt gemessen.

#### 29.11.2021
An diesem Termin konzentrierten wir uns darauf, unsere Algorithmen zu verbessern.

Das Finden des Ausgangs beim Bewegen der Kiste ist nun deutlich stabiler und funktioniert in (fast) allen Fällen.
Außerdem wurde die Geschwindigkeit des Roboters in diesem Abschnitt erhöht um bessere Zeiten erzielen zu können.
Außerdem besteht die Überlegung einen neuen Algorithmus zu implementieren, der etwas schneller ist, indem er nicht die ganze Zeit rechtwinklige Kurven fährt.

Der Farbpunkfinde Algorithmus wurde auch verändert.
Das letzte Mal hatten wir noch eine Implementierung welche auf Zufall basierte.
Um jedoch im Durchschnitt bessere Zeiten zu erhalten haben wir uns für ein deterministischen Ansatz entschieden, der Spalte für Spalte des Suchraums durchsucht.
Zuerst fährt der Roboter bis zur hinteren Wan und dann immer in einem solchen Schema:
```
<-----------<^
-----------> |           
<----------- |
-----------> |
             |
            Start
```

Gegen Ende haben wir zudem noch einige Regler- und Geschwindigkeitswerte des Linienfolgers angepasst.
Diese Anpassung wollen wir die nächste Stunde forführen um bessere Zeiten für das Linienfolgen zu erhalten.
Bisher war das Optimieren der Reglerwerte wenig erfolgreich, da man alle Reglerwerte abhängig voneinander ändern muss. Zum Beispiel müssen alle Reglerwerte angepasst werden, wenn man die Geschwindigkeit steigert, damit der Roboter nicht zu häufig die Bahn verliert.


#### 6.12.2021
Nachdem letzte Woche bereits eine Funktionierende Version des Paketzustellers funktionierte, ging es diese Woche darum einen schnellere Strategie zu implementieren.
Hierzu schweift der Roboter nach links und rechts und sucht mittels des Ultraschallsensors nach der Kiste.
Während dem schwenken speichern und ändern wir die ganze Zeit die kürzeste gesehen Distanz und deren Tacho Werte.
Nach dem schwenken dreht sich der Roboter zurück zur kürzesten Distanz.
Leider konnte der Roboter nicht verlässlich die Box erkennen wenn diese zum Beispiel schräg stand.
Wir werden nächste Stunde probieren weiter daran zu arbeiten, gehen aber davon aus, dass wir doch bei der vorherigen Strategie bleiben.

Beim Brückenfolger wurde heute ein Regler implementiert, der einer geraden Kante folgen kann.
Bis zum nächsten Termin soll ein neues ParcoursWalkable erstellt werden, das sowohl diesen Regler, als auch den Farbsensor nutzt und die erste Kurve mittles Farbsensor erkennt.
Die zweite Kurve soll auch mit dem Farbsensor erkannt werden, allerdings indem der Roboter zuerst gerade über die Kante hinausfährt und dann zurücksetzt um sich um 90 Grad zu drehen.

#### 10.1.2022

Das Boxmoven wurde durch einen neuen Exit Finder stabiler gemacht. Dieser setzt zurück und fährt dann nach vorne um genügend Abstand zur Wand zu erhalten.

Nach langem, erfolglosem bugfixen, haben wir damit begonnen einen neue implementierung des Bridge Followers zu implementieren, die deutlich simpler sein soll. Diese funktioniert noch nicht. Nächste Stunde sehen wir weiter welche Strategie wir nun nutzen.
