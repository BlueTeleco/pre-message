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
(1, "lucas", "111111111"),
(2, "pablo", "123456789"),
(3, "jania", "246813579"),
(4, "jacobo", "987654321"),
(5, "alda", "147258369");

-- Fill the ReEncryptionKeys table with mok keys
INSERT INTO ReEncryptionKeys (delegator, delegatee)
(1, 5),
(2, 4),
(5, 1),
(3, 4);

-- Fill the Chats table with mok chats
INSERT INTO Chats (id, admin, name)
(1, 1, "club"),
(2, 3, "familia");

-- Fill the ChatMembers table with mok chat members
INSERT INTO ChatMembers (chat, member)
(1, 1),
(1, 5),
(2, 1),
(2, 2),
(2, 3),
(2, 4);

-- Fill the Messages table with mok messages
INSERT INTO Messages (id, text, chat, sender)
(1, "Hola mundo!", 1, 1),
(2, "Adios mundo!", 1, 5),
(3, "Hola family :D", 2, 2),
(4, "Que tal?", 2, 1);

