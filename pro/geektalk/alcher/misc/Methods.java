package pro.geektalk.alcher.misc;

import java.awt.Image;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.Tabs;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Lobby;
import org.powerbot.game.api.wrappers.node.Item;
import org.powerbot.game.api.wrappers.widget.Widget;
import org.powerbot.game.api.wrappers.widget.WidgetChild;
import org.powerbot.game.bot.Context;

public class Methods {

	public static void showMessageBox(final String title, final String message) {
		try {
			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {
					JOptionPane.showMessageDialog(null, message, title,
							JOptionPane.INFORMATION_MESSAGE);

				}

			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static int getPlayerAnimation() {
		return Players.getLocal().getAnimation();
	}

	public static boolean isSlotEmpty(final int slot) {
		return Inventory.getItemAt(slot) == null;
	}

	public static boolean isWidgetChildVisible(final WidgetChild wc) {
		return wc != null && wc.validate() && wc.visible() && wc.isOnScreen();
	}

	public static boolean isWidgetVisible(final Widget w) {
		return w != null && w.validate();
	}

	public static boolean inventoryContains(final int id) {
		if (Tabs.getCurrent() != Tabs.INVENTORY) {
			Tabs.INVENTORY.open();
		}
		return isItemVisible(Inventory.getItem(id))
				&& Inventory.getCount(id) > 0;
	}

	public static boolean isInGame() {
		return Game.isLoggedIn() && Game.getClientState() == 11
				&& !Context.resolve().refreshing && !Lobby.isOpen();
	}

	public static void stopScript(final String s) {
		s(s);
		Context.get().getScriptHandler().shutdown();
	}

	public static void s(final String s) {
		Variables.status = s;
		System.out.println(String.format("[High Alcher] %s", s));
	}

	public static int getPerHour(final int base, final long time) {
		return (int) ((base) * 3600000D / (System.currentTimeMillis() - time));
	}

	private static boolean isItemVisible(final Item i) {
		return i != null && i.getWidgetChild().validate()
				&& i.getWidgetChild().visible()
				&& i.getWidgetChild().isOnScreen();
	}

	public static int[] listToArray(final List<Integer> list) {
		int[] ret = new int[list.size()];
		for (int i = 0; i < ret.length; ++i) {
			ret[i] = list.get(i).intValue();
		}
		return ret;
	}

	public static Image getImage(final String url) {
		try {
			return ImageIO.read(new URL(url));
		} catch (IOException e) {
			return null;
		}
	}
}
