import java.util.Arrays;
import java.util.Random;

/**
 * Kth Element Selection Problem:
 *  Given a collection of unsorted elements (possibly an array), it is required to find the kth smallest element
 *  among this collection such that, when k = 0, the element is the minimum element in the collection, and when
 *  k = collection.length() - 1, the element is the maximum element in the collection.
 */
public class KthElementSelector {

    // --------------------------- Kth Element Selection: Randomized Approach ---------------------------
    /**
     * This function employs a randomized approach to find the kth smallest element in an array. It calls
     * the 'randomSelect' function, which recursively partitions and selects elements to determine the kth
     * smallest element. The algorithm randomly selects pivot elements to optimize performance.
     * @param elements An unsorted array of unique integers.
     * @param k The rank of the element to be retrieved, 1 based ranking.
     * @return The kth smallest element (element whose rank is k) in the given unsorted array.
     */
    public static int randomizedApproach(int[] elements, int k){
        return randomSelect(elements, 0, elements.length - 1, k);
    }

    /**
     * The recursive method, that will be delegated to from the public method randomizedApproach()
     * to find the kth smallest element among an array of unsorted unique integers.
     * @param elements An unsorted array of unique integers.
     * @param leftIdx The left index of the partition, upon which the algorithm is currently working on.
     * @param rightIdx The right index of the partition, upon which the algorithm is currently working on.
     * @param k The rank of the element to be found.
     * @return The element's (of rank k) value.
     */
    private static int randomSelect(int[] elements, int leftIdx, int rightIdx, int k){
        // base case
        if(leftIdx == rightIdx)
            return elements[leftIdx];

        int pivotIdx = randomPartition(elements, leftIdx, rightIdx);
        int pivotRank = pivotIdx - leftIdx + 1;
        // found the kth element
        if(k == pivotRank)
            return elements[pivotIdx];
        // recursive cases
        if(k < pivotRank)
            return randomSelect(elements, leftIdx, pivotIdx - 1, k);
        else
            return randomSelect(elements, pivotIdx + 1, rightIdx, k - pivotRank);
    }

    /**
     * This function essentially provides the same functionality of the normal partition() method, it makes use
     * of the normal implementation after swapping the first element of the array (at the leftIdx position)
     * with an element whose position is randomly selected.
     * @param elements An unsorted array of unique integers.
     * @param leftIdx The left index of the partition, upon which the algorithm is currently working on.
     * @param rightIdx The right index of the partition, upon which the algorithm is currently working on.
     * @return The pivot's (randomly selected) index, after being settled in its correct position.
     */
    private static int randomPartition(int[] elements, int leftIdx, int rightIdx){
        Random random = new Random();
        int randomIdx = leftIdx + random.nextInt(rightIdx - leftIdx + 1);
        swap(elements, leftIdx, randomIdx);
        return partition(elements, leftIdx, rightIdx);      // after picking the random pivot, use the normal partition() method.
    }

    // --------------------------- Kth Element Selection: Deterministic Approach ---------------------------
    /**
     * This function employs a deterministic approach to find the kth smallest element in an array, using the median of medians algorithm
     * that selects an array median in linear time.
     * @param elements An unsorted array of unique integers.
     * @param k The rank of the element to be retrieved, 1 based ranking.
     * @return The kth smallest element (element whose rank is k) in the given unsorted array.
     */
    public static int deterministicApproach(int[] elements, int k){
        return deterministicSelect(elements, 0, elements.length - 1, k);
    }

    /**
     * The recursive method used to find the kth smallest element among an array of unsorted unique integers.
     * @param elements An unsorted array of unique integers.
     * @param leftIdx The left index of the partition, upon which the algorithm is currently working on.
     * @param rightIdx The right index of the partition, upon which the algorithm is currently working on.
     * @param k The rank of the element to be found.
     * @return The element's (of rank k) value.
     */
    private static int deterministicSelect(int[] elements, int leftIdx, int rightIdx, int k){
        // base case
        if(leftIdx == rightIdx)
            return elements[leftIdx];

        int[] medians = getMedians(elements, leftIdx, rightIdx);        // get the partition medians (after dividing the partition to groups of 5)
        int medianOfMediansValue = deterministicApproach(medians, (int)Math.ceil(medians.length / 2.0));    // get recursively the medians median
        int medianOfMediansIdx = getElementIdx(elements, leftIdx, rightIdx, medianOfMediansValue);          // compute the median index (that falls in this partition)
        swap(elements, medianOfMediansIdx, leftIdx);                    // swap the median computed, with the most left element of the partition

        int pivotIdx = partition(elements, leftIdx, rightIdx);          // compute the median index after being inserted into its correct position
        int pivotRank = pivotIdx - leftIdx + 1;                         // compute the rank relative to the current partition

        if(k == pivotRank)
            return elements[pivotIdx];      // kth element found
        if(pivotRank > k)                   // kth element is in the left subarray of this partition
            return deterministicSelect(elements, leftIdx, pivotIdx - 1, k);
        else                                // kth element is in the right subarray of this partition
            return deterministicSelect(elements, pivotIdx + 1, rightIdx, k - pivotRank);
        // k = k - pivotRank, because we will process a new subarray of this partition, so the rank
        // of the kth element, must be relative to its starting position.
    }

    /**
     * Calculates the medians of groups of elements within the specified range (the array partition).
     * @param elements An unsorted array of unique integers.
     * @param leftIdx The left index of the partition.
     * @param rightIdx The right index of the partition.
     * @return An array containing the medians of groups of elements found in the specified partition.
     */
    private static int[] getMedians(int[] elements, int leftIdx, int rightIdx){
        int[] medians = new int[getNumberOfGroups(leftIdx, rightIdx)];
        for(int i = leftIdx ; i <= rightIdx ; i += 5)      // jump 5 positions, in each iteration (group size)
            medians[(i - leftIdx) / 5] = getMedian(elements, i, rightIdx);      // insert the group median, into the medians array
        return medians;
    }

    /**
     * Calculates the number of groups of 5 elements within the specified partition.
     * @param leftIdx The left index of the partition.
     * @param rightIdx The right index of the partition.
     * @return The number of groups of 5 elements.
     */
    private static int getNumberOfGroups(int leftIdx, int rightIdx){
        return (int) Math.ceil((rightIdx - leftIdx + 1) / 5.0);
    }

    /**
     * Calculates the median of a group of elements within the specified range.
     * @param elements An unsorted array of unique integers.
     * @param startIdx The starting index of the group.
     * @param maxIdx The maximum index within the group. (keeping in mind that we are working on groups of 5s)
     * @return The median value of the group.
     */
    private static int getMedian(int[] elements, int startIdx, int maxIdx){
        int[] groupElements = new int[Math.min(5, maxIdx - startIdx + 1)];       // Array is initialized with 0 values.

        // fetch the group elements
        System.arraycopy(elements, startIdx, groupElements, 0, groupElements.length);
        // sort the group elements
        Arrays.sort(groupElements);

        return groupElements[(int)(Math.ceil(groupElements.length / 2.0) - 1)];
    }

    /**
     * Finds the index of a specific element within the given subarray.
     * @param elements An unsorted array of unique integers.
     * @param leftIdx The left index of the subarray.
     * @param rightIdx The right index of the subarray.
     * @param element The element, whose index is to be found.
     * @return The index of the element within the subarray.
     * @throws RuntimeException if the element is not found in the specified subarray.
     */
    private static int getElementIdx(int[] elements, int leftIdx, int rightIdx, int element){
        for(int i = leftIdx ; i <= rightIdx ; i++)
            if(elements[i] == element)
                return i;
        // in case that the given element was not found, this indicates a wrong algorithm implementation
        // as the median value calculated (median of medians), can't be found in the given subarray.
        throw new RuntimeException("Element was not found in the given subarray.");
    }

    // --------------------------- Kth Element Selection: Naive Approach ---------------------------

    /**
     * This function employs a naive approach to find the kth smallest element in an array.
     * It sorts the array of elements
     * @param elements An unsorted array of unique integers.
     * @param k The rank of the element to be retrieved, 1 based ranking.
     * @return The kth smallest element (element whose rank is k) in the given unsorted array.
     */
    public static int naiveApproach(int[] elements, int k){
        // utilizing the java built in Arrays.sort() method which works in O(n log n).
        // sorting inplace, meaning that the elements array will be sorted calling this function.
        Arrays.sort(elements);
        return elements[k - 1];     // subtracting 1, because the rank is 1 index based.
    }

    // --------------------------- Utilities methods to be used by the primary methods ---------------------------

    /**
     * Normal partition() implementation, which uses the first element of the array as a pivot element.
     * And will also be utilized by other versions of partitioning methods, such as randomPartition() method.
     * @param elements An unsorted array of unique integers.
     * @param leftIdx The left index of the partition, upon which the algorithm is currently working on.
     * @param rightIdx The right index of the partition, upon which the algorithm is currently working on.
     * @return The pivot's (the left boundary of the partition being processed) index, after being settled in its correct position.
     */
    private static int partition(int[] elements, int leftIdx, int rightIdx){
        int pivot = elements[leftIdx];
        int i = leftIdx;     // use this variable to keep track of the pivot correct position
        for(int j = leftIdx + 1 ; j <= rightIdx ; j++) {
            if (elements[j] <= pivot) {
                // finding an element smaller than pivot, means that the pivot stored position should
                // be incremented, as 1 more element will be stored before it.
                i++;
                swap(elements, i, j);
                // now swap the element that is smaller than the pivot, to put it in the partition set which
                // is smaller than the pivot (will be on the pivot's left).
            }
        }
        swap(elements, i, leftIdx);
        return i;
    }

    /**
     * Provides the facility of swapping 2 elements, in an array of integers, taking the elements (to be swapped) indices.
     * @param elements An unsorted array of unique integers.
     * @param i The index of the first element to be swapped.
     * @param j The index of the second element to be swapped.
     */
    private static void swap(int[] elements, int i, int j){
        int tempElement = elements[i];
        elements[i] = elements[j];
        elements[j] = tempElement;
    }
}
