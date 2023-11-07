import org.junit.jupiter.api.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MaxSideLengthTests {

    private static final String fileName = "tests/p2_input.txt";
    private MaxSideLength maxSideLength;

    @AfterAll
    public static void terminationProcedure(){
        System.out.println("All test cases are done.");
    }

    @BeforeEach
    public void randomizeFile(){
        maxSideLength = new MaxSideLength();
        try {
            FileWriter writer = new FileWriter(fileName);
            int[][] points = generatePoints();
            writer.write(points.length + "\n");
            for(int[] point : points)
                writer.write(point[0] + " " + point[1] + "\n");
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Testcase: Max Side Length 01")
    public void test1(){
        long naiveSolution = naiveApproach(fileName);
        long optimizedSolution = maxSideLength.solve(fileName);
        assertEquals(naiveSolution, optimizedSolution);
    }

    @Test
    @DisplayName("Testcase: Max Side Length 02")
    public void test2(){
        long naiveSolution = naiveApproach(fileName);
        long optimizedSolution = maxSideLength.solve(fileName);
        assertEquals(naiveSolution, optimizedSolution);
    }

    @Test
    @DisplayName("Testcase: Max Side Length 03")
    public void test3(){
        long naiveSolution = naiveApproach(fileName);
        long optimizedSolution = maxSideLength.solve(fileName);
        assertEquals(naiveSolution, optimizedSolution);
    }
    private long naiveApproach(String fileName_){
        int[][] points = scanInput(fileName_);
        if(points.length < 2)
            throw new RuntimeException("2 Points are required at least");
        int[][] closestPair = new int[2][2];
        long shortestDistance = Long.MAX_VALUE;
        for(int[] p1: points){
            for(int[] p2: points){
                if(p1 == p2)
                    continue;
                if(computeEuclideanDistance(p1, p2) < shortestDistance){
                    closestPair[0][0] = p1[0];
                    closestPair[0][1] = p1[1];
                    closestPair[1][0] = p2[0];
                    closestPair[1][1] = p2[1];
                    shortestDistance = computeEuclideanDistance(p1, p2);
                }
            }
        }
        return computeSquareSide(closestPair);
    }

    private long computeEuclideanDistance(int[] pt1, int[] pt2){
        return (long) Math.sqrt(Math.pow(pt1[0] - pt2[0], 2) + Math.pow(pt1[1] - pt2[1], 2));
    }

    private long computeSquareSide(int[][] closestPair){
        return Math.max(
                Math.abs(closestPair[0][0] - closestPair[1][0]),
                Math.abs(closestPair[0][1] - closestPair[1][1])
        );
    }

    private static int[][] generatePoints(){
        Random rand = new Random();
        int numberOfPoints = rand.nextInt(1_0000) + 1; // Random size between 1 and 10 ^ 6
        Map<Integer, Set<Integer>> pointsMap = new HashMap<>();
        int[][] points = new int[numberOfPoints][2];

        for (int i = 0; i < numberOfPoints; i++) {
            int[] point = new int[2];
            do {
                // Generate a random integer between -10^6 and 10 ^ 6
                point[0] = rand.nextInt(20_000_000) - 10_000_000;
                point[1] = rand.nextInt(20_000_000) - 10_000_000;
            } while (pointsMap.get(point[0]) != null && pointsMap.get(point[0]).contains(point[1])); // Continue generating until it's unique
            points[i][0] = point[0];
            points[i][1] = point[1];
            if(pointsMap.containsKey(point[0]))
                pointsMap.get(point[0]).add(point[1]);
            else {
                Set<Integer> tempSet = new HashSet<>();
                tempSet.add(point[1]);
                pointsMap.put(point[0], tempSet);
            }
        }
        return points;
    }

    private static int[][] scanInput(String inputFile){
        int[][] points;
        try {
            File file = new File(inputFile);
            Scanner scanner = new Scanner(file);

            int numberOfPoints = scanner.nextInt();
            points = new int[numberOfPoints][2];

            for(int i = 0 ; i < numberOfPoints ; i++){
                points[i][0] = scanner.nextInt();
                points[i][1] = scanner.nextInt();
            }
            return points;
        } catch (FileNotFoundException E){
            throw new RuntimeException("Input file wasn't found.");
        }
    }
}
