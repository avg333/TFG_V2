package communication;

import communication.model.CloseBrokerDto;
import communication.model.RegisterRequestDto;
import communication.model.RegisterResponseDto;
import communication.model.base.Dto;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CommunicatorTCPTest {

  private static final int RANDOM_FREE_PORT = 0;
  private static final String LOCALHOST = "localhost";

  private ServerSocket serverSocket;

  private CommunicatorTCP client;

  @BeforeEach
  void setUp() throws IOException {
    serverSocket = new ServerSocket(RANDOM_FREE_PORT);
    new Thread(
            () -> {
              try {
                setupClientInServer();
              } catch (IOException e) {
                e.printStackTrace();
              }
            })
        .start();
  }

  @AfterEach
  void tearDown() throws IOException {
    closeServerSocket();
  }

  @Test
  void testCommunicatorTCP() throws IOException {
    // Aqui se crea un CommunicatorTCP con un EntityType, un ip, un puerto y una Position
    // En ese momento se envia un mensaje de registro al servidor y se recibe un mensaje de
    // respuesta
    client = new CommunicatorTCP(LOCALHOST, serverSocket.getLocalPort());
    System.out.println("Testing CommunicatorTCP");
    client.sendMessage(new CloseBrokerDto());
    System.out.println("Tested CommunicatorTCP");
  }

  private void setupClientInServer() throws IOException {
    while (true) {
      final Socket clientSocket = serverSocket.accept();
      final Communicator communicator = new CommunicatorTCP(clientSocket);
      final Dto dto = communicator.receiveMessage(21);

      if (dto instanceof CloseBrokerDto) {
        System.out.println("Closing server");
        return;
      }
      if (dto instanceof RegisterRequestDto) {
        System.out.println("Registering");
        final RegisterResponseDto registerResponseDto = new RegisterResponseDto(1);
        communicator.sendMessage(registerResponseDto);
        System.out.println("Registered");
      }
    }
  }

  private void closeServerSocket() throws IOException {
    try (final Socket clientSocket = new Socket(LOCALHOST, serverSocket.getLocalPort());
        final DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream())) {
      final byte[] message = new CloseBrokerDto().toByteArray();
      out.writeInt(message.length);
      out.write(message);
    }
    serverSocket.close();
    client.close();
  }
}
