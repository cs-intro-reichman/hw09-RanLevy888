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

    public CharData getFirst() {
        if (first == null) return null;
        return first.cp;
    }

    public void addFirst(char chr) {
        CharData newData = new CharData(chr);
        Node newNode = new Node(newData, first);
        first = newNode;
        size++;
    }
    
    public String toString() {
        if (size == 0) return "()";
        StringBuilder str = new StringBuilder("(");
        Node current = first;
        while (current != null) {
            str.append(current.cp.toString());
            current = current.next;
            if (current != null) {
                str.append(" ");
            }
        }
        str.append(")");
        return str.toString();
    }

    public int indexOf(char chr) {
        Node current = first;
        int index = 0;
        while (current != null) {
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

    public CharData[] toArray() {
        CharData[] arr = new CharData[size];
        Node current = first;
        int i = 0;
        while (current != null) {
            arr[i++] = current.cp;
            current = current.next;
        }
        return arr;
    }

    public ListIterator listIterator(int index) {
        if (size == 0) return null;
        Node current = first;
        int i = 0;
        while (i < index && current != null) {
            current = current.next;
            i++;
        }
        return new ListIterator(current);
    }
    
}