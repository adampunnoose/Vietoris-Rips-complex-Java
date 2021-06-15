import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

public class VRC {
	
	
	private String line;
	private int numPoints, lineNum, numDimensions;
	double[][] points;
	
	
	VRC(String path){
		
		//File IO from csv template
		line = "";
		lineNum = 1;
		try { //try to create, if it doesnt work, print the error message
			BufferedReader br = new BufferedReader(new FileReader(path));
			line = br.readLine();
			
			//read the data on the first line of the CSV file
			line = br.readLine();
			String[] values = line.split(",");
			numPoints = Integer.valueOf(values[1]);
			numDimensions = Integer.valueOf(values[0]);
			points = new double[numPoints][numDimensions];
			
			//first point 
			points[0][0] = Integer.valueOf(values[2]);
			points[0][1] = Integer.valueOf(values[3]);
			if(numDimensions == 3) points[0][2] = Integer.valueOf(values[4]);
			
			
			
			//infinitly go through the file, start from line 2 (line 3 when looking at excel)
			while((line = br.readLine()) != null) {
				lineNum += 1;
				values = line.split(","); //array of all values in a line, split up by the commas
				
				
				//read Points
				if(lineNum >= 2 && lineNum < numPoints + 1) {
					points[lineNum - 1][0] = Integer.valueOf(values[2]);
					points[lineNum - 1][1] = Integer.valueOf(values[3]);
					if(numDimensions == 3) points[lineNum - 1][2] = Integer.valueOf(values[4]);
					
				}
				
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void finalPrint(Vector<int[]> list, double[][] points) {
		
		//print simplicies
		for(int i = 0; i < list.size(); i++) {
			System.out.print("[");
			for(int j = 0; j < list.get(i).length; j++) {
				System.out.print(list.get(i)[j]);
			}
			System.out.print("] \n");
			
		}
	}
	

	
	public static double distP(double[] p1, double[] p2) {
		double distance = Math.pow(   Math.pow(p1[0] - p2[0], 2) + Math.pow(p1[1]- p2[1], 2) + Math.pow(p1[2]- p2[2], 2)    ,   0.5   );
		return distance;
	}
	
	public static double[] maxDist(double[][] points) {
		double maxD = 0;
		double point1 = -1;
		double point2 = -1;
		double distance;
		
		for(int i =0; i < points.length; i++) {
			for(int j =i+1; j < points.length; j++) {
				distance = distP(points[i], points[j]);
				if(distance > maxD) {
					maxD = distance;
					point1 = i;
					point2 = j;
					
				}
			}
		}
		
		double[] ans = {maxD, point1, point2};
		System.out.println("The Maximum distance between points is " + ans[0]);
		System.out.println("the Points of most distance are " + ans[1] + " and " + ans[2]);
		return ans;
		
		
	}
	
	public static double[] minDist(double[][] points) {
		double minD = 2147483647;
		double point1 = -1;
		double point2 = -1;
		double distance;
		
		for(int i =0; i < points.length; i++) {
			for(int j =i+1; j < points.length; j++) {
				
				distance = distP(points[i], points[j]);
				if(distance < minD) {
					minD = distance;
					point1 = i;
					point2 = j;
					
				}
			}
		}
		
		double[] ans = {minD, point1, point2};
		System.out.println("The Minimum distance between points is " + ans[0]);
		System.out.println("the Points of least distance are " + ans[1] + " and " + ans[2]);
		
		return ans;
		
		
	}
	
	public static boolean tableSimplexComparison(int[] array, boolean[][] table, int tableColumn) {
		//does simplex of any length exist in table 
		//simplex we are looking for = array.length
		
		boolean output = true;
		for(int i = 0; i < array.length; i++){
			if(table[tableColumn][array[i]] != true) {
				output = false;
			}
		}
		
		return output;
	}
	
	public static int[] insertionArray(int[] array, int j) {
		int[] insertion = new int[array.length + 1];
		insertion[0] = j;
		for (int i = 1; i < insertion.length; i++) {
			insertion[i] = array[i-1];
		}

		return insertion;
	}
	
	public static Vector<Vector<Integer>> Edges(double[][] points, double r) {
		//output an array with all the edges
		//assign points with their index number 
		
		//ArrayList<int[]> group1 = new ArrayList<int[]>();
		Vector<Vector<Integer>> group1 = new Vector<>();
		
		//compare 
		for(int i =0; i < points.length; i++) {
			//initialize the edge array to be inserted
			Vector<Integer> group2 = new Vector<>();
			for(int j =i+1; j < points.length; j++) {
				if(distP(points[i], points[j]) <= r) {
					group2.add(j);
					
					
					
				}
				
			}
			group1.add(group2);
		}
		return group1;
		
		//output: 2d vector, outer index gives point #, inner values are
		//other points within r-distance of the point in the index
		
	}
	
	public static boolean[][] EdgesTable(Vector<Vector<Integer>> index) {
		
		boolean[][] table = new boolean[index.size()][index.size()];
		for(int i =0; i< index.size(); i++) {
			for(int j = 0; j < index.get(i).size(); j++) {
				table[i][index.get(i).get(j)] = true;
			}
		}
		
		return table;
		
	}
	

	public static Vector<int[]> zeroS(Vector<Vector<Integer>> edges){
		//input: edge List
		//output: 0-simplicies
		
		Vector<int[]> output = new Vector<>();
		for(int i = 0; i < edges.size(); i++) {
			int[] ins = new int[] {i};
			output.add(ins);
		}
		return output;
		
	}
	
	public static Vector<int[]> oneS(Vector<Vector<Integer>> edges) {
		//input: edge List
		//output: 1-simplicies
		
		Vector<int[]> output = new Vector<>();
		
		for(int i = 0; i < edges.size(); i++) {
			if (edges.get(i).size() != 0) {
				for(int j = 0; j < edges.get(i).size(); j++) {
					int[] insertion = new int[] {i, edges.get(i).get(j)};
					output.add(insertion);
					
				}
			}
				
			
		}
		return output;
		
		
	}
	
	public static Vector<int[]> twoS(Vector<Vector<Integer>> index,boolean[][] table ) {
		//structure input the one simplex
		//output: 2 simplex
		
		Vector<int[]> output = new Vector();
		
		for (int i = index.size() - 1; i > -1; i-- ) {
			if (index.get(i).size() != 0) {
				for(int j = 0; j < index.get(i).size(); j++) {//initial connection 
					
					for(int k = 0; k < i; k++) {//check connection against table
						//column = i
						//row = index.get(i).get(j)
						if(table[k][i] == true && table[k][index.get(i).get(j)] == true) {
							//int[] insertion = [k i index.get(i).get(j)];
							//int[] insertion = {k i index.get(i).get(j)};
							int[] insertion = new int[]{k, i, index.get(i).get(j)};
							output.add(insertion);
						}
					}
				}
				
			}
			
			//works on getting 3-simplex from edges
		}
		
		return output;
		
		
	}
	

	public static Vector<int[]> threeSPlus(Vector<int[]> index,boolean[][] table, int simplexNum) {
		//input: 3-simplex list and table
		//output: full 4+-simplex list
		Vector<int[]> output = new Vector();
		for(int i =0; i < index.size(); i++) { //go through every array in "index"
			for(int j = index.get(i)[0] - 1; j > -1; j--) {
				if(tableSimplexComparison(index.get(i), table, j) == true) {
					output.add(insertionArray(index.get(i), j));
					
				}
				
			}
		}
		
		return output;
		
	}
	
	public void calculateVRC(double radius) {
		double[] maxList = maxDist(points);
		double[] minList = minDist(points);
		double maxD = maxList[0];
		double minD = minList[0];
		
		Vector<Vector<Integer>> edgeList;
		edgeList = Edges(points,radius);
		boolean[][] edgeTable = EdgesTable(edgeList); //table
		
		Vector<int[]> zeroSimplex = zeroS(edgeList); 
		Vector<int[]> oneSimplex = oneS(edgeList);
		Vector<int[]> twoSimplex = twoS(edgeList, edgeTable);
		Vector<int[]> threeSimplex = threeSPlus(twoSimplex, edgeTable, 3);
		Vector<int[]> fourSimplex = threeSPlus(threeSimplex, edgeTable, 4);
		Vector<int[]> fiveSimplex = threeSPlus(fourSimplex, edgeTable, 5);
		
		Vector<int[]> output = zeroSimplex;
		output.addAll(oneSimplex);
		output.addAll(twoSimplex);
		output.addAll(threeSimplex);
		output.addAll(fourSimplex);
		output.addAll(fiveSimplex);
		
		System.out.println("\nVR(X," + radius + ") subsets of x: ");
		finalPrint(output, points);
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String path = "/Users/adam/Desktop/JAVA Book/VRC Project/Points Template Example.csv";
		VRC complex = new VRC(path);
		complex.calculateVRC(12);
		
	}
	
	
	
}


