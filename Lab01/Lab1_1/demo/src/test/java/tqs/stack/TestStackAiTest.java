
package tqs.stack;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.EmptyStackException;
import java.util.Stack;

public class TestStackAiTest {

    private Stack<Integer> stack;

    @BeforeEach
    public void setUp() {
        stack = new Stack<>();
    }

    @Test
    public void testIsEmpty() {
        assertTrue(stack.isEmpty());
    }

    @Test
    public void testPush() {
        stack.push(1);
        assertFalse(stack.isEmpty());
        assertEquals(1, stack.peek());
    }

    @Test
    public void testPop() {
        stack.push(1);
        int value = stack.pop();
        assertEquals(1, value);
        assertTrue(stack.isEmpty());
    }

    @Test
    public void testPeek() {
        stack.push(1);
        int value = stack.peek();
        assertEquals(1, value);
        assertFalse(stack.isEmpty());
    }

    @Test
    public void testPopEmptyStack() {
        assertThrows(EmptyStackException.class, () -> {
            stack.pop();
        });
    }

    @Test
    public void testPeekEmptyStack() {
        assertThrows(EmptyStackException.class, () -> {
            stack.peek();
        });
    }
}