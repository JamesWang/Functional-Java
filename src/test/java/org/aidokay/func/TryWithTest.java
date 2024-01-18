package org.aidokay.func;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TryWithTest {
    //--enable-preview
    @Test
    public void testCatchRunException_no_exception() {
        int[] updated = new int[1];
        int[] unChanged = new int[1];
        TryWith.catchRunException(() -> {
            updated[0] = 1;
            System.out.println("all good");
        }, ex -> {
            unChanged[0] = 1;
            System.out.println("not run:" + ex.getMessage());
        });
        Assertions.assertEquals(1, updated[0]);
        Assertions.assertEquals(0, unChanged[0]);
    }

    @Test
    public void testCatchRunException_with_exception() {
        int[] unChanged = new int[1];
        TryWith.catchRunException(() -> {
            throw new Exception("bad happened");
        }, ex -> {
            unChanged[0] = 1;
            System.out.println("not run:" + ex.getMessage());
        });
        Assertions.assertEquals(1, unChanged[0]);
    }

    @Test
    public void testCatchCallException_without_exception() {
        var result = TryWith.catchCallException(
                () -> 1,
                ex -> System.out.println("shouldn't be here:" + ex.getMessage())
        );
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(1, result.get());
    }

    @Test
    public void testCatchCallException_with_exception() {
        var result = TryWith.catchCallException(
                () -> {
                    throw new Exception("error happened");
                },
                ex -> System.out.println("shouldn't be here:" + ex.getMessage())
        );
        Assertions.assertTrue(result.isEmpty());
    }
}
