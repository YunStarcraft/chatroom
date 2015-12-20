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
import javax.swing.JOptionPane;

import util.CharacterUtil;
import util.XMLUtil;

public class ServerConnection extends Thread
{
	// private JFrame frame; // current server instance (server is sub-class of
	// JFrame)
	private ServerSocket serverSocket; // TCP method
	private Socket socket; // return from accept()
	private Server server; // current server

	private InputStream inputStream;
	private OutputStream outputStream;

	public ServerConnection(Server server, String threadName, int port)
	{

		this.setName(threadName);// thread name

		try
		{
			this.server = server;
			this.serverSocket = new ServerSocket(port);

			this.server.getjLabel2().setText("running");
			this.server.getjButton().setEnabled(false);
			this.server.getjTextField1().setEditable(false);

		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(this.server, "port has been occupied ", "Warning", JOptionPane.ERROR_MESSAGE);
		}

	}

	@Override
	public void run()
	{
		System.out.println("entering ServerConnection thread,waiting clients");
		while (true)
		{

			try
			{
				/******************************************************
				 * 
				 * client login request check if succeed , send success message
				 * to client
				 * 
				 * 
				 *****************************************************/

				this.socket = this.serverSocket.accept();

				System.out.println("get one connection request from client!");
				InputStream is = socket.getInputStream();
				OutputStream os = socket.getOutputStream();

				byte[] buf = new byte[1000];
				int length = is.read(buf);

				String loginXML = new String(buf, 0, length);// message from
																// client

				// extract username from clients login message
				String username = XMLUtil.extractUsername(loginXML);

				// check whether user login succeed
				boolean isLogin = false;

				String loginResult = null;
				// Check username legal(duplicate)?

				if (this.server.getMap().containsKey(username))
				{
					// duplicate username

					loginResult = "failure";
					isLogin = false;
				} else
				{
					// Unique username, login succeed
					loginResult = "success";
					isLogin = true;

				}

				// response to user login request, succeed or fail message sent
				// to client
				String xml = XMLUtil.constructLoginResultXML(loginResult);
				os.write(xml.getBytes());

				if (isLogin)
				{

					/******************************************************
					 * 
					 * create new thread for each linked client to communicate
					 * this thread has to be created after confirmation message sent to client
					 * Don't mess up client confirmation message with thread!!!
					 * 
					 *****************************************************/

					ServerMessageThread serverMessageThread = new ServerMessageThread(this.server, this.socket);

					// put client username with message thread into map
					this.server.getMap().put(username, serverMessageThread);

					// update userlist
					serverMessageThread.updataUserList();
					serverMessageThread.start();

				}

			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
