package grpc.java.store;

import grpc.java.exception.AlreadyExistsException;
import grpc.java.store.LaptopStore;
import vez.grpc.Laptop;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class LaptopInMemStore implements LaptopStore {

    private final ConcurrentMap<String, Laptop> data;

    public LaptopInMemStore() {
        data = new ConcurrentHashMap<>(0);
    }

    @Override
    public void Save(Laptop in) {

        if (data.containsKey(in.getId())) {
            throw new AlreadyExistsException("Laptop with ID already exists");
        }

        // deep copy
        Laptop other = in.toBuilder().build();
        data.put(other.getId(), other);
    }

    @Override
    public Laptop find(String id) {
        if (data.containsKey(id)) {
            // deep copy
            Laptop other = data.get(id).toBuilder().build();
            return other;
        }
        return null;
    }
}
