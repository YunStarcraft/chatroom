package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.JFrame;

import util.CharacterUtil;

public class ConnectThread extends Thread
{
	private JFrame frame; // current server instance (server is sub-class of
							// JFrame)
	private ServerSocket serverSocket; // TCP method
	private Socket socket; // return from accept()
	private InputStream inputStream;
	private OutputStream outputStream;

	public ConnectThread(JFrame frame, String threadName, int port)
	{
		this.frame = frame;
		this.setName(threadName);// thread name

		try
		{
			serverSocket = new ServerSocket(port);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void run()
	{
		System.out.println("entering conenct thread,waiting clients");
		while (true)
		{

			try
			{
				this.socket = this.serverSocket.accept();

				System.out.println("get one connection request from client!");
				inputStream = socket.getInputStream();
				outputStream = socket.getOutputStream();

				byte[] buf = new byte[1024];
				int length = inputStream.read(buf);

				String info = new String(buf, 0, length);// message from client
				int index = info.lastIndexOf("@@@");
				String username = info.substring(0, index);
				int lastIndex = info.lastIndexOf("@");
				String clientPort = info.substring(lastIndex + 1);

				Server server = (Server) frame;
				Map map = server.getMap();

				if (CharacterUtil.isUsernameDuplicated(map, username))
				{
					String error = CharacterUtil.ERROR;
					outputStream.write(error.getBytes());
					inputStream.close();
					outputStream.close();
					socket.close();
				} else
				{
					String success = CharacterUtil.SUCCESS;
					String info2 = success + "@@@" + CharacterUtil.PORT + "_" + CharacterUtil.PORT2;

					map.put(username, clientPort); // put new username and port
													// into map

					server.setUserList(); // update user list

					outputStream.write(info2.getBytes());
					inputStream.close();
					outputStream.close();
					socket.close();

					Set set = map.keySet();
					Iterator iterator = set.iterator();
					StringBuffer sb = new StringBuffer();

					while (iterator.hasNext())
					{
						String name = (String) iterator.next();
						sb.append(name + "\n");
					}

					String userList = sb.toString(); // get user list

					System.out.println("current user list is "+ userList);
					Set set2 = map.entrySet();
					Iterator iterator2 = set2.iterator();

					while (iterator2.hasNext()) //send new UserList to all the clients
					{
						Map.Entry me = (Map.Entry) iterator2.next();
						String temp = (String) me.getValue();

						int first_index = temp.indexOf("_");
						int last_index = temp.lastIndexOf("_");
						int port = Integer.parseInt(temp.substring(first_index + 1, last_index));
						String address = temp.substring(last_index + 1);

						System.out.println("address of client to connect is "+ address);
						System.out.println("port of client to connect is "+ port);
						
						InetAddress clientAddress = InetAddress.getByName(address);

						
						Socket s = new Socket(clientAddress, port);
						OutputStream os = s.getOutputStream();
						os.write(userList.getBytes());
						os.close();
						s.close();

					}

				}

			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
