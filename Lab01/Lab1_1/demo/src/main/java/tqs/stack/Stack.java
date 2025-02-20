package tqs.stack;

import java.util.LinkedList;
import java.util.NoSuchElementException;

public class Stack {
    private int sizeLimit;
    private int size;
    private LinkedList<Integer> list;

    public Stack(int size) {
        this.list = new LinkedList<>();
        this.sizeLimit = size;
        this.size = 0;

    }

    public Stack() {
        this.list = new LinkedList<>();
        this.size = 0;

    }

    public boolean push(int element) {
        if (this.sizeLimit > 0 && this.size == this.sizeLimit) {
            throw new IllegalStateException("Stack is full");
        }
        list.add(element);
        this.size++;
        return true;
    }

    public int pop() {
        if (this.size == 0) {
            throw new NoSuchElementException("Stack is empty");
        }
        int last = list.removeLast();
        this.size--;
        return last;

    }

    public int peek() {
        if (this.size == 0) {
            throw new NoSuchElementException("Stack is empty");
        }
        return list.getLast();
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public int size() {
        return this.size;
    }

    public int popN(int n){
        if (n > this.size) {
            throw new NoSuchElementException("Stack is empty");
        }
        if (n <=0) {
            throw new IllegalArgumentException("Invalid argument");

        }
        int last = 0;
        for (int i = 0; i < n; i++) {
            last = list.removeLast();
            this.size--;
        }
        return last;
    }
}