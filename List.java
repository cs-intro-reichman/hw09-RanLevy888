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
     * If chr already exists, increments its count then bubble-swaps it forward
     * past any following nodes with a strictly smaller count.
     * Otherwise appends a new node at the end (count = 1).
     */
    public void update(char chr) {
        Node current = first;
        Node prev = null;

        // Search for existing node
        while (current != null) {
            if (current.chr == chr) {
                current.count++;
                // Bubble this node past subsequent nodes with smaller count
                bubbleForward(prev, current);
                return;
            }
            prev = current;
            current = current.next;
        }

        // chr not found — append new node at the end
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

    /**
     * Moves 'node' (whose predecessor is 'prev') forward past any nodes
     * that have a strictly smaller count than node.count.
     */
    private void bubbleForward(Node prev, Node node) {
        while (node.next != null && node.count > node.next.count) {
            Node after = node.next;
            // Swap node and after
            node.next = after.next;
            after.next = node;
            if (prev == null) {
                first = after;
            } else {
                prev.next = after;
            }
            prev = after;
        }
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