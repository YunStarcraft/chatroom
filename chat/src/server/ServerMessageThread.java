package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import util.CharacterUtil;
import util.XMLUtil;

public class ServerMessageThread extends Thread
{
	private Server server;

	private InputStream is;
	private OutputStream os;
	private Socket socket;

	public ServerMessageThread(Server server, Socket socket)
	{

		try
		{
			this.server = server;
			this.is = socket.getInputStream();
			this.os = socket.getOutputStream();
			this.socket = socket;
			System.out.println("current client socket is" + this.socket.getPort());

		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void updataUserList()
	{
		// obtain userlist keyset(username)
		Set<String> users = this.server.getMap().keySet();

		// build userlist xml to be sent to all clients
		String xml = XMLUtil.constructUserList(users);

		String str = "";
		for (String user : users)
		{

			str += user + "\n";
		}
		// first update server userlist
		this.server.getjTextArea1().setText(str);

		Collection<ServerMessageThread> cols = this.server.getMap().values();

		// iterate every client related messageThread,send userlist to everyone
		for (ServerMessageThread smt : cols)
		{
			smt.sendMessage(xml);

		}

	}

	public void sendMessage(String message)
	{
		try
		{
			System.out.println("message XML sent to client is " + message);
			os.write(message.getBytes());
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}

	}

	@Override
	public void run()
	{
		while (true)
		{

			try
			{
				byte[] buf = new byte[1000];
				// message sent from client
				int length = this.is.read(buf);
				String xml = new String(buf, 0, length);
				int type = Integer.parseInt(XMLUtil.extractType(xml));

				// chat message
				if (CharacterUtil.CLIENT_MESSAGE == type)
				{

					String username = XMLUtil.extractUsername(xml);
					String content = XMLUtil.extractContent(xml);

					// construct send to all users message

					String message = username + ":" + content;
					System.out.println("message received from client is " + message);
					// XML file sent to all clients
					String messageXML = XMLUtil.constructServerMessageXML(message);

					Map<String, ServerMessageThread> map = this.server.getMap();

					Collection<ServerMessageThread> cols = map.values();

					for (ServerMessageThread smt : cols)
					{
						// send XML to every client

						smt.sendMessage(messageXML);
					}

				}
				// Client Window Close
				else if (CharacterUtil.CLOSE_CLIENT_WINDOW == type)
				{
					
					
					/*****************************************************************************************
					 *  get the to-be-closed Client username .
					 *  Send the final message to this Client.
					 *  Inform Client its OK to close.
					 *  Client's connection thread retains in run() method 
					 *  (more precisely,program pointer stays on is.read() expression, which is blocked)
					 *  In this case, Client Connection Thread can't be terminated using "break" inside while-loop
					 *  cuz its blocked. Which differs from ServerMessageThread.
					 *  ServerMessageThread calls break inside its own thread.
					 *  MeanWhile,ChatClient send close-window-message and 
					 *  call sendMessage method from ClientConnection Thread.
					 *  
					 *  
					 *  
					 *  
					***************************************************************************************** */
					
					String username = XMLUtil.extractUsername(xml);
					
					ServerMessageThread smt = (ServerMessageThread)this.server.getMap().get(username);
					
					String confirmationXML = XMLUtil.constructCloseWindowConfirmationXML();
					
					smt.sendMessage(confirmationXML);
					
					//remove the closed client from userlist
					this.server.getMap().remove(username);
					//update online userlist
					this.updataUserList();

					
					
					
					this.is.close();
					this.os.close();
					
					//Caution!!!! Terminate current Thread
					break;
				}

			} catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}

}
