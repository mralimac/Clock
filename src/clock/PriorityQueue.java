package clock;

/**
 * Definition of the Priority Queue abstract data type.
 *
 * A collection of items, each with an integer priority. The item with the
 * highest priority is the one returned by head() and is the one removed by
 * remove().
 *
 * This is a generic (i.e. parameterised) type. T can be any class.
 *
 * Implementation method is not specified. Implementations may set a maximum
 * allowed number of items, or not.
 * 
 * @param <T> The type of items stored in the queue.
 */
public interface PriorityQueue<T> {

    /**
     * Add the given item to the queue with the given priority. Throw an
     * exception if it's already full to capacity.
     *
     * @param item
     * @param priority
     * @throws QueueOverflowException
     */
    public void add(T item, int priority) throws QueueOverflowException;

    /**
     * The highest priority item stored. Throw an exception if it's empty.
     *
     * @return The item with the highest priority
     * @throws QueueUnderflowException
     */
    public T head() throws QueueUnderflowException;

    /**
     * Remove the highest priority item from the queue. Throw an exception if
     * it's empty.
     *
     * @throws QueueUnderflowException
     */
    public void remove() throws QueueUnderflowException;

    /**
     * Is the queue empty?
     *
     * @return True if there are no items stored, otherwise False
     */
    public boolean isEmpty();
    
    
    public int getSize();

    /**
     *
     * @param index
     * @return
     */
    public T getItemAtIndex(int index);
}
