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
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import shapes.Laser;
import server.Player;
import server.PongServer;
import shapes.Ball;
import shapes.Paddle;
import shapes.PongShape;
import shapes.Wall;
import spell.Spellkeeper;
import utils.Debugger;

public class Pong extends BasicGame {
    private static final int[] relevantChars = Settings.relevantChars;
    public static Set<DelayedEffect> delayedEffects = Collections.newSetFromMap(new ConcurrentHashMap<DelayedEffect, Boolean>());
    public static Pong pong;
    Debugger debbie = new Debugger(Pong.class.getSimpleName(), Debugger.DEBUG | Debugger.INFO);
    float timeStep;
    int velocityIterations;
    int positionIterations;
    int lastStepTime;
    long frame;
    private List<PongShape> shapeList;
    private Player playerL;
    private Player playerR;
    private World world;
    private Spellkeeper spellkeeper;
    private Ball ball;
    private PongServer server;
    private Scorekeeper score;

    public Pong(String title) {
        super(title);
    }

    public static void main(String[] args) {
        pong = new Pong("pong");
        pong.createGame(null, null, null);
    }

    private void createGame(PongServer bankedServer, Player bankedPlayer1,
                            Player bankedPlayer2) {
        /** Initialize Game Pieces **/
        shapeList = new ArrayList<PongShape>();

        /** Grab a server **/
        if (bankedServer == null) {
            try {
                server = new PongServer(this, relevantChars);
            } catch (IOException e) {
                debbie.e(e.getMessage());
            }
        } else {
            server = bankedServer;
        }
        /** turn debbie off **/
        debbie.disable();

        /** Grab Everything **/
        Vec2 gravity = new Vec2(0, 0);
        world = new World(gravity);
        playerL = bankedPlayer1;
        playerR = bankedPlayer2;
        timeStep = 1f / 60f;
        velocityIterations = Settings.velocityIterations;
        positionIterations = Settings.positionIterations;
        frame = 0;
        score = new Scorekeeper(Settings.winningScore);

        /** Start the Game **/
        AppGameContainer app;
        try {
            app = new AppGameContainer(this);
            app.setDisplayMode(Settings.windowSize[0], Settings.windowSize[1],
                    false);
            app.setVSync(true);
            app.setAlwaysRender(true);
            app.setTargetFrameRate(Settings.fps);

            app.start();
        } catch (SlickException e) {
            debbie.e(e.getMessage());
        }
    }

    public Ball makeLaser(Player player) {
        // Laser Starting and Direction
        float x, y;
        Vec2 direction = player.getPaddle().getShape().getNormals()[1];
        float[] points = player.getPaddle().getPointsInPixels();

        // Set correct values based on player
        switch (player.who()) {
            case Player.LEFT:
                x = ((points[2] + points[4]) / 2);
                y = ((points[3] + points[5]) / 2);
                break;
            case Player.RIGHT:
            default:
                x = ((points[0] + points[6]) / 2);
                y = ((points[1] + points[7]) / 2);
                break;
        }

        return new Laser(Settings.p2m((int) x), Settings.p2m((int) y), Settings.laserRadius, direction,
                getWorld());
    }

    public org.jbox2d.dynamics.World getWorld() {
        return world;
    }

    /**
     * Add/Remove Pong Objects *
     * @param ps - the pong shape we want to remove
     */

    public void removePongShape(PongShape ps) {
        shapeList.remove(ps);
    }
    

    public void addShape(PongShape ps) {
        shapeList.add(ps);
    }
    
    /**
     * Game Loop: init(), render() and update() *
     */

    @Override
    public void init(GameContainer arg0) throws SlickException {
        int width = (int) Settings.p2m(Settings.windowSize[0]);
        int height = (int) Settings.p2m(Settings.windowSize[1]);

        while (playerL == null || playerR == null) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ignored) {
            }
            System.out.println("Waiting for players to load: "
                    + (playerL == null) + " " + (playerR == null));
        }

        makeWalls();
        makeBall(height, width);
        playerL.setPaddle(makePaddle(Player.LEFT));
        playerR.setPaddle(makePaddle(Player.RIGHT));
        frame = 0;

        addPaddlesAndBallToShapeList();
        this.spellkeeper = new Spellkeeper();
    }

    @Override
    public void render(GameContainer arg0, Graphics graphics)
            throws SlickException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Debugger.debugger.d("Shape List " + shapeList.size());
        for (PongShape ps : shapeList){
            try {
                outputStream.write(ps.serialize());
            } catch (IOException e) {
                debbie.e(ps.getId() + " failed to write to bytearrayoutputstream " + e.getMessage());
            }
        }
        server.sendUpdate(outputStream.toByteArray());
    }

    @Override
    public void update(GameContainer arg0, int arg1) throws SlickException {
        ++frame;

        int[] p1keys = playerL.getKeys();
        int[] p2keys = playerR.getKeys();

        step(p1keys, p2keys);

        if (ball.getX() < 0) {
            score.playerScore(1, 1);
            resetBall(1);
        } else if (ball.getX() > Settings.windowSize[0]) {
            score.playerScore(0, 1);
            resetBall(0);
        }
        spellkeeper.update();
        tendDelayedEffects();
    }

    /**
     * Getters *
     */

    public Player getPlayer(long playerId) {
        if (playerL.getId() == playerId) {
            return playerL;
        } else if (playerR.getId() == playerId) {
            return playerR;
        } else {
            Log.info("No player with id: " + playerId);
            return null;
        }
    }

    public Player getPlayer(int player) {
        if (player == Player.LEFT) {
            return playerL;
        } else if (player == Player.RIGHT) {
            return playerR;
        } else {
            return null;
        }
    }

    /**
     * GET ALL GAME PIECES *
     */
    private void addPaddlesAndBallToShapeList() {
        shapeList.add(playerL.getPaddle());
        shapeList.add(playerR.getPaddle());
        shapeList.add(ball);
    }

    /**
     * Utility functions, called ~1/game loop *
     */

    public void step(int[] p1keys, int[] p2keys) {
        execute(p1keys, playerL);
        execute(p2keys, playerR);

        world.step(timeStep, velocityIterations, positionIterations);

        lastStepTime = (int) System.nanoTime() / 1000000;
    }


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


    public void execute(int[] keys, Player player) {
        debbie.d("Executing Player " + player.who());
        float linearVelocity = 0;
        float turnRequest = 0;

        for (int key : keys) {
            switch (key) {
                case Keyboard.KEY_DOWN: {
                    linearVelocity += Settings.paddleSpeed;
                    break;
                }

                case Keyboard.KEY_UP: {
                    linearVelocity -= Settings.paddleSpeed;
                    break;
                }

                case Keyboard.KEY_RIGHT: {
                    turnRequest += 1;
                    break;
                }

                case Keyboard.KEY_LEFT: {
                    turnRequest -= 1;
                    break;
                }
                case Keyboard.KEY_SPACE: {
                    debbie.d(player.getId() + " player on the " + getStringPlayer(player));
                    spellkeeper.tryToCast(player, Keyboard.KEY_SPACE);
                    break;
                }
                case Keyboard.KEY_0: {
                    if (frame > Settings.minFramesBeforeReset) {
                        resetGame();
                    }
                }
            }
        }
        player.getPaddle().getBody().setLinearVelocity(new Vec2(0, linearVelocity));
        rubberBandRotation(turnRequest, player.getPaddle());
    }


    private void resetGame() {
        this.closeRequested();
        Settings.refreshSettings();
        pong = new Pong("pong");
        pong.createGame(server, playerL, playerR);
    }


    private void rubberBandRotation(float request, Paddle paddle) {
        float currentAngle = paddle.getBody().getAngle();
        if (currentAngle > 180) {
            currentAngle = currentAngle - 360;
        }

        if (request == 0) {
            paddle.getBody().setAngularVelocity(-currentAngle * 4);
        } else {
            float rate;
            if (currentAngle * request > 0) {
                currentAngle = Math.abs(currentAngle);
                rate = Math.abs(Settings.maxPaddleRotateAngle - currentAngle)
                        / Settings.maxPaddleRotateAngle;
            } else {
                currentAngle = Math.abs(currentAngle);
                rate = Math.abs(Settings.maxPaddleRotateAngle + currentAngle)
                        / Settings.maxPaddleRotateAngle;
            }

            paddle.getBody().setAngularVelocity(request * rate);
        }

    }

    public void tendDelayedEffects() {
        for (DelayedEffect delayedEffect : delayedEffects) {
            delayedEffect.tick();
            if (delayedEffect.ticksRemaining <= 0) {
                delayedEffects.remove(delayedEffect);
            }
        }
    }

    /**
     * Make Methods *
     */

    private void makeWalls() {
        float width = Settings.windowMeters[0];
        float height = Settings.windowMeters[1];

        Wall botWall = new Wall(width/2, height + 0.001f, 0.001f, width, 0f, false, (char) 0, getWorld());

        Wall topWall = new Wall(width/2, height - 0.001f, 0.001f, width, 0f, false, (char) 0, getWorld());
    }

    private Paddle makePaddle(int player) {
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
        return new Paddle(x, Settings.windowMeters[1] / 2, Settings.paddleLength, color, getWorld());
    }
    
    public void addPlayer(Player player) {
        if (playerL == null) {
            playerL = player;
            playerL.setWho(Player.LEFT);
        } else if (playerR == null) {
            playerR = player;
            playerR.setWho(Player.RIGHT);
        } else {
            Log.info("No room for player " + player.getId());
            // No room for this player.
            // TODO send them an apology
            debbie.w("Not enough room for another player!");
        }
    }

    private void makeBall(int height, int width) {
        ball = new Ball(width / 2, height / 2, Settings.ballRadius, getWorld(),
                true, '1');
        ball.getBody().setLinearVelocity(new Vec2(-Settings.serveSpeed, 0));
    }


    public String getStringPlayer(Player player) {
        return player.isPlayer(playerL) ? "LEFT" : "RIGHT";
    }


    @Override
    public boolean closeRequested() {
        return true;
    }

    @Override
    public String getTitle() {
        return Settings.title;
    }
}