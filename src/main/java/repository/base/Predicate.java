package repository.base;

/*
 *@created 29/10/2022
 *@author DELL
*/
@FunctionalInterface
public interface Predicate<T> {

    boolean isValid(T test);

    default Predicate<T> and(Predicate<T> other) {
        return (t) -> isValid(t) && other.isValid(t);
    }

    default Predicate<T> or(Predicate<T> other) {
        return (t) -> isValid(t) || other.isValid(t);
    }
}
