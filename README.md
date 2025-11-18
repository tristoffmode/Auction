📄 README – Projet R3.06 : Serveur d’Enchères (Java / Sockets)
👥 Étudiant(s)
NGUYEN Quoc Tri


📌 Description du projet
- Ce projet consiste à développer une application client-serveur en Java, utilisant les sockets TCP, permettant de gérer une enchère en temps réel.

- Le serveur reçoit les connexions de plusieurs clients, gère le montant de l’enchère, diffuse les mises à jour et applique une logique de compte à rebours (“Une fois”, “Deux fois”, “Adjugé”).

- Aucune interface graphique (IHM) n’est utilisée : toutes les interactions se font dans le terminal.

🎯 Fonctionnalités principales
🖥️ Côté Serveur
	- Accepte plusieurs clients en parallèle grâce aux threads.
	- Demande le nom du client à chaque connexion.
	- Envoie le montant actuel de l’enchère.
	- Reçoit les nouvelles enchères (valeurs entières).

Diffuse à tous les clients :
	+ la nouvelle enchère
	+ "Une fois" après 10 secondes d’inactivité
	+ "Deux fois" après 20 secondes
	+ "Adjugé" + nom du gagnant après 30 secondes

- Réinitialise le compte à rebours dès qu’un client propose un nouveau montant.
- Gère proprement les déconnexions.

👤 Côté Client
	- Envoie son nom au serveur.
	- Affiche toutes les annonces provenant du serveur.
	- Permet d’envoyer une enchère en tapant un nombre entier.
	- Communication en temps réel via deux threads :
		+ lecture du serveur
		+ saisie utilisateur

🛠️ Technologies / Concepts utilisés
	- Java Sockets (TCP)
	- Multi-threading (Thread, Runnable)
	- Synchronisation (synchronized, objets partagés)
	- Communication texte client–serveur
	- Gestion du broadcast
	- Timers basés sur System.currentTimeMillis()

📁 Contenu du projet
Fichiers inclus :
	- AuctionServer.java — code du serveur
	- AuctionClient.java — code du client
	- README.md — documentation du projet