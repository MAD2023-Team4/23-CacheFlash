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

### Stage 2 - Proposed New Features:
 - Speedrun feature for users who want a challenge, and let them edit the time they have to answer (Hui Ting)
 - Dashboard feature to track user data by calculating the timing to review the flashcards (Forgetting Curve) [Samuel]
 - Information Depository for users to key in notes outside of the flashcards (Hui Ting)
 - Image Relaxation (Bryan)
 -  Multiple choice style - there will be multiple choice questions, and the user selects the correct answer. (John)
 -  Creating flashcards - the user creates custom flashcards for own revision (Bryan)
 -  Improvement of app UI (Victoria)
 
 #### Explanation of Calculation of Optimal Timing (Samuel)
 [The forgetting curve](https://en.wikipedia.org/wiki/Forgetting_curve) can help remind the users when would they almost forget that information (the exact date and timing). This is done by sending them a notification when the retainability has reached a certain level as time passes. For example, a user has finished a deck of flashcards, and the app estimates that he/she will forget about 70% of information in 6 days. This means that R is reduced, until about R = 1 - 0.7 = 0.3. Hence, it will send a notification 6 days later to the user.

 #### How does the App Estimate How Well the User Remembers Information? (Samuel)
 The Forgetting Curve formula has to estimate R, as t, passes. Also, in the equation, another factor that determines R is S. However, S is assumed to be the same for everyone. This means that the equation can assume that everyone is equally good at remembering information. In reality, it is unfortunately not the case! This also shows that learning may not be personalised for everyone, based on the equation. Everyone is treated the same, so a user who may have trouble remembering is assumed to have the same standard of remembering information, just like everyone else. Hence, to personalise learning abilities for everyone, aka personalising S, we introduce another equation to estimate that.
 
$$ S = - \frac{t}{\ln{R}} $$

To estimate t for each flashcard question, measure the percentage of information retained immediately after learning (usually at time t = 0).
To estimate r for each flashcard question, we would prompt the user to rate the confidence of each answer, be it correct or not.

#### Why Rate Confidence of Answers to Estimate Memory Stability? (Samuel)
Research on metacognition has suggested that to think about our own thinking, we need to monitor our understanding, see our confidence in answers, then finding out a solution from there. To [rate your confidence](https://brainscape.zendesk.com/hc/en-us/articles/115002736872-How-should-I-rate-my-confidences-When-should-I-rate-a-5-), simply indicate a sclate of 1-5. The more 1s rated, the more likely to see new cards since those cards rated 1 are new to the user. Let's say we have 10 flashcards. Hence 10 S values are calculated with the above formula. based on the 10 S values, calculate a good estimate of S overall, which is the mean of all 10 S. Substitute it into the Forgetting Curve Formula, and you will get a curve. The app will then use that curve to estimate the duration, as mentioned above. (Refer to Explanation of New Feature if not sure)

[Referenced](https://github.com/peaceknight05/Pentagone) by Peaceknight05
