# MAD-Team4-CacheFlash-Final
CacheFlash - Your flashcards app for studying anytime, anywhere
### About CacheFlash
Wanting to study a particular topic, but struggle to do so? Don't worry, we can help! Introducing the CacheFlash flashcards app, where students from secondary school and junior college can memorise concepts on their own! Its self-directed approach in helping students in their exams can be done at anytime, anywhere. Stage 1 consists of basic features, while stage 2 consists of spaced repetition system (SRS) mode, and more advanced quizzing methods to the user.

#### App Catergory: Education
### Stage 1 - App Features:
- Login and sign-up page for users (Hui Ting)
- Home page - Displays all the default flashcards, and the learn and test feature (Samuel)
- Learn Yourself - user gets to learn concepts using flashcards. (Bryan)
- Test Yourself - user gets to access his/her own knowledge of the topic of choice, by recalling answer by typing answer out, the system will check if the answer typed out matches the correct answer. (John)
- Profile page (Hui Ting)
- App UI and logo (Victoria)
- Uploading to play store (Samuel & Hui Ting)
- Slides (Group)
- Google feedback form for account deletion (John)
- Merging of codes (Samuel & John)
- Adminstrator of repo (Samuel)

### GitHub Previous Commits
Previously, we were experimenting GitHub, and we made mistakes on our previous repo. Hence, we shifted to a new repository, as it is the cleanest way to manage the problem. 
#### Hui Ting's Previous Commits
![image](https://github.com/MAD2023-Team4/MAD-Team4-CacheFlash-Final/assets/133575569/37539aa9-cc7f-45c8-939c-137c6b49fa86)
![image](https://github.com/MAD2023-Team4/MAD-Team4-CacheFlash-Final/assets/133575569/0af23221-f998-4262-9dc1-2cce2ff6c236)
![image](https://github.com/MAD2023-Team4/MAD-Team4-CacheFlash-Final/assets/133575569/7ee4983c-2ef6-4dd8-ae46-675e5bf1bef3)

#### John's Previous Commits
![image](https://github.com/MAD2023-Team4/MAD-Team4-CacheFlash-Final/assets/133575569/62cdd0f3-6e76-442b-833f-5cd0ad4020f4)


### Stage 2 - Proposed New Features:
 -Difficulty level for test yourself(easy-unlimited time,medium-30s per question,hard-15s per question) (Bryan,Hui Ting)
 - Dashboard feature that displays the best and worse performance flashcards by showing the percentage of the flashcard based on each difficulty level(Bryan,Samuel)
 - Quote of the day feature that displays a quote every day, user can also click on the quote to change the quote if they do not like the current quote (Bryan)
 - Profile(Bryan)
 - Creating and initialising of firebase(Bryan)
  1.Change Password feature
  2.Study Preference
   -Favourite Category allows user to choose their favourie category and main activity will display flashcards for that category
   -Study Time allows user to choose the time they want to study and a notification will show up at that time to remind them to study
  3.Notification
   -User can choose to on or off notification,no pop up messages
  4.Study Streak
   - Tracks the amount of times user uses this app(streak, total number of days).There will be a medal for each 10 day streak up till 30 days
 - Information Depository for users to key in notes outside of the flashcards (Hui Ting)
 - Multiple choice style - there will be multiple choice questions, and the user selects the correct answer. (John)
 - Creating flashcards - the user creates custom flashcards for own revision (Bryan)
 - Improvement of app UI (Huiting,Samuel)
 - Design(Samuel,John)
 - Leaderboard(Samuel)
 - Search feature(Samuel)
 - Forget Password feature where user reset their password with verfication through email(John)
 #### Explanation of Calculation of Optimal Timing (Samuel)
 [The forgetting curve](https://en.wikipedia.org/wiki/Forgetting_curve) can help remind the users when would they almost forget that information (the exact date and timing). This is done by sending them a notification when the retainability has reached a certain level as time passes. For example, a user has finished a deck of flashcards, and the app estimates that he/she will forget about 70% of information in 6 days. This means that R is reduced, until about R = 1 - 0.7 = 0.3. Hence, it will send a notification 6 days later to the user.

 #### How does the App Estimate How Well the User Remembers Information? (Samuel)
 The Forgetting Curve formula has to estimate R, as t, passes. Also, in the equation, another factor that determines R is S. However, S is assumed to be the same for everyone. To customise everyone's S, we increase S for every question the user gets right in the "Test Yourself" feature, and vice versa. Substitute it into the Forgetting Curve formula, and you will get a curve. The app will then use that curve to estimate the duration, as mentioned above. (Refer to Explanation of New Feature if not sure)

[Referenced by Peaceknight05](https://github.com/peaceknight05/Pentagone)
