import java.util.Random;

/*
 Kth Element Selection Problem:
 Given a collection of unsorted elements (possibly an array), it is required to find the kth smallest element
 among this collection such that, when k = 0, the element is the minimum element in the collection, and when
 k = collection.length() - 1, the element is the maximum element in the collection.
 */
public class KthElementSelector {

    /**
     * This function employs a randomized approach to find the kth smallest element in an array. It calls
     * the 'randomSelect' function, which recursively partitions and selects elements to determine the kth
     * smallest element. The algorithm randomly selects pivot elements to optimize performance.
     * @param elements An unsorted array of unique integers.
     * @param k The rank of the element to be retrieved, 0 based ranking.
     * @return The kth smallest element (element whose rank is k) in the given unsorted array.
     */
    public static int randomizedApproach(int[] elements, int k){
        return randomSelect(elements, 0, elements.length - 1, k);
    }

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

    /*
    Essentially the same functionality of the normal partition() method, it makes use
    of the normal implementation after swapping the first element of the array (at the leftIdx position)
    with an element whose position is randomly selected.
     */
    private static int randomPartition(int[] elements, int leftIdx, int rightIdx){
        Random random = new Random();
        int randomIdx = leftIdx + random.nextInt(rightIdx - leftIdx + 1);
        swap(elements, leftIdx, randomIdx);
        return partition(elements, leftIdx, rightIdx);      // after picking the random pivot, use the normal partition() method.
    }


    // Normal partition() implementation, which uses the first element of the array as a pivot element.
    // Will be utilized by other versions of partitioning methods, such as randomPartition() method.
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

    private static void swap(int[] elements, int i, int j){
        int tempElement = elements[i];
        elements[i] = elements[j];
        elements[j] = tempElement;
    }

    /*
    # TODO: Comment deterministicApproach()
     */
    public static int deterministicApproach(int[] elements, int k){
        return 0;
    }

    /*
    # TODO: Comment naiveApproach()
     */
    public static int naiveApproach(int[] elements, int k){
        return 0;
    }
}
