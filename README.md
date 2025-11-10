# Java Chat Application

A simple real-time chat application with user authentication, private messaging, and message history built with Java Sockets (server-client), JavaFX GUI (client), and MySQL database.

## Features

- **User registration and login**
- **Private chat between users**
- **Message history stored in MySQL**
- **JavaFX desktop client GUI**

## Technologies

- Java (Sockets, Threads, JDBC)
- JavaFX (GUI framework)
- MySQL (Database for users and messages)

## Setup Instructions

### 1. Prepare the Java Files

### 2. MySQL Database Setup

- Start your MySQL server and create the database and tables.

### 3. Download Dependencies

- **JavaFX SDK:**  
  Download from [GluonHQ](https://gluonhq.com/products/javafx/)  
  the dependency I added -  `D:/javafx-sdk-21.0.9/`

- **MySQL Connector/J:**  
  Download from [MySQL official site](https://dev.mysql.com/downloads/connector/j/)  

### 4. Configure Connection Settings

- In `ChatServer.java`, set your MySQL connection details:
dbConn = DriverManager.getConnection(
"jdbc:mysql://localhost:3306/chat_app", "root", "YOUR_PASSWORD_HERE");

### 5. Build the Project

### 6. Run the Chat Application

- Start the Server
- Start the Client

### 7. Usage

- Register a user and log in from the client window.
- Open multiple client windows for private messaging.
- All chat history is saved in MySQL database.

## Troubleshooting

- If you see **"No suitable driver"**: Add MySQL Connector/J to classpath.
- If you see **"Connection refused"**: Start the Server before starting clients.
- If you see **"Access denied for user"**: Update the username/password in your code to match MySQL settings.
- If you see **JavaFX runtime errors**: Ensure JavaFX SDK is in module-path and VM options.

## Screenshots
**Java ChatServer and ChatClient**
<img width="1919" height="1020" alt="Screenshot 2025-11-10 160403" src="https://github.com/user-attachments/assets/7c1603ce-e85c-496c-9a09-877d1aa047c9" />
---
**Java FX**
<img width="1919" height="1016" alt="Screenshot 2025-11-10 161108" src="https://github.com/user-attachments/assets/ab1d46ea-d281-42c9-8e76-e892cfe83ac9" />
---
**MySQL**
<img width="1919" height="1009" alt="Screenshot 2025-11-10 160508" src="https://github.com/user-attachments/assets/faddcc36-e29d-47c8-a814-32bea30d2d4b" />
---

## Future Scope

1. Group Chat & Channels
2. File and Media Sharing
3. End-to-End Encryption
4. Better UI/UX
5. Online Status & Typing Indicators
6. Robust Authentication & Security
8. Mobile & Web Clients
9. Voice and Video Chat
10. Database Improvements

## Author

Aashi Vishwakarma (Java Developer)

Email : aashivishwakarma1121@gmail.com
