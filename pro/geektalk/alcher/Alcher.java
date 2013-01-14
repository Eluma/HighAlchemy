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
import org.powerbot.core.script.job.state.Node;
import org.powerbot.core.script.job.state.Tree;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.input.Mouse.Speed;
import org.powerbot.game.api.methods.tab.Skills;
import org.powerbot.game.api.methods.widget.WidgetCache;
import org.powerbot.game.bot.Context;
import org.powerbot.game.client.Client;

import pro.geektalk.alcher.loops.Antiban;
import pro.geektalk.alcher.loops.StopScript;
import pro.geektalk.alcher.misc.Const;
import pro.geektalk.alcher.misc.Methods;
import pro.geektalk.alcher.misc.Variables;
import pro.geektalk.alcher.nodes.AlchFirstItem;
import pro.geektalk.alcher.nodes.AlchFourthItem;
import pro.geektalk.alcher.nodes.AlchSecondItem;
import pro.geektalk.alcher.nodes.AlchThirdItem;

@Manifest(authors = { "OneLuckyDuck" }, name = "Hich Alchemy", description = "A task based high alchemy script", version = 1.03, website = "http://www.powerbot.org/community/topic/896258-high-alcher-fast-task-system-open-source/")
public class Alcher extends ActiveScript implements PaintListener,
		MessageListener {

	public static Tree jobContainer = null;
	public static ArrayList<Node> jobs = new ArrayList<Node>();
	static Client client;
	private final RenderingHints antialiasing = new RenderingHints(
			RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

	public void onStart() {
		Methods.showGui();
		Mouse.setSpeed(Speed.FAST);
		Variables.startTime = System.currentTimeMillis();
		Variables.startingExperience = Skills.getExperience(Skills.MAGIC);
		Variables.startingLevel = Skills.getLevel(Skills.MAGIC);
		getContainer().submit(new StopScript());
		getContainer().submit(new Antiban());
	}

	@Override
	public int loop() {
		if (Variables.guiIsDone) {
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
		} else {
			return 500;
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
				220, 12);
		g.drawString(String.format("Alching Item: %d", Variables.slot), 220, 25);
		g.drawString(
				String.format("Coins Made (hr): %s (%s)", coins, coinsHourly),
				220, 38);

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
			String text = m.substring(0, m.indexOf("co") - 1);
			if (text.contains(",")) {
				text = text.replaceAll(",", "");
			}
			Variables.coinsMade += Integer.parseInt(text);
		}
	}
}