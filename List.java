/** A linked list of character data objects. */
public class List {
    private Node first;
    private int size;

    public static class Node {
        char chr;
        int count;
        Node next;
       double p; double cp;
        public Node(char chr, Node next) {
            this.chr = chr;
            this.count = 1;
            this.next = next;
        }
    }

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
        first = new Node(chr, first);
        size++;
    }

    public int indexOf(char chr) {
        Node current = first;
        int index = 0;

        while (current != null) {
            if (current.chr == chr) {
                return index;
            }
            current = current.next;
            index++;
        }

        return -1;
    }

    public void update(char chr) {

    Node current = first;
    Node prev = null;

    while (current != null) {
        if (current.chr == chr) {
            current.count++;
            return;
        }
        prev = current;
        current = current.next;
    }

    Node newNode = new Node(chr, null);

    if (first == null) {
        first = newNode;
    } else {
        prev.next = newNode;
    }

    size++;
}

    public boolean remove(char chr) {
        Node prev = null;
        Node current = first;

        while (current != null) {
            if (current.chr == chr) {
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

    public Node get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }

        Node current = first;

        for (int i = 0; i < index; i++) {
            current = current.next;
        }

        return current;
    }

   public String toString() {

    StringBuilder sb = new StringBuilder();

    Node current = first;

    while (current != null) {
        sb.append("(")
          .append(current.chr).append(" ")
          .append(current.count).append(" ")
          .append(current.p).append(" ")
          .append(current.cp)
          .append(")");

        if (current.next != null) {
            sb.append(" ");
        }

        current = current.next;
    }

    return sb.toString();
}
}