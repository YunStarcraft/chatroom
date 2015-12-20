package client;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

import javax.swing.JOptionPane;

import util.CharacterUtil;
import util.XMLUtil;

public class ClientConnectThread extends Thread
{
	private String hostAddress;

	private int port;

	private String username;

	private Client client;

	private Socket socket;

	private InputStream is;

	private OutputStream os;

	private ChatClient chatClient;

	public Socket getSocket()
	{
		return socket;
	}

	public ClientConnectThread(Client client, String hostAddress, int port, String username)
	{
		this.client = client;
		this.hostAddress = hostAddress;
		this.port = port;
		this.username = username;

		// connect server
		this.connect2Server();

		

	}

	// connect server , called by constructor
	// true, login succeed
	// false, login failure

	private void connect2Server()
	{
		try
		{
			this.socket = new Socket(this.hostAddress, this.port);
			this.is = this.socket.getInputStream();
			this.os = this.socket.getOutputStream();
			System.out.println("now in conncect2Server in clientConnectionThread");
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}

	}

	// user login ,send name to server
	public boolean login()
	{
		try
		{

			String xml = XMLUtil.constructLoginXML(this.username);

			os.write(xml.getBytes()); // send to server the client login message
										// include username

			byte[] buf = new byte[1000];
			int length = is.read(buf); // read response from server, check login
										// succeed?
			String loginResultXML = new String(buf, 0, length);

			String loginResult = XMLUtil.extractLoginResult(loginResultXML);

			System.out.println("login result is  "+ loginResult);
			
			//login succeed
			if ("success".equals(loginResult))
			{
				// init chatroom main frame
				this.chatClient = new ChatClient(this);
				this.client.setVisible(false);
				
				return true;
			} 
			else if("failure".equals(loginResult))
			{
				System.out.println("duplicate name!!");
				System.out.println("duplicate name!!");
				System.out.println("duplicate name!!");
				return false;

			}else{
				System.out.println("No Server Response!!");
				System.out.println("No Server Response!!");
				System.out.println("No Server Response!!");
				return false;
			}

		} catch (Exception ex)
		{
			
			System.out.println("exception throw");
			ex.printStackTrace();
		}
		return false;

	}

	public void sendMessage(String message, String type)
	{
		try
		{
			int t = Integer.parseInt(type);

			String xml = null;
			if (CharacterUtil.CLIENT_MESSAGE == t)
			{
				xml = XMLUtil.constructMessageXML(this.username, message);

			}
			// Client Window Close Message sent to Server
			else if (CharacterUtil.CLOSE_CLIENT_WINDOW == t)
			{

				xml = XMLUtil.constructCloseClientWindowXML(this.username);
			}

			// send chat message to server
			this.os.write(xml.getBytes());

		} catch (Exception ex)
		{
			ex.printStackTrace();
		}

	}

	@Override
	public void run()
	{
		try
		{
			while (true)
			{
				byte[] buf = new byte[1000];
				// Note!! below is blocked function.
				int length = is.read(buf);

				String xml = new String(buf, 0, length);
				int type = Integer.parseInt(XMLUtil.extractType(xml));

				System.out.println("message recevied from server type is  " + type);

				// online userlist
				if (type == CharacterUtil.USER_LIST)
				{
					List<String> list = XMLUtil.extractUserList(xml);
					String users = "";
					for (String user : list)
					{
						users += user + "\n";

					}
					this.chatClient.getJTextArea2().setText(users);

				}
				// message sent from server
				else if (type == CharacterUtil.SERVER_MESSAGE)
				{

					String content = XMLUtil.extractContent(xml);
					this.chatClient.getJTextArea1().append(content + "\n");

				}
				// Close Server Window. Server EXIT
				else if (type == CharacterUtil.CLOSE_SERVER_WINDOW)
				{

					JOptionPane.showMessageDialog(this.chatClient, "Server has been closed, Client will EXIT", "Notice",
							JOptionPane.INFORMATION_MESSAGE);
					// Client Exit
					System.exit(0);
				}
				// Server has confirmed current client window close
				else if (type == CharacterUtil.CLOSE_CLIENT_WINDOW_CONFIRMATION)
				{
					try
					{
						// close input/output stream, close socket
						this.getSocket().getInputStream().close();
						this.getSocket().getOutputStream().close();
						this.getSocket().close();

					} catch (Exception ex)
					{

					} finally
					{

						System.exit(0);// exit client program
					}

				}

			}

		} catch (Exception ex)
		{
			ex.printStackTrace();
		}

	}

}
