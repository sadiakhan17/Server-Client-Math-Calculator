import java.io.*; 
import java.net.*; 
import java.time.LocalDateTime;

class TCPServer {
	
	//create logging file
	private static PrintWriter outputFile;
	

  public static void main(String argv[]) throws Exception 
  {
	  ServerSocket welcomeSocket = null;
	  try {
		  //create logger file
		  outputFile = new PrintWriter("logger.txt");   
	  
		  //welcoming socket
		  welcomeSocket = new ServerSocket(2789); 
	  
		  System.out.println("Server is running. Waiting for client. ");
	  
		  while(true) {
			  //accept incoming connections
			  Socket connectionSocket = welcomeSocket.accept(); 
		  
			  //create new thread for each client
			  Thread clientThread = new Thread(() -> handleClient(connectionSocket));
			  clientThread.start();
		  }
		  
	  } catch (IOException e) {
	      // Handle exception
	      e.printStackTrace();
	  } finally {
	      // Close the ServerSocket in a finally block
		  welcomeSocket.close();
	  }
	 
	  /*
	  //create logger file
	  outputFile = new PrintWriter("logger.txt");   
	  
	  //welcoming socket
	  ServerSocket welcomeSocket = new ServerSocket(2789); 
	  
	  System.out.println("Server is running. Waiting for client. ");
	  
	  while(true) {
		  //accept incoming connections
		  Socket connectionSocket = welcomeSocket.accept(); 
		  
		  //create new thread for each client
		  Thread clientThread = new Thread(() -> handleClient(connectionSocket));
		  clientThread.start();*/
	  
    
  }
  /********************************************************
   * Method: calculate(String equation)
   * Method which takes in a String in the form of a basic math
   * function and outputs the result of the equation, returning
   * quit if q is sent
   * Arguments: String equation - String which contains equation
   * in the format 2+2, 7.2 * 15, etc.
   * Returns: String result which is the calculated value from
   * the given equation.
   ********************************************************/
  private static String calculate(String equation)
  {
	// Removing whitespace from equation
    String eq = equation.replaceAll(" ", "");
	// Basic strings to hold values in equation
    String val1String = "";
    String val2String = "";
	// Character to hold operator of equation
    char operator = '0';
	// Double conversions of val1String & val2String
    double val1 = 0;
    double val2 = 0;
	// Double to store final equation result
    double result = 0;
    
	//For loop which iterates through equation
    for(int i = 0; i < eq.length(); i++)
    {
	  // Finding substrings of input values using isDigit()
      if(Character.isDigit(eq.charAt(i)) || eq.charAt(i) == '.')
      {
        if(operator == '0'){
          val1String = eq.substring(0, i+1);
        }
        else
          val2String = eq.substring(val1String.length()+1, i+1);
      }
      // Switch statement to determine operator
      switch(eq.charAt(i))
      {
        case '+': 
        case '-':
        case 'x':
        case '*':
        case '/':
          operator = eq.charAt(i);
          break;
        case 'q':
          return "quit";
      }
    }
    
	// Converting substring values into doubles
    val1 = Double.parseDouble(val1String);
    val2 = Double.parseDouble(val2String);
    
	// Switch statement based on operator which performs calculation
    switch(operator)
    {
      case '+': 
        result = val1 + val2;
        break;
      case '-':
        result = val1 - val2;
        break;
      case 'x':
      case '*':
        result = val1 * val2;
        break;
      case '/':
        result = val1/val2;
    }
    
	// Returning final value
    return Double.toString(result) + '\n';
  }
  
  private static void handleClient(Socket connectionSocket) {
      try {
		  
		  //set up input output streams
	      BufferedReader inFromClient = 
	        new BufferedReader(new
	        InputStreamReader(connectionSocket.getInputStream())); 

	      DataOutputStream  outToClient = 
	        new DataOutputStream(connectionSocket.getOutputStream()); 
		  
		  //get client name to identify each client by
		  String clientName = inFromClient.readLine();
		  System.out.println("New connection from client " + clientName + "\n");
		  
		  //send ackowndlegemnt
		  outToClient.writeBytes("Acknowledgement "+ clientName + " has connected to server.\n");
		  
		  //get start connection time
		  LocalDateTime connectDate = LocalDateTime.now();
		  
		  //start timer for duration of connection to log 
		  long startTime = System.currentTimeMillis();
		  
		  //log client name, connection socket detials as well as connection time
		  writeToFile(connectDate + ": New connection from client " + clientName);
		  writeToFile(clientName + " socket details: " + connectionSocket);
		 
		  while(true){
		      String clientEquation; 
		      String eqResult = ""; 
			  
			  //get equation and do math 
			  clientEquation = inFromClient.readLine(); 
			  System.out.println("Received from " + clientName + ": " + clientEquation);
			  
			  //to deal with nullpointerexception from the input stream of socket being closed the null was added or you can quit
			  //this also deals with unexpected client disconnection and will log that client disconnected
			  if (clientEquation == null || clientEquation.equalsIgnoreCase("quit") || !connectionSocket.isConnected()){
				  System.out.println("Client " + clientName + " disconnected.");
				  connectionSocket.close(); //close socket
				  
				  //get end connection time
				  LocalDateTime endConnectDate = LocalDateTime.now();
				  
				  //figure out duration of connection
				  long endTime = System.currentTimeMillis();
				  long duration = endTime - startTime;
				  
				  //log duration of connection and that clientName disconnected and when they disconnected
				  writeToFile(endConnectDate + ": Client " + clientName + " disconnected.");
				  writeToFile("Client " + clientName + " connection duration was " + duration + " ms.");
				  
				  
				  break;
			  }
			  
			  //result of math calculation and then send it back
		      eqResult = calculate(clientEquation); 
		      System.out.println("Sending to " + clientName + ": " + eqResult);
		  
			  outToClient.writeBytes(eqResult); 
		  	
		  }

      } catch (IOException e) {
          e.printStackTrace();
      }
  }
  
  //synchornizefd was added because we have a thread per client so we want this write to be thread safe
  //just logging to output file 
  private static synchronized void writeToFile(String content) {
          if (outputFile != null) {
			  outputFile.println(content);
              outputFile.flush();
          }
      }
}



 

           