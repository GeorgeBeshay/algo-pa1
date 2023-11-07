import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class KthElementSelectorTests {

    @AfterAll
    public static void terminationProcedure(){
        System.out.println("All test cases are done.");
    }

    static Stream<Arguments> generateParameters() {
        Random rand = new Random();
        int arraySize = rand.nextInt(10_000_000) + 1; // Random size between 1 and 10 ^ 7
        Set<Integer> uniqueNumbers = new HashSet<>();
        int[] randomArray = new int[arraySize];

        for (int i = 0; i < arraySize; i++) {
            int randomNumber;
            do {
                // Generate a random integer between -10^9 and 10 ^ 9
                randomNumber = rand.nextInt(2_000_000_000) - 1_000_000_000;
            } while (uniqueNumbers.contains(randomNumber)); // Continue generating until it's unique
            uniqueNumbers.add(randomNumber);
            randomArray[i] = randomNumber;
        }

        return Stream.of(Arguments.of(randomArray, rand.nextInt(randomArray.length) + 1));
    }

    @ParameterizedTest
    @MethodSource("generateParameters")
    @DisplayName("Testcase: Kth Element Selection - Randomized Approach")
    public void test_randomizedSelection(int[] arr, int randomRank){
        int actualMedian = KthElementSelector.randomizedApproach(arr,randomRank);       // ranking is 1 based index.
        Arrays.sort(arr);
        int expectedMedian = arr[randomRank - 1];
        assertEquals(expectedMedian, actualMedian);
    }

    @ParameterizedTest
    @MethodSource("generateParameters")
    @DisplayName("Testcase: Kth Element Selection - Deterministic Approach")
    public void test_deterministicSelection(int[] arr, int randomRank){
        int actualMedian = KthElementSelector.deterministicApproach(arr,randomRank);
        Arrays.sort(arr);
        int expectedMedian = arr[randomRank - 1];
        assertEquals(expectedMedian, actualMedian);
    }

    @ParameterizedTest
    @MethodSource("generateParameters")
    @DisplayName("Testcase: Kth Element Selection - Naive Approach")
    public void test_naiveSelection(int[] arr, int randomRank){
        int actualMedian = KthElementSelector.naiveApproach(arr,randomRank);
        Arrays.sort(arr);
        int expectedMedian = arr[randomRank - 1];
        assertEquals(expectedMedian, actualMedian);
    }
}
