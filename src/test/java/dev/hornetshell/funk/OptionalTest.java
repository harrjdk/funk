package dev.hornetshell.funk;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class OptionalTest {

    @Test
    void testOptionalsResolve() throws ClassNotFoundException {
        String foo = "foo!";
        Optional<String> optional = Optional.of(foo);

        assertTrue(optional.isPresent());
        assertFalse(optional.isEmpty());
        assertEquals(String.class.getCanonicalName(), optional.getValueClass().getCanonicalName());
        assertEquals(foo, optional.get());
        optional.ifPresent((value) -> assertEquals(foo, value));
        optional.ifPresentOrElse(value -> assertEquals(foo, value), () -> fail("Shouldn't enter!"));
        assertEquals(foo, optional.getOrElse(() -> "bar"));
        assertEquals(foo, optional.orElseThrow(() -> new RuntimeException("Shouldn't happen!")));
        assertEquals(foo, optional.stream().collect(Collectors.toList()).get(0));
    }

    @Test
    void testOptionalHandleNull() throws ClassNotFoundException {
        String foo = null;
        Optional<String> optional = Optional.ofNullable(foo);

        assertFalse(optional.isPresent());
        assertTrue(optional.isEmpty());
        assertEquals(Object.class.getCanonicalName(), optional.getValueClass().getCanonicalName());
        assertEquals("foo", optional.getOrElse(() -> "foo"));
        optional.ifPresent((value) -> fail());
        optional.ifPresentOrElse(value -> fail(), () -> assertNull(foo));
        assertThrows(RuntimeException.class, () -> optional.orElseThrow(() -> new RuntimeException("Should throw!")));
        assertThrows(RuntimeException.class, () -> optional.get());
        assertTrue(optional.stream().collect(Collectors.toList()).isEmpty());

        assertThrows(RuntimeException.class, () -> Optional.of(foo));

        assertThrows(RuntimeException.class, () -> Optional.empty().get());
    }

    @Test
    void testSerialization() throws IOException, ClassNotFoundException {
        String foo = "foo";
        Optional<String> optional = Optional.ofNullable(foo);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);

        objectOutputStream.writeObject(optional);
        objectOutputStream.flush();
        byte[] bytes = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.close();
        objectOutputStream.close();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);

        Optional<String> optional2 = (Optional<String>) objectInputStream.readObject();
        byteArrayInputStream.close();
        objectInputStream.close();
        assertEquals(optional.get(), optional2.get());
    }

    @Test
    void testSerializationNull() throws IOException, ClassNotFoundException {
        String foo = null;
        Optional<String> optional = Optional.ofNullable(foo);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);

        objectOutputStream.writeObject(optional);
        objectOutputStream.flush();
        byte[] bytes = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.close();
        objectOutputStream.close();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);

        Optional<String> optional2 = (Optional<String>) objectInputStream.readObject();
        byteArrayInputStream.close();
        objectInputStream.close();
        assertTrue(optional2.isEmpty());
    }




}
