package communication;

import exception.CommunicatorCreationException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommunicatorTCP extends Communicator {
  private final Socket clientSocket;
  private final DataOutputStream out;
  private final DataInputStream in;

  public CommunicatorTCP(final Socket clientSocket) {
    try {
      this.clientSocket = clientSocket;
      this.clientSocket.setSoTimeout(TIMEOUT);
      this.out = new DataOutputStream(clientSocket.getOutputStream());
      this.in = new DataInputStream(clientSocket.getInputStream());
    } catch (IOException e) {
      log.error("Error trying to create the socket", e);
      throw new CommunicatorCreationException(e);
    }
  }

  public CommunicatorTCP(final String ip, final int port) {
    this(createSocket(ip, port));
  }

  private static Socket createSocket(final String ip, final int port) {
    try {
      return new Socket(ip, port);
    } catch (IOException e) {
      log.error("Error trying to create the socket", e);
      throw new CommunicatorCreationException(e);
    }
  }

  @Override
  protected final void send(final byte[] message) throws IOException {
    out.writeInt(message.length);
    out.write(message);
    out.flush(); // TODO: check if this is necessary
  }

  @Override
  protected final byte[] receive(final int dataLen) throws IOException {
    final byte[] data = new byte[in.readInt()];
    in.readFully(data, 0, data.length);
    return data;
  }

  @Override
  public final void close() {
    closeResource(in, "input stream");
    closeResource(out, "output stream");
    closeResource(clientSocket, "socket");
  }

  private void closeResource(final AutoCloseable resource, final String resourceName) {
    if (resource != null) {
      try {
        resource.close();
      } catch (Exception e) {
        log.error("Error trying to close the " + resourceName + ".", e);
      }
    }
  }

  @Override
  public String toString() {
    return "ad="
        + clientSocket.getInetAddress().getHostAddress()
        + ", port="
        + clientSocket.getPort();
  }
}
