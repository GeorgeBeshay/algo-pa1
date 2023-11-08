import org.junit.jupiter.api.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MaxSideLengthTests {
    private MaxSideLength maxSideLength;

    @BeforeEach
    public void initialization(){
        // To ensure that the tests are independent and stateless.
        maxSideLength = new MaxSideLength();
    }
    @AfterAll
    public static void terminationProcedure(){
        System.out.println("All test cases are done.");
    }

    @Test
    @DisplayName("Testcase Max Side Length: Convergence at input size = 10^7")
    public void test_convergenceAtTremendousInput(){
        // this test case only checks that the algorithm converges, when dealing with large inputs.
//        Utilities.generateRandomInput("tests/MaxSideLength_Inputs/10p7.txt", 10_000_000);
        long t1 = System.currentTimeMillis();
        maxSideLength.solve("tests/MaxSideLength_Inputs/10p7.txt");
        long t2 = System.currentTimeMillis();
        System.out.println("Algorithm Running Time (Input Size = 10^7) = " + (t2 - t1) + " ms.");
    }

    @Test
    @DisplayName("Testcase Max Side Length: Convergence at input size = 10^6")
    public void test_convergenceAtLargeInput(){
        // this test case only checks that the algorithm converges, when dealing with large inputs.
//        Utilities.generateRandomInput("tests/MaxSideLength_Inputs/10p6.txt", 1_000_000);
        long t1 = System.currentTimeMillis();
        maxSideLength.solve("tests/MaxSideLength_Inputs/10p6.txt");
        long t2 = System.currentTimeMillis();
        System.out.println("Algorithm Running Time (Input Size = 10^6) = " + (t2 - t1) + " ms.");
    }

    @Test
    @DisplayName("Testcase Max Side Length: Output correctness at input size = 10^4")
    public void test_convergenceAndCorrectnessAtModerateInput(){
//        Utilities.generateRandomInput("tests/MaxSideLength_Inputs/10p4.txt", 10_000);
        long t1 = System.currentTimeMillis();
        long naiveSolution = Utilities.naiveApproach("tests/MaxSideLength_Inputs/10p4.txt");
        long t2 = System.currentTimeMillis();
        System.out.println("Naive Algorithm Running Time (Input Size = 10^4) = " + (t2 - t1) + " ms.");
        t1 = System.currentTimeMillis();
        long optimizedSolution = maxSideLength.solve("tests/MaxSideLength_Inputs/10p4.txt");
        t2 = System.currentTimeMillis();
        System.out.println("Algorithm Running Time (Input Size = 10^4) = " + (t2 - t1) + " ms.");
        assertEquals(naiveSolution, optimizedSolution);
    }

    @Test
    @DisplayName("Testcase Max Side Length: Output correctness at input with same x-coordinate")
    public void test_convergenceAndCorrectnessAtSpecialInput_sameX(){
        long t1 = System.currentTimeMillis();
        long naiveSolution = Utilities.naiveApproach("tests/MaxSideLength_Inputs/sameX.txt");
        long t2 = System.currentTimeMillis();
        System.out.println("Naive Algorithm Running Time (Small input, Same x-coordinate) = " + (t2 - t1) + " ms.");
        t1 = System.currentTimeMillis();
        long optimizedSolution = maxSideLength.solve("tests/MaxSideLength_Inputs/sameX.txt");
        t2 = System.currentTimeMillis();
        System.out.println("Algorithm Running Time (Small input, Same x-coordinate) = " + (t2 - t1) + " ms.");
        assertEquals(naiveSolution, optimizedSolution);
    }

    @Test
    @DisplayName("Testcase Max Side Length: False Input (1 point only), throws runtime exception.")
    public void test_throwingRuntimeExceptionAtFalseInput_1point(){
        assertThrows(RuntimeException.class,
                () -> maxSideLength.solve("tests/MaxSideLength_Inputs/badInput_small.txt"));
    }

    @Test
    @DisplayName("Testcase Max Side Length: False Input (points duplication), throws runtime exception.")
    public void test_throwingRuntimeExceptionAtFalseInput_duplicates(){
        assertThrows(RuntimeException.class,
                () -> maxSideLength.solve("tests/MaxSideLength_Inputs/badInput_duplicates.txt"));
    }
}

class Utilities {
    public static long naiveApproach(String fileName){
        int[][] points = scanInput(fileName);
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

    public static void generateRandomInput(String fileName, int inputSize){
        try {
            FileWriter writer = new FileWriter(fileName);
            int[][] points = Utilities.generatePoints(inputSize);
            writer.write(points.length + "\n");
            for(int[] point : points)
                writer.write(point[0] + " " + point[1] + "\n");
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

    private static int[][] generatePoints(int numberOfPoints){
        Random rand = new Random();
        Map<Integer, Set<Integer>> pointsMap = new HashMap<>();
        int[][] points = new int[numberOfPoints][2];

        for (int i = 0; i < numberOfPoints; i++) {
            int[] point = new int[2];
            do {
                // Generate a random integer between -10^6 and 10 ^ 6
                point[0] = rand.nextInt(2_000_000_000) - 1_000_000_000;
                point[1] = rand.nextInt(2_000_000_000) - 1_000_000_000;
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

    private static long computeSquareSide(int[][] closestPair){
        return Math.max(
                Math.abs(closestPair[0][0] - closestPair[1][0]),
                Math.abs(closestPair[0][1] - closestPair[1][1])
        );
    }

    private static long computeEuclideanDistance(int[] pt1, int[] pt2){
        return (long) Math.sqrt(Math.pow(pt1[0] - pt2[0], 2) + Math.pow(pt1[1] - pt2[1], 2));
    }

}