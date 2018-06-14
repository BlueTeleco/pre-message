-- 
-- Script to create the application database
-- 

CREATE TABLE Users (
	id		INTEGER,
	name	VARCHAR(255),
	phone	VARCHAR(255),
	pubKey	BLOB,
	PRIMARY KEY (id)
);

CREATE TABLE ReEncryptionKeys (
	delegator	INTEGER,
	delegatee	INTEGER,
	reKey		BLOB,
	FOREIGN KEY (delegator) REFERENCES Users(id)
	FOREIGN KEY (delegatee) REFERENCES Users(id)
);

CREATE TABLE Chats (
	id		INTEGER,
	admin	INTEGER,
	name	VARCHAR(255),
	PRIMARY KEY (id)
	FOREIGN KEY (admin) REFERENCES Users(id)
);

CREATE TABLE ChatMembers (
	member	INTEGER,
	chat	INTEGER,
	FOREIGN KEY (member) REFERENCES Users(id)
	FOREIGN KEY (chat) 	 REFERENCES Chats(id)
);

CREATE TABLE Messages (
	id			INTEGER,
	text		VARCHAR(255),
	chat		INTEGER,
	sender		INTEGER,
	PRIMARY KEY (id)
	FOREIGN KEY (chat) 	 REFERENCES Chats(id)
	FOREIGN KEY (sender) REFERENCES Users(id)
);

-- Fill the Users table with mock users
INSERT INTO Users (id, name, phone) VALUES
(1, "lucas", "000");

-- Fill the Chats table with mok chats
INSERT INTO Chats (id, admin, name) VALUES
(1, 1, "Yope");

-- Fill the ChatMembers table with mok chat members
INSERT INTO ChatMembers (chat, member) VALUES
(1, 1);
