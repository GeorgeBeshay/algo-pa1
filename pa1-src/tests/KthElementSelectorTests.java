import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Disabled;
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
        int arraySize = rand.nextInt(1000000) + 1; // Random size between 1 and 1,000,000
        Set<Integer> uniqueNumbers = new HashSet<>();
        int[] randomArray = new int[arraySize];

        for (int i = 0; i < arraySize; i++) {
            int randomNumber;
            do {
                randomNumber = rand.nextInt(1000000000); // Generate a random integer between 0 and 1,000,000,000
            } while (uniqueNumbers.contains(randomNumber)); // Continue generating until it's unique
            uniqueNumbers.add(randomNumber);
            randomArray[i] = randomNumber;
        }

        return Stream.of(Arguments.of(randomArray, rand.nextInt(randomArray.length)));
    }

    @ParameterizedTest
    @MethodSource("generateParameters")
    @DisplayName("Testcase: Kth Element Selection - Randomized Approach")
    public void test_randomizedSelection(int[] arr, int randomRank){
        System.out.println(Arrays.toString(arr));
        int actualMedian = KthElementSelector.randomizedApproach(arr,randomRank + 1);       // ranking is 1 based index.
        Arrays.sort(arr);
        int expectedMedian = arr[randomRank];
        assertEquals(expectedMedian, actualMedian);
    }

    @ParameterizedTest
    @MethodSource("generateParameters")
    @DisplayName("Testcase: Kth Element Selection - Deterministic Approach")
    public void test_deterministicSelection(int[] arr, int randomRank){
        System.out.println(Arrays.toString(arr));
        int actualMedian = KthElementSelector.deterministicApproach(arr,randomRank + 1);
        Arrays.sort(arr);
        int expectedMedian = arr[randomRank];
        assertEquals(expectedMedian, actualMedian);
    }

    //    @Disabled
    @ParameterizedTest
    @MethodSource("generateParameters")
    @DisplayName("Testcase: Kth Element Selection - Naive Approach")
    public void test_naiveSelection(int[] arr, int randomRank){
        System.out.println(Arrays.toString(arr));
        int actualMedian = KthElementSelector.naiveApproach(arr,randomRank + 1);
        Arrays.sort(arr);
        int expectedMedian = arr[randomRank];
        assertEquals(expectedMedian, actualMedian);
    }
}
