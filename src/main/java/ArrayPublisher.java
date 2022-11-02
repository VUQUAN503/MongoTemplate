import java.util.concurrent.Flow;
import java.util.function.Predicate;

/*
 *@created 02/11/2022
 *@author DELL
 */

public class ArrayPublisher<T> extends Flux<T> {

    final T[] elements;
    Predicate<T> filter;

    public ArrayPublisher(T[] elements) {
        this.elements = elements;
    }

    public ArrayPublisher(Predicate<T> filter, T[] elements) {
        this.filter = filter;
        this.elements = elements;
    }

    @Override
    public void subscribe(Flow.Subscriber<? super T> subscriber) {
        subscriber.onSubscribe(new Flow.Subscription() {
            int index;
            volatile boolean isCancelled;

            @Override
            public void request(long n) {
                for (int i = index; i < Math.min(n, elements.length) && index < elements.length; ++i) {
                    if (filter.test(elements[i])) {
                        if (elements[i] == null) {
                            cancel();
                            subscriber.onError(new NullPointerException("Element must be not null"));
                            return;
                        }
                        if (isCancelled) {
                            return;
                        }
                        subscriber.onNext(elements[index++]);
                    }
                }
                subscriber.onComplete();
            }

            @Override
            public void cancel() {
                isCancelled = true;
                System.out.println("Invoke cancelled");
            }
        });
    }
}
