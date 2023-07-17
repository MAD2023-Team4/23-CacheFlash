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
 - Speedrun feature for users who want a challenge, and let them edit the time they have to answer (Hui Ting)
 - Dashboard feature to track user data by calculating the timing to review the flashcards (Forgetting Curve) [Samuel]
 - Information Depository for users to key in notes outside of the flashcards (Hui Ting)
 -  Multiple choice style - there will be multiple choice questions, and the user selects the correct answer. (John)
 -  Creating flashcards - the user creates custom flashcards for own revision (Bryan)
 -  ###### Within Profile:
   1. Password Management - User can change password (Bryan)
   2. Study Preference - User gets to filter a certain category of flashcards to be displayed in the home page. (Bryan)
   3. Study Time - User sets a study time, and when the time is up, the app will remind the user to study (Bryan) 
   4. Notification - User can enable or disable notifications for the app (Bryan)
   5. Study Streak - If the user logs into the app continuously for n days, there will be a streak of n days.
       - If n >= 10, user achieves a Beginner medal
       - Else if n >= 20, user achieves an Achiever medal
       - Else if n >= 30, user achieves an Expert medal
       - User can press and hold the Streak Progress bar to see progress
 ### Improvements:
 -  App UI (Whole team)
 -  Enhance profile page - Hui Ting
 
 #### Explanation of Calculation of Optimal Timing (Samuel)
 [The forgetting curve](https://en.wikipedia.org/wiki/Forgetting_curve) can help remind the users when would they almost forget that information (the exact date and timing). This is done by sending them a notification when the retainability has reached a certain level as time passes. For example, a user has finished a deck of flashcards, and the app estimates that he/she will forget about 70% of information in 6 days. This means that R is reduced, until about R = 1 - 0.7 = 0.3. Hence, it will send a notification 6 days later to the user.
 The Forgetting Curve formula has to estimate R, as t, passes. Also, in the equation, another factor that determines R is S. However, S is assumed to be the same for everyone. To customise everyone's S, we increase S for every question the user gets right in the "Test Yourself" feature, and vice versa. Substitute it into the Forgetting Curve formula, and you will get a curve. The app will then use that curve to estimate the duration, as mentioned above. (Refer to Explanation of New Feature if not sure)

[Referenced by Peaceknight05](https://github.com/peaceknight05/Pentagone)
