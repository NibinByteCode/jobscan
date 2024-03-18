**Adv Mobile Application Development**

Group 5

Application Name: **JOBSCAN**

Course Code: **PROG8700-24W-Sec7-Advanced Mobile Application Development**
Professor Name: **Nicolas Blier**

Group Members:
**Nandipati Vamsi - 8922647

Justin Janson – 8868914

Sandeep Kaur – 8899764

Nibin Moideenkutty – 8890966**

Github link: https://github.com/NibinByteCode/jobscan

JobScan is a Job Recruiting Application. It is used by Recruiters and job seeking Candidates. It has mainly Three Navigation Pages –
1.	Home Page
2.	Connections Page and
3.	Profile page.
Every Page has some unique functionalities that make this application effective and user-friendly. All the functionalities and main features of each page or activity in this application are written below: -
1. SplashScreenActivity: - This Activity contains the splash screen with the attractive logo of the application.
2. LoginActivity and SignUpActivity: - After the splash screen, the user will be landed on the login page. This page has two fields- UserEmail and Password. If the user is already registered, then he can log in to the home page. Otherwise, he must go to the signup page to register himself. The main feature of the signup page is that all fields have validation, and the user cannot sign up before filling all the fields with the correct data format. 
3. MainActivity or Home Page: - This is the application's home page which is opened after user login. This page has CardView and recyclerView, which display all posts created by the users. Every post has an image, content and at the top of the post there is a username and profile image who created that post along with timestamp. All the posts are sorted based on time of creation in descending order. The main feature of this page is, the floating icon at the bottom of the page, that allows users to create a new post.

4. PostActivity: - When user clicks on the floating button in the home page, it will navigate him to the PostActivity where the user is given the opportunity to share any job posting/share his/her thought. The special feature of this page is the ability to upload the images into their post which makes it more initiative and adds more meaning to his post. On click of the post button, the user will be navigated to the home page where his post will be displayed on top of the screen.

5. CandidateActivity or Connections Page: - Candidate Activity has recyclerView that displays all candidates who registered with this application. Each candidate data displays through a card view which contains username, user type, designation, and user profile image. Users can click on any card in the connections to view more details about them. This page's special feature is the case sensitive search functionality, which allows users to search for candidates/recruiters.
6.DetailActivity: - When a user clicks on any of the cards in CandidateActivity, then the user will be navigated to the DetailActivity, which displays all details of the selected candidate such as candidate name, type, connections count, designation, company, email, phone number, qualifications, and date of birth. The main functionality on this page is the connect button, which allows the logged-in user to connect with fellow candidates/recruiters. When the user clicks on the connect button, the ID of the selected candidate/recruiter is added to the connections list of the user, and the logged-in user ID is added to the selected candidate/recruiter connections list (i.e., it becomes a two-way connection). At the same time, the connections count of both the logged-in as well as selected candidate/recruiter will be incremented by 1. All these operations are dynamically recorded in the Realtime database.
7.ProfileActivity: - It displays the profile of current logged-in user with all the details and user image. The special functionality of this page is the logout button which entices the user back to the login page.

Extra Features: 

•	This application also has some extra features such as bottom navigation bar that makes it user-friendly. Users can easily go to any application page with this navigation feature. This nav bar has three icons for navigating to Home, Connections, and Profile page. Another feature is in the home page, post creation search bar. 

•	On the home page, the users have ability to view the profile of the other user who has posted the post by clicking on the profile/name on it.

•	All the pages are responsive and can be viewed in both portrait, landscape and tablet view.
