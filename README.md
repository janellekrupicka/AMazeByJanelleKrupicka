# AMazeByJanelleKrupicka
## Table of Contents
1. [General Info] (#general-info)
2. [Project Status] (#project-status)
4. [Notable Code Samples] (#notable-code)
5. [Collaboration] (#collaboration)
***
### General Info
I developed this project in my Fall 2021 CSCI 301 Software Development class taught by Professor Kemper at the College of William & Mary. 
The project is an Android app maze game, which allows the user to traverse a maze in first person view. The code for the manual maze traversal 
and maze graphics were given to me by my professor. I developed the robot maze traversal and the entire app UI. The app is a Secret Garden theme meant 
to make the user feel like they're lost in a mossy-walled garden. The user can choose what level they'd like to play at, which changes the size of the maze,
on the first screen of the app. The maze is then generated and its generating progress is shown with a loading bar. At this point, the user can choose if
they'd like to play the game manually or with the Wizard robot. Then, the robot either solves the maze or the user navigates through the maze.
After finishing the maze, the user can choose if they'd like to randomly generate a new maze or to revisit a past maze.
***
### Project Status
This project is unfinished and I would like to make a number of changes to it. The fixes and changes that I need to make are listed below. 
I will check the changes off as I complete them.
#### AMazeActivity Changes
- [ ] Remove Maze Generation Algorithm Spinner and make DFS the only maze gen algorithm
  - [ ] Delete now obsolete maze generation algorithm classes
- [ ] Make text bigger and easier to read
- [ ] Make skill level SeekBar easier to see
- [ ] Store all strings in strings.xml and reference rather than directly using
#### GeneratingActivity Changes
- [ ] Make loading bar larger and easier to see
- [ ] Remove Wallfollower robot
  - [ ]  Delete now obsolete classes
- [ ] Remove robot type Spinner
  - [ ]  Delete now obsolete classes
#### PlayManuallyActivity Changes
- [ ] Make text bigger and easier to read
- [ ] Make map easier to see
- [ ] Change map on and off buttons to toggle switches
- [ ] Make maze walls look like mossy stone
- [ ] Smooth out the animation when turning
#### PlayAnimationActivity Changes
- [ ] Same as PlayManuallyActivity
- [ ] Fix a bug that causes the robot to crash when there isn't a wall
  - Most likely caused by a memory leak. The robot thinks that it's on the previous map played so it detects walls where they existed on the previous map, 
  but not on the correct map.
#### WinningActivity Changes
- [ ] Make text bigger and easier to read
- [ ] Fix bug that says fewer steps were taken than the minimum expected path length
#### LosingActivity Changes
- [ ] Same as WinningActivity
***
### Notable Code Samples
- GeneratingActivity demonstrates my knowledge of MultiThreading. As the maze for each game generates, a loading bar shows the maze generation progress.
- MazePanel demonstrates my knowledge of Android graphics. MazePanel creates a Custom View that displays the maze game.
- ReliableRobo demonstrates my ability to work with an existing code base and make sense of what I'm looking at in order to develop something new.
***
### Collaboration
My professor was my primary collaborator for this project and provided much needed advice and support when working on this project. 
My professor also wrote a number of the classes, especially the class interfaces, that I worked with on this project.
