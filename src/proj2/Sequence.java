package proj2;  // Gradescope needs this.
/**
 * @author Zuhair AlMassri
 *         4/17/23
 *
 *  Sequence ADT. An ordered collection of String objects with a "current" item
 *  Sequence capacity is the length of contents array, and has elements in contents[0, ....., size-1]
 *                                                         in contents[size, ..., capacity-1] stores null (no stored accessible elements)
 *  currentIndex (int) stores the index of the "current" element at contents[currentIndex] iff there is a current element
 *  size (int) stores the number of elements stored in Sequence
 *  hasCurrent (boolean) True iff sequence has current item
 */
public class Sequence
{
    private String[] contents;
    private int size;
    private int currentIndex;
    private boolean hasCurrent;
    private final static int DEFAULT_CAPACITY = 10;
    private final static int NO_CURRENT = -1;
    private final static int INCREMENT = 1;
    private final static int DECREMENT = -1;

    /**
     * Creates a new sequence with initial capacity 10.
     */
    public Sequence() {
        this.contents = new String[DEFAULT_CAPACITY];
        this.size = 0;
        this.currentIndex = NO_CURRENT;
        this.hasCurrent = false;
    }

    /**
     * Creates a new sequence.
     *
     * @param initialCapacity the initial capacity of the sequence.
     */
    public Sequence(int initialCapacity){
        this.contents = new String[initialCapacity];
        this.size = 0;
        this.currentIndex = NO_CURRENT;
        this.hasCurrent = false;
    }

    /**
     * Adds a string to the sequence in the location before the
     * current element. If the sequence has no current element, the
     * string is added to the beginning of the sequence.
     *
     * The added element becomes the current element.
     *
     * If the sequences's capacity has been reached, the sequence will
     * expand to twice its current capacity plus 1.
     *
     * @param value the string to add.
     */
    public void addBefore(String value)
    {
        doubleCapacity();
        if(!isCurrent() && isEmpty()) {
            setElem(value, 0);
            setCurrentIndex(0);
        } else if (isCurrent() && !isEmpty()){
            addBeforeIndex(getCurrentIndex(), value);
        } else if(!isCurrent() && !isEmpty()){
            addBeforeIndex(0, value);
            setCurrentIndex(0);
        }
        incDecSize(INCREMENT);
        isCurrentAvailable();
    }

    /**
     * Adds a string to the sequence in the location after the current
     * element. If the sequence has no current element, the string is
     * added to the end of the sequence.
     *
     * The added element becomes the current element.
     *
     * If the sequences's capacity has been reached, the sequence will
     * expand to twice its current capacity plus 1.
     *
     * @param value the string to add.
     */
    public void addAfter(String value)
    {
        doubleCapacity();
        if(!isCurrent() && isEmpty()) {
            setElem(value, size());
            setCurrentIndex(size());
        } else if (isCurrent() && !isEmpty()) {
            addAfterIndex(getCurrentIndex(), value);
            setCurrentIndex(getCurrentIndex()+1);
        } else if(!isCurrent() && size() != 0){
            setElem(value, size());
            setCurrentIndex(size());
        }
        incDecSize(INCREMENT);
        isCurrentAvailable();
    }

    /**
     * @return true if and only if the sequence has a current element.
     */
    public boolean isCurrent()
    {
        return hasCurrent;
    }

    /**
     * @return the capacity of the sequence.
     */
    public int getCapacity()
    {
        return contents.length;
    }

    /**
     * @return the element at the current location in the sequence, or
     * null if there is no current element.
     */
    public String getCurrent()
    {
        if(isCurrent()){
            return getElement(getCurrentIndex());
        } else{
            return null;
        }
    }

    /**
     * Increase the sequence's capacity to be
     * at least minCapacity.  Does nothing
     * if current capacity is already >= minCapacity.
     *
     * @param minCapacity the minimum capacity that the sequence
     * should now have.
     */
    public void ensureCapacity(int minCapacity)
    {
        if(getCapacity() < minCapacity){
            String[] biggerContents = new String[minCapacity];
            for (int i =0; i < size(); i++){
                biggerContents[i] = this.getElement(i);
            }
            replaceContents(biggerContents);
        }
    }

    /**
     * Places the contents of another sequence at the end of this sequence.
     *
     * If adding all elements of the other sequence would exceed the
     * capacity of this sequence, the capacity is changed to make (just enough) room for
     * all of the elements to be added.
     *
     * Postcondition: NO SIDE EFFECTS!  the other sequence should be left
     * unchanged.  The current element of both sequences should remain
     * where they are. (When this method ends, the current element
     * should refer to the same element that it did at the time this method
     * started.)
     *
     * @param another the sequence whose contents should be added.
     */
    public void addAll(Sequence another)
    {
        Sequence anotherClone = another.clone();
        this.ensureCapacity(anotherClone.size() + this.size());
        for (int i = 0; i < anotherClone.size(); i++){
            this.setElem(anotherClone.getElement(i), size());
            incDecSize(INCREMENT);
        }
        if((anotherClone.size() + this.size()) > this.getCapacity()){
            this.trimToSize();
        }
    }

    /**
     * Move forward in the sequence so that the current element is now
     * the next element in the sequence.
     *
     * If the current element was already the end of the sequence,
     * then advancing causes there to be no current element.
     *
     * If there is no current element to begin with, do nothing.
     */
    public void advance()
    {
        if(isCurrent()){
            if(reachedCapacity()){
                setCurrentIndex(NO_CURRENT);
            }else if(!reachedCapacity()){
                setCurrentIndex(getCurrentIndex()+1);
            }
            isCurrentAvailable();
        }
    }

    /**
     * Make a copy of this sequence.  Subsequence changes to the copy
     * do not affect the current sequence, and vice versa.
     *
     * Postcondition: NO SIDE EFFECTS!  This sequence's current
     * element should remain unchanged.  The clone's current
     * element will correspond to the same place as in the original.
     *
     * @return the copy of this sequence.
     */
    public Sequence clone()
    {
        Sequence clone = new Sequence(this.getCapacity());
        for(int index = 0; index < size(); index++){
            clone.addAfter(this.getElement(index));
        }
        clone.setCurrentIndex(this.getCurrentIndex());
        return clone;
    }

    /**
     * Remove the current element from this sequence.  The following
     * element, if there was one, becomes the current element.  If
     * there was no following element (current was at the end of the
     * sequence), the sequence now has no current element.
     *
     * If there is no current element, does nothing.
     */
    public void removeCurrent()
    {
        if(isCurrent()){
            for(int i = getCurrentIndex(); i < size(); i++){
                swap(i, i + 1);
                }
            if(getCurrent() == null){
                setCurrentIndex(NO_CURRENT);
            }
            incDecSize(DECREMENT);
            isCurrentAvailable();
        }
    }

    /**
     * @return the number of elements stored in the sequence.
     */
    public int size()
    {
        return this.size;
    }

    /**
     * Sets the current element to the start of the sequence.  If the
     * sequence is empty, the sequence has no current element.
     */
    public void start()
    {
        if(!isEmpty()){
            setCurrentIndex(0);
        }
        isCurrentAvailable();
    }

    /**
     * Reduce the current capacity to its actual size, so that it has
     * capacity to store only the elements currently stored.
     */
    public void trimToSize()
    {
        String[] trimmed = new String[size()];
        for(int index = 0; index < size(); index++){
            trimmed[index] = this.getElement(index);
        }
        replaceContents(trimmed);

    }

    /**
     * Produce a string representation of this sequence.  The current
     * location is indicated by a >.  For example, a sequence with "A"
     * followed by "B", where "B" is the current element, and the
     * capacity is 5, would print as:
     *
     *    {A, >B} (capacity = 5)
     *
     * The string you create should be formatted like the above example,
     * with a comma following each element, no comma following the
     * last element, and all on a single line.  An empty sequence
     * should give back "{}" followed by its capacity.
     *
     * @return a string representation of this sequence.
     */
    public String toString()
    {
        String toReturn = "{";
        if(isEmpty()){
            toReturn += "}";
        }else{
            for (int index = 0; index < size(); index++) {
                if (index != size() - 1) {
                    if (index != getCurrentIndex()) {
                        toReturn += getElement(index) + ", ";
                    } else {
                        toReturn += ">" + getElement(index) + ", ";
                    }
                } else{
                    if (index != getCurrentIndex()) {
                        toReturn += getElement(index) + "}";
                    } else {
                        toReturn += ">" + getElement(index) + "}";
                    }
                }
            }
        }
        toReturn += " (capacity = " + getCapacity() + ")";
        return toReturn;
    }

    /**
     * Checks whether another sequence is equal to this one.  To be
     * considered equal, the other sequence must have the same size
     * as this sequence, have the same elements, in the same
     * order, and with the same element marked
     * current.  The capacity can differ.
     *
     * Postcondition: NO SIDE EFFECTS!  this sequence and the
     * other sequence should remain unchanged, including the
     * current element.
     *
     * @param other the other Sequence with which to compare
     * @return true iff the other sequence is equal to this one.
     */
    public boolean equals(Sequence other)
    {
        if (!(this.size() == other.size())){
            return false;
        } else{
            for (int i = 0; i < size(); i++){
                if(!this.getElement(i).equals(other.getElement(i))){
                    return false;
                }
            }
            if(this.getCurrentIndex() != other.getCurrentIndex()){
                return false;
            }
        }
        return true;
    }

    /**
     *
     * @return true if Sequence empty, else false
     */
    public boolean isEmpty()
    {
        return size() == 0;
    }

    /**
     *  empty the sequence.  There should be no current element.
     */
    public void clear()
    {
        if(!isEmpty()){
            for(int i = size() - 1; i >= 0; i--){
                setElem(null, i);
                incDecSize(DECREMENT);
            }
            setCurrentIndex(NO_CURRENT);
            isCurrentAvailable();
        }
    }

    /**
     * A method that checks whether the sequence has a currentIndex or not
     */
    private void isCurrentAvailable(){
        if (getCurrentIndex() == NO_CURRENT){
            hasCurrent = false;
        }else if(getCurrentIndex() >= 0){
            hasCurrent = true;
        }
    }

    /**
     * A method that returns a boolean depending on whether the currentIndex reached the last stored element in Sequence
     * @return true, iff the last element is reached. false, otherwise
     */
    private boolean reachedCapacity(){
        return getCurrentIndex() == size()-1;
    }

    /**
     * A method that returns the currentIndex
     * @return index of the current element
     */
    private int getCurrentIndex(){
        return currentIndex;
    }

    /**
     * A method that returns a specific element stored in Sequence with a corresponding index
     * @param index the index of requested element
     * @return the element requested stored in Sequence corresponding to entered index
     */
    private String getElement(int index){
        return this.contents[index];
    }

    /**
     * A method that sets a given value to a given index in the Sequence
     * @param value value to be sat
     * @param index index for where to set the wanted element in Sequence
     */
    private void setElem(String value, int index){
        this.contents[index] = value;
    }

    /**
     * A method that swaps two elements using their corresponding indecies
     * @param elemIndexA index of elementA
     * @param elemIndexB index of elementB
     */
    private void swap(int elemIndexA, int elemIndexB){
        String elemHolderA = getElement(elemIndexA);
        String elemHolderB = getElement(elemIndexB);
        setElem(elemHolderB, elemIndexA);
        setElem(elemHolderA, elemIndexB);
    }

    /**
     * A method that adds an element to sequence before a given index
     * @param index the index of an element to add another element before
     * @param value the value of the newly added element
     */
    private void addBeforeIndex(int index, String value){
        for(int i = size() - 1; i >= index; i--){
            setElem(getElement(i), i + 1);
        }
        setElem(value, index);
    }

    /**
     * A method that adds an element to sequence after a given index
     * @param index the index of an element to add another element after
     * @param value the value of the newly added element
     */
    private void addAfterIndex(int index, String value){
        for(int i = size() - 1; i > index; i--){
            setElem(getElement(i), i + 1);
        }
        setElem(value, index+1);
    }

    /**
     * A method that doubles the capacity of the Sequence plus 1
     */
    private void doubleCapacity(){
        if(size() == getCapacity()){
            int newCapacity = (2*getCapacity())+1;
            ensureCapacity(newCapacity);
        }
    }

    /**
     * A method that replaces the Array containing Sequence contents with another Array
     * @param toReplace the Array that will replace the contents Array
     */
    private void replaceContents(String[] toReplace){
        this.contents = toReplace;
    }

    /**
     * A method to increment/decrement the size of the Sequence
     * Increments by one ONLY when num == 1. Decrements by one ONLY when num == -1
     * Does nothing if num != {1, -1}
     * @param num 1 or -1 depending on the operation
     */
    private void incDecSize(int num){
        if(num == 1 || num == -1){
            this.size += num;
        }
    }

    /**
     * A method to set the value of the currentIndex to entered value
     * @param value the value for the currentIndex to be set as
     */
    private void setCurrentIndex(int value){
        this.currentIndex = value;
    }
}