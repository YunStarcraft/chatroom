package server;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import util.CharacterUtil;
import util.XMLUtil;

public class Server extends JFrame
{
	// private JFrame jFrame;
	private JLabel jLabel1;
	private JLabel jLabel2;
	private JLabel jLabel3;

	private JButton jButton;
	private JPanel jPanel1;
	private JPanel jPanel2;

	private JScrollPane jScrollPane1;
	private JTextArea jTextArea1;
	private JTextField jTextField1;

	private static Thread thread;

	private Map<String, ServerMessageThread> map = new HashMap<String, ServerMessageThread>();

	// private Map map = new HashMap(); // map to check whether new client
	// username exist already

	public JTextField getjTextField1()
	{
		return jTextField1;
	}

	public JTextArea getjTextArea1()
	{
		return jTextArea1;
	}

	public void setjTextArea1(JTextArea jTextArea1)
	{
		this.jTextArea1 = jTextArea1;
	}

	public JButton getjButton()
	{
		return jButton;
	}

	public JLabel getjLabel2()
	{
		return jLabel2;
	}

	public Map getMap()
	{
		return map;
	}

	public void setMap(Map map)
	{
		this.map = map;
	}

	public Server(String name)
	{
		super("name"); // father class JFrame constructor
		this.initComponent();

	}

	// update user list on server side, display new one
	public void setUserList()
	{

		this.jTextArea1.setText("");// clear previous content

		for (Iterator iter = map.keySet().iterator(); iter.hasNext();)
		{
			String username = (String) iter.next();
			this.jTextArea1.append(username + "\n");
		}

	}

	// init UI
	private void initComponent()
	{
		jPanel1 = new JPanel();
		jPanel2 = new JPanel();

		jLabel1 = new JLabel();
		jLabel2 = new JLabel();
		jLabel3 = new JLabel();

		jTextField1 = new JTextField(10);
		jButton = new JButton();
		jScrollPane1 = new JScrollPane();
		jTextArea1 = new JTextArea();

		jPanel1.setBorder(BorderFactory.createTitledBorder("Server Info"));
		jPanel2.setBorder(BorderFactory.createTitledBorder("Online User List"));

		jLabel1.setText("Server Statue");
		jLabel2.setText("Stop");

		jLabel3.setForeground(new Color(204, 0, 51));
		jLabel3.setText("port number");

		jTextField1.setText("5555");

		jButton.setText("Start Server");
		jButton.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				execute(e);
				// TODO Auto-generated method stub

			}

		});

		/***
		 * add actionListner for CLOSE WINDOWS
		 */
		// inner class anonymous methods
		this.addWindowListener(new WindowAdapter()
		{
			@Override

			public void windowClosing(WindowEvent e)
			{
				try
				{
					Collection<ServerMessageThread> cols = Server.this.map.values();

					String messageXML = XMLUtil.constructCloseServerWindowXML();

					for (ServerMessageThread smt : cols)
					{

						smt.sendMessage(messageXML);
					}

				} catch (Exception ex)
				{
					ex.printStackTrace();
				} finally
				{
					System.exit(0);
				}

			}

		});

		jPanel1.add(jLabel1);
		jPanel1.add(jLabel2);
		jPanel1.add(jLabel3);
		jPanel1.add(jTextField1);
		jPanel1.add(jButton);

		jTextArea1.setEditable(false); // user-modified online list not allowed
		jTextArea1.setRows(20);
		jTextArea1.setColumns(30);
		jTextArea1.setForeground(new Color(0, 51, 204));

		jScrollPane1.setViewportView(jTextArea1); // JTextArea inside Scroll

		jPanel2.add(jScrollPane1);

		this.getContentPane().add(jPanel1, BorderLayout.NORTH);
		this.getContentPane().add(jPanel2, BorderLayout.SOUTH);

		this.setSize(500, 500);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setAlwaysOnTop(true);
		this.setResizable(false);
		// this.pack();
		this.setVisible(true);

	}

	// Button Listener Inner anonymous method
	private void execute(ActionEvent e)
	{

		System.out.println("button pressed");
		String hostPort = this.jTextField1.getText();
		/**
		 * input port check whether it is legal number
		 */

		if (CharacterUtil.isEmpty(hostPort))
		{
			JOptionPane.showMessageDialog(this, "port cant not be empty", "Alert", JOptionPane.WARNING_MESSAGE);
			this.jTextField1.requestFocus();
			return;
		}

		if (!CharacterUtil.isNumber(hostPort))
		{
			JOptionPane.showMessageDialog(this, "port must be number", "Alert", JOptionPane.WARNING_MESSAGE);
			this.jTextField1.requestFocus();
			return;

		}
		if (!CharacterUtil.isPortCorrect(hostPort))
		{
			JOptionPane.showMessageDialog(this, "port number must be legal", "Alert", JOptionPane.WARNING_MESSAGE);
			this.jTextField1.requestFocus();
			return;
		}

		/**
		 * Thread Creation Part!! This is the place Server side start
		 * multi-thread for the whole project
		 */

		int port = Integer.parseInt(hostPort);

		/**
		 * thread is waiting for clients to connect
		 */
		// thread = new ConnectThread(this,"Connect Thread",port);

		thread = new ServerConnection(this, "Connect Thread", port);
		thread.start();

		/**
		 * below is included in ServerConnection Thread
		 */
		// this.jButton.setText("running");
		// this.jLabel3.setText("running");
		// this.jTextField1.setEnabled(false);
		// this.jButton.setEnabled(false);

	}

	public static void main(String[] args)
	{
		Server server = new Server("Server");

	}
}
