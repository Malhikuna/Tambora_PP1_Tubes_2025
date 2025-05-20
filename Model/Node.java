package Model;

public class Node {
    /* Atribut */
    private int data;
    private Node next;

    /* Constructor */
    public Node(int data) {
        this.data = data;
        this.next = null;
    }
    /* Getter & Setter */
    public void setData (int data) {
        this.data = data;
    }

    public int getData() {
        return data;
    }

    public void setNext (Node next) {
        this.next = next;
    }

    public Node getNext() {
        return next;
    }
}
