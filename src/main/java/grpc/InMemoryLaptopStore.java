package grpc;

import proto.Laptop;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class InMemoryLaptopStore implements LaptopStore {

    private final ConcurrentMap<String, Laptop> data;

    public InMemoryLaptopStore() {
        data = new ConcurrentHashMap<>();
    }

    @Override
    public void save(Laptop laptop) {
        Laptop other = laptop.toBuilder().build();
        data.put(other.getId(), other);
    }
}
