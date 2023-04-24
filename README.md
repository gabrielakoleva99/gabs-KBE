# KBE Projekt Template

## TODO
1. Ersetzen Sie die Überschrift mit `TEAMNAME`
2. Geben Sie Ihre Teammitglieder in die Tabelle ein
3. Folgen Sie der Anweisungen in der htwb-kbe-repo-template/pom.xml
4. Folgen Sie **KBE Repository einrichten**
5. Erstellen Sie die Branches (Groß- und Kleinschreibung beachten):
   - `runmerunner`
   - `songsservlet`
   - `songsWSa`
   - `songsWSb`

| Name            | Matrikelnummer  |
| :-------------- | --------------- |
| Gabriela Koleva | 0570902         |
| Endrit Limani   | 0569702         |



## KBE Repository einrichten

Erstellen Sie ein leeres GitHub-Repository (kein `.gitignore`, `README.md` und `LICSENSE`) mit `TEAMNAME` als Repository-Name. Übertragen Sie dann den Inhalt des Template-Repository in Ihr erstelltes Repository:

```bash
cd htwb-kbe-repo-template
git init
git add -A
git commit -m "init"
git remote add origin <YOUR GIT CLONE LINK>
git push -u origin main
```
## Falls 'push -u origin main' nicht klappen sollte

Dann in https://github.com/settings/tokens/ einen Token (classic) erstellen:
- 'Note' angeben
- mind 6 Monate lang gültig
- repo und workflow selektieren
- 'Generate token' klicken
- den Token kopieren und irgendwo sicher speichern

Im Projektverzeichnis 'htwb-kbe-repo-template/' ins github einloggen und nochmal versuchen:
```bash
~/htwb-kbe-repo-template $ git config --global user.name "YOUR USERNAME"
~/htwb-kbe-repo-template $ git config --global user.email "YOUR EMAIL"
~/htwb-kbe-repo-template $ git push -u origin main
```
^^^ Hier erfolgt Abfrage nach username und password, hier nicht das password eingeben sondern den Token pasten:
```bash
htwb-kbe-repo-template $ git push -u origin main
Username for 'https://github.com': YOUR USERNAME
Password for 'https://eschuler22@github.com': TOKEN (not password)
```

## runmerunner clean, bauen, testen & verpacken
```
mvn -pl runmerunner clean package 
```

ODER

```
cd runmerunner
mvn clean package 
```



## songsservlet clean, bauen, testen & verpacken

```
mvn -pl songsservlet clean package 
```

ODER

```
cd songsservlet
mvn clean package 
```



## songsWS clean, bauen, testen & verpacken
```
mvn -pl songsWS clean package 
```

ODER

```
cd songsWS 
mvn clean package
```



## Alle Projekte clean, bauen, testen & verpacken: > mvn clean package

```
mvn clean package
```






