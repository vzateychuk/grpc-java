package grpc.java.store;

import vez.grpc.Laptop;

public interface LaptopStore {

    void Save(Laptop in);

    Laptop find(String id);
}
