package proj2;  // Gradescope needs this.
/**
 *
 *  Sequence ADT. An ordered collection of String objects with a "current" item
 *  Sequence capacity is the length of contents array, and has elements in contents[0, ....., size-1]
 *                                                         in contents[size, ..., capacity-1] stores null
 *  currentIndex (int) stores the index of the "current" element at contents[currentIndex] iff there is a current element
 *  size (int) stores the number of elements stored in Sequence
 *  hasCurrent (boolean) True iff sequence has current item
 */
public class SequenceBackup
{
    private String[] contents;
    private int size;
    private int currentIndex;
    private boolean hasCurrent;
    private final static int DEFAULT_CAPACITY = 10;
    private final static int NO_CURRENT = -1;

    /**
     * Creates a new sequence with initial capacity 10.
     */
    public SequenceBackup() {
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
    public SequenceBackup(int initialCapacity){
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
            this.currentIndex = 0;
        } else if (isCurrent() && !isEmpty()){
            String[] shifted = new String[getCapacity()];
            for (int i = 0; i < getCurrentIndex(); i++){
                shifted[i] = getElement(i);
            }
            for(int i =  getCurrentIndex()+1; i <= size(); i++){
                shifted[i] = getElement(i-1);
            }
            this.contents = shifted;
            setElem(value, getCurrentIndex());
        } else if(!isCurrent() && !isEmpty()){
            String[] shifted = new String[getCapacity()];
            shifted[0] = value;
            for(int i =  1; i <= size(); i++){
                shifted[i] = getElement(i-1);
            }
            this.currentIndex = 0;
            this.contents = shifted;
        }
        size++;
        currentIsAvailable();
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
            this.currentIndex = size();
        } else if (isCurrent() && !isEmpty()) {
            String[] shifted = new String[getCapacity()];
            for (int i = 0; i <= getCurrentIndex(); i++){
                shifted[i] = getElement(i);
            }
            for(int i =  getCurrentIndex()+1; i <= size(); i++){
                shifted[i] = getElement(i-1);
            }
            this.contents = shifted;
            setElem(value, getCurrentIndex()+1);
            this.currentIndex = getCurrentIndex()+1;
        } else if(!isCurrent() && size() != 0){
            setElem(value, size());
            this.currentIndex = size();
        }
        size++;
        currentIsAvailable();
    }

    private void doubleCapacity(){
        if(size() == getCapacity()){
            int newCapacity = (2*getCapacity())+1;
            ensureCapacity(newCapacity);
        }
    }


    //YOU NEED TO ADD A NEW METHOD CALLED changeContainer that changes the array directly and a method to incrementSize
    private void setElem(String value, int index){
        this.contents[index] = value;
    }

    private void swap(int elemIndexA, int elemIndexB){
        String elemHolderA = getElement(elemIndexA);
        String elemHolderB = getElement(elemIndexB);
        this.contents[elemIndexA] = elemHolderB;
        this.contents[elemIndexB] = elemHolderA;
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


    private int getCurrentIndex(){
        return currentIndex;
    }


    private String getElement(int index){
        return this.contents[index];
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
            this.contents = biggerContents;
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
    public void addAll(SequenceBackup another)
    {
        SequenceBackup anotherClone = another.clone();
        this.ensureCapacity(anotherClone.size() + this.size());
        for (int i = 0; i < anotherClone.size(); i++){
            this.setElem(anotherClone.getElement(i), size());
            size++;
        }
        if((anotherClone.size() + this.size()) > this.getCapacity()){
            this.trimToSize();
        }
    }


    private void currentIsAvailable(){
        if (getCurrentIndex() == NO_CURRENT){
            hasCurrent = false;
        }else if(getCurrentIndex() >= 0){
            hasCurrent = true;
        }
    }

    private boolean reachedCapacity(){
        return getCurrentIndex() == size()-1;
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
                this.currentIndex = NO_CURRENT;
            }else if(!reachedCapacity()){
                this.currentIndex++;
            }
            currentIsAvailable();
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
    public SequenceBackup clone()
    {
        SequenceBackup clone = new SequenceBackup(getCapacity());
        for(int index = 0; index < size(); index++){
            clone.addAfter(this.getElement(index));
        }
        clone.currentIndex = this.getCurrentIndex();
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
                currentIndex = NO_CURRENT;
            }
            size--;
            currentIsAvailable();
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
            currentIndex = 0;
        }
        currentIsAvailable();
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
        this.contents = trimmed;

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
    public boolean equals(SequenceBackup other)
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
        for(int i = size() - 1; i >= 0; i--){
            setElem(null, i);
            size--;
        }
        this.currentIndex = NO_CURRENT;
        currentIsAvailable();
    }

}