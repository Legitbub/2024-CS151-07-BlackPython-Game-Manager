# 2024-CS151-07-BlackPython-Snakes.Game-Manager 

# Overview

This project involves implementing a Blackjack and Snake game system in JavaFX. 
We will be using splitting each game into its own package, which will then be handled 
by a GameManager class to run the application. Users can create a login and have 
their high scores saved in the game's records.

Each game includes its own GUI file that handles the display
and events from user interaction (clicking buttons, pressing on the keyboard,
etc.). Game logic is handled OOP style in various class files for each aspect
of the game.
# Table of Contents
1. [Features](#Features)
2. [Installation](#installation-instructions)
3. [Project Structure](#project-structure)
4. [Contributions](#contributions)
5. [Acknowledgments](#acknowledgments)

# Features:
## Login System and Scores
Upon launch, the `GameManager` will prompt the User to either login or create 
an account if they haven't done so already. Once the User logs in, they 
will be taken to the `Main Menu` where they can view previous high scores 
for each account and choose which game they want to play.

Users can enter into one of 2 games:
## Blackjack
Play a game of Blackjack with 2 other CPU players and a dealer. The user 
goes first and can 
``` 
Bet: place a bet from their bankroll before playing. 
``` 
Players are dealt 2 random cards from a deck at the start of the game. 
Users have the choice to
```
Hit: get another card from the deck
```
or 
```
Stay: end their turn
``` 
based on the value of their 
hand.

Hands that: 
```
Go over 21 (bust): automatically lose the amount of the bet 
from the User's bankroll, unless the dealer also busts.
```
```
Beat the dealer's hand: win the amount of the bet.
```
```
Win via blackjack (ACE and a 10 value card such as KING): win double 
the amount of the bet.
```
```
Tie with the dealer (push): do not gain or lose money from the bankroll.
```
Players can exit to the `Main Menu` at any time using the toolbar button at 
the top or `Logout`. Clicking `Save` will store the state of the current 
game to the clipboard, which can be pasted into save state field from the 
Blackjack launcher to load the saved game.

## Snake
Play a game of Snake where the player controls a snake (green line) to pick 
up food (red blocks) in a matrix. Each time the snake picks up a food, it 
will increase in length by 1 block. If the snake hits a wall or runs into 
itself, the game is over.

Players can: 

```
Press arrow keys: control the snake's orthogonal movemment
```
```
Press ESCAPE: pause/unpause the game
```

On a game over, players can click the green `Restart` button on screen to 
try again. The game can be exited via the `Main Menu` or `Logout` at any 
time via the toolbar buttons at the top.
# Installation Instructions:
To set up the project locally:

Clone the repository:
```rb
git clone https://github.com/Legitbub/2024-CS151-07-BlackPython-Game-Manager.git
```

Navigate to the project directory:
```rb
cd 2024-CS151-07-BlackPython-Game-Manager
```
Setup JavaFX Before Running:
```rb
Add appropriate .jar files required by javaFX into JRE System Library
```
Run the project:
```rb
Within IDE:
Enter into Runner.java inside the Manager folder
Click run located above main function

With Command Line:
java Runner.java
```

# Usage
Make an account, log in, and have fun! Hope you have a good time trying 
out our games!
# Contributions: 

### Justin: `Blackjack`, `Manager`

### Josh: `Blackjack`

### Bao: `Snakes`, `Manager`, `UML`

### Vats: `Snakes`, `Manager`

# Acknowledgments:
This project was developed as part of the CS151 course at San Jose State University. Special thanks to Professor Telvin Zhong for his teaching throughout the Fall 2024 semester.

