package pong;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

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
import shapes.ServeMachine;
import manager.SpellManager;
import pong.contact.PaddleBallPair;
import pong.contact.PongContactListener;
import pongutils.IllegalShapeException;
import serialization.CommandUpdate;
import serialize.PongPacket;
import server.PongServer;
import shapes.Ball;
import shapes.InfoBoard;
import shapes.Laser;
import shapes.Paddle;
import shapes.PongShape;
import shapes.Wall;
import spell.DelayedSpell;
import utils.Debugger;
import utils.Keyboard;
import utils.Registry;
import utils.Settings;

public class Pong {
    private Debugger debbie = new Debugger(Pong.class.getSimpleName());

    /**
     * System *
     */
    final int WAIT = 1000 / Settings.fps;
    boolean game_is_running = true;
    int lastStepTime;
    long frame;


    private Map<Integer, Player> players;
    private Map<Integer, Integer> remoteToLocal;

    /**
     * Pong *
     */
    private World world;
    private PongServer server;
    private SpellManager spellManager;
    private EffectManager effectManager;
    private ServeMachine serveMachine;

    private InfoBoard infoBoard;
    private Ball ball;
    private Set<DelayedSpell> delayedEffects = Collections.newSetFromMap(new ConcurrentHashMap<DelayedSpell, Boolean>());

    /**
     * Packet Lists *
     */
    private List<PongShape> shapeList;
    private List<PongPacket> persistentNonShape;
    private List<PongPacket> nonPersistentNonShape;


    /**
     * Constructor
     * Creates a Pong Game
     */
    public Pong(Player playerL, Player playerR, PongServer server) {
        /** Attach the server **/
        this.server = server;
        this.remoteToLocal = new HashMap<Integer, Integer>();

        /** Create a World **/
        this.world = new World(new Vec2(0, 0));

        /** Get Players **/
        this.players = new HashMap<Integer, Player>();
        addPlayer(playerL);
        addPlayer(playerR);

        /** Global Physics Effect Manager **/
        effectManager = new EffectManager("drag");
    }


    /**
     * *******************************************
     * Methods that add  objects to serialize list
     * *******************************************
     */
    public void addPlayer(Player player) {
        for (int i = 0; i < Player.TOTAL; i++) {
            if (!players.containsKey(i)) {
                players.put(i, player);
                debbie.i("Added player ID " + player.getId() + " as " + i);
                remoteToLocal.put(player.getId(), i);
                player.setWho(i);
                return;
            }
        }

        // No room for this player.
        // TODO send them an apology
        debbie.w("Not enough room for another player!");
    }

    public void start() {
        resetGame();
        init();

        while (game_is_running) {
            try {
                update();
                packagePackets();
                Thread.sleep(WAIT);

            } catch (Exception e) {
                e.printStackTrace();
            }

            if (players.size() < Settings.REQUIRED_PLAYERS) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    /**
     * Resetting the game *
     */
    private void resetGame() {
        /** Initialize Packets  **/
        this.shapeList = new ArrayList<PongShape>();
        this.persistentNonShape = new ArrayList<PongPacket>();
        this.nonPersistentNonShape = new ArrayList<PongPacket>();

        /** Initiate Game InfoBoards*/
        this.infoBoard = new InfoBoard(Settings.winningScore);

        /** Initialize frame count **/
        frame = 0;
    }

    /**
     * Game Loop: init(), render() and update() *
     */


    public void init() {
        /** Create Game Pieces **/
        makeWalls();
        makeBall((int) Settings.windowMeters[1], (int) Settings.windowMeters[0]);
        frame = 0;

        PongContactListener contactListener = new PongContactListener();


        /** Loop through players **/
        Paddle paddle;
        for (Player player : players.values()) {
            player.setPong(this);
            paddle = makePaddle(player.who());
            player.setPaddle(paddle);
            addShape(paddle);
            contactListener.registerPair(new PaddleBallPair(player.getPaddle(), ball, this));
        }

        addShape(ball);
        addPersistentNonShape(infoBoard);

        /** Set Contact Listener**/
        world.setContactListener(contactListener);

        /** Spell keeper for this game **/
        this.spellManager = new SpellManager(this);

        /** Create serveMachine **/
        this.serveMachine=new ServeMachine(Settings.windowMeters[0]/2, Settings.windowMeters[1]/2, this);
        addShape(serveMachine);
    }

    public void update() {
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
     * Starting the Game *
     */
    public void packagePackets() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        for (PongPacket ps : persistentNonShape) {
            buildArray(outputStream, ps);
        }

        for (PongPacket ps : shapeList) {
            buildArray(outputStream, ps);
        }

        for (PongPacket ps : nonPersistentNonShape) {
            buildArray(outputStream, ps);
        }

        nonPersistentNonShape.clear();
        server.sendUpdate(remoteToLocal.keySet().toArray(new Integer[remoteToLocal.size()]), outputStream.toByteArray());
    }

    /**
     * ***************************************
     * Methods that create objects
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

    private Paddle makePaddle(int player) {
        float x;
        byte spriteSheetId = 0;

        switch (player) {
            case Player.LEFT:
                x = 0.5f;
                spriteSheetId = Registry.RED_PADDLE;
                break;
            case Player.RIGHT:
                x = Settings.windowMeters[0] - 0.5f;
                spriteSheetId = Registry.BLUE_PADDLE;
                break;
            default:
                x = 1f;
        }
        debbie.i("Making Paddle for player " + player);
        return new Paddle(x, Settings.windowMeters[1] / 2, Settings.paddleLength, spriteSheetId, getWorld(), this);
    }

    public void addShape(PongShape ps) {
        //TODO - add default contact for every shape - maybe coded in the Shape Class itself. (template for contacts)
        shapeList.add(ps);
    }

    public void addPersistentNonShape(PongPacket pp) {
        persistentNonShape.add(pp);
    }

    /**
     * Utility functions, called ~1/game loop *
     */

    public void step() {
        for (Player player : players.values()) {
            execute(player);
            player.step(frame);
        }
        infoBoard.setMana(players.get(Player.LEFT).mana, players.get(Player.RIGHT).mana);

        world.step(Settings.timeStep, Settings.velocityIterations, Settings.positionIterations);

        lastStepTime = (int) System.nanoTime() / 1000000;
    }

    /**
     * ***************************************
     * Methods that modify current objects
     * ***************************************
     */
    private void resetBall(int i) {
        System.out.println("serving from " + serveMachine.getxPos() + ", " + serveMachine.getyPos());
        serveMachine.serve(i, ball);
    }

    public void tendDelayedEffects() {
        for (DelayedSpell delayedEffect : delayedEffects) {
            delayedEffect.tick();
            if (delayedEffect.ticksRemaining <= 0) {
                delayedEffects.remove(delayedEffect);
            }
        }
    }

    public void buildArray(ByteArrayOutputStream outputStream, PongPacket ps) {
        try {
            outputStream.write(ps.serialize());
        } catch (IOException e) {
            debbie.e(Registry.getPacketId(ps.getClass()) + " failed to write to bytearrayoutputstream " + e.getMessage());
        } catch (IllegalShapeException e) {
            e.printStackTrace();
        }
    }

    /**
     * ***************************************
     * Methods that get current objects
     * ***************************************
     */
    public org.jbox2d.dynamics.World getWorld() {
        return world;
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

    public void received(int connectionId, CommandUpdate update) {
        getConnectedPlayer(connectionId).setKeys(update);
    }

    public Player getConnectedPlayer(int id) {
        debbie.i(remoteToLocal.toString());
        return getPlayer(remoteToLocal.get(id));
    }

    public Player getPlayer(int player) {
        return players.get(player);
    }

    public void addNonPersistent(PongPacket pp) {
        nonPersistentNonShape.add(pp);
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

    public Debugger getDebbie() {
        return debbie;
    }

    public Map<Integer, Player> getPlayers() {
        return players;
    }

    public Set<DelayedSpell> getDelayedEffects() {
        return delayedEffects;
    }

    /**
     * ***************************************
     * Methods that remove current  objects
     * ***************************************
     */

    public void removePongShape(PongShape ps) {
        shapeList.remove(ps);
    }

    public void removeConnection(int id) {
        players.remove(remoteToLocal.get(id));
        remoteToLocal.remove(id);
    }
}