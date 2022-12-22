# 5B-data-forum

# Table des matières
- [Visualisation](#visualisation)
  - [Naviguer](#naviguer)
  - [Ajouter des évènements](#ajouter-des-évènements)
  - [Charger un fichier généré par le moteur](#charger-un-fichier-généré-par-le-moteur)
- [Parser](#parser)
- [Moteur](#moteur)

![myChart](https://user-images.githubusercontent.com/57568980/208807429-08a6eafa-5506-46e4-8493-15660157b158.jpg)

![image](https://user-images.githubusercontent.com/57568980/209232567-99c5ab96-a52f-46ed-aec6-c297adf60fc5.png)


## Visualisation

Cet outils permet d'analyser l'activité d'étudiants sur un forum de travail.
Il possible de comparer l'activité des étudiants au fil du temps afin de déterminer les facteurs ayant un impact sur celle-ci.
A cette fin il est également possible de renseigner des évènements ponctuels (examens, vacances, travaux, etc.) dans le but de les mettre en relation avec les variations d'activité.

### Naviguer

Il est possible de zoomer sur le graphique et de filtrer les courbes (utilisateurs et moyenne)

https://user-images.githubusercontent.com/57568980/209205983-e8154333-87aa-45a3-86b4-3908946d4689.mp4

### Ajouter des évènements

https://user-images.githubusercontent.com/57568980/209217868-28bdb130-bf1e-4bc0-80f8-09800d7b9561.mp4

### Charger un fichier généré par le moteur

Par défaut le fichier issue du traitement de la base de données est chargé.
Il est possible de charger un fichier d'indicateurs différent produit par le moteur.

https://user-images.githubusercontent.com/57568980/209206973-fe80baa2-fbf3-4a82-9d3f-8b587e858860.mp4

## Parser

Le parser extrait les données de la base de données pour les formattées conformément au modèle accepté par le moteur

Pour extraire les données de la base de données il faut utiliser le parser qui se trouve dans .\dist, pour celà deux possibilitées:
- en executant le jar directement: 
```powershell
java -jar .\Parser.jar
```
- en executant le fichier powershell, soit en double cliquant dessus soit avec la commande:
```powershell
.\Parser
```

## Moteur

Le moteur calcule les indicateurs à partir des données formattées par le parser

Pour calculer les indicateurs à partir des données formattées il faut utiliser le moteur qui se trouve dans .\dist, pour celà deux possibilitées:
- en executant le jar directement: 
```powershell
java -jar .\Moteur.jar
```
- en executant le fichier powershell, soit en double cliquant dessus soit avec la commande:
```powershell
.\Moteur
```
