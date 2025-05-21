package logistik.Structure;

import logistik.Model.Node;
import logistik.Model.Transaksi;

public class StructureQueue {
    Node REAR;
    Node FRONT;

    public void enqueue(Transaksi data) {
        Node newNode = new Node(data);
        if (isEmpty()) {
            FRONT = newNode;
            REAR = newNode;
        } else {
            REAR.setNext(newNode);
            REAR = newNode;
        }
    }
    public boolean isEmpty() {
        return FRONT == null;
    }

    public int size() {
        int size = 0;
        Node curNode = FRONT;
        while (curNode != null) {
            size++;
            curNode = curNode.getNext();
        }
        return size;
    }

    public Transaksi front() {
        return FRONT != null ? FRONT.getData() : null;
    }

    public void dispose(Node temp) {
        temp.setNext(null);
    }

    public Transaksi dequeue() {
    if (FRONT != null) {
        Transaksi data = FRONT.getData();

        if (FRONT == REAR) {
            // Jika hanya ada 1 elemen
            FRONT = null;
            REAR = null;
        } else {
            Node temp = FRONT;
            FRONT = FRONT.getNext();
            dispose(temp);
        }

        return data;
        } else {
            System.out.println("Queue kosong, tidak bisa dequeue.");
            return null;
        }
    }

    public void displayElement() {
    if (isEmpty()) {
        System.out.println("List Kosong");
    } else {
        Node curNode = FRONT;
        while (curNode != null) {
            System.out.print(curNode.getData() + " ");
            curNode = curNode.getNext();
            }
        }
    }
}
