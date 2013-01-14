package pro.geektalk.alcher.loops;

import org.powerbot.core.script.job.LoopTask;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.util.Random;

import pro.geektalk.alcher.misc.Methods;

public class Antiban extends LoopTask {

	@Override
	public int loop() {
		final int random = Random.nextInt(0, 100);
		if (random < 10 || random > 95) {
			Methods.s("Camera stuff");
			switch (random) {
			case 1:
				Camera.setAngle(Random.nextInt(400, 1200));
				Camera.setPitch(Random.nextInt(5, 35));
				break;
			case 3:
				Camera.setAngle(Random.nextInt(500, 2000));
				Camera.setPitch(Random.nextInt(60, 90));
				break;
			case 5:
				break;
			case 7:
				Camera.setPitch(Random.nextInt(25, 70));
				break;
			case 9:
				Camera.setAngle(Random.nextInt(500, 700));
				break;
			case 95:
				break;
			case 97:
				Camera.setAngle(Random.nextInt(1200, 2000));
				Camera.setPitch(Random.nextInt(5, 35));
				break;
			case 99:
			default:
				if (Random.nextInt(0, 11) < 8) {
					Camera.setAngle(Random.nextInt(500, 2000));
					Camera.setPitch(Random.nextInt(60, 90));
				}
				break;
			}
		}
		return 1000;
	}

}
