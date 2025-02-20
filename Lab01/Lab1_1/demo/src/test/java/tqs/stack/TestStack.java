
package tqs.stack;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.NoSuchElementException;

public class TestStack {

    @Test
    public void testContructor() {
        Stack stack = new Stack();
        assertEquals(stack.isEmpty(), true);
        assertEquals(stack.size(), 0);
    }

    @Test
    public void testPush() {
        Stack stack = new Stack();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        stack.push(4);
        stack.push(5);
        assertEquals(stack.size(), 5);
    }

    @Test
    public void testPeek() {
        Stack stack = new Stack();
        stack.push(1);
        assertEquals(stack.peek(), 1);
        stack.push(2);
        assertEquals(stack.peek(), 2);
    }

    @Test
    public void testIsEmpty() {
        Stack stack = new Stack();
        assertEquals(stack.isEmpty(), true);
        stack.push(1);
        assertEquals(stack.isEmpty(), false);
    }

    @Test
    @Disabled
    public void testPop() {
        Stack stack = new Stack();
        stack.push(1);
        assertEquals(stack.pop(), 1);
        stack.push(2);
        stack.push(3);
        stack.push(4);
        stack.push(5);
        assertEquals(stack.pop(), 5);
        assertEquals(stack.pop(), 4);
        assertEquals(stack.pop(), 3);
        assertEquals(stack.pop(), 2);
        assertEquals(stack.size(), 0);
        try {
            stack.pop();
        } catch (NoSuchElementException e) {
            assertEquals(e.getClass(), NoSuchElementException.class);
        }
        try {
            stack.peek();
        } catch (NoSuchElementException e) {
            assertEquals(e.getClass(), NoSuchElementException.class);
        }
    }

    @Test
    public void testBounded() {
        Stack stack = new Stack(3);
        stack.push(1);
        stack.push(2);
        stack.push(3);
        try {
            stack.push(4);
        } catch (IllegalStateException e) {
            assertEquals(e.getClass(), IllegalStateException.class);
        }
    }

    @Test
    public void testPopN() {
        Stack stack = new Stack();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        stack.push(4);
        stack.push(5);
        assertEquals(stack.popN(3), 3);
        assertEquals(stack.size(), 2);
        assertEquals(stack.popN(2), 1);
        assertEquals(stack.size(), 0);
        try {
            stack.popN(1);
        } catch (NoSuchElementException e) {
            assertEquals(e.getClass(), NoSuchElementException.class);
        }
        try {
            stack.popN(0);
        } catch (IllegalArgumentException e) {
            assertEquals(e.getClass(), IllegalArgumentException.class);
        }
    }
}
