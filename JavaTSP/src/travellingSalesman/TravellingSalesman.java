package travellingSalesman;

//This class calls all the functions
//It calculates the rum time and checks the size
//of the cities and runs what algorithm accordingly
public class TravellingSalesman extends Functions {
	public static void main(String[] args) {
		// Starts the data function, gets the input from user
		long startTime = System.nanoTime();
		Functions cityData = new Functions();
		try {
			cityData.obtainData();
		} catch (Exception except) {
			except.printStackTrace();
		}
		// Creates the adjacency matrix and prints of cities
		cityData.printCities(cities);
		cityData.createAdjacencyMatrix();
		TravelCity createPath = new TravelCity();

		// Checks the size off cities and runs different algorithm
		if (cityCount <= 10) {
			createPath.breadthFirstSearch();
		} else if(cityCount >= 11 && cityCount <= 150){
			createPath.checkBestPath();
		} else if(cityCount >= 151){
			createPath.createInitQueue();
			createPath.bestFirstSearch(0);
		}
		// Finishes the timer and calculates the time taken and returns the
		// result in nano seconds
		System.out.println(" ");
		long endTime = System.nanoTime();
		System.out.println("Total execution time: " + ((endTime - startTime) / 1000000000.0));
	}
}
