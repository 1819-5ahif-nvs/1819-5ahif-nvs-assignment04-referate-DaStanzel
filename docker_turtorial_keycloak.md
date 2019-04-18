# Aufsetzen des Keycloak Servers mit Docker
## Manuelles aufsetzen ohne Dockerfile
Zuerst muss das Docker Image von Dockerhub heruntergeladen werden.
Dies wird mit dem Befehl

```
docker pull jboss/keycloak
```

ausgeführt.

Da das Dockerimage auf Basis eines Wildfly Servers läuft ist die Bedienung sehr ähnlich. Die Adminkonsole läuft auf ``` 127.0.0.1:9990 ``` und die HTTP Management Konsole auf ``` 127.0.0.1:9990/management ```. 

Um den Keycloak Server zu starten verwendet man den Befehl 
```
docker run -p 8080:8180 jboss/keycloak
```
Man muss den Port auf ``` 8180 ``` um mappen, da auf dem Port 8080 der Wildfly mit dem selbstgeschriebenen Server läuft.

Beim ersten Start ist auf dem Keycloak Server kein User angelegt. Den User benötigen wir, da wir noch die Ressource für unser System im Keycloak Server anlegen müssen.

Um diesen User anzulegen gibt es 2 Möglichkeiten. Die erste Möglichkeit ist den Docker Container zu starten und den User beim Start anzulegen. 
Um den Container mit den Userdaten zu starten verwendet man den Befehl
```
docker run -e KEYCLOAK_USER=USERNAME -e KEYCLOAK_PASSWORD=PASSWORT jboss/keycloak
```

Die zweite Möglichkeit, für welche ich mich entschieden habe, ist einen Befehl auszuführen während der Keycloak Server läuft und den Docker Container danach neu zu starten. Hier wird das Script Add User aufgerufen.
Dies wird durch die Befehle
```
docker exec CONTAINER keycloak/bin/add-user-keycloak.sh -u USERNAME -p PASSWORT

docker restart CONTAINER
```


## Aufsetzen des Keycloak Servers mit Dockerfile
Im File ``` Keycloak.yml ``` hat man die gesamte Konfiguration, welche wir oben manuell durchgeführt haben, zusammengefasst.
Mit dem Befehl
```
docker-compose -f Keycloak.yml up
```
bauen wir diesen Server.

## Weiteres Vorgehen
### Aufsetzen des Realms
Danach läuft unser Keycloak Server und am Beispiel des Book Shops legen wir uns am laufendem Server unter der IP ``` http://localhost:8180/auth ``` können wir auf die Keycloak Admin Konsole zugreifen.
Der Username hierfür ist ``` admin ``` und das Passwort ist ``` passme ```. 
In dieser Konsole müssen wir dann einen neuen Realm anlegen, welcher ein Projekt identifizieren.
Dies geschieht in der Admin Konsole, wenn man beim Dropdown Menu ``` Menu ``` auf den Punkt ``` Add Realm ``` clickt. Der Name in unserem Fall ist Bookshop, genau so wie der Displayname. 

Nach diesem Punkt wird man weitergeleitet auf die Admin Konsole. Diesmal befinden wir uns jedoch nicht in der  ```master``` sondern in der ``` Bookshop ``` Verwaltung.

### User hinzufügen
In diesem Fenster müssen wir uns jetzt einen User anlegen. Hierfür gehen wier wieder ins ``` Menu ``` und auf den Punkt ```Add User ```. Hier muss man einen Usernamen eingeben und im Tab ``` Credentials ``` ein temporäres Passwort eingeben. Dies muss beim ersten Login vom User geändert werden.


### Clients registrieren
Um unsere Applikation am Keycloak Server zu registrieren, müssen wir uns einen neuen Client anlegen. Dies geschieht in der Admin Konsole im Branch ```Bookshop``` unter dem Punkt ```Clients```.
Hier clicken wir auf ```Create```und geben unserem Client eine ID in unserem Fall ist die ID ```Bookshop```. Das einzige was jetzt noch zu tun ist, ist die URL unserer Rest Schnittstelle anzugeben im Textfeld ```Root URL``` in unserem fall ist diese ```http://localhost:8080/bookshop/rs```. Nun clicken wir auf ```Save``` und unser Client ist angelegt.

Im File ```README.md``` ist schlussendlich noch erklärt, was an unserem REST Server in der web.xml noch einzutragen ist.

### Login am Keycloak Server
Unter der Domain unseres Rest Servers sollte jetzt ein Login Fenster erscheinen.
