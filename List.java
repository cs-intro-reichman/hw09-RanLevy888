/** A linked list of character data objects. */
public class List {

    private Node first;
    private int size;
    
    public List() {
        first = null;
        size = 0;
    }

    public int getSize() {
        return size;
    }

    public Node getFirst() {
        return first;
    }

    public void addFirst(char chr) {
        CharData newData = new CharData(chr);
        Node newNode = new Node(newData, first);
        first = newNode;
        size++;
    }
    
    public int indexOf(char chr) {
        Node current = first;
        int index = 0;
        while (current != null) {
            // שים לב: אם ב-Node קוראים לשדה value במקום cp, שנה את זה כאן
            if (current.cp.chr == chr) { 
                return index;
            }
            current = current.next;
            index++;
        }
        return -1;
    }

    public void update(char chr) {
        int index = indexOf(chr);
        if (index != -1) {
            get(index).count++;
        } else {
            addFirst(chr);
        }
    }

    public boolean remove(char chr) {
        Node prev = null;
        Node current = first;
        while (current != null) {
            if (current.cp.chr == chr) {
                if (prev == null) {
                    first = first.next;
                } else {
                    prev.next = current.next;
                }
                size--;
                return true;
            }
            prev = current;
            current = current.next;
        }
        return false;
    }

    public CharData get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        Node current = first;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.cp;
    }

    public ListIterator listIterator(int index) {
        return new ListIterator(get(index));
    }

    public String toString() {
        if (size == 0) return "()";
        StringBuilder sb = new StringBuilder("(");
        Node current = first;
        while (current != null) {
            sb.append(current.cp.toString()).append(" ");
            current = current.next;
        }
        return sb.toString().trim() + ")";
    }
}