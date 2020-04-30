package dev.hornetshell.funk;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class Optional<T> implements Serializable {

    public static <T> Optional<T> of(T object) {
        if (object == null) {
            throw new RuntimeException("Object cannot be null!");
        }
        return new Optional<T>(object);
    }

    public static <T> Optional<T> ofNullable(T object) {
        return new Optional<T>(object);
    }

    public static Optional empty() {
        return new Optional();
    }

    private Object value;
    private String className;

    public Optional(){
        // no-op
    }

    private Optional(T value) {
        if (value != null) {
            this.className = value.getClass().getCanonicalName();
            this.value = value;
        } else {
            this.className = Object.class.getCanonicalName();
        }
    }

    public Class<T> getValueClass() throws ClassNotFoundException {
        if (value != null) {
            return (Class<T>) value.getClass();
        } else {
            return (Class<T>) Class.forName(className);
        }
    }


    public boolean isEmpty() {
        return value == null;
    }

    public boolean isPresent() {
        return value != null;
    }

    public void ifPresent(Consumer<T> consumer) {
        if (value != null) {
            consumer.accept((T) value);
        }
    }

    public void ifPresentOrElse(Consumer<T> ifPresent, Runnable orElse) {
        if (value != null) {
            ifPresent.accept((T) value);
        } else {
            orElse.run();
        }
    }

    public T get() {
        if (value == null) {
            throw new RuntimeException("Null found in optional!");
        }
        return (T) value;
    }

    public T getOrElse(Supplier<T> orElse) {
        if (value != null) {
            return (T) value;
        } else {
            return orElse.get();
        }
    }

    public <X extends Throwable> T orElseThrow(Supplier<X> exceptionSupplier) throws X  {
        if (value != null) {
            return (T) value;
        } else {
            throw exceptionSupplier.get();
        }
    }

    public Stream<T> stream() {
        final Stream.Builder<T> builder = Stream.builder();
        if (value != null) {
            return builder.add((T) value).build();
        } else {
            return builder.build();
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws ClassNotFoundException, IOException {
        className = objectInputStream.readUTF();
        Class<?> clazz = Class.forName(className);
        value = clazz.cast(objectInputStream.readObject());
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        if (value != null) {
            objectOutputStream.writeUTF(value.getClass().getCanonicalName());
        } else {
            objectOutputStream.writeUTF(Object.class.getCanonicalName());
        }
        objectOutputStream.writeObject(value);
    }
}
