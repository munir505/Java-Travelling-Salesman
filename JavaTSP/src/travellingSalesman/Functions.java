package travellingSalesman;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;

//This class gets the cities from the text file
//This class also holds the functions for the algorithms
public class Functions {
	// Initiate variables
	private ArrayList<Integer> fileTemp = new ArrayList<Integer>();
	// Test name
	private String fileName = " ";
	// Amount of cities and elements
	public static int cityElements = 3;
	public static int cityCount;
	// Holds the cities and adjacency matrix
	public static int[][] cities;
	public static double[][] adjacencyMatrix;
	public static int[] cityList;

	private void userInput() {
		Scanner getFile = new Scanner(System.in);
		System.out.println("Type either 1, 2, 3, 4, 5, 6, 7, to run specific city list (no spaces)");
		String file = getFile.nextLine();
		if(file.equals("1")) {
			fileName = "trainingtest1.txt";
		} else if(file.equals("2")) {
			fileName = "trainingtest2.txt";
		} else if(file.equals("3")) {
			fileName = "trainingtest3.txt";
		} else if(file.equals("4")) {
			fileName = "test1.txt";
		} else if(file.equals("5")) {
			fileName = "test2.txt";
		} else if(file.equals("6")) {
			fileName = "test3.txt";
		} else if(file.equals("7")) {
			fileName = "test4.txt";
		}else{
			System.out.println("Please re-enter");
			System.out.println(file + " is not a valid request");
			
			userInput();
		}
		getFile.close();
	}
	
	// Reading selected users file
	public void obtainData() throws Exception {
		userInput();
		File dataFile = new File(fileName);
		Scanner reader = new Scanner(dataFile);
		// Reads each integer
		while (reader.hasNext()) {
			int nextVal = reader.nextInt();
			fileTemp.add(nextVal);
		}
		// Setting sizes for the arrays
		cityCount = fileTemp.size() / cityElements;
		cities = new int[cityCount][cityElements];
		adjacencyMatrix = new double[cityCount][cityCount];
		cityList = new int[cityCount];
		reader.close();
		fillCities();
	}

	// Fills cities array with temporary array list
	private void fillCities() {
		// Gets elements from array list
		int fileIndex = 0;
		for (int city = 0; city < cityCount; city++) {
			for (int value = 0; value < cityElements; value++) {
				cities[city][value] = fileTemp.get(fileIndex);
				fileIndex++;
			}
		}
		fillCityList();
	}

	// Create single array with only city numbers
	private void fillCityList() {
		for (int fillCityList = 0; fillCityList < cityCount; fillCityList++) {
			cityList[fillCityList] = cities[fillCityList][0];
		}
	}

	// Using Euclidean distance to calculate the distance
	private double distanceSum(int pointOneX, int pointOneY, int pointTwoX, int pointTwoY) {
		// Subtracting the X and Y values
		int xLength = pointTwoX - pointOneX;
		int yLength = pointTwoY - pointOneY;
		// Squaring the subtracted values
		int xSquare = xLength * xLength;
		int ySquare = yLength * yLength;
		// Adding the squared values
		int distanceSquare = xSquare + ySquare;
		// Getting the square root of the distance
		double euclideanDistance = Math.sqrt(distanceSquare);
		return euclideanDistance;
	}

	// Creates adjacency matrix holding all distances from and to cities
	public void createAdjacencyMatrix() {
		for (int fromCity = 0; fromCity < cityCount; fromCity++) {
			for (int toCity = 0; toCity < cityCount; toCity++) {
				if (cities[fromCity][0] == cities[toCity][0]) {
					adjacencyMatrix[fromCity][toCity] = 0;
				} else {
					// Creating 4 integers for the X and Y values
					int pointOneX = cities[fromCity][1];
					int pointOneY = cities[fromCity][2];
					int pointTwoX = cities[toCity][1];
					int pointTwoY = cities[toCity][2];
					// Adding to the matrix using Euclidean distance
					adjacencyMatrix[fromCity][toCity] = distanceSum(pointOneX, pointOneY, pointTwoX, pointTwoY);
				}
			}
		}
	}

	// Calculates the distance of a path and returns the distance
	public double totalDistance(int[] travelPath) {
		double totalSum = 0;
		int distanceRunTime = 0;
		// Checks how many usable elements in the array
		for (int city = 0; city < travelPath.length; city++) {
			if (travelPath[city] != 0) {
				distanceRunTime++;
			}
		}
		// Starts to calculate the whole distance using the adjacency matrix
		for (int fromCity = 0, toCity = 1; fromCity < distanceRunTime - 1; fromCity++, toCity++) {
			double distanceMatrix = adjacencyMatrix[travelPath[fromCity] - 1][travelPath[toCity] - 1];
			totalSum = totalSum + distanceMatrix;
		}
		// Prints the path
		for (int printAll = 0; printAll < distanceRunTime - 1; printAll++) {
			System.out.print(travelPath[printAll] + "--> ");
		}
		// Returns the total distance
		System.out.print(travelPath[distanceRunTime - 1]);
		System.out.println(" ");
		System.out.println("Total Distance");
		return totalSum;
	}

	// Takes a path and returns only the distance of it
	public double shortestDistance(int[] travelPath) {
		double totalSum = 0;
		int distanceRunTime = 0;
		// Checks how many elements in the path
		for (int currentElement = 0; currentElement < travelPath.length; currentElement++) {
			if (travelPath[currentElement] != 0) {
				distanceRunTime++;
			}
		}
		// Adding the distances together
		for (int fromCity = 0, toCity = 1; fromCity < distanceRunTime - 1; fromCity++, toCity++) {
			double distanceMat = adjacencyMatrix[travelPath[fromCity] - 1][travelPath[toCity] - 1];
			totalSum = totalSum + distanceMat;
		}
		return totalSum;
	}

	// Prints off the cities
	public void printCities(int[][] TravelType) {
		// Prints the city
		for (int city = 0; city < cityCount; city++) {
			// Prints all elements
			for (int value = 0; value < cityElements; value++) {
				System.out.print(TravelType[city][value] + " ");
			}
			System.out.println(" ");
		}
	}

	// Creates an array with all the possible paths that a current path can take
	public int[] checkPossiblePaths(int[] currentPath) {
		int[] possiblePaths = new int[cityCount];
		for (int checkerIndex = 0; checkerIndex < cityCount; checkerIndex++) {
			int containsPath = 0;
			boolean contains = false;
			for (int pathContains = 0; pathContains < cityCount; pathContains++) {
				// Checks if a city is already in the path
				if (cityList[checkerIndex] == currentPath[pathContains]) {
					contains = true;
				} else {
					// Adds to the array if not already contained
					containsPath = cityList[checkerIndex];
				}
			}
			if (contains == false) {
				possiblePaths[checkerIndex] = containsPath;
			}
		}
		// Sorts whole array from left to right removes all zeros in path
		for (int sortPossible = 0; sortPossible < cityCount - 1; sortPossible++) {
			for (int pushLeft = 0; pushLeft < cityCount - 1; pushLeft++) {
				// Looks for zeros and pushes elements to the left
				if (possiblePaths[pushLeft] == 0) {
					possiblePaths[pushLeft] = possiblePaths[pushLeft + 1];
					possiblePaths[pushLeft + 1] = 0;
				}
			}
		}
		return possiblePaths;
	}

	// Returns the size of all possible paths that can be taken
	public int possiblePathSize(int[] possiblePath) {
		int possibleSize = 0;
		for (int pathIndex = 0; pathIndex < cityCount; pathIndex++) {
			// Looking for zeros if array index equals
			// zero increment possible size
			if (possiblePath[pathIndex] != 0) {
				possibleSize++;
			}
		}
		return possibleSize;
	}

	// Checks the whole array for the shortest single path
	public int[] shortestPath(int[][] allPaths) {
		int[] shortestPath = new int[cityCount];
		double currentShortest = 0;
		for (int currentPath = 0; currentPath < allPaths.length; currentPath++) {
			// Fills shortest path array with the current shortest
			double currentDistance = shortestDistance(allPaths[currentPath]);
			// Checks if the current shortest is smaller
			if (currentDistance < currentShortest || currentShortest == 0) {
				currentShortest = currentDistance;
				shortestPath = allPaths[currentPath];
			}
		}
		return shortestPath;
	}

	// Expands every single path in the 2D array
	public int[][] expandPaths(int[][] currentPaths) {
		int[][] expandedPaths;
		// Check at what point the array can be added to
		int addIndex = 0;
		while (currentPaths[0][addIndex] != 0) {
			addIndex++;
		}
		int[] possible = new int[cityCount];
		// Checks the possible paths a path can take and loop will run that long
		possible = checkPossiblePaths(currentPaths[0]);
		int pathsSize = possiblePathSize(possible);
		int expandedSize = currentPaths.length * pathsSize;
		expandedPaths = new int[expandedSize][cityCount];
		// Does this for all paths in the array
		for (int currentPath = 0, expandedIndex = 0; currentPath < currentPaths.length; currentPath++) {
			// Gets possible path and size a path can take
			int[] possiblePaths = new int[cityCount];
			possiblePaths = checkPossiblePaths(currentPaths[currentPath]);
			int possibleSize = possiblePathSize(possiblePaths);
			// Adds all possible paths to the new array
			for (int addPossible = 0; addPossible < possibleSize; addPossible++) {
				for (int fillCurrent = 0; fillCurrent < cityCount; fillCurrent++) {
					expandedPaths[expandedIndex][fillCurrent] = currentPaths[currentPath][fillCurrent];
				}
				expandedPaths[expandedIndex][addIndex] = possiblePaths[addPossible];
				expandedIndex++;
			}
		}
		// Returns the fully expanded array
		return expandedPaths;
	}

	// Expands a single array
	public int[][] expandSingle(int[] currentPath) {
		int[][] expandedPaths;
		// Gets the point where a element can be added
		int addIndex = 0;
		while (currentPath[addIndex] != 0) {
			addIndex++;
		}
		// Sets the size of new array
		int[] possible = new int[cityCount];
		possible = checkPossiblePaths(currentPath);
		int pathsSize = possiblePathSize(possible);
		expandedPaths = new int[pathsSize][cityCount];
		// Goes through the array and adds elements accordingly
		for (int firstPath = 0; firstPath < pathsSize; firstPath++) {
			// Checks possible paths and adds if can
			int[] possiblePaths = new int[cityCount];
			possiblePaths = checkPossiblePaths(currentPath);
			for (int fillFirst = 0; fillFirst < cityCount; fillFirst++) {
				expandedPaths[firstPath][fillFirst] = currentPath[fillFirst];
			}
			expandedPaths[firstPath][addIndex] = possiblePaths[firstPath];
		}
		// Return a expanded array
		return expandedPaths;
	}
}
