package dev.sim0n.anticheat.util;

import lombok.Getter;

@Getter
public class LastAccessEvictingArray<E> {
    private final int capacity;

    private E[] data;
    private int pointer;

    private boolean empty = true;
    private boolean full;

    public LastAccessEvictingArray(int capacity) {
        this.capacity = capacity;
        this.data = (E[])new Object[capacity];
    }

    public E get(int index) {
        if (index >= this.capacity)
            throw new IndexOutOfBoundsException();

        return this.data[index];
    }

    public void push(E element) {
        ++this.pointer;

        if (this.pointer >= this.capacity) {
            this.pointer = 0;
            this.full = true;
        }

        this.data[this.pointer] = element;
        this.empty = false;
    }

    public E last() {
        return this.data[this.pointer];
    }

    public void clear() {
        for (int i = 0; i < this.capacity; ++i)
            this.data = (E[]) new Object[this.capacity];

        this.pointer = 0;
        this.full = false;
    }

}
