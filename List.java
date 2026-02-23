/** A linked list of character data objects. */
public class List {
    private Node first;
    private int size;

    public static class Node {
        char chr;
        int count;
        Node next;
        double p;
        double cp;

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

    /**
     * If chr already exists, increments its count and moves it to the correct
     * sorted position (ascending by count).  Otherwise adds a new node at the
     * end (count = 1, the smallest possible value).
     */
    public void update(char chr) {
        Node current = first;
        Node prev = null;

        // Search for existing node
        while (current != null) {
            if (current.chr == chr) {
                current.count++;

                // Remove from current position
                if (prev == null) {
                    first = current.next;
                } else {
                    prev.next = current.next;
                }
                size--;

                // Re-insert in sorted position (ascending count)
                insertSorted(current);
                size++;
                return;
            }
            prev = current;
            current = current.next;
        }

        // chr not found — append new node at the end (count=1, smallest)
        Node newNode = new Node(chr, null);
        if (first == null) {
            first = newNode;
        } else {
            Node tail = first;
            while (tail.next != null) {
                tail = tail.next;
            }
            tail.next = newNode;
        }
        size++;
    }

    /** Inserts an existing node into the list in ascending-count order. */
    private void insertSorted(Node node) {
        node.next = null;

        // Insert before the first node whose count is strictly greater
        if (first == null || node.count <= first.count) {
            node.next = first;
            first = node;
            return;
        }

        Node current = first;
        while (current.next != null && current.next.count <= node.count) {
            current = current.next;
        }
        node.next = current.next;
        current.next = node;
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

    /** Returns a string like: ((a 1 0.25 0.25) (b 3 0.75 1.0)) */
    public String toString() {
        StringBuilder sb = new StringBuilder("(");

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

        sb.append(")");
        return sb.toString();
    }
}