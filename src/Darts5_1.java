
public class Darts5_1{

	/**
	*  The Darts program is an application that allows two users to simulate a game of darts.
	* by taking turns each typing in a score they wish to aims for,
	* the accuracy of each players aim is determined by a random number generator
	*
	* @author  b00247535, b00263840
	* @since   08/05/2014 
	* This Method begins the program by initiating default values for variables that will be
	* changed in later methods, the variables are then passed to Menu.
	*/
public static void main(String[] args) {
	
		//The program begins here.
	int [][] ReplayArray = new int [0][0];
	int MaxTurn = 0;
	String Player1Name = "Player 1";
	String Player2Name = "Player 2";
	Menu(Player1Name, Player2Name, ReplayArray, MaxTurn);
}

/**
 * Gives the user the rules of darts then asks each player to type in their names
 * Each players score is defaulted to 501 and turn number is set to 0
 * An array is created which will keep track of each turn result throughout the game.
 */
public static void NewGame(){
	
	TextIO.putln("Modifiers = Single, Double, Treble (s, d, t)");
	TextIO.putln("Numbers = 1-20, 25, 50");
	TextIO.putln("Entering 25 or 50 changes the modifier to single");
	TextIO.putln("**Remember your last dart must land on a double or 50 to win the game**");
	TextIO.putln("LET'S PLAY DARTS!");
	boolean GameOver = false;
	TextIO.putln("Please enter name of Player 1: ");
	String Player1Name = TextIO.getWord();
	TextIO.putln("Please enter name of Player 2: ");
	String Player2Name = TextIO.getWord();
	int Player1Score = 501;
	int Player2Score = 501;
	int TurnNumber = 0;
	int [][] ReplayArray = new int [99][4];
StartTurn(Player1Name, Player2Name, Player1Score, Player2Score, TurnNumber, GameOver, ReplayArray);
}

/**
 * Begins a turn by checking whether the turn number is odd or even,
 * the current player for this turn is determined from this.
 * Turn is called after this.
 */
private static void StartTurn(String Player1Name, String Player2Name, int Player1Score, int Player2Score, int TurnNumber, boolean GameOver, int ReplayArray[][]){
	
	do{
		TurnNumber ++;
		TextIO.putln();
		TextIO.putln ("This is Turn: " + TurnNumber);
		int LoopVal = 0;
		//if TurnNumber is odd, player1score = currentplayerscore
		//if TurnNumber is even, player2score = currentplayerscore
			if ( TurnNumber % 2 == 0) {
			TextIO.put (Player2Name + ", ");
				Player2Score = Turn(Player1Name, Player2Name, Player2Score, TurnNumber, LoopVal, ReplayArray);
			}
			else {
			TextIO.put (Player1Name + ", ");
				Player1Score = Turn(Player1Name, Player2Name, Player1Score, TurnNumber, LoopVal, ReplayArray);
			}
				
		}
	while (GameOver == false);
	
}

/**
 * A simulation of the throwing of a dart is looped 3 times during a turn
 * and the value of these single darts is subtracted from the players score
 * The loop stops and the value isn't subtracted if the player has gone bust this turn.
 */
private static int Turn(String Player1Name, String Player2Name, int CurrentPlayerScore, int TurnNumber, int LoopVal, int ReplayArray[][]) {

	int SingleScore;
	
		do {
			TextIO.putln("Your remaining score is: " + CurrentPlayerScore);
			TextIO.putln ();
				SingleScore = Input(Player1Name, Player2Name, CurrentPlayerScore, TurnNumber, ReplayArray, LoopVal);
				boolean Bust = BustCheck(CurrentPlayerScore, SingleScore);
				if (Bust == false){
					CurrentPlayerScore = CurrentPlayerScore - SingleScore;				
					ReplayArray[TurnNumber][LoopVal] = (SingleScore);
				}
				else LoopVal = 3;
				LoopVal++;
		   }
		while (LoopVal < 3);
		
		ReplayArray[TurnNumber][3] = CurrentPlayerScore;
		
	return CurrentPlayerScore;
	
}

/**
 * The Program asks the user to input a target modifier and number.
 * If a player enters 25 or 50, the modifier is changed to single,
 * because of this, the number input happens after the modifier input.
 * The function then calls on two other functions (one for modifier and one for number)
 * which determines whether the dart hits or misses its target. It then tells the player
 * whether the result of their dart throw. WinCheck is then called to make sure that
 * if a win occurs, the rest of the function is not executed. If a win is not achieved,
 * the result of the player's turn is displayed by calling ThrowDisplay.
 */
private static int Input(String Player1Name, String Player2Name, int CurrentPlayerScore, int TurnNumber, int ReplayArray[][], int LoopVal) {

	char ModifierInput;
	int Modifier = 0;
	boolean ValidMod = false;
	do	{
		TextIO.putln("Please enter a modifier and then a number (e.g. t16): ");
		ModifierInput = TextIO.getChar();
		
		switch (ModifierInput){
		case 's':
			Modifier = 1;
			ValidMod = true;
			break;
		case 'd':
			Modifier = 2;
			ValidMod = true;
			break;
		case 't':
			Modifier = 3;
			ValidMod = true;
			break;
		default: break;
		}
	}
	while (ValidMod == false);
	
	int NumberInput;
	boolean ValidNum = false;
	
	do{
		NumberInput = TextIO.getInt();
		TextIO.putln ();
		if (NumberInput<21 && NumberInput>0){
			ValidNum = true;
		}
		else if (NumberInput == 25 || NumberInput == 50){
			TextIO.putln("Throw has been changed to single.");
			ValidNum = true;
			Modifier = 1;
		}
	}
	while (ValidNum == false);
	
	int NumberHit = NumHitMissCheck(NumberInput, Modifier);
	int ModHit = ModHitMissCheck(Modifier);
	if (NumberHit != NumberInput || ModHit != Modifier){
		TextIO.putln("You missed your target.");
	}
	else {
		TextIO.putln("You hit your target.");
	}
	
	WinCheck(Player1Name, Player2Name, NumberHit, ModHit, CurrentPlayerScore, TurnNumber, ReplayArray, LoopVal);
	
	int SingleScore = 0;
	
	SingleScore = SingleDart(NumberHit, ModHit);
	ThrowDisplay(NumberHit, ModHit);

	return SingleScore;
	
}

/**
 * The result of a single dart is calculated by multiplying the number hit by the modifier.
 * The result is ten returned.
 */
private static int SingleDart(int NumberHit, int Modifier) {

	//Below is the original function for point 2 in the specification
	//int TotalScore = AimedScore * Modifier;
	
	int SingleScore = NumberHit * Modifier;
	return SingleScore;
}

/**
 * The throw result is displayed by multiplying the number hit by the modifier
 * Unless the number hit was a 0 then a miss is displayed.
 */
private static void ThrowDisplay(int NumberHit, int Modifier) {

	if (NumberHit == 0){
		TextIO.putln("Throw result is: You missed the board!");	
	}
	else
	TextIO.putln("Throw result is: " + NumberHit * Modifier);	
}

/**
 * Checks to see if a player has won every time a dart is thrown. 
 * If a player has scored a double or a 50 and put their total score remaining to 0
 * they have won the game and a victory message is displayed followed by a replay of the game,
 * then the menu displays again.
 */
private static void WinCheck(String Player1Name, String Player2Name, int NumberInput, int Modifier, int CurrentPlayerScore, int TurnNumber, int ReplayArray[][], int LoopVal){

	if (Modifier == 2 && CurrentPlayerScore - (NumberInput*Modifier) == 0 || NumberInput == 50 && CurrentPlayerScore - NumberInput == 0){
		if ( TurnNumber % 2 == 0) {
			TextIO.putln ("Congratulations! " + Player2Name + " has won the game!");
			TextIO.putln ();
			ReplayArray[TurnNumber][LoopVal] = (NumberInput*Modifier);
		}
		else {
			TextIO.putln ("Congratulations! " + Player1Name + " has won the game!");
			TextIO.putln ();
			ReplayArray[TurnNumber][LoopVal] = (NumberInput*Modifier);
		}
		int MaxTurn = TurnNumber + 1;
		Replay(Player1Name, Player2Name, ReplayArray, MaxTurn);
	}
}

/**
 * Checks to see if a player has went bust every time a dart is thrown.
 * If the players score remaining is less than 2 they go bust and their turn ends.
 * The method WinCheck already checks if the score remaining is 0 and the modifier is a double.
 */
private static boolean BustCheck(int CurrentPlayerScore, int SingleScore){

	boolean Bust = false;
	if (CurrentPlayerScore - (SingleScore) < 2){
		TextIO.putln("Your score is too low to win, moving on to next turn...");
		Bust = true;
	}
	return Bust;
}

/**
 * A replay of the game last played is displayed using an array of the each turns result.
 * Each turns result is displayed using a loop that finishes when the last turn is displayed.
 *  The current player for each turn is determined from whether the Turn Number was odd or even.
 * Each players inputed name is also used in the replay.
 */
private static void Replay(String Player1Name, String Player2Name, int ReplayArray[][], int MaxTurn){

	for (int TurnNumber = 1; TurnNumber < MaxTurn; TurnNumber++){
		if ( TurnNumber % 2 == 0) {
			TextIO.put ("On Turn " + TurnNumber + ", " + Player2Name + " Scored: " + ReplayArray[TurnNumber][0] + ", ");
			TextIO.put ( + ReplayArray[TurnNumber][1] + ", ");
			TextIO.putln ( + ReplayArray[TurnNumber][2]);
			TextIO.putln ("Turn Total: " + (ReplayArray[TurnNumber][0] + ReplayArray[TurnNumber][1] + ReplayArray[TurnNumber][2]));
			TextIO.putln ("Their remaining score was: " + ReplayArray[TurnNumber][3]);
			TextIO.putln ();
			}
		else {
				TextIO.put ("On Turn " + TurnNumber + ", " + Player1Name + " Scored: " + ReplayArray[TurnNumber][0] + ", ");
				TextIO.put ( + ReplayArray[TurnNumber][1] + ", ");
				TextIO.putln ( + ReplayArray[TurnNumber][2]);
				TextIO.putln ("Turn Total: " + (ReplayArray[TurnNumber][0] + ReplayArray[TurnNumber][1] + ReplayArray[TurnNumber][2]));
				TextIO.putln ("Their remaining score was: " + ReplayArray[TurnNumber][3]);
				TextIO.putln ();
		}	
	}
	Menu(Player1Name, Player2Name, ReplayArray, MaxTurn);
}

/**
 * The menu calls the 4 main functions of the program.
 * the chosen function is determined by which key between 1 and 5 the user types
 * followed by enter
 * 1) calls NewGame()
 * 2) calls Replay()
 * 3) calls SaveGame()
 * 4) calls LoadGame()
 * 5) Quits the program
 */
private static void Menu(String Player1Name, String Player2Name, int ReplayArray[][], int MaxTurn){

	TextIO.writeStandardOutput();
	TextIO.putln ();
	TextIO.putln ("Welcome to the Main Menu. Please select one of the following options: ");
	TextIO.putln ("1) Start a new game");
	TextIO.putln ("2) Replay the last game");
	TextIO.putln ("3) Save the game just played");
	TextIO.putln ("4) Replay a saved game");
	TextIO.putln ("5) Quit");
	TextIO.putln ();
	int MenuChoice = TextIO.getInt();
	
	switch(MenuChoice){
	case 1:
		NewGame();
		break;
	
	case 2:
		TextIO.putln();
		Replay(Player1Name, Player2Name, ReplayArray, MaxTurn);
		break;
		
	case 3:
		SaveGame(Player1Name, Player2Name, ReplayArray, MaxTurn);
		break;
		
	case 4:
		LoadGame(Player1Name, Player2Name, ReplayArray, MaxTurn);
		break;
		
	case 5:
		System.exit(0);
	
	default: 
		TextIO.putln("Invalid Input, Please Enter a Valid choice between 1 and 5.");
		Menu(Player1Name, Player2Name, ReplayArray, MaxTurn);
	}
}

/**
 * NumHitMissCheck, ModHitMissCheck, and find were all worked on individually by B00263840.
 * This function is a twin function to ModHitMissCheck (they are called together).
 * It stores an array for the layout of a dart board (excluding bull's eye). Using
 * 100 numbers means that all possible outcomes can be accounted for, and desirable
 * outcomes can be made significantly more likely than others. A series of if 
 * statements are used to assign different values to MissDistance depending on the
 * range within which the random number lies. MissDistance is then used to altar the
 * position of the dart board accordingly (the greater MissDistance, the further along
 * the board the result becomes). The Number Hit is then returned to Input.
 */
private static int NumHitMissCheck(int NumberInput, int Modifier){

	int NumberHit;
	int [] BoardLayout = {20, 1, 18, 4, 13, 6, 10, 15, 2, 17, 3, 19, 7, 16, 8, 11, 14, 9, 12, 5};
	int TargetIndex = find(BoardLayout, NumberInput);
	//http://stackoverflow.com/questions/6171663/how-to-find-index-of-int-array-in-java-from-a-given-value
	int MissValue = (int)(Math.random()*100);
	int MissDistance = 0;
	
	if (79 < MissValue && MissValue < 88){
		MissDistance = 1;
	}
	else if (87 < MissValue && MissValue < 91){
		MissDistance = 2;
	}
	else if (90 < MissValue){
		MissDistance = 2 + (MissValue - 91);
	}
	else if (11 < MissValue && MissValue < 20){
		MissDistance = -1;
	}
	else if (8 < MissValue && MissValue < 12){
		MissDistance = -2;
	}
	else if (MissValue < 9){
		MissDistance = -2 - (9 - MissValue);
	}
		
	if (MissDistance == -10){			//Miss, 25, and Bull's Eye are accounted 
		NumberHit = 50;					//for in the Random Number Generator.
	}
	else if (MissDistance == -11){
		NumberHit = 0;
	}
	else if (MissDistance == 11){
		NumberHit = 25;
	}
	else{
		int NumberHitIndex = TargetIndex + MissDistance;
		if (NumberHitIndex > 19){
			NumberHitIndex = NumberHitIndex - 20;
		}
		else if (NumberHitIndex < 0){
			NumberHitIndex = NumberHitIndex + 20;
		}
		NumberHit = BoardLayout[NumberHitIndex];
	}
	

	return NumberHit;	
}

/**
 * NumHitMissCheck, ModHitMissCheck, and find were all worked on individually by B00263840.
 * This function is a twin function to NumHitMissCheck. Since there are fewer
 * possible outcomes than in NumHitMissCheck, the random number generator only 
 * uses 30 numbers. Different outcomes are assigned higher/lower probabilities
 * than each other by increases/decreases in the ranges included in the if statements.
 * When aiming for a double, the player has a significantly higher chance of missing 
 * the board, as the double ring is located at the outer edge of the board. After
 * calculating the new modifier, it is returned to Input.
 */
private static int ModHitMissCheck(int Modifier){

	int MissValue = (int)(Math.random()*30);
		if (Modifier == 1){
			switch (MissValue){
			case 0:
				Modifier = 0;
				break;
			
			case 2:
				Modifier = 2;
				break;
			
			case 3:
				Modifier = 3;
				break;
			}
		}
		
		else if (Modifier == 2){
			if (MissValue < 3){
				Modifier = 0; 
			}
			else if (2 < MissValue && MissValue < 8){
				Modifier = 1;
			}
			else if (MissValue == 8){
				Modifier = 3;
			}
		}
		
		else if (Modifier == 3){
			if (MissValue < 6){
				Modifier = 1;
			}
			else if (MissValue == 6 || MissValue == 7){
				Modifier = 2;
			}
			else if (MissValue == 8){
				Modifier = 0;
			}
		}
		
	return Modifier;
}

/**
 * NumHitMissCheck, ModHitMissCheck, and find were all worked on individually by B00263840.
 * This function is used purely to find locate the position
 * of a specific number in the array BoardLayout, and then return
 * this position.
 */
public static int find(int[] BoardLayout, int NumberInput) {

    int i = 0;
	for(i=0; i<BoardLayout.length; i++) 
         if(BoardLayout[i] == NumberInput){
         return i;
         }
	return i;
}

/**
 * this is a part b00247535 worked on individually
 * Asks the user to enter a file location they wish to save a replay to
 * then a replay is played and written to a text file at that location.
 */
private static void SaveGame (String Player1Name, String Player2Name, int ReplayArray[][], int MaxTurn){

	//Spaces can't be used in file/destination
	TextIO.putln("Enter the folder you wish to store your save in");
	TextIO.putln("Folder and filename must not have spaces in them");
	TextIO.putln("For Example: H:/DartsSave.txt");
	String FileLocation = TextIO.getWord();
	TextIO.writeFile(FileLocation);
	Replay(Player1Name, Player2Name, ReplayArray, MaxTurn);
}

/**
 * this is a part b00247535 worked on individually
 * Asks the user to enter a file location they wish to load a replay from
 * a text file at that location is read and displayed in the program
 * a problem could be that any text file can be read but then again it allows
 * for the editing of replays.
 */
private static void LoadGame(String Player1Name, String Player2Name, int ReplayArray[][], int MaxTurn){

	//Spaces can't be used in file/destination
	String currentLine = null;
	TextIO.putln("Enter the folder you wish to load your save from and the filename");
	TextIO.putln("Folder and filename must not have spaces in them");
	TextIO.put("For Example: H:/DartsSave.txt ");
	String FileLocation = TextIO.getWord();
	TextIO.readFile(FileLocation);
	while (!TextIO.eof()) {
	currentLine = TextIO.getln();
	TextIO.putln (currentLine);
	}
	TextIO.readStandardInput();
	Menu(Player1Name, Player2Name, ReplayArray, MaxTurn);
}

}