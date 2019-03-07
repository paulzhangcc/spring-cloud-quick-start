import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author paul
 * @description
 * @date 2019/3/7
 */
public class TestBridgeMethod {
    public static class Warehouse<T>{
        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }

        private T value;

    }

    public static class WarehouseInteger extends Warehouse<Integer>{
        @Override
        public void setValue(Integer value) {
            super.setValue(value);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Method[] declaredMethods = WarehouseInteger.class.getDeclaredMethods();
        Arrays.asList(declaredMethods).forEach((value)->{
            System.out.println(value);
        });
        Thread.sleep(Integer.MAX_VALUE);
    }

}
