package client;

import java.awt.Component;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import util.CharacterUtil;

public class Client extends JFrame
{
	private GridBagLayout gridbag;

	private JButton jButton1;

	private JButton jButton2;

	private JLabel jLabel1;

	private JLabel jLabel2;

	private JLabel jLabel3;

	private JPanel jPanel;

	private JTextField username;

	private JTextField hostAddress;

	private JTextField port;

	private Thread thread;

	public Client(String name)
	{
		super(name);

		initComponents(); // initialize UI
	}

	private void initComponents()
	{
		jPanel = new JPanel();

		jLabel1 = new JLabel();
		jLabel2 = new JLabel();
		jLabel3 = new JLabel();

		username = new JTextField(15);
		hostAddress = new JTextField(15);
		port = new JTextField(15);

		jButton1 = new JButton();
		jButton2 = new JButton();

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setAlwaysOnTop(true);
		this.setResizable(false);

		jPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("User Login"));

		jLabel1.setText("UserName");
		jLabel2.setText("ServerName");
		jLabel3.setText("Port");

		jButton1.setText("Login");
		jButton2.setText("Reset");

		/**********************************************************
		 * Grid Layout Test Starts
		 **********************************************************/
		/*
		 * this.gridbag = new GridBagLayout();
		 * 
		 * GridBagConstraints constraints;
		 * 
		 * 
		 * 
		 * addComponent(jLabel1, 0, 0, 1, 1, 10, 100, GridBagConstraints.NONE,
		 * GridBagConstraints.EAST); addComponent(username, 1, 0, 9, 1, 90, 100,
		 * GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
		 * addComponent(jLabel2, 0, 1, 1, 1, 10, 100, GridBagConstraints.NONE,
		 * GridBagConstraints.EAST); addComponent(hostAddress, 1, 1, 9, 1, 90,
		 * 100, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
		 * addComponent(jLabel3, 0, 2, 1, 1, 10, 100, GridBagConstraints.NONE,
		 * GridBagConstraints.EAST); addComponent(port, 1, 2, 9, 1, 90, 100,
		 * GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST); //
		 * addComponent(jButton1, 5, 2, 1, 1, 10, 100, //
		 * GridBagConstraints.NONE, GridBagConstraints.EAST); //
		 * addComponent(bcc, 6, 2, 4, 1, 40, 100, //
		 * GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST); //
		 * 
		 * jPanel.setLayout(gridbag);
		 * 
		 */

		/**********************************************************
		 * Grid Layout Test Ends
		 **********************************************************/

		jButton1.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Client.this.login(e);
			}
		});

		jButton2.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{

				Client.this.jButtonActionPerformed(e);
			}
		});
		username.setText("jerry");
		hostAddress.setText("localhost");
		port.setText("5555");

		jPanel.add(jLabel1);
		jPanel.add(username);
		jPanel.add(jLabel2);
		jPanel.add(hostAddress);
		jPanel.add(jLabel3);
		jPanel.add(port);

		jPanel.add(jButton1);
		jPanel.add(jButton2);

		this.getContentPane().add(jPanel);

		this.setSize(270, 300);
		this.setVisible(true);
	}

	private void login(ActionEvent event)
	{
		System.out.println("client login pressed");
		String username = this.username.getText();
		String hostAddress = this.hostAddress.getText();
		String hostPort = this.port.getText();

		if (CharacterUtil.isEmpty(username))
		{
			JOptionPane.showMessageDialog(this, "uername cant not be empty", "Alert", JOptionPane.WARNING_MESSAGE);
			this.username.requestFocus();
			return;
		}
		if (!CharacterUtil.isCorrect(username))
		{
			JOptionPane.showMessageDialog(this, "username cannot contain @ or \\", "Alert",
					JOptionPane.WARNING_MESSAGE);
			this.username.requestFocus();
			return;
		}

		if (CharacterUtil.isEmpty(hostAddress))
		{
			JOptionPane.showMessageDialog(this, "hostaddress cant not be empty", "Alert", JOptionPane.WARNING_MESSAGE);
			this.hostAddress.requestFocus();
			return;
		}
		if (CharacterUtil.isEmpty(hostPort))
		{
			JOptionPane.showMessageDialog(this, "port cant not be empty", "Alert", JOptionPane.WARNING_MESSAGE);
			this.port.requestFocus();
			return;
		}
		if (!CharacterUtil.isNumber(hostPort))
		{
			JOptionPane.showMessageDialog(this, "port must be number", "Alert", JOptionPane.WARNING_MESSAGE);
			this.port.requestFocus();
			return;

		}

		if (!CharacterUtil.isPortCorrect(hostPort))
		{
			JOptionPane.showMessageDialog(this, "port number must be legal", "Alert", JOptionPane.WARNING_MESSAGE);
			this.port.requestFocus();
			return;
		}

		System.out.println("input check passed, try to conenct server");
		// if program reaches here, all input is legal
		int portInt = Integer.parseInt(hostPort);

		// CharacterUtil.SERVER_HOST = hostAddress; //server address
		// CharacterUtil.CLIENT_NAME = username;

		ClientConnectThread clientConnectThread = new ClientConnectThread(this, hostAddress, portInt, username);

		if(clientConnectThread.login())
		{
			clientConnectThread.start();

		}
		else 
		{
			JOptionPane.showMessageDialog(this, "Login Failed, Start Server First.No Duplicate Name Allowed", "Error",
					JOptionPane.INFORMATION_MESSAGE);
		
		}

	}

	private void jButtonActionPerformed(ActionEvent evt)
	{
		this.username.setText("");
		this.hostAddress.setText("");
		this.port.setText("");

	}

	public static void main(String[] args)
	{
		System.out.println("client start");
		new Client("User Login");
	}

}
