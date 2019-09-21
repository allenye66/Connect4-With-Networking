package netgame.connect4;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import grid.Location;
import netgame.common.Hub;


/**
 *  A main class for the network connect4 game.  Main routine
 *  shows a dialog where the user can choose to be a server or
 *  a client.  If the user chooses to be a server, then a connect4Hub
 *  is created to manage the game; the game will not start until a
 *  second player has connected as a client.  To act as a client,
 *  the user must know the host name or IP address of the computer
 *  and the port number where the server is waiting for a connection.
 *  When run as a client, this program does not create a hub;
 *  rather, it connects to the hub that was created by the server.
 *  In either case, a connect4Window is created where the game will 
 *  be played. .
 */
public class Main 
{

    private static final int DEFAULT_PORT = 45017;
    private static final int DEFAULT_CONNECT_SIZE = 4;
    
    public static void main(String[] args) 
    {
        
        // First, construct a panel that will be placed into a JOptionPane confirm dialog.
        
        JLabel message = new JLabel("Welcome to Networked Connect4!", JLabel.CENTER);
        message.setFont(new Font("Serif", Font.BOLD, 16));
        
        final JRadioButton selectMPMode = new JRadioButton("Multi-player Game");
        final JRadioButton selectSPMode = new JRadioButton("Single player Game");
        
        final JTextField listeningPortInput = new JTextField("" + DEFAULT_PORT, 5);
        final JTextField connectNInput = new JTextField("" + DEFAULT_CONNECT_SIZE, 5);
        final JTextField hostInput = new JTextField(30);
        final JTextField connectPortInput = new JTextField("" + DEFAULT_PORT, 5);
        
        
        final JRadioButton selectServerMode = new JRadioButton("Start a new game");
        final JRadioButton selectClientMode = new JRadioButton("Connect to existing game");
        
        // ************** set for first frame  
        
        ButtonGroup groupMode = new ButtonGroup();
        groupMode.add(selectMPMode);
        groupMode.add(selectSPMode);
        ActionListener radioListenerMode = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == selectMPMode) {
                    
                }
                else {
                   
                }
            }
        };
        selectMPMode.addActionListener(radioListenerMode);
        selectSPMode.addActionListener(radioListenerMode);
        selectMPMode.setSelected(true);
        
        ButtonGroup group = new ButtonGroup();
        group.add(selectServerMode);
        group.add(selectClientMode);
        ActionListener radioListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == selectServerMode) {
                    listeningPortInput.setEnabled(true);
                    hostInput.setEnabled(false);
                    connectPortInput.setEnabled(false);
                    listeningPortInput.setEditable(true);
                    hostInput.setEditable(false);
                    connectPortInput.setEditable(false);
                }
                else {
                    listeningPortInput.setEnabled(false);
                    hostInput.setEnabled(true);
                    connectPortInput.setEnabled(true);
                    listeningPortInput.setEditable(false);
                    hostInput.setEditable(true);
                    connectPortInput.setEditable(true);
                }
            }
        };
        selectServerMode.addActionListener(radioListener);
        selectClientMode.addActionListener(radioListener);
        selectServerMode.setSelected(true);
        hostInput.setEnabled(false);
        connectPortInput.setEnabled(false);
        hostInput.setEditable(false);
        connectPortInput.setEditable(false);
        
        JPanel ModePanel = new JPanel();
        ModePanel.setLayout(new GridLayout(0,4,5,5));
        ModePanel.setBorder(BorderFactory.createCompoundBorder(
                     BorderFactory.createLineBorder(Color.BLACK, 2),
                     BorderFactory.createEmptyBorder(6,6,6,6) ));
        
        ModePanel.add(message);
        JPanel rowMode;
        ModePanel.add(selectMPMode);
        ModePanel.add(selectSPMode);
        
        

        int actionMode = JOptionPane.showConfirmDialog(null, ModePanel, "Connect4 Networking", 
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (actionMode != JOptionPane.OK_OPTION)
                return;
                
        if (selectMPMode.isSelected()) {
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(0,1,5,5));
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
                     BorderFactory.createLineBorder(Color.BLACK, 2),
                     BorderFactory.createEmptyBorder(6,6,6,6) ));
        
        inputPanel.add(message);
        
        JPanel row;
        
        inputPanel.add(selectServerMode);
        
        row = new JPanel();
        row.setLayout(new FlowLayout(FlowLayout.LEFT));
        row.add(Box.createHorizontalStrut(40));
        row.add(new JLabel("Listen on port: "));
        row.add(listeningPortInput);
        inputPanel.add(row);
        
        row = new JPanel();
        row.setLayout(new FlowLayout(FlowLayout.RIGHT));
        row.add(Box.createHorizontalStrut(40));
        row.add(new JLabel("Connected Size (less than 8): "));
        row.add(connectNInput);
        inputPanel.add(row);
        
        inputPanel.add(selectClientMode);
        
        row = new JPanel();
        row.setLayout(new FlowLayout(FlowLayout.LEFT));        
        row.add(Box.createHorizontalStrut(40));
        row.add(new JLabel("Computer: "));
        row.add(hostInput);
        inputPanel.add(row);

        row = new JPanel();
        row.setLayout(new FlowLayout(FlowLayout.LEFT));
        row.add(Box.createHorizontalStrut(40));
        row.add(new JLabel("Port Number: "));
        row.add(connectPortInput);
        inputPanel.add(row);
        
        
        // Show the dialog, get the user's response and -- if the user doesn't
        // cancel -- start a game.  If the user chooses to run as the server
        // then a connect4GameHub (server) is created and after that a connect4Window
        // is created that connects to the server running on  localhost, which was
        // just created.  In that case, the game will wait for a second connection. 
        // If the user chooses to connect to an existing server, then only
        // a connect4Window is created, that will connect to the specified
        // host where the server is running.
        int connectN;
        while (true) // Repeats until a game is started or the user cancels.
        {  

            int action = JOptionPane.showConfirmDialog(null, inputPanel, "ConnectN Networking", 
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
            if (action != JOptionPane.OK_OPTION)
                return;
            if (selectServerMode.isSelected()) {
                int port;
                try {
                    port = Integer.parseInt(listeningPortInput.getText().trim());
                    if (port <= 0)
                        throw new Exception();
                }
                catch (Exception e) {
                    message.setText("Illegal port number!");
                    listeningPortInput.selectAll();
                    listeningPortInput.requestFocus();
                    continue;
                }
                try {
                	connectN = Integer.parseInt(connectNInput.getText().trim());
                    if (connectN <= 0 || connectN>=8)
                        throw new Exception();
                }
                catch (Exception e) {
                    message.setText("Illegal Connect size!");
                    listeningPortInput.selectAll();
                    listeningPortInput.requestFocus();
                    continue;
                }
                Hub hub;
                try 
                {
                    hub = new Connect4GameHub(port); //creates hub
                }
                catch (Exception e)
                {
                    message.setText("Error: Can't listen on port " + port);
                    listeningPortInput.selectAll();
                    listeningPortInput.requestFocus();
                    continue;
                }
                try {
                	boolean gameStart=true;
                	Connect4Window win= new Connect4Window("localhost",port,true); //creates client window
                	Connect4Game game=win.getGame(); //access game
                	win.connectN=connectN;
                	win.gameMode=1;
                	game.gameMode=1;
                	while(gameStart) //loops everytime game restarts
                	{
            			if (win.getgameOverFlag()==false) 
            			{
            				game.playGame(); //loops in Connect4Game until game is over
            				win.setgameOverFlag(true);
            				win.connection.send( new int[] {-1, -1} );  // -1: let HUB knows gameOver
            				try
            				{ 
            					Thread.sleep(30000); 
        					}
            				catch (InterruptedException e)
            				{ 
            					System.out.println("InterruptedException occurred."); 
        					}
            				win.connection.send( new int[] {-2, -2} ); // -2: let HUB restarts game
            				System.out.println("Start new Game " + win.getID());
            				win.setgameOverFlag(false);
            				game.receiveCount=2;
            				game.playerIndex = 0;
            				int gridLength=win.gridLength;
            				for(int i = 0;i < gridLength; i++)
            					for(int j = 0; j < win.gridLength; j++) game.world.remove(new Location(i,j));
            				//game.world.add(new Location(gridLength/2-1, gridLength/2-1), new Piece(Color.RED));
            				//game.world.add(new Location(gridLength/2-1, gridLength/2), new Piece(Color.BLUE));
            				//game.world.add(new Location(gridLength/2, gridLength/2-1), new Piece(Color.BLUE));
            				//game.world.add(new Location(gridLength/2, gridLength/2), new Piece(Color.RED));
            			}
                	  }
                }
                catch (IOException e)
                {
                    message.setText("Could not connect to server on localhost!!");
                    hub.shutDownHub();
                    continue;
                }
                break;
            }
            else //Player #2
            {
                String host;
                int port;
                host = hostInput.getText().trim();
                host="localhost";
                if (host.length() == 0) 
                {
                    message.setText("You must enter a computer name!");
                    hostInput.requestFocus();
                    continue;
                }
                try 
                {
                    port = Integer.parseInt(connectPortInput.getText().trim());
                    if (port <= 0)
                        throw new Exception();
                }
                catch (Exception e) 
                {
                    message.setText("Illegal port number!");
                    connectPortInput.selectAll();
                    connectPortInput.requestFocus();
                    continue;
                }
                try {
                	boolean gameStart=true;
                	Connect4Window win= new Connect4Window(host,port,true);
                	Connect4Game game=win.getGame();
                	win.connectN=4;
                	win.gameMode=1;
                	game.gameMode=1;
                	while(gameStart) {
            			if (win.getgameOverFlag()==false) {
            				game.playGame();
            				win.setgameOverFlag(true);
            				win.connection.send( new int[] {-1, -1} );  // -1: let HUB knows gameOver
            				try
            				{ Thread.sleep(30000); }
            				catch (InterruptedException e)
            				{ System.out.println("InterruptedException occurred."); }
            				win.connection.send( new int[] {-2, -2} ); // -2: let HUB restarts game
            				System.out.println("Start new Game " + win.getID());
            				win.setgameOverFlag(false);
            				game.receiveCount=2;
            				game.playerIndex = 0;
            				int gridLength=win.gridLength;
            				for(int i=0;i<gridLength;i++)
            					for(int j=0;j<win.gridLength;j++) game.world.remove(new Location(i,j));
            				//game.world.add(new Location(gridLength/2-1, gridLength/2-1), new Piece(Color.RED));
            				//game.world.add(new Location(gridLength/2-1, gridLength/2), new Piece(Color.BLUE));
            				//game.world.add(new Location(gridLength/2, gridLength/2-1), new Piece(Color.BLUE));
            				//game.world.add(new Location(gridLength/2, gridLength/2), new Piece(Color.RED));
            			}
                	}
                }
                catch (IOException e) {
                    message.setText("Could not connect to specified host and port.");
                    hostInput.selectAll();
                    hostInput.requestFocus();
                    continue;
                }
                break;
            }
        }
        }
        
    }
}