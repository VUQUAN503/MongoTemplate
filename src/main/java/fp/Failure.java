package fp;

import java.util.function.Function;

/*
 *@created 30/10/2022
 *@author DELL
*/

public record Failure<A>(Object error) implements Try<A> {

    @Override
    @SuppressWarnings("unchecked")
    public <B> Try<B> map(Function<A, B> map) {
        return (Failure<B>)this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <B> Try<B> flatMap(Function<A, Try<B>> map) {
        return (Try<B>) this;
    }

    @Override
    public boolean isFailure() {
        return true;
    }
}
