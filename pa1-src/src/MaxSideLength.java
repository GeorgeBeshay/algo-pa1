import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Given a set of two-dimensional points, the goal is to compute the maximum side length  of the
 * square that can be drawn around each point, such that no two squares intersect, except at the sides.
 * Notes:
 *  - Each point will be at the center of the surrounding square.
 *  - The sides of the squares can only be horizontal and vertical.
 */
public class MaxSideLength {
    /**
     * Main method that demonstrates the usage of this class.
     * @param args Command-line arguments (not used in this example).
     */
    public static void main(String[] args) {
        if(args.length < 1)
            throw new RuntimeException("Input file was not passed as an argument.");
        MaxSideLength maxSideLength = new MaxSideLength();
        System.out.println(maxSideLength.solve(args[0]));
    }

    /**
     * Solves the problem of finding the maximum side length of squares around a set of points.
     * Such that no two squares intersect, and all the points are at the center of their squares.
     * @param inputFile The path to the input file containing the point coordinates.
     * @return The maximum side length of squares around the points.
     */
    public long solve(String inputFile){
        int[][] xPoints = scanInput(inputFile);                         // points sorted by the x-coordinate
        int[][] yPoints = Arrays.copyOf(xPoints, xPoints.length);       // points sorted by the y-coordinate

        // sort in O(n log n);
        Arrays.sort(xPoints, Comparator.comparingInt((int[] point) -> point[0]).thenComparingInt(point -> point[1]));        // sort by the x-coordinate
        Arrays.sort(yPoints, Comparator.comparingInt(point -> point[1]));        // sort by the y-coordinate

        // compute the nearest pair of points
        int[][] closestPair = findClosestPair(xPoints, yPoints, 0, xPoints.length - 1);

        // compute the surrounding square minimum distance
        return computeSquareSide(closestPair);
    }

    /**
     * Recursively finds the closest pair of points in the xPoints array using the divide and conquer algorithm.
     * @param xPoints 2D array of points sorted by the x-coordinate.
     * @param yPoints 2D array of points sorted by the y-coordinate.
     * @param leftIdx Index of the left boundary in the current search interval in the xPoints array.
     * @param rightIdx Index of the right boundary in the current search interval in the yPoints array.
     * @return A 2D array containing the closest pair of points.
     */
    private int[][] findClosestPair(int[][] xPoints, int[][] yPoints, int leftIdx, int rightIdx){
        // base case
        if(rightIdx - leftIdx + 1 <= 3)
            return baseCase(xPoints, leftIdx, rightIdx);

        // divide
        int partitionPointIndex = leftIdx + ((rightIdx - leftIdx) / 2);     // the index of the point at which the partition is being made.
        int verticalLineValue = xPoints[partitionPointIndex][0];            // the x-coordinate value of the partitioning point (vertical line)
        int[][] yPointsLeft = getNewYs(yPoints, xPoints[partitionPointIndex], true);
        int[][] yPointsRight = getNewYs(yPoints, xPoints[partitionPointIndex], false);

        // conquer
        int[][] leftNearestPoints = findClosestPair(xPoints, yPointsLeft, leftIdx, partitionPointIndex);
        int[][] rightNearestPoints = findClosestPair(xPoints, yPointsRight, partitionPointIndex + 1, rightIdx);

        // combine
        int[][] nearestPoints = getMinOfPoints(leftNearestPoints, rightNearestPoints);
        // points that fall in 2 * delta around the vertical line (delta in each side) SORTED BY THE Y-COORDINATE
        int[][] pointsInStripe = getPointsInStripe(yPoints, verticalLineValue, computeEuclideanDistance(nearestPoints));
        int[][] nearestPointsInStripe = getNearestPointsInStripe(pointsInStripe);
        if(nearestPointsInStripe != null
                && computeEuclideanDistance(nearestPointsInStripe) < computeEuclideanDistance(nearestPoints)) {
            System.arraycopy(nearestPointsInStripe[0], 0, nearestPoints[0], 0, 2);
            System.arraycopy(nearestPointsInStripe[1], 0, nearestPoints[1], 0, 2);
        }

        return nearestPoints;
    }

    /**
     * Computes the side length of the square formed by the closest pair of points.
     * Which is essentially the maximum difference between either the x-coordinates or the y-coordinates
     * of those points.
     * @param closestPair A 2D array containing the closest pair of points.
     * @return The side length of the square around the closest pair of points.
     */
    private long computeSquareSide(int[][] closestPair){
        return Math.max(
                Math.abs(closestPair[0][0] - closestPair[1][0]),
                Math.abs(closestPair[0][1] - closestPair[1][1])
        );
    }

    /**
     * Gets the nearest pair of points within a stripe of points based on their Euclidean distance.
     * @param pointsInStripe A 2D array containing points within the stripe of width 2 * delta.
     * @return A 2D array containing the nearest pair of points within the strip, or null if there are fewer than 2 points.
     */
    private int[][] getNearestPointsInStripe(int[][] pointsInStripe){
        if(pointsInStripe.length < 2)
            return null;
        int[][] points = new int[2][2];
        long smallestDistance = Long.MAX_VALUE;
        for(int i = 0 ; i < pointsInStripe.length ; i++){
            for(int j = i + 1 ; j < Math.min(pointsInStripe.length, i + 7) ; j++){
                long tempDistance = computeEuclideanDistance(pointsInStripe[i], pointsInStripe[j]);
                if(tempDistance < smallestDistance) {
                    points[0][0] = pointsInStripe[i][0];
                    points[0][1] = pointsInStripe[i][1];
                    points[1][0] = pointsInStripe[j][0];
                    points[1][1] = pointsInStripe[j][1];
                    smallestDistance = tempDistance;
                }
            }
        }
        return points;
    }

    /**
     * Filters and extracts points from the input 2D array 'yPoints' based on whether they belong to the specified partition.
     * Points that meet the criteria (in the left or right partition) are extracted and returned as a new 2D array.
     * @param yPoints          The 2D array of points sorted by the y-coordinate.
     * @param partitioningPoint The point used for partitioning.
     * @param inLeft           A boolean flag indicating whether to include points in the left partition or
     *                         not (so to include in the right partition).
     * @return A new 2D array containing the points that belong to the specified partition.
     */
    private int[][] getNewYs(int[][] yPoints, int[] partitioningPoint, boolean inLeft){
        int count = 0;

        for(int[] point : yPoints)
            if (isInSpecifiedPartition(point, partitioningPoint, inLeft))
                count++;

        int[][] newPoints = new int[count][2];
        int tempIndex = 0;
        for(int[] point : yPoints)
            if (isInSpecifiedPartition(point, partitioningPoint, inLeft)) {
                newPoints[tempIndex][0] = point[0];
                newPoints[tempIndex++][1] = point[1];
            }

        return newPoints;
    }

    /**
     * Checks whether a given point belongs to the specified partition based on the partitioning point and direction.
     * @param point             The point to check, represented as a 2D array [x, y].
     * @param partitioningPoint The point used for partitioning, represented as a 2D array [x, y].
     * @param inLeft            A boolean flag indicating whether to check for inclusion in the left partition.
     * @return True if the point belongs to the specified partition, false otherwise.
     */
    private boolean isInSpecifiedPartition(int[] point, int[] partitioningPoint, boolean inLeft){
        return (
                inLeft &&
                        ((point[0] < partitioningPoint[0]) || ((point[0] == partitioningPoint[0]) && (point[1] <= partitioningPoint[1])))
        ) || (!inLeft &&
                ((point[0] > partitioningPoint[0]) || ((point[0] == partitioningPoint[0]) && (point[1] > partitioningPoint[1])))
        );
    }

    /**
     * Filters and extracts points within a specified delta range around a vertical line from the input 2D array 'yPoints'.
     * @param yPoints      The 2D array of points sorted by the y-coordinate.
     * @param verticalLine The x-coordinate of the vertical line.
     * @param delta        The maximum distance from the vertical line to include points.
     * @return A new 2D array containing the points that fall within the specified delta range around the vertical line.
     */
    private int[][] getPointsInStripe(int[][] yPoints, long verticalLine, long delta){
        int pointsCount = 0;
        for (int[] point : yPoints) {
            if (point[0] <= verticalLine + delta && point[0] >= verticalLine - delta)
                pointsCount++;
        }

        int[][] points = new int[pointsCount][2];
        int tempIndex = 0;
        for (int[] yPoint : yPoints) {
            if (yPoint[0] <= verticalLine + delta && yPoint[0] >= verticalLine - delta) {
                points[tempIndex][0] = yPoint[0];
                points[tempIndex++][1] = yPoint[1];
            }
        }

        return points;
    }

    /**
     * Compares two pairs of points and returns the pair with the smaller Euclidean distance.
     * @param leftPoints  A 2D array representing the first pair of points as [point1, point2].
     * @param rightPoints A 2D array representing the second pair of points as [point1, point2].
     * @return The pair of points with the smaller Euclidean distance.
     */
    private int[][] getMinOfPoints(int[][] leftPoints, int[][] rightPoints){
        if(computeEuclideanDistance(leftPoints[0], leftPoints[1]) <=
                computeEuclideanDistance(rightPoints[0], rightPoints[1]))
            return leftPoints;
        else
            return rightPoints;
    }

    /**
     * Find the closest pair of points in a small set of points (where n is <= 3) using a brute-force approach.
     * @param xPoints 2D array of points sorted by the x-coordinate.
     * @param leftIdx Index of the left boundary in the current search interval.
     * @param rightIdx Index of the right boundary in the current search interval.
     * @return A 2D array containing the closest pair of points within the specified range.
     * @throws RuntimeException if the base case requires at least 2 points.
     */
    private int[][] baseCase(int[][] xPoints, int leftIdx, int rightIdx){
        if(rightIdx - leftIdx + 1 < 2)
            throw new RuntimeException("Base Case Requires 2 points at least.");
        int[][] nearestPoints = new int[2][2];
        long smallestDistance = Long.MAX_VALUE;

        for(int i = leftIdx ; i < rightIdx ; i++) {
            for (int j = leftIdx + 1; j <= rightIdx; j++) {
                if (i == j)
                    continue;

                long tempDistance = computeEuclideanDistance(xPoints[i], xPoints[j]);
                if (tempDistance < smallestDistance) {
                    nearestPoints[0][0] = xPoints[i][0];
                    nearestPoints[0][1] = xPoints[i][1];
                    nearestPoints[1][0] = xPoints[j][0];
                    nearestPoints[1][1] = xPoints[j][1];
                    smallestDistance = tempDistance;
                }
            }
        }

        return nearestPoints;
    }

    /**
     * Computes the Euclidean distance between two points represented as 2D arrays of integers.
     * @param pt1 The first point represented as an array [x, y].
     * @param pt2 The second point represented as an array [x, y].
     * @return The Euclidean distance between the two points in the 2D space.
     */
    private long computeEuclideanDistance(int[] pt1, int[] pt2){
        return (long) Math.sqrt(Math.pow(pt1[0] - pt2[0], 2) + Math.pow(pt1[1] - pt2[1], 2));
    }

    /**
     * Computes the Euclidean distance between two points represented as arrays in a 2D space.
     * @param points A 2D array containing two points as [point1, point2], where each point is represented as an array [x, y].
     * @return The Euclidean distance between the two points in the 2D space.
     */
    private long computeEuclideanDistance(int[][] points){
        return computeEuclideanDistance(points[0], points[1]);
    }

    /**
     * Reads and parses input data from a text file, extracting an array of 2D points.
     * @param inputFile The path to the input text file containing point data.
     * @return A 2D array of points, where each point is represented as an array [x, y].
     * @throws RuntimeException if the input file is not found.
     */
    private static int[][] scanInput(String inputFile){
        int[][] points;
        try {
            File file = new File(inputFile);
            Scanner scanner = new Scanner(file);

            int numberOfPoints = scanner.nextInt();
            points = new int[numberOfPoints][2];

            // to check that the input contains no duplicates
            HashMap<Integer, Set<Integer>> duplicatesCheck = new HashMap<>();

            for(int i = 0 ; i < numberOfPoints ; i++){
                points[i][0] = scanner.nextInt();
                points[i][1] = scanner.nextInt();
                checkInput(points[i], duplicatesCheck);
            }

            return points;
        } catch (FileNotFoundException E){
            throw new RuntimeException("Input file wasn't found.");
        }
    }

    /**
     * Checks the validity of a 2D point and ensures that there are no duplicates.
     * @param point The 2D point to be checked, represented as an array [x, y].
     * @param duplicatesCheck A HashMap used to track and check for duplicate points.
     * @throws RuntimeException if the input point is invalid or if a duplicate point is detected.
     */
    private static void checkInput(int[] point, HashMap<Integer, Set<Integer>> duplicatesCheck){
        if(duplicatesCheck.containsKey(point[0])
                && duplicatesCheck.get(point[0]).contains(point[1]))
            throw new RuntimeException("Invalid Input, duplicates are not allowed");
        if(duplicatesCheck.containsKey(point[0]))
            duplicatesCheck.get(point[0]).add(point[1]);
        else {
            HashSet<Integer> tempSet = new HashSet<>();
            tempSet.add(point[1]);
            duplicatesCheck.put(point[0], tempSet);
        }
    }
}