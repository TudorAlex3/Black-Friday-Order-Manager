# Black-Friday-Order-Manager

###### ➢ Game
In the "run" method, I create a list of accounts, a dictionary, and a list of image names used for the graphical interface using the "JsonInput" and "JsonInputPicture" classes, in which I have read the data from a .json file. Depending on the user's choices, I call either the "options" method or the "optionsGraphic" method.

The "options" method represents the menu with the moves and choices that the player can make and executes the indicated commands. The "options" method models the user's instructions using the graphical interface.

In addition to the "stories" method that displays a story based on the cell, I have also added the "startRules" method that displays the rules and instructions of the game for the console mode. By importing "java.util.concurrent.TimeUnit," I displayed the rules with a small delay to give the player the opportunity to read the instructions.

