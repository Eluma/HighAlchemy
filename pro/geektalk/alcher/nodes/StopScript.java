package pro.geektalk.alcher.nodes;

import org.powerbot.core.script.job.LoopTask;
import org.powerbot.game.api.methods.Game;

import pro.geektalk.alcher.misc.Const;
import pro.geektalk.alcher.misc.Methods;

public class StopScript extends LoopTask {

	@Override
	public int loop() {
		if (Game.getClientState() != Game.INDEX_MAP_LOADED) {
			return 2500;
		}
		if (Methods.isSlotEmpty(0) && Methods.isSlotEmpty(1)
				&& Methods.isSlotEmpty(2) && Methods.isSlotEmpty(3)) {
			Methods.stopScript("Out of items");
		}
		if (!Methods.inventoryContains(Const.NATURE_RUNE)) {
			Methods.stopScript("Out of runes");
		}
		return 1000;
	}

}
