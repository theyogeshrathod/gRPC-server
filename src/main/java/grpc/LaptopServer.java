package grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class LaptopServer {

    private static final Logger logger = Logger.getLogger(LaptopServer.class.getName());

    private final int port;
    private final Server server;

    public LaptopServer(int port) {
        this.port = port;
        LaptopService service = new LaptopService(new InMemoryLaptopStore());
        server = ServerBuilder.forPort(port).addService(service).build();
    }

    public void start() throws IOException {
        server.start();
        logger.info("Server started on port: " + port);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                LaptopServer.this.stop();
            } catch (InterruptedException e) {
                e.printStackTrace(System.err);
            }
            logger.info("Shutting gPRC server because JVM shut down");
        }));
    }

    public void stop() throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
            logger.info("Server stopping from port: " + port);
        }
    }

    private void awaitTermination() throws InterruptedException{
        if (server != null) {
            server.awaitTermination();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {

        LaptopServer laptopServer = new LaptopServer(3000);
        laptopServer.start();
        laptopServer.awaitTermination();
    }
}
