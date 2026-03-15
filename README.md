# Systeme de Gestion Academique
## Mini-Projet Java — Interfaces (POO) + JavaFX

---

## Structure du Projet

```
AcademicSystem/
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
├── out/
├── data/   (cree automatiquement)
└── README.md
```

---

## Etapes pour lancer le projet

### 1. Ouvrir un terminal dans VSCode
```
Terminal > New Terminal
```

### 2. Aller dans le dossier du projet
```cmd
cd D:\AcademicSystem
```

### 3. Creer le dossier de sortie
```cmd
mkdir out
```

### 4. Compiler le projet
```cmd
javac --module-path "C:\Users\pc\Downloads\openjfx-25.0.1_windows-x64_bin-sdk\javafx-sdk-25.0.1\lib" --add-modules javafx.controls -encoding UTF-8 -d out src/interfaces/ICalculable.java src/interfaces/IAffichable.java src/interfaces/IEvaluable.java src/interfaces/IExportable.java src/interfaces/IRecherchable.java src/models/Module.java src/models/Etudiant.java src/controllers/GestionEtudiants.java src/views/LoginView.java src/views/DashboardView.java src/views/EtudiantsView.java src/views/NotesView.java src/views/ExportView.java src/MainApp.java
```

### 5. Lancer l'application
```cmd
java --module-path "C:\Users\pc\Downloads\openjfx-25.0.1_windows-x64_bin-sdk\javafx-sdk-25.0.1\lib" --add-modules javafx.controls -cp out MainApp
```

---

## Connexion Admin
- Utilisateur : admin
- Mot de passe : 1234

---

## Fonctionnalites
1. Login administrateur securise
2. Dashboard avec statistiques de la promotion
3. Gestion etudiants (ajouter, modifier, supprimer, rechercher)
4. Gestion des notes par etudiant avec graphique
5. Export releve individuel en .txt
6. Export promotion complete en .txt
7. Sauvegarde automatique en CSV

---

## Interfaces Java utilisees
| Interface      | Role                                      | Implementee par          |
|----------------|-------------------------------------------|--------------------------|
| ICalculable    | Calcul moyenne, meilleur/pire module      | Etudiant                 |
| IAffichable    | Affichage infos et releve de notes        | Etudiant, Module         |
| IEvaluable     | Note, mention, validation                 | Module                   |
| IExportable    | Export .txt et format CSV                 | Etudiant                 |
| IRecherchable  | Recherche par nom ou ID                   | GestionEtudiants         |
