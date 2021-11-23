# Lego Mindstorms - Gruppe 6

##### 15.11.2021
An diesem Termin wurde zum ersten Mal der gesamte Parcours-Abschnitt des Linenfolgens bewältigt.
Sowohl Lücken, rechte Winkel und Hinternisse werden nun erfolgreich befahren.

Der Linienfolger implementiert eine Mischung aus Regler und Zustandsautomat.
Im *normalen* Modus folgt der Linienfolger nur den Meldungen des Reglers. Sobald aber eine zu starke Kurve oder Lücke erkannt wird, springt der Roboter in einen anderen Zustand und versucht die Linie zuerst in der Nähe wiederzufinden. Ist dies nicht möglich, wird ein Stück weiter gerade aus gefahren und dort weiter nach einer Linie gesucht.

Außerdem wurde der Parcours-Abschnitt der Brücke teilweise bewältigt. Der Roboter kann die Brücke hoch fahren, eine Linkskurve machen, der Brücke folgen, Links wieder runterfahren. Einzig das Treffen der schmalen Lücke am Ende der Brücke ist noch nicht implementiert.

Zudem arbeiteten wir auch an dem Parcours-Abschnitts des Box-Bewegens. Diese Funktionalität war jedoch noch nicht einsatzbereit und erfordert weitere Ändrungen. 

##### 22.11.2021
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
