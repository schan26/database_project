DROP TABLE ManagedBy CASCADE CONSTRAINTS;
DROP TABLE PolicyHeld CASCADE CONSTRAINTS;
DROP TABLE Payment CASCADE CONSTRAINTS;
DROP TABLE Claim CASCADE CONSTRAINTS;
DROP TABLE Autocare CASCADE CONSTRAINTS;
DROP TABLE Healthcare CASCADE CONSTRAINTS;
DROP TABLE Policy CASCADE CONSTRAINTS;
DROP TABLE Client1 CASCADE CONSTRAINTS;
DROP TABLE Client3 CASCADE CONSTRAINTS;
DROP TABLE Client5 CASCADE CONSTRAINTS;
DROP TABLE Client6 CASCADE CONSTRAINTS;
DROP TABLE Agent2 CASCADE CONSTRAINTS;
DROP TABLE Agent1 CASCADE CONSTRAINTS;
DROP TABLE Accident CASCADE CONSTRAINTS;
DROP TABLE Vehicle CASCADE CONSTRAINTS;
DROP TABLE License CASCADE CONSTRAINTS;
DROP TABLE Provider2 CASCADE CONSTRAINTS;
DROP TABLE Provider1 CASCADE CONSTRAINTS;


CREATE TABLE Client1 (
                         c_phoneNum VARCHAR(13) PRIMARY KEY,
                         firstName VARCHAR(50)

);
CREATE TABLE Client3 (
                         c_phoneNum VARCHAR(13) PRIMARY KEY,
                         lastName VARCHAR(50),
                         FOREIGN KEY (c_phoneNum) REFERENCES Client1 ON DELETE CASCADE
);
CREATE TABLE Client5 (
                         c_phoneNum VARCHAR(13) PRIMARY KEY,
                         birthdate DATE,
                         FOREIGN KEY (c_phoneNum) REFERENCES Client1 ON DELETE CASCADE
);
CREATE TABLE Client6 (
                         clientID INT PRIMARY KEY CHECK ( clientID >= 0 ),
                         c_phoneNum VARCHAR(13) UNIQUE,
                         FOREIGN KEY (c_phoneNum) REFERENCES Client1 ON DELETE CASCADE
);


CREATE TABLE Provider1(
                          p_phoneNumber VARCHAR(13) PRIMARY KEY,
                          p_name VARCHAR(50)
);

CREATE TABLE Provider2(
                          plicense# VARCHAR(20) PRIMARY KEY,
                          p_phoneNumber VARCHAR(13),
                          FOREIGN KEY(p_phoneNumber) REFERENCES Provider1 ON DELETE CASCADE
);


CREATE TABLE Policy (
                        policyID INT PRIMARY KEY CHECK ( policyID >= 0 ),
                        policyName VARCHAR(100),
                        plicense# VARCHAR(20) NOT NULL,
                        coverageLimit INT CHECK ( coverageLimit >= 0 ),
                        startDate DATE,
                        endDate DATE,
                        policyStatus VARCHAR(20),
                        FOREIGN KEY(plicense#) REFERENCES Provider2 ON DELETE CASCADE
);


CREATE TABLE Healthcare (
                            policyID INT PRIMARY KEY CHECK ( policyID >= 0 ),
                            premium INT CHECK ( premium >= 0 ),
                            deductible INT CHECK ( deductible >= 0 ),
                            FOREIGN KEY(policyID) REFERENCES Policy
                                ON DELETE CASCADE
);

CREATE TABLE Vehicle (
                         VIN VARCHAR(17) PRIMARY KEY,
                         clientID INT NOT NULL,
                         type VARCHAR(10),
                         make VARCHAR(10),
                         year INT CHECK ( year > 1900 ),
                         FOREIGN KEY(clientID) REFERENCES Client6
                             ON DELETE CASCADE
);

CREATE TABLE Autocare(
                         policyID INT PRIMARY KEY CHECK ( policyID >= 0 ),
                         vin VARCHAR(17) NOT NULL,
                         liabilityCoverage INT CHECK ( liabilityCoverage >= 0 ),
                         comprehensiveCoverage INT CHECK ( comprehensiveCoverage >= 0 ),
                         FOREIGN KEY(policyID) REFERENCES Policy
                             ON DELETE CASCADE,
                         FOREIGN KEY(vin) REFERENCES Vehicle
                             ON DELETE SET NULL
);

CREATE TABLE Accident (
                          accidentID INT PRIMARY KEY CHECK ( accidentID >= 0 ),
                          clientID INT NOT NULL,
                          accidentDate DATE,
                          Location VARCHAR(200),
                          FOREIGN KEY(clientID) REFERENCES Client6
                              ON DELETE CASCADE
);


CREATE TABLE Claim(
                      claimID INTEGER PRIMARY KEY CHECK ( claimID >= 0 ),
                      accidentID INT NOT NULL,
                      policyID INT NOT NULL,
                      claimAmount INT CHECK ( claimAmount >= 0 ),
                      claimDate DATE,
                      Status VARCHAR(10),
                      UNIQUE(accidentID),
                      FOREIGN KEY (accidentID) REFERENCES Accident
                          ON DELETE CASCADE,
                      FOREIGN KEY (policyID) REFERENCES Policy
                          ON DELETE CASCADE
);

CREATE TABLE Payment (
                         paymentID INTEGER PRIMARY KEY CHECK ( paymentID >= 0 ),
                         policyID INTEGER,
                         clientID INTEGER,
                         amount INTEGER CHECK ( amount >= 0 ),
                         paymentDate DATE,
                         paymentMethod VARCHAR(20),
                         FOREIGN KEY (policyID) REFERENCES Policy(policyID) ON DELETE CASCADE,
                         FOREIGN KEY (clientID) REFERENCES Client6 ON DELETE CASCADE
);




CREATE TABLE Agent1(
                       a_phoneNumber VARCHAR(13) PRIMARY KEY,
                       a_name VARCHAR(50)
);
CREATE TABLE Agent2(
                       agentLicense# VARCHAR(10) PRIMARY KEY,
                       a_phoneNumber VARCHAR(13),
                       FOREIGN KEY(a_phoneNumber) REFERENCES Agent1 ON DELETE CASCADE
);

CREATE TABLE License(
                        licenseNumber VARCHAR(20),
                        Country VARCHAR(30),
                        clientID INT,
                        Expirydate DATE,
                        PRIMARY KEY (licenseNumber, country, clientID),
                        FOREIGN KEY (clientID) REFERENCES Client6
                            ON DELETE CASCADE
);


CREATE TABLE PolicyHeld (
                            clientID INTEGER,
                            policyID INTEGER,
                            PRIMARY KEY (clientID, policyID),
                            FOREIGN KEY (clientID) REFERENCES Client6(clientID)
                                ON DELETE CASCADE,
                            FOREIGN KEY (policyID) REFERENCES Policy(policyID)
                                ON DELETE CASCADE

);


CREATE TABLE ManagedBy (
                           AgentLicense# VARCHAR(10),
                           policyID INTEGER,
                           PRIMARY KEY (AgentLicense#, policyID),
                           FOREIGN KEY (AgentLicense#) REFERENCES Agent2(AgentLicense#),
                           FOREIGN KEY (policyID) REFERENCES Policy(policyID)
                                    ON DELETE CASCADE

);

--insert statetments


INSERT INTO Client1 VALUES ('9999999999', 'James');
INSERT INTO Client1 VALUES ('1234567890', 'Alice');
INSERT INTO Client1 VALUES ('2222222222', 'Bob');
INSERT INTO Client1 VALUES ('1111111111', 'Diana');
INSERT INTO Client1 VALUES ('6789023455', 'Ethan');

INSERT INTO Client3 VALUES ('1234567890', 'Smith');
INSERT INTO Client3 VALUES ('2222222222', 'Johnson');
INSERT INTO Client3 VALUES ('1111111111', 'Brown');
INSERT INTO Client3 VALUES ('6789023455', 'Lee');
INSERT INTO Client3 VALUES ('9999999999', 'Feng');

INSERT INTO Client5 VALUES ('1234567890', '1999-01-01');
INSERT INTO Client5 VALUES ('9999999999', '1988-03-14');
INSERT INTO Client5 VALUES ('1111111111', '1996-07-31');
INSERT INTO Client5 VALUES ('2222222222', '1995-12-31');
INSERT INTO Client5 VALUES ('6789023455', '2001-08-10');

INSERT INTO Client6 VALUES (1, '1234567890');
INSERT INTO Client6 VALUES (2, '2222222222');
INSERT INTO Client6 VALUES (3, '1111111111');
INSERT INTO Client6 VALUES (4, '9999999999');
INSERT INTO Client6 VALUES (5, '6789023455');

INSERT INTO Provider1 VALUES ('4457763597', 'Kendall');
INSERT INTO Provider1 VALUES ('2364567890', 'Macy');
INSERT INTO Provider1 VALUES ('7784562300', 'Amy');
INSERT INTO Provider1 VALUES ('3451234578', 'Stewart');
INSERT INTO Provider1 VALUES ('2368872005', 'Lewis');

INSERT INTO Provider2 VALUES ('RN007890', '4457763597');
INSERT INTO Provider2 VALUES ('XT665578', '2364567890');
INSERT INTO Provider2 VALUES ('ZC125467', '7784562300');
INSERT INTO Provider2 VALUES ('RF439056', '3451234578');
INSERT INTO Provider2 VALUES ('TK007123', '2368872005');

INSERT INTO Policy VALUES (106, 'Standard Auto Plan', 'RN007890', 50000, '2022-02-01', '2024-02-01', 'Expired');
INSERT INTO Policy VALUES (107, 'Premium Auto Plan', 'XT665578', 100000, '2023-05-25', '2023-05-25', 'Expired');
INSERT INTO Policy VALUES (103, 'Basic Health Coverage', 'RF439056', 20000, '2012-09-01', '2022-09-01', 'Expired');
INSERT INTO Policy VALUES (104, 'Comprehensive Health Plan', 'RN007890', 75000, '2023-03-20', '2028-03-20', 'Active');
INSERT INTO Policy VALUES (105, 'Basic Health Coverage', 'RF439056', 30000, '2025-12-01', '2026-12-01', 'Pending');
INSERT INTO Policy VALUES (102, 'Standard Health Care', 'RN007890', 50000, '2022-02-01', '2024-02-01', 'Expired');
INSERT INTO Policy VALUES (101, 'Premium Health Insurance', 'XT665578', 100000, '2023-05-25', '2023-05-25', 'Expired');
INSERT INTO Policy VALUES (108, 'VIP autocare', 'TK007123', 20000, '2012-09-01', '2022-09-01', 'Expired');
INSERT INTO Policy VALUES (109, 'Premium vehicle insurance', 'ZC125467', 75000, '2023-03-20', '2028-03-20', 'Active');
INSERT INTO Policy VALUES (110, 'Premium Auto Plan', 'XT665578', 30000, '2025-12-01', '2026-12-01', 'Pending');

INSERT INTO Healthcare VALUES (101, 1200, 500);
INSERT INTO Healthcare VALUES (102, 1250, 520);
INSERT INTO Healthcare VALUES (103, 1300, 550);
INSERT INTO Healthcare VALUES (104, 1350, 600);
INSERT INTO Healthcare VALUES (105, 1375, 610);

INSERT INTO Vehicle VALUES ('1HGCM502432G015', 1, 'Sedan', 'Toyota', 2020);
INSERT INTO Vehicle VALUES ('2ABCM123456B000', 2, 'Hatchback', 'Toyota', 2021);
INSERT INTO Vehicle VALUES ('3BCDJ222222A999', 3, 'Sedan', 'Honda', 2010);
INSERT INTO Vehicle VALUES ('4ABCDE000000A33', 4, 'Sedan', 'Hyundai', 2012);
INSERT INTO Vehicle VALUES ('5FGHJ666666E228', 5, 'Sedan', 'Mazda', 2018);

INSERT INTO Autocare VALUES (106, '1HGCM502432G015', 250000, 150000);
INSERT INTO Autocare VALUES (107, '2ABCM123456B000', 250000, 150000);
INSERT INTO Autocare VALUES (108, '3BCDJ222222A999', 200000, 100000);
INSERT INTO Autocare VALUES (109, '4ABCDE000000A33', 200000, 100000);
INSERT INTO Autocare VALUES (110, '5FGHJ666666E228', 200000, 100000);

INSERT INTO Payment VALUES (1, 106, 1, 900, '2024-01-05', 'Credit');
INSERT INTO Payment VALUES (2, 107, 2, 1200, '2024-02-14', 'Debit');
INSERT INTO Payment VALUES (3, 103, 3, 700, '2024-03-06', 'Cash');
INSERT INTO Payment VALUES (4, 109, 4, 700, '2024-04-06', 'Cash');
INSERT INTO Payment VALUES (5, 110, 5, 700, '2024-05-06', 'Cash');

INSERT INTO Accident VALUES (301, 3, '2024-01-25', 'Main St and 1st Ave');
INSERT INTO Accident VALUES (302, 2, '2023-08-25', 'Fraser St and 41st Ave');
INSERT INTO Accident VALUES (303, 2, '2023-08-25', 'Fraser St and 41st Ave');
INSERT INTO Accident VALUES (304, 4, '2023-07-28', 'Fraser St and 41st Ave');
INSERT INTO Accident VALUES (305, 5, '2025-06-25', 'Fraser St and 41st Ave');
INSERT INTO Accident VALUES (306, 3, '2025-01-25', 'Fraser St and 41st Ave');
INSERT INTO Accident VALUES (307, 3, '2025-02-25', 'Knight St and 41st Ave');
INSERT INTO Accident VALUES (308, 3, '2025-03-25', 'Victoria Drive and 41st Ave');
INSERT INTO Accident VALUES (309, 3, '2025-04-25', 'Fraser St and 49th Ave');
INSERT INTO Accident VALUES (310, 3, '2025-05-25', 'Fraser St and 58th Ave');

INSERT INTO Claim VALUES (401, 301, 106, 2500, '2025-01-15', 'Pending');
INSERT INTO Claim VALUES (406, 306, 106, 3000, '2025-02-15', 'Pending');
INSERT INTO Claim VALUES (407, 307, 106, 5000, '2025-03-15', 'Approved');
INSERT INTO Claim VALUES (408, 308, 106, 2800, '2025-04-15', 'Approved');
INSERT INTO Claim VALUES (409, 309, 106, 2500, '2025-05-15', 'Approved');
INSERT INTO Claim VALUES (410, 310, 106, 2100, '2025-06-15', 'Denied');
INSERT INTO Claim VALUES (402, 302, 107, 6000, '2024-03-15', 'Denied');
INSERT INTO Claim VALUES (403, 303, 107, 1500, '2022-12-05', 'Pending');
INSERT INTO Claim VALUES (404, 304, 109, 4500, '2025-05-18', 'Approved');
INSERT INTO Claim VALUES (405, 305, 110, 3000, '2025-02-13', 'Denied');




INSERT INTO Agent1 VALUES ('6046046004', 'John Smith');
INSERT INTO Agent2 VALUES ('D1235', '6046046004');
INSERT INTO Agent1 VALUES ('1231231234', 'Chris James');
INSERT INTO Agent2 VALUES ('A1111', '1231231234');
INSERT INTO Agent1 VALUES ('8897838790', 'Bob');
INSERT INTO Agent2 VALUES ('B7789', '8897838790');
INSERT INTO Agent1 VALUES ('1234567890', 'Matt');
INSERT INTO Agent2 VALUES ('C0000', '1234567890');
INSERT INTO Agent1 VALUES ('8888888888', 'James');
INSERT INTO Agent2 VALUES ('E9999', '8888888888');

INSERT INTO License VALUES ('D1234567', 'Canada', 1,  '2027-05-01');
INSERT INTO License VALUES ('A2345678', 'Canada', 2,  '2026-05-01');
INSERT INTO License VALUES ('C1234899', 'Canada', 3,  '2026-07-19');
INSERT INTO License VALUES ('B333000', 'Canada', 4,  '2028-04-01');
INSERT INTO License VALUES ('L990568', 'Canada', 5,  '2028-09-01');



INSERT INTO PolicyHeld VALUES (1, 106);
INSERT INTO PolicyHeld VALUES (1, 101);
INSERT INTO PolicyHeld VALUES (2, 107);
INSERT INTO PolicyHeld VALUES (3, 105);
INSERT INTO PolicyHeld VALUES (4, 109);
INSERT INTO PolicyHeld VALUES (5, 110);
INSERT INTO PolicyHeld VALUES (3, 103);

INSERT INTO ManagedBy values ('D1235', 104);
INSERT INTO ManagedBy values ('A1111', 105);
INSERT INTO ManagedBy values ('E9999', 103);
INSERT INTO ManagedBy values ('E9999', 106);
INSERT INTO ManagedBy values ('E9999', 107);
INSERT INTO ManagedBy values ('E9999', 108);
INSERT INTO ManagedBy values ('E9999', 109);