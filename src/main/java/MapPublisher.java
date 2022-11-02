import java.util.concurrent.Flow;
import java.util.function.Function;

/*
 *@created 02/11/2022
 *@author DELL
*/

class MapPublisher<IN, OUT> extends Flux<OUT> {

    final Flow.Publisher<IN> parent;
    final Function<IN, OUT> mapper;

    MapPublisher(Flow.Publisher<IN> parent
            , Function<IN, OUT> mapper
    ) {
        this.parent = parent;
        this.mapper = mapper;
    }

    @Override
    public void subscribe(Flow.Subscriber<? super OUT> subscriber) {
        parent.subscribe(new MapSubscriber<>(mapper, subscriber));
    }

    static class MapSubscriber<IN, OUT> implements Flow.Subscriber<IN>, Flow.Subscription {

        final Function<IN, OUT> mapper;
        final Flow.Subscriber<? super OUT> actual;
        Flow.Subscription upstream;
        boolean terminated;

        MapSubscriber(Function<IN, OUT> mapper, Flow.Subscriber<? super OUT> actual) {
            this.mapper = mapper;
            this.actual = actual;
        }

        @Override
        public void onSubscribe(Flow.Subscription subscription) {
            upstream = subscription;
            actual.onSubscribe(this);
        }

        @Override
        public void onNext(IN item) {
            if (terminated) {
                return;
            }
            try {
                OUT out = mapper.apply(item);
                actual.onNext(out);
            } catch (Throwable error) {
                cancel();
                onError(error);
            }
        }

        @Override
        public void onError(Throwable throwable) {
            if (terminated) {
                return;
            }
            terminated = true;
            actual.onError(throwable);
        }

        @Override
        public void onComplete() {
            if (terminated) {
                return;
            }
            terminated = true;
            actual.onComplete();
        }

        @Override
        public void request(long n) {
            upstream.request(n);
        }

        @Override
        public void cancel() {
            upstream.cancel();
        }
    }
}
