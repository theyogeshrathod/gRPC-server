package grpc;

import io.grpc.stub.StreamObserver;
import proto.*;

import java.util.UUID;
import java.util.logging.Logger;

public class LaptopService extends LaptopServiceGrpc.LaptopServiceImplBase {

    private static final Logger logger = Logger.getLogger(LaptopService.class.getName());

    private final LaptopStore store;

    public LaptopService(LaptopStore store) {
        this.store = store;
    }

    /* Unary RPC */
    @Override
    public void storeLaptop(StoreLaptopRequest request, StreamObserver<StoreLaptopResponse> responseObserver) {
        Laptop laptop = request.getLaptop();
        UUID uuid = createAndStoreLaptop(laptop);

        StoreLaptopResponse response = StoreLaptopResponse.newBuilder().setId(uuid.toString()).build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();

        logger.info("Laptop successfully created with Id: " + uuid);
    }

    /* Server streaming RPC */
    @Override
    public void getAllLaptops(Empty request, StreamObserver<StoreLaptopResponse> responseObserver) {
        // Perform logic
        StoreLaptopResponse response1 = StoreLaptopResponse.newBuilder().setId("laptop_1").build();
        responseObserver.onNext(response1);

        // Perform logic
        StoreLaptopResponse response2 = StoreLaptopResponse.newBuilder().setId("laptop_2").build();
        responseObserver.onNext(response2);

        // Perform logic
        StoreLaptopResponse response3 = StoreLaptopResponse.newBuilder().setId("laptop_3").build();
        responseObserver.onNext(response3);

        // Perform logic
        StoreLaptopResponse response4 = StoreLaptopResponse.newBuilder().setId("laptop_4").build();
        responseObserver.onNext(response4);

        // Mark completed
        responseObserver.onCompleted();
    }

    /* Client streaming RPC */
    @Override
    public StreamObserver<StoreLaptopRequest> storeLaptops(StreamObserver<StoreLaptopResponse> responseObserver) {
        StreamObserver<StoreLaptopRequest> requestObserver = new StreamObserver<>() {
            @Override
            public void onNext(StoreLaptopRequest request) {
                // Call received here when client calls onNext.
            }

            @Override
            public void onError(Throwable t) {
                // Call received here when client throws error.
            }

            @Override
            public void onCompleted() {
                // Call received here when client marks completed from its end..
            }
        };

        // responseObserver.onNext();
        // responseObserver.onCompleted();

        return requestObserver;
    }

    /* Bidirectional streaming RPC */
    @Override
    public StreamObserver<StoreLaptopRequest> storeLaptopsContinuously(StreamObserver<StoreLaptopResponse> responseObserver) {
        StreamObserver<StoreLaptopRequest> requestObserver = new StreamObserver<>() {
            @Override
            public void onNext(StoreLaptopRequest request) {
                // Call received here when client calls onNext.
                StoreLaptopResponse response =
                        StoreLaptopResponse.newBuilder().setId(createAndStoreLaptop(request.getLaptop()).toString()).build();
                responseObserver.onNext(response);
            }

            @Override
            public void onError(Throwable t) {
                // Call received here when client throws error.
            }

            @Override
            public void onCompleted() {
                // Call received here when client marks completed from its end..
            }
        };

        return requestObserver;
    }

    private UUID createAndStoreLaptop(Laptop laptop) {
        String id = laptop.getId();

        UUID uuid;
        if (id.isEmpty()) {
            uuid = UUID.randomUUID();
        } else {
            uuid = UUID.fromString(id);
        }
        store.save(laptop.toBuilder().setId(uuid.toString()).build());

        return uuid;
    }
}
