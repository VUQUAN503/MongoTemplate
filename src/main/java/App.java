import fp.Failure;
import fp.Success;
import fp.Try;
import repository.DogRepository;
import repository.DogRepositoryImpl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/*
 *@created 29/10/2022
 *@author DELL
*/
public class App {
    public static void main(String[] args) {
        DogRepository repository = new DogRepositoryImpl();
        System.out.println(repository.findByName("Milk"));
        System.out.println(repository.count("Milk"));
        System.out.println(repository.count("Lisa"));
    }

    static class TimingDynamicInvocationHandler implements InvocationHandler {

        private final Map<String, Method> methods = new HashMap<>();

        private final Object target;

        public TimingDynamicInvocationHandler(Object target) {
            this.target = target;

            for(Method method: target.getClass().getDeclaredMethods()) {
                this.methods.put(method.getName(), method);
            }
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args)
                throws Throwable {
            long start = System.nanoTime();
            System.out.println(method.getName());
            Object result = methods.get(method.getName()).invoke(target, args);
            long elapsed = System.nanoTime() - start;
            System.out.println(elapsed);
            return result;
        }
    }

    static Try<Integer> withDraw(Integer number) {
        if(number > 100) {
            return new Failure<>("can't not enough money to withdraw");
        }
        return new Success<>(number);
    }
}
