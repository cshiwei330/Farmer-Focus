## Team Members:

- CHEW SHI WEI S10221849H
- CHUA KAI YI S10219179E
- KOH KA-WEI DARRYL S10221893J
- RADCHANON DON SUKKRAM S10223354J
- TAN YU XIN AISLINN S10222271H

## Description of App: 
Farmer Focus is a productivity app that helps you keep track of your tasks the fun way! No more sticky notes or countless applications, an all in one app that will be your best tool to help organize your life.

> - Create your own account to store your progress
> - Keep track of your tasks
> - Add event and recurring tasks according to your schedule
> - Edit your tasks if your schedule changes
> - Mark tasks as completed to update your progress
> - Be reminded of urgent tasks
> - Focus better with a dedicated countdown timer
> - Track your time with a inbuilt stopwatch
> - Visualize insights of your recent and overall productivity
> - Upgrade your own personal farm from completing milestones
> - View your tasks quickly directly from your home screen with a widget

Attributes:
Cloud Image: <a href="https://www.freepik.com/vectors/assortment">Assortment vector created by freepik - www.freepik.com</a>

Chicken gif: Farm Animal Party Sticker by Hay Day for iOS & Android | GIPHY

Cow gif: https://gifer.com/en/tHW

Grass gif: https://gifer.com/en/ZhkE

Splash Screen: https://www.istockphoto.com/vector/farm-animals-gm466647826-60097292

Lazychicken gif: https://giphy.com/gifs/namecheap-yeti-henny-hedgy-LpGfreBAOqxjjQDpqQ

Alert sound: https://mixkit.co/free-sound-effects/alarm/


## Contribution of each member:

| Name:  | Contribution: | Concept: |
| ------------- | ------------- | ------------- |
| CHEW SHI WEI  | Home page (mood tracker database), Task layout, App Icon, App Widget and Google Play Release | Database, Recycler View, Event Handling, App Widgets, Remote View, ListView, Animation|
| CHUAH KAI YI  | Calender, Add New Task (date/time), Database (date and time), Navigation system, Farm  | Database, Recycler View, Event Handling, Array Adaptor, Animation & Gifs, ViewPager, Persistent Storage  |
| KOH KA-WEI DARRYL  | Settings, Account settings, change password, Database (update user), Change profile picture  | Database, Shared preferences, Multimedia, Event Handling, Animation, CountDownTimer, Chronometer, Persistant Storage |
| RADCHANON DON SUKKRAM  | Task, Database (task), Add New Task, View Task Page, Edit Task, Recurring Task and Notification   | Database, Recycler View, Event Handling, User Interface(Notification), Alarm Manager, Broadcast Receiver |
| TAN YU XIN AISLINN  | Login & Sign up pages (Account database), App Icon, Statistics and Splash Screen  | Database, Shared Preferences, Event Handling, Android Charts, Interactions with other apps, User Interface(Splash Screen) |

## Screenshots of app:
## Log In Page
- User can key in their account details and log into the app
- If the username or password is incorrect, they will not be able to log in
- User can select "Remember Me" to stay logged in the app even when they close the app
- User can select "SIGN UP HERE" if they want to create a new account
- "LOG IN" button brings the user to the home page if the account is valid

## Sign Up Page
- Username that user enters cannot be existing in the database already, must be entirely new
- Username is case sensitive
- Input of Password and Confirm Password must be the same
- Password is case sensitive
- User has to agree to terms and conditions
- "BACK" and "SIGN UP" button both brings the user back to the login page, where the user had to log in with an existing account

## Home
- Mood selector which records the mood of the user
- It also shows upcoming tasks (displays tasks with dates that are one week from now)

![Home Page](https://user-images.githubusercontent.com/103928761/182027458-30c06572-45d8-44e0-b6b8-c7c57e6b2019.png)

### Mood Message
- A message will be displayed according to the mood selected

![msg displays](https://user-images.githubusercontent.com/103928761/182027494-abf7566f-9d68-43ae-8778-0c968664a760.png)

### Dialog when User enters a different mood for the day
- Pop up will be shown If a different mood is selected on the same day
- User can confirm if they want to proceed with their change of mood

![dialogue](https://user-images.githubusercontent.com/103928761/182027532-3d246b9e-96a2-46d1-9919-d2cbe60df318.png)


## Task
- Allows user to view all task

![Task Page](https://user-images.githubusercontent.com/103928761/182027643-94d83a9a-3e4b-46ef-be99-f5cf761bda65.png)

- Allows user to filter all task for a simpler view of tasks

![Filter Task](https://user-images.githubusercontent.com/103928761/182027691-777972f8-81d1-4832-8999-792a7f8fcbff.png)

- Allows user to clear all tasks

![Clear All Task](https://user-images.githubusercontent.com/103928761/182027727-17db524d-0981-4011-9f56-cc8dddd5a72f.png)

### Adding of Tasks
- Enter details of the new task
- Click CREATE NEW TASK Button
- Task will be added to the task list

![Add New Task 2](https://user-images.githubusercontent.com/103928761/182027834-26931994-992a-42dc-8b94-1acca33731bc.png)
![Add New Task 1](https://user-images.githubusercontent.com/103928761/182027836-2c106c59-8fb2-4099-adec-a25f65010621.png)

### View Tasks
- Allows user to view selected task details
- Allows user to delete specific task

![View Task](https://user-images.githubusercontent.com/103928761/182027894-a6ed3983-c518-4314-9c24-0c62f217e3cd.png)

### Delete Tasks
- If it is an event task, confirmation dialog will show up

![Delete Specific Task](https://user-images.githubusercontent.com/103928761/182027974-1173a12b-3bde-42e8-897b-c4ed99f6c051.png)

- If a recurring task, the user has the option to delete just the selected task or future recurring tasks. Only after selection, then the confirmation dialog show up

![Delete Recurring Task](https://user-images.githubusercontent.com/103928761/182027979-c9278cbd-89a0-4c12-ab66-8250c8e76828.png)

### Edit Tasks
- Allows user to edit a task that has been created
- Click on SAVE button to save changes
- If event task, changes will be saved immediately
- If recurring task, user can decide to either delete for current task only or current task and all future recurring tasks

![Edit Task](https://user-images.githubusercontent.com/103928761/182028006-68923084-b29c-40d4-9cbc-2b4420999b38.png)
![Edit Recurring Task](https://user-images.githubusercontent.com/103928761/182028007-9fd61bf2-5bfc-48d5-ae1b-86c67bf3b1d9.png)


## Timer
- Create tasks in tasks page
- Select tasks to start time (second pic)
- Click start to begin
- Click finish if you have completed before the duration is up
- Tasks will be mark as completed once users click finish or the duration is up

![timer](https://user-images.githubusercontent.com/103928761/182028156-eac45fcb-069c-4803-b4be-334236ebff71.png)
![selecttask](https://user-images.githubusercontent.com/103928761/182028219-4b8a0162-70d2-487e-ae31-52b29756fc25.png)


## Stopwatch
- Click start  to begin
- Once stopwatch is running, start changes to pause
- Reset button sets the stopwatch back to 00:00

![stopwatch](https://user-images.githubusercontent.com/103928761/182028254-c181cf44-bdf9-4edd-8b3e-359ec111abc2.png)


## Calendar
- Click on left and right buttons to change months
- Dates with tasks show have indicator circles to indicate
- Click on a date to see tasks for that date
- Clicking on a task in the scrollable list view shows the task details

![image](https://user-images.githubusercontent.com/103928761/182028445-6a948dbb-888f-40e2-9864-12978d8e30d7.png)


## Statistics
- Cards to show the number of completed and pending tasks
- A bar chart of completed tasks by day is shown, and only the data of the past week is available
- Display the user’s mood of the week
- Search recurring tasks
   -  Click on the "CLICK HERE" button to go to the recurring task statistics page

![Screenshot_20220731_154458](https://user-images.githubusercontent.com/103928761/182028323-f436733d-5616-4495-80e9-1ec48406bff1.png)

### Recurring Tasks Statistics
- Users can search the title of the recurring task name
- Bar chart of time taken per attempt at recurring tasks will be shown 

![Screenshot_20220731_154524](https://user-images.githubusercontent.com/103928761/182028386-7bbe50a5-30d1-4f93-a421-10375f6e0f14.png)


## Farm 
- Swipe to left/right to switch fragments

### Barn
- Click on barn  to open dialog to see upgrade upgrade requirement
- Requirements based on number of completed Event tasks
- Max upgrade is level 3 for barn

![farm_1](https://user-images.githubusercontent.com/103928761/182031097-78ab0cae-52bc-45a5-a9de-c7935f5650d5.png)

### Silo and Farm Plots
- Click on silo to open dialog to see upgrade upgrade requirement
- Requirements based on number of completed or created recurring tasks
- Solo must have be level 1 or be built to upgrade further
- Max upgrade is level 3 for silo
- Farm plots based on number tasks in following 7 days

![farm_2](https://user-images.githubusercontent.com/103928761/182031158-edfd1fd8-710e-4fba-9769-becd02c65ce0.png)


## Notifications 
- Notifications will show up to remind the user of urgent tasks if alert is set when adding the task
![Notification](https://user-images.githubusercontent.com/103928761/182028025-0ec13b9f-748b-4039-b69d-c942215fb300.png)


## Widget
- A shortcut for users to view their daily tasks in their homepage
- Also acts a shortcut to access our app (when the top part of the widget is clicked)
- Widget updates accordingly when new tasks are added, edited or deleted. 
- [Refer to this link](https://www.maketecheasier.com/create-android-widget/) if you’re unsure of how to add widgets to your homescreen

![widget](https://user-images.githubusercontent.com/103928761/182027578-f4dee4d7-5c00-4375-a3e1-0ad3c061e1c5.png)


## Account Settings
- Users can change profile picture by clicking the profile picture icon
- Users can set a nickname 
- Users can change password

![accountsettings](https://user-images.githubusercontent.com/103928761/182029795-d76d2975-30d2-4673-bf55-033c08499611.png)

### Change Profile Picture
- Users can either pick the male or female farmer as their desired profile picture

![changepfp](https://user-images.githubusercontent.com/103928761/182029811-1ee4877c-b585-48d1-9215-fea9e631beba.png)

### Change Password
- Requires the user to enter a valid username and password
- New password cannot be the same as old password
- New password and confirm password must be the same

![changepass](https://user-images.githubusercontent.com/103928761/182029843-1f7c1a95-4937-4441-b0ab-2ba91987f6e3.png)


