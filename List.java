public class List {

    // Points to the first node in this list
    private Node first;

    // The number of elements in this list
    private int size;
	
    /** Constructs an empty list. */
    public List() {
        first = null;
        size = 0;
    }
    
    /** Returns the number of elements in this list. */
    public int getSize() {
 	      return size;
    }

    /** Returns the CharData of the first element in this list. */
    public CharData getFirst() {
        return first.cp;
    }

    /** GIVE Adds a CharData object with the given character to the beginning of this list. */
    public void addFirst(char chr) {
        CharData rishon = new CharData(chr);
        Node head = new Node(rishon, first);
        first = head;
        this.size++;
    }
    
    /** GIVE Textual representation of this list. */
    public String toString() {
        if (size == 0) return "()";

        Node check = first;
        StringBuilder text = new StringBuilder();
        text.append("(");
        while (check != null)
        {
            String temp = check.toString();
            text.append(temp + " ");
            check = check.next; 
        }
        return text.substring(0, text.length() - 1) + (")");
    }

    /** Returns the index of the first CharData object in this list
     *  that has the same chr value as the given char,
     *  or -1 if there is no such object in this list. */
    public int indexOf(char chr) {
        if (size == 0) return -1;

        int value = 0;
        Node check = first;
        while (check != null)
        {
            if (chr == check.cp.chr) return value;
            check = check.next;
            value++;
        }
        return -1;
    }

    /** If the given character exists in one of the CharData objects in this list,
     *  increments its counter. Otherwise, adds a new CharData object with the
     *  given chr to the beginning of this list. */
    public void update(char chr) {
        if (indexOf(chr) == -1) {addFirst(chr);}
        
        else 
        {
            Node temp = first;
            for (int i = 0; i < indexOf(chr); i++)
            {
                temp = temp.next;
            }
            temp.cp.count++;
        }
    }

    /** GIVE If the given character exists in one of the CharData objects
     *  in this list, removes this CharData object from the list and returns
     *  true. Otherwise, returns false. */
    public boolean remove(char chr) {
        int index = indexOf(chr);
        if (index == -1) return false;
        if (first == null) return false;
        if (index == 0) 
        {
            first = first.next;
            this.size--;
        }
        else 
        {
            Node temp = first;
            for (int i = 0; i < index - 1; i++)
            {
                temp = temp.next;
            }
            temp.next = temp.next.next;
            this.size--;
        }
        return true;
    }

    /** Returns the CharData object at the specified index in this list. 
     *  If the index is negative or is greater than the size of this list, 
     *  throws an IndexOutOfBoundsException. */
    public CharData get(int index) {
        if (index < 0 || index >= this.size) throw new IndexOutOfBoundsException();
        Node temp = first;
        for (int i = 0; i < index; i++)
        {
            temp = temp.next;
        }
        return temp.cp;
    }

    /** Returns an array of CharData objects, containing all the CharData objects in this list. */
    public CharData[] toArray() {
	    CharData[] arr = new CharData[size];
	    Node current = first;
	    int i = 0;
        while (current != null) {
    	    arr[i++]  = current.cp;
    	    current = current.next;
        }
        return arr;
    }

    /** Returns an iterator over the elements in this list, starting at the given index. */
    public ListIterator listIterator(int index) {
	    // If the list is empty, there is nothing to iterate   
	    if (size == 0) return null;
	    // Gets the element in position index of this list
	    Node current = first;
	    int i = 0;
        while (i < index) {
            current = current.next;
            i++;
        }
        // Returns an iterator that starts in that element
	    return new ListIterator(current);
    }
}