package grpc.java.sample;

import org.junit.Test;
import vez.grpc.Laptop;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class SerializerTest {

    @Test
    public void writeAndReadBinaryFile() throws IOException {

        String binaryFile = "laptop.bin";
        Laptop laptop = new Generator().NewLaptop();

        Serializer serializer = new Serializer();
        serializer.WriteBinaryFile(laptop, binaryFile);

        Laptop readLaptop = serializer.ReadBinaryFile(binaryFile);
        assertEquals(laptop, readLaptop);
    }

}