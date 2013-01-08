package pro.geektalk.alcher;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import org.powerbot.core.event.events.MessageEvent;
import org.powerbot.core.event.listeners.MessageListener;
import org.powerbot.core.event.listeners.PaintListener;
import org.powerbot.core.script.ActiveScript;
import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.core.script.job.state.Tree;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.input.Mouse.Speed;
import org.powerbot.game.api.methods.tab.Skills;
import org.powerbot.game.api.methods.widget.WidgetCache;
import org.powerbot.game.api.wrappers.widget.WidgetChild;
import org.powerbot.game.bot.Context;
import org.powerbot.game.client.Client;

import pro.geektalk.alcher.misc.Const;
import pro.geektalk.alcher.misc.Methods;
import pro.geektalk.alcher.misc.Variables;
import pro.geektalk.alcher.nodes.AlchFirstItem;
import pro.geektalk.alcher.nodes.AlchFourthItem;
import pro.geektalk.alcher.nodes.AlchSecondItem;
import pro.geektalk.alcher.nodes.AlchThirdItem;
import pro.geektalk.alcher.nodes.StopScript;

@Manifest(authors = { "OneLuckyDuck" }, name = "Hich Alchemy", description = "A task based high alchemy script", version = 0.1)
public class Alcher extends ActiveScript implements PaintListener,
		MessageListener {

	public static Tree jobContainer = null;
	public static ArrayList<Node> jobs = new ArrayList<Node>();
	static Client client;
	private final RenderingHints antialiasing = new RenderingHints(
			RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

	public void onStart() {
		Methods.showMessageBox(
				"KEY BIND!",
				"Make sure to have the high alchemy spell binded to the '0' key. The script will not function otherwise.\n\n Any bugs please report in full detail on the thread. \n\nThanks for using!");
		Task.sleep(3500);
		Mouse.setSpeed(Speed.FAST);
		Variables.startTime = System.currentTimeMillis();
		Variables.startingExperience = Skills.getExperience(Skills.MAGIC);
		Variables.startingLevel = Skills.getLevel(Skills.MAGIC);
		getContainer().submit(new StopScript());
	}

	@Override
	public int loop() {
		if (Game.getClientState() != Game.INDEX_MAP_LOADED) {
			return 2500;
		}
		if (client != Context.client()) {
			WidgetCache.purge();
			Context.get().getEventManager().addListener(this);
			client = Context.client();
		}
		if (jobContainer != null) {
			final Node job = jobContainer.state();
			if (job != null) {
				jobContainer.set(job);
				getContainer().submit(job);
				job.join();
			}
		} else {
			jobs.add(new AlchFirstItem());
			jobs.add(new AlchSecondItem());
			jobs.add(new AlchThirdItem());
			jobs.add(new AlchFourthItem());
			jobContainer = new Tree(jobs.toArray(new Node[jobs.size()]));
		}
		return 100;
	}

	@Override
	public void onRepaint(Graphics g1) {
		final Point mouse = Mouse.getLocation();
		final Graphics2D g = (Graphics2D) g1;
		final Dimension game = Game.getDimensions();
		g.setRenderingHints(antialiasing);

		final NumberFormat df = DecimalFormat.getInstance();
		final int gain = Skills.getExperience(Skills.MAGIC)
				- Variables.startingExperience;

		final int xpHourly = Methods.getPerHour(gain, Variables.startTime);

		final String gainedExperience = df.format(gain);
		final String xpHour = df.format(xpHourly);

		final int alchsHour = Methods.getPerHour(Variables.alchs,
				Variables.startTime);

		final String alchs = df.format(Variables.alchs);
		final String alchsHourly = df.format(alchsHour);

		final int coinsHour = Methods.getPerHour(Variables.coinsMade,
				Variables.startTime);

		final String coins = df.format(Variables.coinsMade);
		final String coinsHourly = df.format(coinsHour);

		// -- Fill top bar
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, (int) Game.getDimensions().getWidth(), 50);

		g.setColor(Color.GRAY);
		g.setFont(new Font("Arial", Font.BOLD, 11));
		g.drawString("Run Time: " + Const.TIMER.toElapsedString(), 3, 12);
		g.drawString(String.format("Experience Gained (hr): %s (%s)",
				gainedExperience, xpHour), 3, 25);
		g.drawString(String.format("Alchs (hr): %s (%s)", alchs, alchsHourly),
				3, 38);
		g.drawString(
				String.format("Level info: %d/%d",
						Skills.getLevel(Skills.MAGIC), Variables.startingLevel),
				210, 12);
		g.drawString(String.format("Alching Item: %d", Variables.slot), 210, 25);
		g.drawString(
				String.format("Coins Made (hr): %s (%s)", coins, coinsHourly),
				210, 38);

		// -- Mouse
		g.setColor(Mouse.isPressed() ? Color.WHITE.brighter() : Color.BLACK
				.darker());
		g.drawLine(mouse.x + game.width, mouse.y, mouse.x - game.width, mouse.y);
		g.drawLine(mouse.x, mouse.y + game.height, mouse.x, mouse.y
				- game.height);

		// -- Status and label
		g.setColor(Color.GRAY);
		g.setFont(new Font("Kristen ITC", Font.BOLD, 11));
		g.drawString("High Alchemy by OneLuckyDuck", 5, 310);

		Graphics2D g2 = (Graphics2D) g.create();
		g2.setColor(Color.BLUE);
		g2.setFont(new Font("Arial", Font.BOLD, 12));
		g2.drawImage(Const.BAR, 0, 510, null, null);
		g2.drawString("Status: " + Variables.status, 15, 522);

	}

	@Override
	public void messageReceived(MessageEvent msg) {
		final String m = msg.getMessage();
		if (m.toLowerCase().contains("have been added ")) {
			Variables.alchs++;
			final WidgetChild wc = Widgets.get(137, 58).getChild(5);
			String s = wc.getText();
			String text = s.substring(0, s.indexOf("co") - 1);
			if (text.contains(",")) {
				text = text.replaceAll(",", "");
			}
			Variables.coinsMade += Integer.parseInt(text);
		}

	}

}