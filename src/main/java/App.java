import java.util.Objects;

/*
 *@created 29/10/2022
 *@author DELL
*/
public class App {
    public static void main(String[] args) {
        Flux.fromArray(1, 2, null, 4, 5)
                .filter(Objects::nonNull)
                .map(item -> "item_" + item)
                .subscriber(System.out::println
                        , t -> System.out.println(t.getMessage())
                        , () -> System.out.println("I'm done"));
    }
}
