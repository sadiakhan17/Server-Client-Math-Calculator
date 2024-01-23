import java.io.*;
import java.net.*;

class TCPClient {

    public static void main(String[] args) {
        // Init variables for client name and user input
        String clientName;
        String userInput;

        // Buffered reader for user input
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

        try {
            // Create a socket and connect to the server at IP address 127.0.0.1 and port 2789
            Socket clientSocket = new Socket("127.0.0.1", 2789);

            // Create output and input streams for communication with the server
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            // Prompt the user to enter their name
            System.out.print("Enter your name: ");
            clientName = inFromUser.readLine();
            
            // Send the client's name to the server
            outToServer.writeBytes(clientName + '\n');

            // Receive acknowledgment from the server and print it
            String acknowledgment = inFromServer.readLine();
            System.out.println("Server: " + acknowledgment);

            // Main loop for communication with the server
            while (true) {
                // Prompt the user to enter a math calculation or type 'quit' to exit
                System.out.print("Enter a math calculation (or type 'quit' to exit): ");
                userInput = inFromUser.readLine();

                // Check if the socket is still connected before writing to the server
                if (!clientSocket.isClosed()) {
                    outToServer.writeBytes(userInput + '\n');
                }

                // If the user types 'quit', exit the loop
                if (userInput.equalsIgnoreCase("quit")) {
                    System.out.println("Connection will be terminated.\n");
                    break;
                }

                // Receive the result of the calculation from the server and print it
                String calculation = inFromServer.readLine();
                System.out.println("Server: " + calculation);
            }

            // Close the connection when the loop exits
            System.out.println("Closing connection...");
            clientSocket.close();

        } catch (ConnectException e) {
            // Handle the case where the client cannot connect to the server
            System.err.println("Error: Unable to connect to the server. Make sure the server is running.");
        } catch (IOException e) {
            // Handle IO exceptions
            e.printStackTrace();
        }
    }
}
