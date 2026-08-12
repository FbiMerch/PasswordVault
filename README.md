Iată conținutul integral al fișierului **`README.md`**. Copiază tot textul din blocul de mai jos și lipsește-l în fișierul tău `README.md` din IntelliJ (sau creează un fișier nou cu numele `README.md` în folderul proiectului).

# 🔒 Password Vault (Java Swing & SQLite)

![Java](https://img.shields.io/badge/Java-17%2B-blue.svg)
![SQLite](https://img.shields.io/badge/SQLite-3-green.svg)
![Security](https://img.shields.io/badge/Encryption-AES--256-red.svg)
![License](https://img.shields.io/badge/License-MIT-brightgreen.svg)

A secure, desktop-based **Password Manager** written in **Java** using **Swing GUI**, **SQLite** for local database storage, and **AES-256** encryption to safeguard sensitive credentials.

---

## 📋 Features

- 🔑 **Master Password Access**: Protects the vault using PBKDF2 key derivation from a user-defined Master Password.
- 🛡️ **AES-256 Encryption**: All stored passwords are encrypted before being written to the SQLite database.
- 🎲 **Strong Password Generator**: Generates cryptographically secure random passwords (16 characters).
- 🖥️ **Graphical User Interface (GUI)**: Clean Java Swing UI with capabilities to view, search, add, and delete records.
- 💾 **Encrypted Backup Export**: Export stored records to an external backup text file (`vault_backup.txt`).
- 🔍 **Real-time Search**: Filter entries dynamically by site name.

---

## 🏗️ Architecture & OOP Design

The project strictly follows Object-Oriented Programming (OOP) principles and separates concerns into clear layers:

| Class | Type | Responsibility |
| :--- | :--- | :--- |
| `Credential.java` | **Model** | Encapsulates credential data (`id`, `site`, `username`, `password`, `notes`). |
| `CryptoUtils.java` | **Security Service** | Handles PBKDF2 key derivation, AES-256 encryption/decryption, and random password generation. |
| `DatabaseManager.java` | **DAO Layer** | Manages SQLite database connectivity, schema creation, and CRUD operations. |
| `VaultGUI.java` | **View / Controller** | Renders the graphical user interface (Swing), captures user inputs, and triggers operations. |

---

## 🚀 Getting Started & Setup

### Prerequisites

- **Java Development Kit (JDK)**: Version 17 or higher.
- **IDE**: IntelliJ IDEA (or any Java IDE).
- **SQLite JDBC Driver**: `org.xerial:sqlite-jdbc` (version `3.45.1.0` or higher).

---

## ⚙️ How to Run in IntelliJ IDEA

1. **Clone the repository:**
  
   git clone [https://github.com/FbiMerch/PasswordVault.git](https://github.com/FbiMerch/PasswordVault.git)

2. **Open in IntelliJ IDEA:**
* Open IntelliJ IDEA $\rightarrow$ **Open** $\rightarrow$ select the `PasswordVault` directory.

3. **Add SQLite JDBC Dependency:**
* Go to **File** $\rightarrow$ **Project Structure** $\rightarrow$ **Libraries**.
* Click **`+`** (New Project Library) $\rightarrow$ **From Maven...**.
* Search for `org.xerial:sqlite-jdbc:3.45.1.0` and click **OK**.


4. **Run the Application:**
* Open `VaultGUI.java`.
* Click the green **Run** arrow or press `Shift + F10`.


---

## 🖥️ Application Interface Overview

1. **Authentication Screen**: Prompts for the Master Password on launch.
2. **Main Dashboard**: Displays a tabular list of stored credentials with decrypted passwords.
3. **Add Entry Dialog**: Allows adding site credentials with encrypted storage.
4. **Password Generator**: Generates strong 16-character passwords on demand.
5. **Export**: Saves an encrypted local copy to `vault_backup.txt`.

