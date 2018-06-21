import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.border.BevelBorder;

public class OutlinePing extends JFrame {

	public OutlinePing() {
		super("Network Scanner");
		Font myFont = new Font("Serif", Font.BOLD, 16);
		// menu begin
		JMenuBar menubar = new JMenuBar();
		setJMenuBar(menubar);

		JMenu scanMenu = new JMenu("Scan");
		JMenu gotoMenu = new JMenu("Go to");
		JMenu commandsMenu = new JMenu("Commands");
		JMenu favoritesMenu = new JMenu("Favorite");
		JMenu toolsMenu = new JMenu("Tools");
		JMenu helpMenu = new JMenu("Help");

		menubar.add(scanMenu);
		menubar.add(gotoMenu);
		menubar.add(commandsMenu);
		menubar.add(favoritesMenu);
		menubar.add(toolsMenu);
		menubar.add(helpMenu);

		JMenuItem loadFromFileAction = new JMenuItem("Load from file...");
		JMenuItem exportAllAction = new JMenuItem("Export all...");
		JMenuItem exportSelectionAction = new JMenuItem("Export selection...");
		JMenuItem quitAction = new JMenuItem("Quit");

		scanMenu.add(loadFromFileAction);
		scanMenu.add(exportAllAction);
		scanMenu.add(exportSelectionAction);
		scanMenu.add(quitAction);

		JMenuItem nextAliveHostAction = new JMenuItem("Next alive host");
		JMenuItem nextOpenPortAction = new JMenuItem("Next open port");
		JMenuItem NextDeadHostAction = new JMenuItem("Next dead host");
		JMenuItem previousAliveHostAction = new JMenuItem("previous alive host");
		JMenuItem previousOpenPortAction = new JMenuItem("previous open port");
		JMenuItem previousDeadHostAction = new JMenuItem("Previous dead host");
		JMenuItem findAction = new JMenuItem("Find...");

		gotoMenu.add(nextAliveHostAction);
		gotoMenu.add(nextOpenPortAction);
		gotoMenu.add(NextDeadHostAction);
		gotoMenu.addSeparator();
		gotoMenu.add(previousAliveHostAction);
		gotoMenu.add(previousOpenPortAction);
		gotoMenu.add(previousDeadHostAction);
		gotoMenu.addSeparator();
		gotoMenu.add(findAction);

		JMenuItem showDetailsAction = new JMenuItem("Show details");
		JMenuItem rescanIPsAction = new JMenuItem("Rescan IP(s)");
		JMenuItem deleteIPsAction = new JMenuItem("Delete IP(s)");
		JMenuItem copyIPAction = new JMenuItem("Copy IP");
		JMenuItem copyDetailsAction = new JMenuItem("Copy details");
		JMenuItem openaction = new JMenuItem("Open");

		commandsMenu.add(showDetailsAction);
		commandsMenu.addSeparator();
		commandsMenu.add(rescanIPsAction);
		commandsMenu.add(deleteIPsAction);
		commandsMenu.addSeparator();
		commandsMenu.add(copyIPAction);
		commandsMenu.add(copyDetailsAction);
		commandsMenu.addSeparator();
		commandsMenu.add(openaction);

		JMenuItem addCurrentAction = new JMenuItem("Add current...");
		JMenuItem manageFavoritesAction = new JMenuItem("Manage favorites...");

		favoritesMenu.add(addCurrentAction);
		favoritesMenu.add(manageFavoritesAction);

		JMenuItem preferencesAction = new JMenuItem("Preferences...");
		JMenuItem fetchersAction = new JMenuItem("Fetchers...");
		JMenuItem selectionAction = new JMenuItem("Selection");
		JMenuItem scanStatisticsAction = new JMenuItem("Scan statistics");

		toolsMenu.add(preferencesAction);
		toolsMenu.add(fetchersAction);
		toolsMenu.addSeparator();
		toolsMenu.add(selectionAction);
		toolsMenu.add(scanStatisticsAction);

		JMenuItem gettingStatedAction = new JMenuItem("Getting Stated");
		JMenuItem officialWebsiteAction = new JMenuItem("Official website");
		JMenuItem faqAction = new JMenuItem("FAQ");
		JMenuItem reportAnIssueAction = new JMenuItem("Report an issue");
		JMenuItem pluginsAction = new JMenuItem("Plugins");
		JMenuItem commandLineUsageAction = new JMenuItem("Command-line usage");
		JMenuItem checkForNewerVersionAction = new JMenuItem("Check for newer version...");
		JMenuItem aboutAction = new JMenuItem("About");

		helpMenu.add(gettingStatedAction);
		helpMenu.add(officialWebsiteAction);
		helpMenu.add(faqAction);
		helpMenu.add(reportAnIssueAction);
		helpMenu.add(pluginsAction);
		helpMenu.add(commandLineUsageAction);
		helpMenu.add(checkForNewerVersionAction);
		helpMenu.add(aboutAction);

		quitAction.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.exit(0);
			}
		});
		// menu end

		// statusbar begin

		JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		add(statusPanel, BorderLayout.SOUTH);
		JLabel readyLabel = new JLabel("Ready");
		JLabel displayLabel = new JLabel("Display All");
		JLabel threadLabel = new JLabel("Thread: 0");
		statusPanel.add(readyLabel);
		statusPanel.add(displayLabel);
		statusPanel.add(threadLabel);
		readyLabel.setBorder(new BevelBorder(BevelBorder.RAISED));
		displayLabel.setBorder(new BevelBorder(BevelBorder.RAISED));
		threadLabel.setBorder(new BevelBorder(BevelBorder.RAISED));
		readyLabel.setPreferredSize(new Dimension(300, 20));
		displayLabel.setPreferredSize(new Dimension(150, 20));
		threadLabel.setPreferredSize(new Dimension(150, 20));
		readyLabel.setFont(myFont);
		displayLabel.setFont(myFont);
		threadLabel.setFont(myFont);
		// statusbar end

		// table begin
		String titles[] = new String[] { "IP", "Ping", "TTL", "Hostname", "Ports[0+]" };
		Object[][] stats = initializeTableData();
		JTable jTable = new JTable(stats, titles);

		JScrollPane sp = new JScrollPane(jTable);
		add(sp, BorderLayout.CENTER);
		// table end

		// toolbar begin
		JToolBar toolbar1 = new JToolBar();
		toolbar1.setLayout(new FlowLayout(FlowLayout.LEFT));
		JToolBar toolbar2 = new JToolBar();
		toolbar2.setLayout(new FlowLayout(FlowLayout.LEFT));

		JLabel IbRangStart = new JLabel("IP Range:");
		JTextField tfRangeStart = new JTextField(10);
		JLabel IbRangend = new JLabel("to ");
		JTextField tfRangend = new JTextField(10);
		JComboBox combobox = new JComboBox();

		IbRangend.setFont(myFont);
		tfRangend.setPreferredSize(new Dimension(90, 30));
		IbRangStart.setFont(myFont);
		tfRangeStart.setPreferredSize(new Dimension(90, 30));

		JLabel lbHostname = new JLabel("Hostname: ");
		JTextField tfHostname = new JTextField(10);
		JButton btup = new JButton("IP");
		JComboBox cbOption = new JComboBox();
		cbOption.addItem("/24");
		cbOption.addItem("/26");
		combobox.addItem("IP Range");
		JButton btStart = new JButton("Start ");

		lbHostname.setFont(myFont);
		tfHostname.setPreferredSize(new Dimension(90, 30));
		btup.setPreferredSize(new Dimension(90, 30));
		cbOption.setPreferredSize(new Dimension(90, 30));
		btStart.setPreferredSize(new Dimension(90, 30));
		combobox.setPreferredSize(new Dimension(90, 30));

		toolbar1.add(IbRangStart);
		toolbar1.add(tfRangeStart);
		toolbar1.add(IbRangend);
		toolbar1.add(tfRangend);
		toolbar1.add(combobox);

		toolbar2.add(lbHostname);
		toolbar2.add(tfHostname);
		toolbar2.add(btup);
		toolbar2.add(cbOption);
		toolbar2.add(btStart);

		JPanel pane = new JPanel(new BorderLayout());
		pane.add(toolbar1, BorderLayout.NORTH);
		pane.add(toolbar2, BorderLayout.SOUTH);

		add(pane, BorderLayout.NORTH);

		// toolbar end

		String myIp = null;
		String myHostname = null;
		try {
			InetAddress ia = InetAddress.getLocalHost();

			myIp = ia.getHostAddress();
			myHostname = ia.getHostName();
		} catch (Exception e) {

		}
		String fixedIp = myIp.substring(0, myIp.lastIndexOf(".")+1);
		tfRangeStart.setText(fixedIp+"1");
		tfRangend.setText(fixedIp+"254");
		tfHostname.setText(myHostname);
		
		//System.out.println(myIp + "    " + myHostname);

		setSize(700, 700);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		
		btStart.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Pinging[] pi = new Pinging[254];
				for(int i=0; i<=253; i++) {
					pi[i] = new Pinging(fixedIp + (i+1));
					pi[i].start();
				}
				for(int i=0; i<=253; i++) {
					Object[] msg = pi[i].getMsg();
					if (msg[1] == null) {
						msg[1] = "[n/a]";
						msg[2] = "[n/s]";
						msg[3] = "[n/s]";
					} else if (msg[3] == null) {
						msg[3] = "[n/a]";
					}
					stats[i][0] = msg[0];
					stats[i][1] = msg[1];
					stats[i][2] = msg[2];
					stats[i][3] = msg[3];
					stats[i][4] = msg[4];
					
				}
				
				jTable.repaint();
			}
		});
	}

	
	public Object[][] initializeTableData() {
		Object[][] results = new Object[254][5];
		return results;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		OutlinePing op = new OutlinePing();
	}

}
