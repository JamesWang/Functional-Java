package org.aidokay.func;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.function.BiFunction;
import java.util.function.Function;

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

    private final Function<Object, Boolean> objToBool =
            ExtFunc.asFunc(String::valueOf).andThen(Boolean::valueOf);
    @Test
    public void testExtFunc_toBoolean_true(){
        var transmitter = ExtFunc
                .asFunc((A<Object> x) -> x.getValue())
                .transform(objToBool)
                //.transform(String::valueOf).transform(Boolean::valueOf)
                .pipe(B::setValueValid);

        A<Object> a = new A<>("true");
        B b = new B();

        transmitter.accept(a, b);
        Assertions.assertTrue(b.isValueValid());
    }

    @Test
    public void testExtFunc_pipeWithReturns_toBoolean_true(){
        var transmitter = ExtFunc
                .asFunc((A<Object> x) -> x.getValue())
                .transform(objToBool)
                .pipeThenGetValue(B::setValueValid);

        A<Object> a = new A<>("true");
        B b = new B();

        var value = transmitter.apply(a, b);
        Assertions.assertTrue(value);
        Assertions.assertTrue(b.isValueValid());
    }
}
