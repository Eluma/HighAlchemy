package pro.geektalk.alcher.loops;

import org.powerbot.core.script.job.LoopTask;
import org.powerbot.game.api.methods.Environment;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.tab.Skills;
import org.powerbot.game.bot.Context;

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
			Environment.saveScreenCapture(String.format("%s - %d - %s", Context
					.get().getDisplayName(), Skills.getLevel(Skills.MAGIC),
					Const.TIMER.toElapsedString()));
			Methods.stopScript("Out of items");
		}
		if (!Methods.inventoryContains(Const.NATURE_RUNE)) {
			Environment.saveScreenCapture(String.format("%s - %d - %s", Context
					.get().getDisplayName(), Skills.getLevel(Skills.MAGIC),
					Const.TIMER.toElapsedString()));
			Methods.stopScript("Out of runes");
		}
		return 1000;
	}
}
