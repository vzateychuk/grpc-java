package grpc.java.sample;

import com.google.protobuf.util.JsonFormat;
import vez.grpc.Laptop;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Serializer {
    public void WriteBinaryFile(Laptop laptop, String filename) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(filename)) {
            laptop.writeTo(fos);
        }
    }

    public Laptop ReadBinaryFile(String filename) throws IOException {
        try (FileInputStream fis = new FileInputStream(filename)) {
            return Laptop.parseFrom(fis);
        }
    }

    public void WriteJsonFile(Laptop laptop, String filename) throws IOException {
        JsonFormat.Printer printer = JsonFormat.printer()
                .includingDefaultValueFields()
                .preservingProtoFieldNames();

        String json = printer.print(laptop);

        try (FileOutputStream fos = new FileOutputStream(filename)) {
            fos.write(json.getBytes());
        }
    }

    public static void main(String[] args) throws IOException {
        Serializer serializer = new Serializer();
        Laptop laptop = serializer.ReadBinaryFile("laptop.bin");
        serializer.WriteJsonFile(laptop, "laptop.json");
    }
}
