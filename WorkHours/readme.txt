At the first screen you are presented, first press sign up, then sign up with email.
Enter some details (has input validation). 
You will then be sent to the main screen.

When you are at the main screen of the app, you have three tabs you can swipe navigate between.
Your income and schedule at the bottom is connected to the amount of shifts you have in your application, tax is automatically deducted.
You can also press the menu button to enter the profile page, here you can change user specific application settings like username and wage.

On the first screen, you can choose a date, which you want to attach a shift to. 
Pressing the "new shift" button takes you to a new view where you can decide a from time, and a to time. 
You have the alternative to make it a recurring event, and a notification is created for each shift.
Saving the shift pushes the event to an SQLite database.
Pressing the email button lets you choose a time-period to email.
The email receiver is bound to the employer email in the profile page.

Second tab is reserved for upcoming shifts.
Clicking a shift lets the user change the time of the event.
Notifications bound to the event are automatically changed.

Third tab is an archive page of all shifts added to the application.
Each event can be clicked on.
This lets the user decide if he has worked this shift.
Clicking OK saves the changes, clicking DELETE PERMANENTLY deletes the shift.

Some application specific info:
Shift can have status confirmed or unconfirmed.
Confirmed equals worked, and can only be changed through the notification which pops up at shift end,
or through the archive page after shift has ended.