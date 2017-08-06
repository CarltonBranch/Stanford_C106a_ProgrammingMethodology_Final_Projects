DROP DATABASE IF EXISTS Face_Pamphlet;
CREATE DATABASE Face_Pamphlet;
USE Face_Pamphlet;

DROP TABLE IF EXISTS Profile;
CREATE TABLE Profile(
                     ProfileId Varchar(8) NOT NULL PRIMARY KEY,
					 FirstName Varchar(25),
                     LastName varchar(50),
                     Birthday date,
                     Image longblob,
                     DateJoined varchar (15)
                     );
                     
DROP TABLE IF EXISTS Profile_Friend;
CREATE TABLE Profile_Friend(
							 ProfileFriendId int NOT NULL PRIMARY KEY AUTO_INCREMENT,
							 ProfileId_FK Varchar(8) NOT NULL,
                             FriendId Varchar(8) NOT NULL 
						    );

DROP TABLE IF EXISTS StatusMessage;                            
CREATE TABLE StatusMessage(
							StatusMessageId int NOT NULL auto_increment,
                            ProfileId_FK Varchar(8),
                            StatusMsg varchar(255),
                            MessagePostDate varchar(30),
                            PRIMARY KEY (StatusMessageId),
                            FOREIGN KEY (ProfileId_FK) REFERENCES Profile (ProfileId)
                            );
                            
                            