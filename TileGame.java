package tilegame;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

public class TileGame extends BasicGame {
	// enum to keep track of the current map
	private enum mapPlaying {
		map1, map2
	}

	// set the current map
	private mapPlaying currentMap = mapPlaying.map2;
	// initiate maps
	private TiledMap map1, map2;
	// initiate Images
	private Image character;
	// character positions
	private float x = 32f, y = 32f;

	/**
	 * The collision map indicating which tiles block movement - generated based
	 * on tile properties
	 */
	private boolean[][] blocked1, blocked2;
	// size of tiles
	private static final int SIZE = 32;

	public TileGame() {
		// name of game
		super("Wizard game");
	}

	public static void main(String[] arguments) {
		try {
			AppGameContainer app = new AppGameContainer(new TileGame());
			// size of display
			app.setDisplayMode(960, 480, false);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		// Load maps
		map1 = new TiledMap("tilegame/map1.tmx");
		map2 = new TiledMap("tilegame/map2.tmx");
		// Load characters
		character = new Image("tilegame/characterfox.png");
		// Set size of arrays
		blocked1 = new boolean[map1.getWidth()][map1.getHeight()];
		blocked2 = new boolean[map2.getWidth()][map2.getHeight()];
		// Fill array of blocked locations
		fillBlockedArray(map1, blocked1);
		fillBlockedArray(map2, blocked2);

	}

	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {
		handleMovement(container, delta);
	}

	private void handleMovement(GameContainer container, int delta) {

		Input input = container.getInput();
		// Up key pressed
		if (input.isKeyDown(Input.KEY_UP)) {
			// Check if possible to move up
			if (!isBlocked(x, y - delta * 0.1f)
					&& !isBlocked(x + SIZE, y - delta * 0.1f)) {
				// The lower the delta the slowest the sprite will animate.
				y -= delta * 0.1f;
			}
			// Down key pressed
		} else if (input.isKeyDown(Input.KEY_DOWN)) {
			// Check if possible to move down
			if (!isBlocked(x, y + SIZE + delta * 0.1f)
					&& !isBlocked(x + SIZE, y + SIZE + delta * 0.1f)) {

				y += delta * 0.1f;
			}
			// Left key pressed
		} else if (input.isKeyDown(Input.KEY_LEFT)) {
			// check if possible to move left
			if (!isBlocked(x - delta * 0.1f, y)
					&& !isBlocked(x - delta * 0.1f, y + SIZE)) {

				x -= delta * 0.1f;
			}
			// Right key pressed
		} else if (input.isKeyDown(Input.KEY_RIGHT)) {
			// check if possible to move right
			if (!isBlocked(x + SIZE + delta * 0.1f, y)
					&& !isBlocked(x + SIZE + delta * 0.1f, y + SIZE)) {

				x += delta * 0.1f;
			}
		}
	}

	public void render(GameContainer container, Graphics g)
			throws SlickException {
		//draw map
		map2.render(0, 0);
		//draw character at coord x and y
		character.draw((int) x, (int) y);
	}

	private boolean isBlocked(float x, float y) {
		//Check which tile the player is standing on
		int xBlock = (int) x / SIZE;
		int yBlock = (int) y / SIZE;
		//Compare to correct map (replace with switch method?)
		if (currentMap == mapPlaying.map1)
			return blocked1[xBlock][yBlock];
		else
			return blocked2[xBlock][yBlock];

	}

	/**
	 * Checks map for tiles with blocked variable, then updates array with
	 * details of where blocked.
	 * 
	 * @param map
	 *            TiledMap
	 * @param array
	 *            boolean[][]
	 */
	private void fillBlockedArray(TiledMap map, boolean[][] array) {
		for (int xAxis = 0; xAxis < map.getWidth(); xAxis++) {
			for (int yAxis = 0; yAxis < map.getHeight(); yAxis++) {
				int tileID = map.getTileId(xAxis, yAxis, 0);
				String value = map.getTileProperty(tileID, "blocked", "false");
				if ("true".equals(value)) {
					array[xAxis][yAxis] = true;
				}
			}
		}
	}// end of CheckForBlocked
}
