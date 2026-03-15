# 🎓 Système de Gestion Académique

> Application de bureau JavaFX destinée aux administrateurs d'établissements d'enseignement supérieur, permettant la gestion complète des étudiants, des notes et des relevés de performance.

---

## 📌 Contexte

Ce projet a été réalisé dans le cadre du **Mini-Projet IHM** (Interfaces Homme-Machine) —  
**École Supérieure de Technologie Sidi Bennour (ESTSB)** — DUT Génie Informatique, 2ème année.

L'objectif principal est de démontrer la maîtrise des **interfaces Java** comme contrat de comportement entre les classes, ainsi que les principes fondamentaux de la **programmation orientée objet**.

| | |
|---|---|
| **Étudiant**            | Anass Bellagrid                                      |
| **Filière**             | Génie Informatique — DUT 2ème année                  |
| **Établissement**       | École Supérieure de Technologie Sidi Bennour (ESTSB) |
| **Professeur**          | Mr Kamal El Hattab                                   |
| **Année universitaire** | 2025 / 2026                                          |
| **Date de rendu**       | 15 Mars 2026                                         ||


## 🖥️ Aperçu de l'Application

| Écran         | Description                                           |
|---------------|-------------------------------------------------------|
| **Login**     | Authentification sécurisée de l'administrateur        |
| **Dashboard** | Vue d'ensemble statistique de la promotion            |
| **Étudiants** | Gestion CRUD complète avec recherche en temps réel    |
| **Notes**     | Saisie des notes, graphique, relevé instantané        |
| **Export**    | Export des relevés individuels et promotion en `.txt` |

---

## ⚙️ Technologies Utilisées

| Technologie            | Version | Rôle                           |
|------------------------|---------|--------------------------------|
| Java (OpenJDK Temurin) | 17.0.16 | Langage principal              |
| JavaFX                 | 25.0.1  | Interface graphique            |
| Git / GitHub           |    —    | Versioning du code             |
| VSCode                 |    —    | Environnement de développement |

---

## 🏗️ Architecture — Interfaces Java

Le projet utilise **5 interfaces Java** comme pilier de l'architecture :

```
interfaces/
├── ICalculable.java     → Calcul de moyenne, meilleur/pire module
├── IAffichable.java     → Affichage des informations et relevé
├── IEvaluable.java      → Note, mention, validation d'un module
├── IExportable.java     → Export en .txt et format CSV
└── IRecherchable.java   → Recherche par nom ou identifiant
```

### Diagramme simplifié

```
«interface»          «interface»          «interface»
ICalculable          IAffichable          IEvaluable
     ▲                    ▲                    ▲
     │                    │                    │
     └──────────┬─────────┘                    │
            Etudiant                        Module

«interface»          «interface»
IExportable          IRecherchable<T>
     ▲                    ▲
     │                    │
  Etudiant          GestionEtudiants
```

---

## 📁 Structure du Projet

```
Projet IHM/
├── src/
│   ├── interfaces/
│   │   ├── ICalculable.java
│   │   ├── IAffichable.java
│   │   ├── IEvaluable.java
│   │   ├── IExportable.java
│   │   └── IRecherchable.java
│   ├── models/
│   │   ├── Module.java
│   │   └── Etudiant.java
│   ├── controllers/
│   │   └── GestionEtudiants.java
│   ├── views/
│   │   ├── LoginView.java
│   │   ├── DashboardView.java
│   │   ├── EtudiantsView.java
│   │   ├── NotesView.java
│   │   └── ExportView.java
│   └── MainApp.java
├── out/           ← Fichiers compilés (ignoré par Git)
├── data/          ← Sauvegarde CSV automatique (ignoré par Git)
├── .gitignore
└── README.md
```

---

## 🚀 Installation et Lancement

### Prérequis
- **Java 17+** — [Télécharger OpenJDK Temurin](https://adoptium.net/)
- **JavaFX SDK 25** — [Télécharger JavaFX](https://gluonhq.com/products/javafx/)

### Étapes

**1. Cloner le projet**
```bash
git clone https://github.com/wh0s-ans/Gestion-Academique.git
cd Gestion-Academique
```

**2. Créer le dossier de sortie**
```cmd
mkdir out
```

**3. Compiler**
```cmd
javac --module-path "CHEMIN\javafx-sdk-25.0.1\lib" --add-modules javafx.controls -encoding UTF-8 -d out src/interfaces/ICalculable.java src/interfaces/IAffichable.java src/interfaces/IEvaluable.java src/interfaces/IExportable.java src/interfaces/IRecherchable.java src/models/Module.java src/models/Etudiant.java src/controllers/GestionEtudiants.java src/views/LoginView.java src/views/DashboardView.java src/views/EtudiantsView.java src/views/NotesView.java src/views/ExportView.java src/MainApp.java
```

**4. Lancer**
```cmd
java --module-path "CHEMIN\javafx-sdk-25.0.1\lib" --add-modules javafx.controls -cp out MainApp
```

> Remplacez `CHEMIN` par le chemin de votre installation JavaFX.

---

## 🔐 Connexion

| Champ        | Valeur  |
|--------------|---------|
| Utilisateur  | `admin` |
| Mot de passe | `1234`  |

---

## ✨ Fonctionnalités

- ✅ Authentification administrateur
- ✅ Tableau de bord avec statistiques en temps réel
- ✅ Ajout / modification / suppression d'étudiants
- ✅ Recherche instantanée par nom
- ✅ Saisie et gestion des notes par module
- ✅ Calcul automatique de la moyenne pondérée
- ✅ Graphique des notes par module (BarChart)
- ✅ Génération du relevé de notes
- ✅ Export relevé individuel en `.txt`
- ✅ Export promotion complète en `.txt`
- ✅ Sauvegarde automatique en fichier CSV

---

## 📄 Licence

Projet académique — ESTSB 2025/2026. Tous droits réservés.
