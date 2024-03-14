package rtg.api.util.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class SparseList<T> extends ArrayList<T> {
    public static <T> Collector<T, SparseList<T>, SparseList<T>> toSparseList() {
        return new Collector<T, SparseList<T>, SparseList<T>>() {
            @Override
            public Supplier<SparseList<T>> supplier() {
                return SparseList::new;
            }

            @Override
            public BiConsumer<SparseList<T>, T> accumulator() {
                return ArrayList::add;
            }

            @Override
            public BinaryOperator<SparseList<T>> combiner() {
                throw new IllegalStateException("Parallel operations not supported for SparseLists!");
            }

            @Override
            public Function<SparseList<T>, SparseList<T>> finisher() {
                return Function.identity();
            }

            @Override
            public Set<Characteristics> characteristics() {
                return new HashSet<Characteristics>() {{
                    add(Characteristics.IDENTITY_FINISH);
                }};
            }
        };
    }

    private void fillSpace(int upTo) {
        for (int i = size() - 1; i < upTo + 1; i++) {
            super.add(null);
        }
    }

    @Override
    public T set(final int i, final T t) {
        ensureCapacity(i + 1);
        final int size = size();
        if (i >= size) {
            fillSpace(i);
        }
        return super.set(i, t);
    }

    @Override
    public T get(int i) {
        if (i < size()) {
            return super.get(i);
        }
        return null;
    }

    public boolean containsKey(final int key) {
        if (key < size()) {
            return super.get(key) != null;
        }
        return false;
    }
}