import java.util.concurrent.Flow;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/*
 *@created 02/11/2022
 *@author DELL
*/
public abstract class Flux<T> implements Flow.Publisher<T> {

    static Object[] source;


    @SafeVarargs
    public static <T> Flux<T> fromArray(T... elements) {
        source = elements;
        return new ArrayPublisher<>(elements);
    }

    public <K> Flux<K> map(Function<T, K> mapper) {
        return new MapPublisher<>(this, mapper);
    }

    @SuppressWarnings(("unchecked"))
    public Flux<T> filter(Predicate<T> filter) {
        return new ArrayPublisher<>(filter, (T[]) source);
    }

    public void subscriber(Consumer<T> doOnNext, Consumer<Throwable> onError, Runnable onComplete) {
        this.subscribe(new Flow.Subscriber<>() {
                           @Override
                           public void onSubscribe(Flow.Subscription subscription) {
                               subscription.request(10);
                           }

                           @Override
                           public void onNext(T item) {
                               doOnNext.accept(item);
                           }

                           @Override
                           public void onError(Throwable throwable) {
                               onError.accept(throwable);
                           }

                           @Override
                           public void onComplete() {
                               onComplete.run();
                           }
                       }
        );
    }
}
