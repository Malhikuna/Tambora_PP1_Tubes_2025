package Structure;

import Model.Node;

public class StructureQueue {
    Node rear;
    Node front;

    public void enqueue(int data) {
        Node newNode = new Node(data);
        if (isEmpty()) {
            front = newNode;
            rear = newNode;
        } else {
            rear.setNext(newNode);
            rear = newNode;
        }
    }
    public boolean isEmpty() {
        return front == null;
    }

    public int size() {
        int size = 0;
        Node curNode = front;
        while (curNode != null) {
            size++;
            curNode = curNode.getNext();
        }
        return size;
    }

    public int front() {
        return front != null ? front.getData() : -1;
    }

    public void dequeue() {
        if (isEmpty()) {
            System.out.println("Queue is empty");
        } else {
            front = front.getNext();
            if (front == null) {
                rear = null;
            }
        }
    }

    public void displayElement() {
        if (isEmpty()) {
            System.out.println("Size :" + size());
            System.out.println("Isempty: " + isEmpty());
            System.out.println("Elemen Queue: Queue Kosong");
        } else {
            Node curNode = front;
            System.out.println("Size :" + size());
            System.out.println("Isempty: " + isEmpty());
            System.out.print("Elemen Queue : ");
            while (curNode != null) {
                size();
                System.out.print(curNode.getData() + " ");
                curNode = curNode.getNext();
            }
            System.out.println(" ");
            System.out.println("Front: " + front());
        }
    }

}
