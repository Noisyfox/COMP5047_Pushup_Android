package sway.comp5047.usyd.edu.push_updetector.devices;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by noisyfox on 2017/10/15.
 */

public class DeviceController implements Closeable {

    private final Socket mSocket;
    private final PrintWriter mOut;
    private final BufferedReader mIn;

    public DeviceController() throws IOException {
        mSocket = new Socket("192.168.4.1", 80);
        mOut = new PrintWriter(mSocket.getOutputStream());
        mIn = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
    }

    public boolean executeCommand(String command) throws IOException {
        mOut.printf("%s\r\n", command);
        mOut.flush();
        String result = mIn.readLine();

        if(mOut.checkError()){
            throw new IOException("Something wrong!");
        }

        return "OK".equals(result);
    }

    @Override
    public void close() throws IOException {
        mSocket.close();
    }
}
