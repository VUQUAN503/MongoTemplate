package fp;

import java.util.function.Function;

/*
 *@created 30/10/2022
 *@author DELL
*/
public interface Try<A> {

    <B> Try<B> map(Function<A, B> map);

    <B> Try<B> flatMap(Function<A, Try<B>> map);

    boolean isFailure();
}
