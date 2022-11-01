package fp;

import java.util.function.Function;

/*
 *@created 30/10/2022
 *@author DELL
*/

public record Success<A>(A value) implements Try<A> {

    @Override
    public <B> Try<B> map(Function<A, B> map) {
        return new Success<>(map.apply(value));
    }

    @Override
    public <B> Try<B> flatMap(Function<A, Try<B>> map) {
        return map.apply(value);
    }

    @Override
    public boolean isFailure() {
        return false;
    }
}
