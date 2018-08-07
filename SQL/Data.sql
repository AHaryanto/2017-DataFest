INSERT Syntax

INSERT book syntax:
  INSERT INTO BOOK (book_ID, author_ID, publisher_ID, customer_rating, price, condition, title, category, year)
  VALUES (‘142’, ‘Doug Stuns’, ‘Syb1’, ‘5’, ‘20.00’, ‘new’, ‘Database 101’, ‘Computer’, ‘2017’);
  *only customer_rating and condition may be NULL

INSERT publisher syntax:
  INSERT INTO PUBLISHER (publisher_ID, book_ID, publisher_name)
  VALUES (‘Syb1’, ‘142’, ‘Sybex’);

INSERT author syntax:
  INSERT INTO PUBLISHER (publisher_ID, book_ID, publisher_name)
  VALUES (‘Syb1’, ‘142’, ‘Sybex’);

INSERT customer syntax:
  INSERT INTO CUSTOMER (customer_ID, fname, lname, street1, street2, city, country, postal)
  VALUES (‘21’, ‘John’, ‘Doe’, ‘143 Fake St’, ‘1’, ‘Columbus’, ‘US’, ‘43210’);

Restrictions:
  Both the publisher and the author of a book must be added into the database before the book.	



DELETE Syntax

DELETE book syntax:
  DELETE FROM BOOK
  WHERE *;
  (deletes all book entities from the database)
Or
  DELETE FROM BOOK
  WHERE author_ID = ‘Nate Weiss’;
  (deletes all book entities with the given author from the database)
Or
  DELETE FROM BOOK
  WHERE author_ID = ‘Nate Weiss’ AND Year = 1999;
  (deletes all book entities with the given author_ID and year from the database)

DELETE publisher syntax:
  DELETE FROM PUBLISHER
  WHERE *;
  (deletes all publisher records from the database)
Or
  DELETE FROM PUBLISHER
  WHERE publisher_ID = ‘Syb1’;
  (deletes all publisher records whose ID is “Syb1” from the database)

DELETE author syntax:
  DELETE FROM AUTHOR
  WHERE *;
  (deletes all author records from the database)
Or
  DELETE FROM AUTHOR
  WHERE author_ID = ‘Jordan Hart’;
  (deletes all author records whose ID is ‘Jordan Hart” from the database)

DELETE customer syntax:
  DELETE FROM CUSTOMER
  WHERE *;
  (deletes all customer records from the database)
Or
  DELETE FROM CUSTOMER
  WHERE customer_ID = ‘1’;
  (deletes all customer records whose ID is “1” from the database)
Or
  DELETE FROM CUSTOMER
  WHERE city = ‘Cleveland’ AND lname = ‘Smith’;
  (deletes all customer records whose last name is “Smith” and lives in “Cleveland” from the database)

Restrictions:
  Must delete all books from a publisher and author before deleting a publisher or author.

