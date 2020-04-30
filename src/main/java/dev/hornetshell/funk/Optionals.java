package dev.hornetshell.funk;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class Optionals {

    public static FindAnyCollector findAny() {
        return new FindAnyCollector();
    }

    static class FindAnyCollector implements Collector<Optional, Optional, Optional> {

        @Override
        public Supplier<Optional> supplier() {
            return Optional::empty;
        }

        @Override
        public BiConsumer<Optional, Optional> accumulator() {
            return (a, b) -> {};
        }

        @Override
        public BinaryOperator<Optional> combiner() {
            return (a, b) -> {
                if (a.isPresent()) {
                    return a;
                } else if (b.isPresent()) {
                    return b;
                } else {
                    return Optional.empty();
                }
            };
        }

        @Override
        public Function<Optional, Optional> finisher() {
            return Function.identity();
        }

        @Override
        public Set<Characteristics> characteristics() {
            return Set.of(Characteristics.IDENTITY_FINISH, Characteristics.UNORDERED);
        }
    }
}
