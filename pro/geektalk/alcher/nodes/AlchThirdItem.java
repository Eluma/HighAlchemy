package pro.geektalk.alcher.nodes;

import java.awt.Point;

import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.input.Keyboard;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.wrappers.node.Item;
import org.powerbot.game.api.wrappers.widget.WidgetChild;

import pro.geektalk.alcher.misc.Const;
import pro.geektalk.alcher.misc.Methods;
import pro.geektalk.alcher.misc.Variables;

public class AlchThirdItem extends Node {

	@Override
	public boolean activate() {
		return Methods.inventoryContains(Const.NATURE_RUNE)
				&& Methods.isSlotEmpty(0) && Methods.isSlotEmpty(1)
				&& !Methods.isSlotEmpty(2);
	}

	@Override
	public void execute() {
		Variables.slot = 3;
		final WidgetChild widget = Widgets.get(Const.WIDGET_TEXT,
				Const.WIDGETCHILD_TEXT).getChild(0);
		final Item item = Inventory.getItemAt(2);
		if (item != null) {
			Methods.s(String.format("Alching %s", item.getName()));
			if (widget.getText().contains("ast")) {
				final Point p = item.getWidgetChild().getCentralPoint();
				if (Random.nextInt(0, 10) % 2 == 0) {
					Mouse.click(p.x + Random.nextInt(-7, 7),
							p.y + Random.nextInt(-7, 7), true);
				} else {
					Mouse.click(p, true);
				}
				Keyboard.sendText("0", false);
				Task.sleep(2000);
			} else {
				Keyboard.sendText("0", false);
				Mouse.click(item.getWidgetChild().getCentralPoint(), true);
				Keyboard.sendText("0", false);
				Task.sleep(2000);
			}
		}
	}

}
