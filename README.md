# TOWER v1.0.
### What is TOWER?

TOWER is a currently based, Andorid Application, to facilitate trade of textbooks between Hofstra University students. 

### Initial Requirements of project
1. Students should enable students to sell their unwanted textbooks to other students. Students will browse
the avaialable textbooks on their mobile (Android or IOS based) and must have access to the following capabilities.
2. Registration for the system by providing their name, Hofstra ID, e-mail Address, and a password. Ability to log-in 
to the system by enterign their Hofstra ID and password. 
3. Search for textbooks by title, ISBN number, or author, and scroll through the
results of their search. The results of any search should include the price and
condition of the book. Note that more than one of the same textbook may be
returned by the search due to multiple students listing the same book. In this
case the user should be presented with a list of results showing the different
prices and conditions
4. Purchase textbooks by sending an email to the textbook buyer and owner
indicating an intent to purchase. Note that you are not required to implement a
payment execution system. Instead, you are looking to match potential buyers
with sellers.
5. Each user will also be able to sell textbooks. The user will need to enter the title
and ISBN number, price and condition of the book.
6. Remove user’s posted textbooks from the system once the student has completed
a sale or if the user decides to not sell the textbook.
7. Display additional information about textbook search results, provided directly
from Amazon, including an image of the textbook cover, price, authors, and the
book description.

### Changed Requirements
1. It is a Android Based as of for now. 
2. No Change.
3. No Change.
4. Puts the buyer into direct contact with the seller through email.
5. Books are automatically matched through the Google APi to make adding books easier. If non-existent in API, Sellers will
need to manually add a book with Title, Author, ISBN-13, Description, Price and Condition.
6. No Change.
7. Instead Amazon API, we had to change the requirements to fit a Google API due to project members not having a amazon affliate business account.

### Extra Features Added
1. A browse function to see the latest books added to the system
2. A settings option to change email address and password
3. A forgot password option
4. Added Privacy and EULA Agreements.
5. Added a password stength dectector
6. Made it so you cant type letters in a numbers only are vice versa. 

### How is TOWER made?

It is based out of the Android Studio IDE, with the language Java being used, with firebase implementation as a database 
to store various students and books as well as implementation of the google API.

### Download?
TOWER is currently not available to download from the google play store as of right now. To run it you need the android studio application and a emulator. 

### Screenshots
<img src="https://github.com/Tower-CSC-190/TOWER/blob/master/screenshots/Home.png" width="300" height="600"> <img src="https://github.com/Tower-CSC-190/TOWER/blob/master/screenshots/Search%20after%20input.png" width="300" height="600"> <img src="https://github.com/Tower-CSC-190/TOWER/blob/master/screenshots/Login.png" width="300" height="600"> <img src="https://github.com/Tower-CSC-190/TOWER/blob/master/screenshots/Sign-up.png" width="300" height="600"> <img src="https://github.com/Tower-CSC-190/TOWER/blob/master/screenshots/Settings.png" width="300" height="600">



