import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.params.provider.Arguments;
import java.util.Random;

/**
 * This class performs performance analysis for various k-th element selection algorithms implemented.
 */
public class KthSelectorAnalysis {

    /**
     * A functional interface for selector algorithms.
     * Contains only 1 method, which is 'apply()' that takes array of unsorted unique elements and
     * the rank of the element to be found.
     */
    @FunctionalInterface
    interface SelectorAlgorithm {
        int apply(int[] elements, int k);
    }


    /**
     * The main method to execute performance analysis for different scenarios.
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        long[][] performanceAnalysis = analyze(1, 10_000_000, 10, 1_000_000_000, 25);
        displayPerformance(performanceAnalysis, 1, 10);

        System.out.println();

        performanceAnalysis = analyze(1, 10_000_000, 2, 1_000_000_000, 25);
        displayPerformance(performanceAnalysis, 1, 2);
    }

    /**
     * Display the performance results in a tabular form, to be easily copied to a Google sheet.
     * @param performance The performance data to display.
     * @param initialSize The initial size for the array of elements.
     * @param sizeScalingFactor The size scaling factor, which will be multiplied by the size after each iteration.
     */
    private static void displayPerformance(long[][] performance, int initialSize, int sizeScalingFactor){
        for(int i = 0 ; i < performance[0].length ; i++){
            System.out.println(
                    (int)(initialSize * Math.pow(sizeScalingFactor, i))
                        + "\t"
                        + performance[0][i] + "\t"
                        + performance[1][i] + "\t"
                        + performance[2][i] + "\t"
            );
        }
    }

    /**
     * Perform performance analysis for various k-th element selection algorithms.
     * @param initialSize The initial size for the array of elements.
     * @param maxSize The maximum size for the array of elements.
     * @param sizeScalingFactor The size scaling factor for the array of elements.
     * @param valuesRange The range of values for random data generation.
     * @param samplesPerSize The number of samples to run for each size.
     * @return A 2D array containing performance data, where the 1st row represents the results for the naive approach
     * and the data in the 2nd row represents the results of the randomized approach, and the third row
     * contains the results of the deterministic approach.
     */
    private static long[][] analyze(int initialSize, int maxSize, int sizeScalingFactor, int valuesRange, int samplesPerSize){
        // 3 rows for the 3 approaches
        int sizesCount = (int) Math.floor(Math.log10(maxSize) / Math.log10(sizeScalingFactor)) + 1;
        long[][] performanceAnalysis = new long[3][sizesCount];

        // methodCode represents Naive, Randomized and Deterministic approaches, respectively.
        for(int methodCode = 0 ; methodCode < 3 ; methodCode++) {
            // Store the approach to be used in the current iteration (represented by the methodCode variable)
            SelectorAlgorithm tempAlg = getSelectorAlgorithm(methodCode);
            for (int size = initialSize; size <= maxSize; size *= sizeScalingFactor) {
                Arguments args = generateParameters(size, valuesRange);
                long avgTime = runMultipleSamples(tempAlg, samplesPerSize, args, size);
                performanceAnalysis[methodCode][(int) Math.floor(Math.log10(size) / Math.log10(sizeScalingFactor))] = avgTime;
            }
        }

        return performanceAnalysis;
    }

    /**
     * Run the selector algorithm multiple times on the same array and calculate the average time.
     * @param alg The selector algorithm to run.
     * @param samplesPerSize The number of samples to run for each size.
     * @param args The arguments for the algorithm.
     * @param size The size of the array of elements.
     * @return The average time taken to run the algorithm.
     */
    private static long runMultipleSamples(SelectorAlgorithm alg, int samplesPerSize, Arguments args, int size){
        long avgTime = 0;
        for(int sample = 0 ; sample < samplesPerSize ; sample++){
            int[] tempCopy = Arrays.copyOf((int[])args.get()[0], size);
            long startTime = System.currentTimeMillis();
            alg.apply(tempCopy, (int) args.get()[1]);
            long finishTime = System.currentTimeMillis();
            avgTime += (finishTime - startTime);
        }
        avgTime /= samplesPerSize;
        return avgTime;
    }

    /**
     * Get the appropriate selector algorithm based on the method code.
     * @param methodCode The method code representing the algorithm.
     * @return The selector algorithm.
     */
    private static SelectorAlgorithm getSelectorAlgorithm(int methodCode){
        switch (methodCode) {
            case 0:
                return KthElementSelector::naiveApproach;
            case 1:
                return KthElementSelector::randomizedApproach;
            case 2:
                return KthElementSelector::deterministicApproach;
            default:
                break;
        }
        throw new RuntimeException("Method Code is not valid.");
    }

    /**
     * Generate random parameters for the analysis.
     * @param arraySize The size of the array.
     * @param range The range of values for random data generation.
     * @return Arguments containing the random array and the random rank value k.
     */
    private static Arguments generateParameters(int arraySize, int range){
        Random rand = new Random();
        Set<Integer> uniqueNumbers = new HashSet<>();
        int[] randomArray = new int[arraySize];

        for (int i = 0; i < arraySize; i++) {
            int randomNumber;

            do {
                // Generate a random integer between -range and range
                randomNumber = rand.nextInt(2 * range) - range;
            } while (uniqueNumbers.contains(randomNumber)); // Continue generating until it's unique

            uniqueNumbers.add(randomNumber);
            randomArray[i] = randomNumber;
        }

        return (Arguments.of(randomArray, rand.nextInt(randomArray.length) + 1));
    }
}
