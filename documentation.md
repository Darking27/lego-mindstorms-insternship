# Lego Mindstorms - Gruppe 6

##### 15.11.2021
An diesem Termin wurde zum ersten Mal der gesamte Parcours-Abschnitt des Linenfolgens bewältigt.
Sowohl Lücken, rechte Winkel und Hinternisse werden nun erfolgreich befahren.

Der Linienfolger implementiert eine Mischung aus Regler und Zustandsautomat.
Im *normalen* Modus folgt der Linienfolger nur den Meldungen des Reglers. Sobald aber eine zu starke Kurve oder Lücke erkannt wird, springt der Roboter in einen anderen Zustand und versucht die Linie zuerst in der Nähe wiederzufinden. Ist dies nicht möglich, wird ein Stück weiter gerade aus gefahren und dort weiter nach einer Linie gesucht.

Außerdem wurde der Parcours-Abschnitt der Brücke teilweise bewältigt. Der Roboter kann die Brücke hoch fahren, eine Linkskurve machen, der Brücke folgen, Links wieder runterfahren. Einzig das Treffen der schmalen Lücke am Ende der Brücke ist noch nicht implementiert.

Zudem arbeiteten wir auch an dem Parcours-Abschnitts des Box-Bewegens. Diese Funktionalität war jedoch noch nicht einsatzbereit und erfordert weitere Ändrungen. 
