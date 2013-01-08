package pro.geektalk.alcher.nodes;

import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.input.Keyboard;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.wrappers.node.Item;
import org.powerbot.game.api.wrappers.widget.WidgetChild;

import pro.geektalk.alcher.misc.Const;
import pro.geektalk.alcher.misc.Methods;
import pro.geektalk.alcher.misc.Variables;

public class AlchSecondItem extends Node {

	@Override
	public boolean activate() {
		return Methods.inventoryContains(Const.NATURE_RUNE) && Methods.isSlotEmpty(0) && !Methods.isSlotEmpty(1);
	}

	@Override
	public void execute() {
		Variables.slot = 2;
		final WidgetChild widget = Widgets.get(Const.WIDGET_TEXT,
				Const.WIDGETCHILD_TEXT).getChild(0);
		final Item item = Inventory.getItemAt(1);
		if(item != null) {
			Methods.s(String.format("Alching %s", item.getName()));
			if(widget.getText().contains("ast")) {
				Mouse.click(item.getWidgetChild().getCentralPoint(), true);
				Keyboard.sendText("0", false);
				Task.sleep(2300);
			} else {
				Keyboard.sendText("0", false);
				Mouse.click(item.getWidgetChild().getCentralPoint(), true);
				Keyboard.sendText("0", false);
				Task.sleep(2300);
			}
		}	
	}

}
