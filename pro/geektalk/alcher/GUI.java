package pro.geektalk.alcher;

import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import pro.geektalk.alcher.misc.Variables;

public class GUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	GridLayout grid;
	Container pane;

	JLabel label1 = new JLabel(
			"Make sure to have the high alchemy spell binded to the '0' key. The script will not function otherwise.\n\n Any bugs please report in full detail on the thread. \n\nThanks for using!");

	JButton btnStart = new JButton("Start");

	public GUI() {
		pane = getContentPane();
		grid = new GridLayout(2, 0);

		this.setTitle("High Alcher by OneLuckyDuck");
		this.setLayout(grid);
		this.setSize(975, 200);
		
		label1.setFont(new Font("Arial", Font.BOLD, 12));
		pane.add(label1);

		btnStart.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				startScript(e);

			}

		});
		pane.add(btnStart);

	}

	public void startScript(ActionEvent e) {
		Variables.guiIsDone = true;
		this.dispose();
	}
}
