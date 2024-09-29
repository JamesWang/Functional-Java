package org.aidokay.func;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.function.BiFunction;

import static org.aidokay.func.Funcs.asFunc;

public class FuncBridgeTest {

    static class A<T> {
        private final T value;

        A(T value) {
            this.value = value;
        }

        public T getValue(){
            return value;
        }
    }

    static class B {
        public boolean isValueValid() {
            return isValueValid;
        }

        public void setValueValid(boolean valueValid) {
            isValueValid = valueValid;
        }
        private boolean isValueValid;
    }

    @Test
    public void testBridgeWithTransformer_toBoolean_true(){
        var transmitter = Funcs.bridge(
                asFunc((A<?> x) -> x.getValue()).andThen(String::valueOf),
                Boolean::valueOf,
                B::setValueValid
        );
        A<String> a = new A<>("true");
        B b = new B();

        transmitter.accept(a, b);
        Assertions.assertTrue(b.isValueValid());
    }
    @Test
    public void testBridgeWithTransformer_toInteger_10(){
        final BiFunction<A<String>, ?, Integer> transmitter = Funcs.bridgeWithReturning(
                A::getValue,
                Integer::valueOf,
                (Object x, Integer num) -> System.out.println("number=" + num)
        );

        A<String> a = new A<>("123");

        var result = transmitter.apply(a, null);
        Assertions.assertEquals(123, result);
    }

}
