package travellingSalesman;

//This class has the algorithms used 
//It has BFS and alpha Beta Pruning algorithm
public class TravelCity extends Functions {
	// Initiates needed global variables
	private static int printIndex = 0;
	private static int[][] queueCities;
	private static int[][] expandedPath;
	private int[] addToQueue = new int[cityCount];

	// Initiates a BFS, will be used for BFS and alpha Beta Pruning algorithm
	public void createInitQueue() {
		queueCities = new int[cityCount - 1][cityCount];
		for (int initQueue = 0; initQueue < cityCount - 1; initQueue++) {
			queueCities[initQueue][0] = cities[0][0];
			queueCities[initQueue][1] = cities[initQueue + 1][0];
		}
	}

	private void getShortest(int[] shortest) {
		// Prints only the shortest path
		if (printIndex == 0) {
			System.out.println(" ");
			// Adds the final elements to the paths and calculates the smallest
			int[][] finishedPaths = new int[queueCities.length][cityCount + 1];
			for (int addCities = 0; addCities < queueCities.length; addCities++) {
				// Add the element
				for (int addElement = 0; addElement < cityCount; addElement++) {
					finishedPaths[addCities][addElement] = queueCities[addCities][addElement];
				}
				finishedPaths[addCities][cityCount] = finishedPaths[addCities][0];
			}
			// Runs few functions to find smallest path
			shortest = new int[cityCount];
			shortest = shortestPath(finishedPaths);
			System.out.println(totalDistance(shortest));
			printIndex++;
		}

	}

	// Gets fastest Path and distance
	private void bfsPrint(double shortestDistance, int[] fastestPath) {
		// prints off answers
		System.out.println(" ");
		System.out.println("Best Result");
		totalDistance(fastestPath);
		System.out.println(shortestDistance);

	}

	// This is the Breadth first search algorithm
	public void breadthFirstSearch() {
		// Initiates the first 2 elements
		createInitQueue();
		// Initiates integers for later use, they need to constantly change
		int reducedSize = cityCount - 2;
		int[] fastestPath = new int[cityCount + 1];
		double shortestDistance = 0;
		// starts the Breadth first search for all cities
		for (int currentCity = 0; currentCity < cityCount - 2; currentCity++) {
			// Calculates size for the next array
			int expandSize = queueCities.length * reducedSize;
			expandedPath = expandPaths(queueCities);
			queueCities = new int[expandSize][cityCount];
			queueCities = expandedPath;
			// Reduces the amount of the paths as a new element is added
			reducedSize--;
			// Once the BFS has finished this will run
			if (currentCity == cityCount - 3) {
				// Adds the last element to the paths and calculates the
				// distance
				for (int checkShortest = 0; checkShortest < expandedPath.length; checkShortest++) {
					int[] calculatedPath = new int[cityCount + 1];
					for (int getPath = 0; getPath < cityCount; getPath++) {
						calculatedPath[getPath] = expandedPath[checkShortest][getPath];
						calculatedPath[cityCount] = calculatedPath[0];
					}
					double currentDistance = shortestDistance(calculatedPath);
					// Checks for the smallest path and saves it
					if (currentDistance < shortestDistance || shortestDistance == 0) {
						shortestDistance = currentDistance;
						fastestPath = calculatedPath;
					}
				}
			}
		}
		// Prints off the fastest path
		bfsPrint(shortestDistance, fastestPath);
	}

	// Starts alpha Beta Pruning algorithm
	private void alphaBetaPruning() {
		// Gets the shortest path from the queue
		int[] shortestPath = shortestPath(queueCities);
		// Expands the smallest Path
		int[][] expandSingle = expandSingle(shortestPath);
		shortestPath = new int[cityCount];
		// Gets the shortest path of the expanded
		shortestPath = shortestPath(expandSingle);
		double expandedShortest = shortestDistance(shortestPath);
		int expandedSize = 0;
		// Checks if the path is larger than the rest of the queue
		for (int checkSmaller = 0; checkSmaller < queueCities.length; checkSmaller++) {
			double checkShorter = shortestDistance(queueCities[checkSmaller]);
			if (checkShorter < expandedShortest) {
				expandedSize++;
			}
		}
		// Holds all paths that are smaller than expanded
		int[][] smallerPathHolder;
		smallerPathHolder = new int[expandedSize][cityCount];
		for (int addSmaller = 0, addIndex = 0; addSmaller < queueCities.length; addSmaller++) {
			// Adds to a new temporary array
			double checkShorter = shortestDistance(queueCities[addSmaller]);
			if (checkShorter < expandedShortest) {
				smallerPathHolder[addIndex] = queueCities[addSmaller];
				addIndex++;
			}
		}
		// Expands all the smaller paths and adds to the queue for repeat
		queueCities = expandPaths(smallerPathHolder);
		// Checks if the paths have been create recursive function
		if (queueCities[0][cityCount - 1] == 0) {
			alphaBetaPruning();
		}
		queueCities[0] = addToQueue;
		getShortest(shortestPath);
	}

	// This runs a best first search
	public void bestFirstSearch(int compare) {
		// Gets the shortest path of a queue
		int[] shortestPath = shortestPath(queueCities);
		// Expands the shortest Paths
		int[][] expandSingle = expandSingle(shortestPath);
		// Gets the shortest of the expanded
		shortestPath = new int[cityCount];
		shortestPath = shortestPath(expandSingle);
		// Add the shortest queue and runs again
		queueCities = new int[1][cityCount];
		queueCities[0] = shortestPath;
		// Checks whether to compare with alpha Beta Pruning
		if (queueCities[0][cityCount - 1] == 0) {
			bestFirstSearch(compare);
		}
		// Adds it to the queue
		if (compare == 1) {
			addToQueue = queueCities[0];
		} else {
			// Gets the shortest distance and prints
			getShortest(queueCities[0]);
		}
	}

	// Runs both algorithms to find best path
	public void checkBestPath() {
		// Runs two algorithms to get best path
		createInitQueue();
		bestFirstSearch(1);
		createInitQueue();
		alphaBetaPruning();
	}
}
