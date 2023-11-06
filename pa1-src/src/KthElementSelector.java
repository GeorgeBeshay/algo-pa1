import java.util.Random;

public class KthElementSelector {
    /*
     Kth Element Selection Problem:
     Given a collection of unsorted elements (possibly an array), it is required to find the kth smallest element
     among this collection such that, when k = 0, the element is the minimum element in the collection, and when
     k = collection.length() - 1, the element is the maximum element in the collection.
     */

    /*
    # TODO: Comment randomizedApproach()
     */
    public static int randomizedApproach(int[] elements, int k){
        return randomSelect(elements, 0, elements.length - 1, k);
    }

    private static int randomSelect(int[] elements, int leftIdx, int rightIdx, int k){
        // base case
        if(leftIdx == rightIdx)
            return elements[leftIdx];


        return 0;
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
        return partition(elements, leftIdx, rightIdx);
    }

    private static void swap(int[] elements, int i, int j){
        int tempElement = elements[i];
        elements[i] = elements[j];
        elements[j] = tempElement;
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
