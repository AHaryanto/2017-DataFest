Views

1) Customer History
With this view here we can see how many books customers have bought in their time with the store. This could be useful if the store comes up with a reward system for people who frequently buy books there.

Relational Algebra:
PURCHASED_BOOK BOOK ⨝ book_ID = book_ID BUYS
COUNT customer_ID ℱCOUNT book_ID (PURCHASED_BOOK)
RESULT π customer_ID, Count_book_ID (COUNT)

SQL Statements:
CREATE VIEW Customer_History
AS 	SELECT A.customer_ID, count(A.book_ID)
FROM BOOK B, BUYS A
WHERE B.book_ID = A.book_ID
GROUP BY A.customer_ID;

2) Total Books
This view allows us to see how many books are in each individual store. This is useful since it could tall managers when they need to order more books for that individual store.

Relational Algebra:
AVAILABLE_BOOK BOOK ⨝ book_ID = book_ID AVAILABLE_IN
COUNT store_ID ℱCOUNT book_ID (AVAILABLE_BOOK)
RESULT π store_ID, Count_book_ID (COUNT)

SQL Statements:
CREATE VIEW Total_Books
AS 	SELECT A.store_ID, count(A.book_ID)
FROM BOOK B, AVAILABLE_IN A
WHERE B.book_ID = A.book_ID
GROUP BY A.store_ID;

