package pong;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import org.lwjgl.input.Keyboard;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import manager.EffectManager;
import manager.SoundManager;
import manager.SpellManager;
import pong.contact.PaddleBallPair;
import pong.contact.PongContactListener;
import serialize.PongPacket;
import server.PongServer;
import shapes.Ball;
import shapes.InfoBoard;
import shapes.Laser;
import shapes.Paddle;
import shapes.PongShape;
import utils.Registry;
import shapes.Wall;
import spell.DelayedSpell;
import utils.Debugger;
import utils.IllegalShapeException;
import utils.Settings;

public class Pong extends BasicGame {
    int lastStepTime;
    long frame;
    long gameId;
    private Debugger debbie = new Debugger(Pong.class.getSimpleName());
    private PongServer server;
    private Set<DelayedSpell> delayedEffects = Collections.newSetFromMap(new ConcurrentHashMap<DelayedSpell, Boolean>());
    private List<PongShape> shapeList;
    private List<PongPacket> pongPacketList;
    private Map<Integer, Player> players;
    private Map<Long, Integer> remoteToLocal;
    private World world;
    private SpellManager spellManager;
    private Ball ball;
    private InfoBoard infoBoard;
    private EffectManager effectManager;
    private SoundManager soundManager;

    /**
     * Constructor
     * Creates a Pong Game
     *
     * @param title -  title of the Pong window
     */
    public Pong(String title, Player playerL, Player playerR, long gameId, PongServer server) {
        super(title);
        /** Attach the server **/
        this.server = server;
        this.gameId = gameId;
        this.remoteToLocal = new HashMap<Long, Integer>();

        /** Create a World **/
        this.world = new World(new Vec2(0, 0));

        /** Get Players **/
        this.players = new HashMap<Integer, Player>();
        addPlayer(playerL);
        addPlayer(playerR);

        /** Global Physics Effect Manager **/
        effectManager = new EffectManager("drag");

        /** Sound effects **/
        soundManager = new SoundManager();
    }

    public void start() {
        resetGame();
        createGame();
    }

    /**
     * Resetting the game *
     */
    private void resetGame() {
        /** Initialize Game Pieces **/
        this.shapeList = new ArrayList<PongShape>();
        this.pongPacketList = new ArrayList<PongPacket>();

        /** Initiate Game InfoBoards*/
        this.infoBoard = new InfoBoard(Settings.winningScore);

        /** Initialize frame count **/
        frame = 0;
    }

    /**
     * Starting the Game *
     */
    private void createGame() {
        /** Start the Game **/
        AppGameContainer app;
        try {
            app = new AppGameContainer(this);
            app.setDisplayMode(10, 10,
                    false);
            app.setVSync(true);
            app.setAlwaysRender(true);
            app.setTargetFrameRate(Settings.fps);

            app.start();
        } catch (SlickException e) {
            debbie.e(e.getMessage());
        }
    }

    public void render(GameContainer arg0, Graphics graphics)
            throws SlickException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        debbie.d("Shape List " + shapeList.size());
        byte[] cereal;
        for (PongShape ps : shapeList) {
            try {
                cereal = ps.serialize();
                if (cereal.length > 0)
                    outputStream.write(cereal);
            } catch (IOException e) {
                debbie.e(Registry.getPacketId(ps.getClass()) + " failed to write to bytearrayoutputstream " + e.getMessage());
            } catch (IllegalShapeException e) {
                e.printStackTrace();
            }
        }
        server.sendUpdate(outputStream.toByteArray());
    }

    @Override
    public boolean closeRequested() {
        return true;
    }

    @Override
    public String getTitle() {
        return Settings.title;
    }

    /**
     * Game Loop: init(), render() and update() *
     */

    @Override
    public void init(GameContainer arg0) throws SlickException {
        /** Create Game Pieces **/
        makeWalls();
        makeBall((int) Settings.windowMeters[1], (int) Settings.windowMeters[0]);
        makePaddle(Player.LEFT);
        makePaddle(Player.RIGHT);
        frame = 0;

        PongContactListener contactListener = new PongContactListener();


        /** Loop through players **/
        for (Player player : players.values()) {
            shapeList.add(player.getPaddle());
            contactListener.registerPair(new PaddleBallPair(player.getPaddle(), ball, this));
        }

        shapeList.add(ball);

        pongPacketList.add(infoBoard);
        pongPacketList.addAll(shapeList);

        /** Set Contact Listener**/
        world.setContactListener(contactListener);

        /** Spell keeper for this game **/
        this.spellManager = new SpellManager(this);
    }

    @Override
    public void update(GameContainer arg0, int arg1) throws SlickException {
        ++frame;

        step();

        /** Check for Score **/
        if (ball.getX() < 0) {
            infoBoard.playerScored(Player.RIGHT);
            resetBall(Player.RIGHT);
        } else if (ball.getX() > Settings.windowSize[0]) {
            infoBoard.playerScored(Player.LEFT);
            resetBall(Player.LEFT);
        }

        /** Apply Global Effects **/
        effectManager.applyForces(shapeList);

        /** Update Spells **/
        spellManager.update();
        tendDelayedEffects();
    }

    /**
     * Utility functions, called ~1/game loop *
     */

    public void step() {
        for (Player player : players.values()) {
            execute(player);
        }

        world.step(Settings.timeStep, Settings.velocityIterations, Settings.positionIterations);

        lastStepTime = (int) System.nanoTime() / 1000000;
    }

    /**
     * ***************************************
     * Methods that modify current objects
     * ***************************************
     */
    private void resetBall(int i) {
        Vec2 ballVelocity;
        switch (i) {
            case Player.LEFT:
                ballVelocity = new Vec2(-Settings.serveSpeed, 0);
                break;
            case Player.RIGHT:
                ballVelocity = new Vec2(Settings.serveSpeed, 0);
                break;
            default:
                debbie.e("You served the ball to not a player: player # " + i
                        + ". Players are 0 and 1.");
                throw new RuntimeException();
        }
        ball.setPosition(Settings.windowMeters[0] / 2, Settings.windowMeters[1] / 2);
        ball.getBody().setAngularVelocity(0);
        ball.getBody().setLinearVelocity(ballVelocity);
        debbie.i("reset ball to (" + ball.getX() + ", " + ball.getY() + ")");
    }

    public void tendDelayedEffects() {
        for (DelayedSpell delayedEffect : delayedEffects) {
            delayedEffect.tick();
            if (delayedEffect.ticksRemaining <= 0) {
                delayedEffects.remove(delayedEffect);
            }
        }
    }

    public void execute(Player player) {
        debbie.d("Executing Player " + player.who());
        Paddle curPaddle = player.getPaddle();

        for (int key : player.getKeys()) {
            switch (key) {
                case Keyboard.KEY_DOWN: {
                    curPaddle.setYVelocity(Settings.paddleSpeed);
                    break;
                }

                case Keyboard.KEY_UP: {
                    curPaddle.setYVelocity(-Settings.paddleSpeed);
                    break;
                }

                case Keyboard.KEY_RIGHT: {
                    curPaddle.rubberBandRotation(1);
                    break;
                }

                case Keyboard.KEY_LEFT: {
                    curPaddle.rubberBandRotation(-1);
                    break;
                }
                case Keyboard.KEY_SPACE: {
                    spellManager.castSpell(player, Keyboard.KEY_SPACE);
                    break;
                }
                case Keyboard.KEY_Q: {
                    spellManager.castSpell(player, Keyboard.KEY_Q);
                    break;
                }
                case Keyboard.KEY_0: {
                    if (frame > Settings.minFramesBeforeReset) {
                        resetGame();
                    }
                }
            }
        }

        curPaddle.execute();
    }

    /**
     * ***************************************
     * Methods that create or add  objects
     * ***************************************
     */

    private void makeWalls() {
        float width = Settings.windowMeters[0];
        float height = Settings.windowMeters[1];

        new Wall(width / 2, height + 0.001f, 0.001f, width, false, (char) 0, getWorld(), this);
        new Wall(width / 2, 0 - 0.001f, 0.001f, width, false, (char) 0, getWorld(), this);
    }

    private void makeBall(int height, int width) {
        ball = new Ball(width / 2, height / 2, Settings.ballRadius, getWorld(),
                true, '1', this);
        ball.getBody().setLinearVelocity(new Vec2(-Settings.serveSpeed, 0));
    }

    private void makePaddle(int player) {
        float x;
        char color = 0;

        switch (player) {
            case Player.LEFT:
                x = 0.5f;
                color = '0';
                break;
            case Player.RIGHT:
                x = Settings.windowMeters[0] - 0.5f;
                color = '2';
                break;
            default:
                x = 1f;
        }
        debbie.i("Making Paddle for player " + player);
        players.get(player).setPaddle(new Paddle(x, Settings.windowMeters[1] / 2, Settings.paddleLength, color, getWorld(), this));
    }

    /**
     * ***************************************
     * Methods that get current objects
     * ***************************************
     */
    public org.jbox2d.dynamics.World getWorld() {
        return world;
    }

    public Ball makeLaser(Player player) {
        // Laser Starting and Direction
        float x, y;
        Vec2 direction;
        float[] points = player.getPaddle().getPointsInPixels();

        // Set correct values based on player
        switch (player.who()) {
            case Player.LEFT:
                x = ((points[2] + points[4]) / 2);
                y = ((points[3] + points[5]) / 2);
                direction = player.getPaddle().getShape().getNormals()[1];
                debbie.i("PLAYER LEFT " + x + " " + y + " " + direction.x + " " + direction.y);
                break;
            case Player.RIGHT:
            default:
                x = ((points[0] + points[6]) / 2);
                y = ((points[1] + points[7]) / 2);
                direction = player.getPaddle().getShape().getNormals()[3];
                debbie.i("PLAYER RIGHT " + x + " " + y + " " + direction.x + " " + direction.y);
                break;
        }

        return new Laser(Settings.p2m((int) x), Settings.p2m((int) y), Settings.laserRadius, direction,
                getWorld(), this);
    }

    public void addPlayer(Player player) {
        for (int i = 0; i < Player.TOTAL; i++) {
            if (!players.containsKey(i)) {
                player.setPong(this);
                players.put(i, player);
                debbie.i("Added player ID " + player.getId() + " as " + i);
                remoteToLocal.put(player.getId(), i);
                player.setWho(i);
                return;
            }
        }

        Log.info("No room for player " + player.getId());
        // No room for this player.
        // TODO send them an apology
        debbie.w("Not enough room for another player!");
    }

    public void addShape(PongShape ps) {
        shapeList.add(ps);
    }


    public Debugger getDebbie() {
        return debbie;
    }

    public Map<Integer, Player> getPlayers() {
        return players;
    }

    public Player getPlayer(int player) {
        return players.get(player);
    }

    public Set<DelayedSpell> getDelayedEffects() {
        return delayedEffects;
    }

    public SoundManager getSoundManager() {
        return soundManager;
    }

    public void setMana(byte leftMana, byte rightMana) {
        infoBoard.setMana(leftMana, rightMana);
    }

    public Player getConnectedPlayer(long id) {
        debbie.i(remoteToLocal.toString());
        return getPlayer(remoteToLocal.get(id));
    }

    /**
     * ***************************************
     * Methods that remove current  objects
     * ***************************************
     */

    public void removePongShape(PongShape ps) {
        shapeList.remove(ps);
    }


}