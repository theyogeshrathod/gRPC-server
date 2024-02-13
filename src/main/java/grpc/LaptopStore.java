package grpc;

import proto.Laptop;

public interface LaptopStore {
    void save(Laptop laptop);
}
