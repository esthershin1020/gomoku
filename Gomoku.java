import javafx.application.Application; 
import javafx.stage.Stage; 
import javafx.scene.Scene; 
import javafx.scene.control.Button; 
import javafx.event.EventHandler; 
import javafx.event.ActionEvent; 
import javafx.scene.layout.GridPane; 
import javafx.scene.paint.Color; 
import javafx.scene.layout.Background; 
import javafx.scene.layout.BackgroundFill; 
import javafx.geometry.Insets; 
import javafx.scene.layout.CornerRadii;
import java.util.List;

/*
 * class that represents/is the Gomoku game
 * @author Esther Shin 
 */

public class Gomoku extends Application{
  /*
   * field winNumber  represents/stores the number of pieces in a row needed to win a game
   */
  private int winNumber;
  
  /*
   * field PieceLocation represents each square on the Gomoku board that a piece will be on
   */
  private PieceLocation[][] board;
  
  /* 
   * field isBlackTurn  stores whether or not it is the black piece player's turn to play
   */
  private boolean isBlackTurn = true;
  
  /*
   * field noExistingButton  stores the overall background/design for the board itself
   */
  Background noExistingButton = new Background(new BackgroundFill(Color.GREEN, null, new Insets(0.75, 0.75, 0.75, 0.75)));
  
  /*
   * field whiteButton  stores white button creation (its details and what makes a button a white button)
   */
  Background whiteButton = new Background(new BackgroundFill(Color.GREEN, null, new Insets(0.5, 0.5, 0.5, 0.5)), 
                                          new BackgroundFill(Color.WHITE, new CornerRadii(100, true), new Insets(2.5, 2.5, 2.5, 2.5)));
  
  /*
   * field blackButton  stores black button creation information (its details and what makes a button a black button)
   */
  Background blackButton = new Background(new BackgroundFill(Color.GREEN, null, new Insets(0.5, 0.5, 0.5, 0.5)), 
                                          new BackgroundFill(Color.BLACK, new CornerRadii(100, true), new Insets(2.5, 2.5, 2.5, 2.5)));
  
  /*
   * field gameIsOver  stores whether or not the actual game is over (therefore someone has won)
   */
  boolean gameIsOver = false;
  
  /*
   * method that sets the value of isBlackTurn
   * @param isBlackTurn Represents whether or not it is the black player's turn 
   */ 
  public void setIsBlackTurn(boolean isBlackTurn) {
    this.isBlackTurn = isBlackTurn;
  }
  
  
  /*
   * method that builds and displays the Gomoku board depending on different types of input(s)
   * @param primaryStage The window (in this case a Gomoku board) that is desired to be shown on the screen 
   */ 
  public void start(Stage primaryStage){
    getParameters().getRaw();
    /*
     * makes the board a certain size and the winNumber a certain int if no parameters are inputted
     */
    if(getParameters().getRaw().size() == 0){
      board = new PieceLocation[19][19];
      winNumber = 5;
      //System.out.println("19x19 grid, 5 in a row to win");
    }
    /*
     * makes the board a certain size and the winNumber a certain int if one parameter is inputted
     */
    else if(getParameters().getRaw().size() == 1){
      board = new PieceLocation[19][19];
      winNumber = Integer.parseInt(getParameters().getRaw().get(0));
      //System.out.println("19x19 grid, input number in a row to win");
    }
    /*
     * makes the board a certain size and the winNumber a certain int if two parameters are inputted
     */
    else if(getParameters().getRaw().size() == 2){
      board = new PieceLocation[Integer.parseInt(getParameters().getRaw().get(0))][Integer.parseInt(getParameters().getRaw().get(1))];
      winNumber = 5;
      //System.out.println("input grid size, 5 in a row to win");
    }
    /*
     * makes the board a certain size and the winNumber a certain int if three parameters are inputted
     */
    else if(getParameters().getRaw().size() == 3){
      board = new PieceLocation[Integer.parseInt(getParameters().getRaw().get(1))][Integer.parseInt(getParameters().getRaw().get(2))];
      winNumber = Integer.parseInt(getParameters().getRaw().get(0));
      //System.out.println("input grid size, input in a row to win");
    }
    else{
      System.out.println("Error: Invalid number of inputs, please try again");
    }
    
    GridPane gridPane = new GridPane();
    Scene scene = new Scene(gridPane);  
    
    /*
     * loops through each row of the board in order to create a button for each row
     */
    for (int row = 0; row < board.length; row = row + 1){
      /*
       * loops through each column of the board and creates a button in each row of each column
       */
      for (int column = 0; column < board[row].length; column = column + 1){
        board[row][column] = new PieceLocation(row, column);
        gridPane.add(board[row][column], column, row);
        board[row][column].setMinSize(40,40);
        board[row][column].setOnAction(new ButtonClick());
        board[row][column].setBackground(noExistingButton);
      }
    }
    
    primaryStage.setScene(scene);
    primaryStage.setTitle("Gomoku");
    primaryStage.show();               
  }
  
  /*
   * method that determines how many pieces (of the same color) are in a straight line
   * @param board  the current Gomoku board that is being played on 
   * @param currentRow  the current row that the currently played piece is on
   * @param currentColumn  the current column that the currently played piece is on 
   * @param direction  the direction in which to search how many of the same pieces are in a straight line 
   * @return the int value that is the number of pieces (that are of the same color) in a straight line 
   */
  public int numberInLine(PieceLocation[][] board, int currentRow, int currentColumn, String direction){
    /*
     * numberOfPiecesInStraightLine: stores how many pieces of the same color are in a straight line; starts at 1 sinceit includes the current played piece
     */
    int numberOfPiecesInStraightLine = 1;
    /*
     * notInARow: stores whether or not pieces of the same color are in a row
     */
    boolean notInARow = false;
    if(direction.equals("north")){
      /*
       * loops through each row above the currently played piece
       */
      for(int row = currentRow-1; row > -1 && notInARow == false; row = row - 1){
        if(!isBlackTurn && board[row][currentColumn].getBackground() == whiteButton){
          numberOfPiecesInStraightLine = numberOfPiecesInStraightLine + 1;
        }
        else if(isBlackTurn && board[row][currentColumn].getBackground() == blackButton){
          numberOfPiecesInStraightLine = numberOfPiecesInStraightLine + 1;
        }
        else {
          notInARow = true;
        }
      }
    }
    else if(direction.equals("east")){
      /*
       * loops through each column to the right of the currently played piece
       */
      for(int column = currentColumn + 1; column < board.length && notInARow == false; column = column + 1){
        if(!isBlackTurn && board[currentRow][column].getBackground() == whiteButton){
          numberOfPiecesInStraightLine = numberOfPiecesInStraightLine + 1;
        }
        else if(isBlackTurn && board[currentRow][column].getBackground() == blackButton){
          numberOfPiecesInStraightLine = numberOfPiecesInStraightLine + 1;
        }
        else {
          notInARow = true;
        }
      }
    }
    else if(direction.equals("south")){
      /*
       * loops through each row below the currently played piece
       */
      for(int row = currentRow + 1; row < board.length && notInARow == false; row = row + 1){
        if(!isBlackTurn && board[row][currentColumn].getBackground().equals(whiteButton)){
          numberOfPiecesInStraightLine = numberOfPiecesInStraightLine + 1;
        }
        else if(isBlackTurn && board[row][currentColumn].getBackground().equals(blackButton)){
          numberOfPiecesInStraightLine = numberOfPiecesInStraightLine + 1;
        }
        else {
          notInARow = true;
        }
      }
    }
    else if(direction.equals("west")){
      /*
       * loops through each column to the left of the currently played piece
       */
      for(int column = currentColumn - 1; column > -1 && notInARow == false; column = column - 1){
        if(!isBlackTurn && board[currentRow][column].getBackground().equals(whiteButton)){
          numberOfPiecesInStraightLine = numberOfPiecesInStraightLine + 1;
        }
        else if(isBlackTurn && board[currentRow][column].getBackground().equals(blackButton)){
          numberOfPiecesInStraightLine = numberOfPiecesInStraightLine + 1;
        }
        else {
          notInARow = true;
        }
      }
    }
    else if(direction.equals("northeast")){
      /*
       * loops through each row above and each column to the right of the currently played piece
       */
      for(int row = currentRow - 1, column = currentColumn + 1; row > -1 && column < board.length && notInARow == false; row = row - 1, column = column + 1){
        if(!isBlackTurn && board[row][column].getBackground().equals(whiteButton)){
          numberOfPiecesInStraightLine = numberOfPiecesInStraightLine + 1;
        }
        else if(isBlackTurn && board[row][column].getBackground().equals(blackButton)){
          numberOfPiecesInStraightLine = numberOfPiecesInStraightLine + 1;
        }
        else {
          notInARow = true;
        }
      }
    }
    else if(direction.equals("southeast")){
      /*
       * loops through each row below and each column to the right of the currently played piece
       */
      for(int row = currentRow + 1, column = currentColumn + 1; row < board.length && column < board.length && notInARow == false; row = row + 1, column = column + 1){
        if(!isBlackTurn && board[row][column].getBackground().equals(whiteButton)){
          numberOfPiecesInStraightLine = numberOfPiecesInStraightLine + 1;
        }
        else if(isBlackTurn && board[row][column].getBackground().equals(blackButton)){
          numberOfPiecesInStraightLine = numberOfPiecesInStraightLine + 1;
        }
        else {
          notInARow = true;
        }
      }
    }
    else if(direction.equals("southwest")){
      /*
       * loops through each row below and each column to the left of the currently played piece
       */
      for(int row = currentRow + 1, column = currentColumn - 1; row < board.length && column > -1 && notInARow == false; row = row + 1, column = column - 1){
        if(!isBlackTurn && board[row][column].getBackground().equals(whiteButton)){
          numberOfPiecesInStraightLine = numberOfPiecesInStraightLine + 1;
        }
        else if(isBlackTurn && board[row][column].getBackground().equals(blackButton)){
          numberOfPiecesInStraightLine = numberOfPiecesInStraightLine + 1;
        }
        else {
          notInARow = true;
        }
      }
    }
    else if(direction.equals("northwest")){
      /*
       * loops through each row above and each column to the left of the currently played piece
       */
      for(int row = currentRow - 1, column = currentColumn - 1; row > -1 && column > -1 && notInARow == false; row = row - 1, column = column - 1){
        if(!isBlackTurn && board[row][column].getBackground().equals(whiteButton)){
          numberOfPiecesInStraightLine = numberOfPiecesInStraightLine + 1;
        }
        else if(isBlackTurn && board[row][column].getBackground().equals(blackButton)){
          numberOfPiecesInStraightLine = numberOfPiecesInStraightLine + 1;
        }
        else {
          notInARow = true;
        }
      }
    }
    else{
      return -1;
    }
    return numberOfPiecesInStraightLine;
  }
  
  /*
   * method that determines whether or not a square on the board is empty
   * @param board  the current Gomoku board that is being played on 
   * @param currentRow  the current row that the currently played piece is on
   * @param currentColumn  the current column that the currently played piece is on 
   * @param direction  the direction in which to search how many of the same pieces are in a straight line 
   * @return the boolean value stating whether or not a square on the board is empty (true if empty, false if not empty) 
   */
  public boolean isOpen(PieceLocation[][] board, int currentRow, int currentColumn, String direction){
    /*
     * isEmptySquare: stores whether or not a board square is empty or not
     */
    boolean isEmptySquare = false;
    if(direction.equals("north")){
      /*
       * loops through each row above the currently played piece
       */
      for(int row = currentRow-1; row > -1 && isEmptySquare == false; row = row - 1){
        if(!isBlackTurn && !board[row][currentColumn].getBackground().equals(whiteButton)){
          if (board[row][currentColumn].getBackground().equals(blackButton)){
            return false;
          }
          else {
            return true;
          }
        }
        else if(isBlackTurn && !board[row][currentColumn].getBackground().equals(blackButton)){
          if(board[row][currentColumn].getBackground().equals(whiteButton)){
            return false;
          }
          else{
            return true;
          }
        }
      }
    }
    else if(direction.equals("east")){
      /*
       * loops through each column to the right of the currently played piece
       */
      for(int column = currentColumn + 1; column < board.length && isEmptySquare == false; column = column + 1){
        if(!isBlackTurn && !board[currentRow][column].getBackground().equals(whiteButton)){
          if (board[currentRow][column].getBackground().equals(blackButton)){
            return false;
          }
          else {
            return true;
          }
        }
        else if(isBlackTurn && !board[currentRow][column].getBackground().equals(blackButton)){
          if(board[currentRow][column].getBackground().equals(whiteButton)){
            return false;
          }
          else{
            return true;
          }
        }
      }
    }
    else if(direction.equals("south")){
      /*
       * loops through each row below the currently played piece
       */
      for(int row = currentRow + 1; row < board.length && isEmptySquare == false; row = row + 1){
        if(!isBlackTurn && !board[row][currentColumn].getBackground().equals(whiteButton)){
          if (board[row][currentColumn].getBackground().equals(blackButton)){
            return false;
          }
          else {
            return true;
          }
        }
        else if(isBlackTurn && !board[row][currentColumn].getBackground().equals(blackButton)){
          if(board[row][currentColumn].getBackground().equals(whiteButton)){
            return false;
          }
          else{
            return true;
          }
        }
      }
    }
    else if(direction.equals("west")){
      /*
       * loops through each column to the left of the currently played piece
       */
      for(int column = currentColumn - 1; column > -1 && isEmptySquare == false; column = column - 1){
        if(!isBlackTurn && !board[currentRow][column].getBackground().equals(whiteButton)){
          if (board[currentRow][column].getBackground().equals(blackButton)){
            return false;
          }
          else {
            return true;
          }
        }
        else if(isBlackTurn && !board[currentRow][column].getBackground().equals(blackButton)){
          if(board[currentRow][column].getBackground().equals(whiteButton)){
            return false;
          }
          else{
            return true;
          }
        }
      }
    }
    else if(direction.equals("northeast")){
      /*
       * loops through each row above and each column to the right of the currently played piece
       */
      for(int row = currentRow - 1, column = currentColumn + 1; row > -1 && column < board.length && isEmptySquare == false; row = row - 1, column = column + 1){
        if(!isBlackTurn && !board[row][column].getBackground().equals(whiteButton)){
          if (board[row][column].getBackground().equals(blackButton)){
            return false;
          }
          else {
            return true;
          }
        }
        else if(isBlackTurn && !board[row][column].getBackground().equals(blackButton)){
          if(board[row][column].getBackground().equals(whiteButton)){
            return false;
          }
          else{
            return true;
          }
        }
      }
    }
    else if(direction.equals("southeast")){
      /*
       * loops through each row below and each column to the right of the currently played piece
       */
      for(int row = currentRow + 1, column = currentColumn + 1; row < board.length && column < board.length && isEmptySquare == false; row = row + 1, column = column + 1){
        if(!isBlackTurn && !board[row][column].getBackground().equals(whiteButton)){
          if (board[row][column].getBackground().equals(blackButton)){
            return false;
          }
          else {
            return true;
          }
        }
        else if(isBlackTurn && !board[row][column].getBackground().equals(blackButton)){
          if(board[row][column].getBackground().equals(whiteButton)){
            return false;
          }
          else{
            return true;
          }
        }
      }
    }
    else if(direction.equals("northwest")){
       /*
       * loops through each row above and each column to the left of the currently played piece
       */
      for(int row = currentRow - 1, column = currentColumn - 1; row > -1 && column > -1 && isEmptySquare == false; row = row - 1, column = column - 1){
        if(!isBlackTurn && !board[row][column].getBackground().equals(whiteButton)){
          if (board[row][column].getBackground().equals(blackButton)){
            return false;
          }
          else {
            return true;
          }
        }
        else if(isBlackTurn && !board[row][column].getBackground().equals(blackButton)){
          if(board[row][column].getBackground().equals(whiteButton)){
            return false;
          }
          else{
            return true;
          }
        }
      }
    }
    else if(direction.equals("southwest")){
      /*
       * loops through each row below and each column to the left of the currently played piece
       */
      for(int row = currentRow + 1, column = currentColumn - 1; row < board.length && column > -1 && isEmptySquare == false; row = row + 1, column = column - 1){
        if(!isBlackTurn && !board[row][column].getBackground().equals(whiteButton)){
          if (board[row][column].getBackground().equals(blackButton)){
            return false;
          }
          else {
            return true;
          }
        }
        else if(isBlackTurn && !board[row][column].getBackground().equals(blackButton)){
          if(board[row][column].getBackground().equals(whiteButton)){
            return false;
          }
          else{
            return true;
          }
        }
      }
    }
    return false;
  }
  
  /*
   * method that prevents the simultaneous creation of two or more groups of three pieces in a row where both ends of the three pieces in a row have empty board squares
   * @param board  the current Gomoku board that is being played on 
   * @param currentRow  the current row that the currently played piece is on
   * @param currentColumn  the current column that the currently played piece is on  
   * @return the boolean value stating whether or not the three-three rule is being followed (true if rule is being followed, false if rule is not being followed) 
   */
  public boolean threeThreeRuleFollowed(PieceLocation[][] board, int currentRow, int currentColumn){
    /*
     * totalThrees: stores the number of groups of three pieces in a row with empty spaces on either end 
     */
    int totalThrees = 0;
    /*
     * if the number of pieces in a straight line north and south of the currently played piece equals one minus the winNumber 
     * AND both ends of each straight line pieces are open, then increment the number of rows that are have three pieces in a row 
     */
    if (numberInLine(board, currentRow, currentColumn, "north") + 
        numberInLine(board, currentRow, currentColumn, "south") == winNumber - 1 
       && isOpen(board, currentRow, currentColumn, "north") 
          && isOpen(board, currentRow, currentColumn, "south")){
      totalThrees++;
    }
    /*
     * if the number of pieces in a straight line northeast and southwest of the currently played piece equals one minus the winNumber 
     * AND both ends of each straight line pieces are open, then increment the number of rows that are have three pieces in a row 
     */
    if (numberInLine(board, currentRow, currentColumn, "northeast") + 
        numberInLine(board, currentRow, currentColumn, "southwest") == winNumber - 1
       && isOpen(board, currentRow, currentColumn, "northeast") 
          && isOpen(board, currentRow, currentColumn, "southwest")){
      totalThrees++;
    }
    /*
     * if the number of pieces in a straight line east and west of the currently played piece equals one minus the winNumber 
     * AND both ends of each straight line pieces are open, then increment the number of rows that are have three pieces in a row 
     */
    if (numberInLine(board, currentRow, currentColumn, "east") + 
        numberInLine(board, currentRow, currentColumn, "west") == winNumber - 1
       && isOpen(board, currentRow, currentColumn, "east") 
          && isOpen(board, currentRow, currentColumn, "west")){
      totalThrees++;
    }
    /*
     * if the number of pieces in a straight line southeast and northwest of the currently played piece equals one minus the winNumber 
     * AND both ends of each straight line pieces are open, then increment the number of rows that are have three pieces in a row 
     */
    if (numberInLine(board, currentRow, currentColumn, "southeast") + 
        numberInLine(board, currentRow, currentColumn, "northwest") == winNumber - 1
       && isOpen(board, currentRow, currentColumn, "southeast") 
          && isOpen(board, currentRow, currentColumn, "northwest")){
      totalThrees++;
    }
    return !(totalThrees > 1); 
  }
  
  /*
   * method that prevents the simultaneous creation of two or more groups of four pieces in a row 
   * @param board  the current Gomoku board that is being played on 
   * @param currentRow  the current row that the currently played piece is on
   * @param currentColumn  the current column that the currently played piece is on  
   * @return the boolean value stating whether or not the four-four rule is being followed (true if rule is being followed, false if rule is not being followed) 
   */
  public boolean fourFourRuleFollowed(PieceLocation[][] board, int currentRow, int currentColumn){
    /*
     * totalFoursOnceMoveIsMade: stores the number of groups of four pieces in a row 
     */
    int totalFoursOnceMoveIsMade = 0;
    /*
     * if the number of pieces in a straight line north and south of the currently played piece equals the winNumber, 
     * then increment the number of rows that are have four pieces in a row 
     */
    if (numberInLine(board, currentRow, currentColumn, "north") + 
        numberInLine(board, currentRow, currentColumn, "south") == winNumber){
      totalFoursOnceMoveIsMade++;
    }
    /*
     * if the number of pieces in a straight line northeast and southwest of the currently played piece equals the winNumber, 
     * then increment the number of rows that are have four pieces in a row 
     */
    if (numberInLine(board, currentRow, currentColumn, "northeast") + 
        numberInLine(board, currentRow, currentColumn, "southwest") == winNumber){
      totalFoursOnceMoveIsMade++;
    }
    /*
     * if the number of pieces in a straight line east and west of the currently played piece equals the winNumber, 
     * then increment the number of rows that are have four pieces in a row 
     */
    if (numberInLine(board, currentRow, currentColumn, "east") + 
        numberInLine(board, currentRow, currentColumn, "west") == winNumber){
      totalFoursOnceMoveIsMade++;
    }
    /*
     * if the number of pieces in a straight line southeast and northwest of the currently played piece equals the winNumber, 
     * then increment the number of rows that are have four pieces in a row 
     */
    if (numberInLine(board, currentRow, currentColumn, "southeast") + 
        numberInLine(board, currentRow, currentColumn, "northwest") == winNumber){
      totalFoursOnceMoveIsMade++;
    }
    return !(totalFoursOnceMoveIsMade > 1); 
  }
  
  /*
   * method that determines whether a move that is made is a winning move (in other words, if the currently placed move makes the current player the winner) 
   * @param board  the current Gomoku board that is being played on 
   * @param currentRow  the current row that the currently played piece is on
   * @param currentColumn  the current column that the currently played piece is on  
   * @return the boolean value stating whether or not the currently played piece is the winning move (if a piece makes the winning move, it prints out who the winner is) 
   */
  public boolean isAWin(PieceLocation[][] board, int currentRow, int currentColumn){
    if(isBlackTurn == true){
      /*
      * if the number of pieces in a straight line north and south of the currently played piece equals the winNumber, 
      * then print out black as the winner and the game is over 
      */
      if (numberInLine(board, currentRow, currentColumn, "north") + 
          numberInLine(board, currentRow, currentColumn, "south") == winNumber + 1){
        System.out.println("Black is the winner!");
        gameIsOver = true;
      }
      /*
      * if the number of pieces in a straight line northeast and southwest of the currently played piece equals the winNumber, 
      * then print out black as the winner and the game is over 
      */
      if (numberInLine(board, currentRow, currentColumn, "northeast") + 
        numberInLine(board, currentRow, currentColumn, "southwest") == winNumber + 1){
        System.out.println("Black is the winner!");
        gameIsOver = true;
      }
      /*
      * if the number of pieces in a straight line east and west of the currently played piece equals the winNumber, 
      * then print out black as the winner and the game is over 
      */
      if (numberInLine(board, currentRow, currentColumn, "east") + 
        numberInLine(board, currentRow, currentColumn, "west") == winNumber + 1){
        System.out.println("Black is the winner!");
        gameIsOver = true;
      }
      /*
      * if the number of pieces in a straight line southeast and northwest of the currently played piece equals the winNumber, 
      * then print out black as the winner and the game is over 
      */
      if (numberInLine(board, currentRow, currentColumn, "southeast") + 
        numberInLine(board, currentRow, currentColumn, "northwest") == winNumber + 1){
        System.out.println("Black is the winner!");
        gameIsOver = true;
      }
    }
    else{
      /*
      * if the number of pieces in a straight line north and south of the currently played piece equals the winNumber, 
      * then print out white as the winner and the game is over 
      */
      if (numberInLine(board, currentRow, currentColumn, "north") + 
          numberInLine(board, currentRow, currentColumn, "south") == winNumber + 1){
        System.out.println("White is the winner!");
        gameIsOver = true;
      }
      /*
      * if the number of pieces in a straight line northeast and southwest of the currently played piece equals the winNumber, 
      * then print out white as the winner and the game is over 
      */
      if (numberInLine(board, currentRow, currentColumn, "northeast") + 
        numberInLine(board, currentRow, currentColumn, "southwest") == winNumber + 1){
        System.out.println("White is the winner!");
        gameIsOver = true;
      }
      /*
      * if the number of pieces in a straight line east and west of the currently played piece equals the winNumber, 
      * then print out white as the winner and the game is over 
      */
      if (numberInLine(board, currentRow, currentColumn, "east") + 
        numberInLine(board, currentRow, currentColumn, "west") == winNumber + 1){
        System.out.println("White is the winner!");
        gameIsOver = true;
      }
      /*
      * if the number of pieces in a straight line southeast and northwest of the currently played piece equals the winNumber, 
      * then print out white as the winner and the game is over 
      */
      if (numberInLine(board, currentRow, currentColumn, "southeast") + 
        numberInLine(board, currentRow, currentColumn, "northwest") == winNumber + 1){
        System.out.println("White is the winner!");
        gameIsOver = true;
      }
    }
    return true;  
  }
  
  /*
   * main method: runs the GUI and makes the board appear (depending on the inputs)
   * @param args  String that determines the number of pieces in a row needed to win and the size of the board (row by column), depending on the number of input(s)
   */
  public static void main(String[] args){
    Application.launch(args); 
  }
  
  /*
   * A button click event handler that alternates between colors when buttons are clicked, and applies all of the rules and helper methods above
   */
  public class ButtonClick implements EventHandler<ActionEvent>{
    public void handle(ActionEvent e){
      PieceLocation b = (PieceLocation) e.getSource();
      
      /*
       * if the background is green, the four-four rule and the three-three rule have not been broken, a win has not been made, and the game is not over, 
       * then let the game be played the each button click alternate between black and white buttons
       */
      if(b.getBackground() == noExistingButton && fourFourRuleFollowed(board,b.getRow(),b.getColumn()) && 
         threeThreeRuleFollowed(board,b.getRow(),b.getColumn()) && !gameIsOver){
         isAWin(board,b.getRow(),b.getColumn());
        if(isBlackTurn == true){
          b.setBackground(blackButton);
          isBlackTurn = false;
        }
        else{
          b.setBackground(whiteButton);
          isBlackTurn = true;
        }
      }
      else{
        System.out.println("You cannot make this move");
      }
    }
  }
  /*
   * class that represents each square on the board (in other words, a piece's location) and allows the row and column of a piece of be accessed
   */
  public class PieceLocation extends Button{
    /*
     * field row  represents the number of the piece's row
     */
    private int row;
    /*
     * field column  represents the number of the piece's column
     */
    private int column;
    
    /*
     * constructor for the PieceLocation class 
     * @param row  the row of a piece
     * @param column  the column of a piece
     */
    public PieceLocation(int row, int column){
      this.row = row;
      this.column = column;
    }
    
    /*
     * method that gets the value of row
     */
    public int getRow(){
      return row;
    }
    
    /*
     * method that gets the value of column
     */
    public int getColumn(){
      return column;
    }
  }
}