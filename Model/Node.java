package Model;

public class Node {
    /* Atribut */
    private Transaksi data;
    private Node next;

    /* Constructor */
    public Node(Transaksi data) {
        this.data = data;
        this.next = null;
    }
    /* Getter & Setter */
    public void setData (Transaksi data) {
        this.data = data;
    }

    public Transaksi getData() {
        return data;
    }

    public void setNext (Node next) {
        this.next = next;
    }

    public Node getNext() {
        return next;
    }
}
