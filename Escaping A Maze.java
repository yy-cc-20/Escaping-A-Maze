/*
	Visualize how the program find a path from start to finish in a maze. X are walls, blanks are corridors,
	'S' is the starting point, 'E' is the ending point. At each intersection, the pc is allowed to 
	- Go up
	- Go left
	- Go right
	- Go down
 */

package backtracking;

import java.awt.Point;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Stack;

public class Maze {
	private static Scanner scanner = new Scanner(System.in);
	
	// Notations on the maze
	// WALL = 'X'
	private final static char CORRIDOR = ' ';
	private final static char STARTING_POINT = 'S';
	private final static char ENDING_POINT = 'E';
	
	private final static Point UNKNOWN_POSITION = new Point(-1, -1);
	private final static char DEAD_END = 'D';
	private final static char CURRENT_POSITION = '0';
	
	private final static char GO_UP    = '^';
	private final static char GO_RIGHT = '>';
	private final static char GO_DOWN  = 'v';
	private final static char GO_LEFT  = '<';
	
	// data member
	private char[][] maze;
	private Point startingPoint;
	private Point endingPoint;
	
	public Maze(char[][] m) {
		maze = m.clone();
		findStartingAndEndingPoint();		
	}
	
	// Display the path to get to the ending point on the maze
	public void displayAnimatedSolution() {
		if (startingPoint.equals(endingPoint)
				|| startingPoint.equals(UNKNOWN_POSITION) 
				|| endingPoint.equals(UNKNOWN_POSITION)) {
			System.out.println("Invalid Input");
			return;
		}
		
		// Initialize variables
		char[][] markedMaze = new char[maze.length][]; 
		for (int i = 0; i < maze.length; ++i) { // Clone 2D array
			markedMaze[i] = maze[i].clone(); 
		}
		
		if (move(markedMaze, startingPoint.x, startingPoint.y))
			display(markedMaze, -1, -1); // Does not need to show current position
		else
			System.out.println("Destination cannot be reached.");
	}
	
	// Using backtracking algorithm
	// Find the shortest path to the ending point
	/** @param markedMaze 	contains the maze and visited path
	 * @param x, y 			current	position
	 * @return true if the path is found
	 */
	private boolean move(char[][] markedMaze, int x, int y) {
		display(markedMaze, x, y);
		clearScreen();
		
		// If reach the goal node
		if (markedMaze[y][x] == ENDING_POINT) 
			return true;
		
		// If reach a child node
		if (y > 0 
				&& (markedMaze[y - 1][x] == CORRIDOR 
				|| markedMaze[y - 1][x] == ENDING_POINT)) { // Go up
			markedMaze[y][x] = GO_UP;
			if (move(markedMaze, x, y - 1)) // Exploring a subtree
				return true;
		}
		if (x + 1 < markedMaze[y].length 
				&& (markedMaze[y][x + 1] == CORRIDOR
				|| markedMaze[y][x + 1] == ENDING_POINT)) { // Go right
			markedMaze[y][x] = GO_RIGHT; 
			if (move(markedMaze, x + 1, y))
				return true;
		}
		if (y + 1 < markedMaze.length 
				&& (markedMaze[y + 1][x] == CORRIDOR
				|| markedMaze[y + 1][x] == ENDING_POINT)) { // Go down
			markedMaze[y][x] = GO_DOWN; 
			if (move(markedMaze, x, y + 1))
				return true;
		}
		if (x > 0 
				&& (markedMaze[y][x - 1] == CORRIDOR
				|| markedMaze[y][x - 1] == ENDING_POINT)) { // Go left
			markedMaze[y][x] = GO_LEFT; 
			if (move(markedMaze, x - 1, y))
				return true;
		}
		
		// If reach a leaf node
		markedMaze[y][x] = DEAD_END; 
		display(markedMaze, x, y);
		clearScreen();
		return false;
	}
	
	// pre: call clearScreen()
	private void display(char[][] markedMaze, int x, int y) {
		markedMaze[startingPoint.y][startingPoint.x] = STARTING_POINT;  
		markedMaze[endingPoint.y][endingPoint.x] = ENDING_POINT; 
		
		for (int i = 0; i < markedMaze.length; ++i) {
			for (int j = 0; j < markedMaze[i].length; ++j) {
				if (x == j && y == i)
					System.out.print(CURRENT_POSITION); // Layer 1
				else
					System.out.print(markedMaze[i][j]); // Layer 2
			}
			System.out.println();
		}
	}

	// Assuming one startingPoint and endingPoint
	private void findStartingAndEndingPoint() {
		for (int i = 0; i < maze.length; ++i) {
			for (int j = 0; j < maze[i].length; ++j) {
				if (maze[i][j] == STARTING_POINT)
					startingPoint = new Point(j, i);
				else if (maze[i][j] == ENDING_POINT)
					endingPoint = new Point(j, i);
			}
		}
		if (startingPoint.equals(new Point(0, 0)) && maze[0][0] != STARTING_POINT) // If startingPoint not found
			startingPoint = new Point(UNKNOWN_POSITION);
		else if (endingPoint.equals(new Point()) && maze[0][0] != ENDING_POINT) // If endingPoint not found
			endingPoint = new Point(UNKNOWN_POSITION);
	}
	
	static void clearScreen() {
		System.out.println("Enter >>");
		scanner.nextLine();
		for (int i = 0; i < 22; ++i)
			System.out.println();
	}
	
	// Test Case
	static char[][] maze1;
	static char[][] maze2;
	static char[][] maze3;
	static char[][] maze4;
	
	static { // static initialization block
		maze1 = new char[][] { // Best Case
			"XXXX".toCharArray(),
			"XSEX".toCharArray(),
			"XXXX".toCharArray()
		};

		
		maze2 = new char[][] { // Common Case
			"XXXXXXXXXX".toCharArray(), 
			"X  X X X X".toCharArray(),
			"XX X   X X".toCharArray(),
			"XX XXX   X".toCharArray(),
			"X    X XXX".toCharArray(),
			"XX X X XEX".toCharArray(),
			"X  X   X X".toCharArray(),
			"XX XXX X X".toCharArray(),
			"XS   X   X".toCharArray(),
			"XXXXXXXXXX".toCharArray()
		};
			
		maze3 = new char[][] { // Worst Case
			"XXXXXXXXXX".toCharArray(),
			"X        X".toCharArray(),
			"X        X".toCharArray(),
			"X        X".toCharArray(),
			"X        X".toCharArray(),
			"X        X".toCharArray(),
			"X        X".toCharArray(),
			"XX XXXXXXX".toCharArray(),
			"XES      X".toCharArray(),
			"XXXXXXXXXX".toCharArray()
		};
			
		maze4 = new char[][] { // Unreachable Destination
			"XXXXXXXXXX".toCharArray(),
			"X  X X X X".toCharArray(),
			"XX X   X X".toCharArray(),
			"XX XXX   X".toCharArray(),
			"X    X XXX".toCharArray(),
			"XX X X XEX".toCharArray(),
			"X  X   XXX".toCharArray(),
			"XX XXX X X".toCharArray(),
			"XS   X   X".toCharArray(),
			"XXXXXXXXXX".toCharArray(),
		};
	}
	
	public static void main(String... args) {
		int mazeID;
		Maze aMaze;
		
		while (true) {
			System.out.println("Choose a maze:");
			System.out.println("1. Best Case");
			System.out.println("2. Common Case");
			System.out.println("3. Worst Case");
			System.out.println("4. Worst Case (Unreachable Destination)");
			System.out.println("-1. Exit");			
			
			// Input Validation
			while (true) {
				System.out.print(">> ");
				try {
					mazeID = scanner.nextInt();
					scanner.nextLine();
					if (mazeID == -1 || mazeID <= 4 && mazeID >= 1)
						break;
				} catch (InputMismatchException ex) {
					scanner.nextLine(); // Consume bad input
				}
			}
			
			switch (mazeID) {
				case -1:
					scanner.close();
					return;
				case 1:
					aMaze = new Maze(maze1);
					break;
				case 2:
					aMaze = new Maze(maze2);
					break;
				case 3:
					aMaze = new Maze(maze3);
					break;
				default: // case 4
					aMaze = new Maze(maze4);			
			}
		
			System.out.println();
			aMaze.displayAnimatedSolution();
			System.out.println();
		}
	}
}
