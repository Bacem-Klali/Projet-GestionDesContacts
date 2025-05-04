Gestionnaire de Contacts

Petit projet Java en Swing pour apprendre à créer une interface graphique qui permet de gérer une liste de contacts (nom, téléphone, email) et les enregistrer dans une base de données.


Fonctionnalités:

- Ajouter un nouveau contact

- Modifier un contact sélectionné

- Supprimer un contact

- Rechercher un contact par son nom (en temps réel)

- Sauvegarde des contacts dans une base de données SQL


Interface:

- L'application s'ouvre dans une fenêtre avec :

- Un formulaire pour entrer le nom, téléphone et email

- Trois boutons : Ajouter, Modifier, Supprimer

- Une zone de recherche pour filtrer les contacts

- Une liste des contacts enregistrés


Comment utiliser:

1.Lance le programme (Main.java)

2.Dans la fenêtre :

    Remplis les champs (le nom est obligatoire)

    Clique sur Ajouter pour enregistrer un nouveau contact

3.Pour modifier :

    Clique sur un contact dans la liste

    Change les infos dans les champs

    Clique sur Modifier

4.Pour supprimer :

    Sélectionne un contact

    Clique sur Supprimer

5.Utilise la barre de recherche pour filtrer la liste par nom.


Quelques règles dans l'application:

- Le nom ne peut pas contenir de chiffres

- Le téléphone ne doit pas contenir de lettres

- L’email est optionnel, mais s’il est rempli, il doit être valide


Technologies utilisées:

- Java (Swing pour l'interface)

- JDBC pour la base de données

- SQL (base locale)


 Structure du projet:

     /contactmanager
        Main.java         <-- le fichier principal avec l’interface
        Contact.java      <-- modèle de données (classe Contact)
        Database.java     <-- gestion de la connexion à la base
