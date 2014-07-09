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

import server.Player;
import server.PongServer;
import shapes.Ball;
import shapes.InfoBoard;
import shapes.Laser;
import shapes.Paddle;
import shapes.PongShape;
import shapes.Wall;
import spell.Spellkeeper;
import utils.Debugger;
import utils.Settings;

public class Pong extends BasicGame {
    private Debugger debbie = new Debugger(Pong.class.getSimpleName(), Debugger.INFO);

    private PongServer server;


    private Set<DelayedEffect> delayedEffects = Collections.newSetFromMap(new ConcurrentHashMap<DelayedEffect, Boolean>());

    int lastStepTime;
    long frame;
    long gameId;
    private List<PongShape> shapeList;
    private Player playerL;
    private Player playerR;
    private World world;
    private Spellkeeper spellkeeper;
    private Ball ball;
    private InfoBoard infoBoard;

    /** Constructor
     * Creates a Pong Game
     * @param title -  title of the Pong window
     */
    public Pong(String title, Player playerL, Player playerR, long gameId, PongServer server) {
        super(title);
        /** Attach the server **/
        this.server = server;
        this.gameId = gameId;

        /** Create a World **/
        this.world = new World( new Vec2(0, 0));

        /** Get Players **/
        this.playerL = playerL;
        this.playerR = playerR;
    }

    public void start(){
        resetGame();
        createGame();
    }

    /** Starting the Game **/
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

    /** Resetting the game **/
    private void resetGame(){
        /** Initialize Game Pieces **/
        this.shapeList = new ArrayList<PongShape>();

        /** Initiate Game InfoBoards*/
        this.infoBoard = new InfoBoard(Settings.winningScore);

        /** Initialize frame count **/
        frame = 0;
    }
    /**
     * Game Loop: init(), render() and update() *
     */

    @Override
    public void init(GameContainer arg0) throws SlickException {
        /** Create Game Peices **/
        makeWalls();
        makeBall((int)Settings.windowMeters[1], (int) Settings.windowMeters[0]);
        playerL.setPaddle(makePaddle(Player.LEFT));
        playerR.setPaddle(makePaddle(Player.RIGHT));
        frame = 0;

        /** Add all game pieces **/
        shapeList.add(infoBoard);
        shapeList.add(playerL.getPaddle());
        shapeList.add(playerR.getPaddle());
        shapeList.add(ball);

        /** Spell keeper for this game **/
        this.spellkeeper = new Spellkeeper(this);
    }


    public void render(GameContainer arg0, Graphics graphics)
            throws SlickException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Debugger.debugger.d("Shape List " + shapeList.size());
        byte[] cereal;
        for (PongShape ps : shapeList){
            try {
                cereal = ps.serialize_();
                if (cereal.length > 0)
                    outputStream.write(cereal);
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
            infoBoard.playerScored(Player.RIGHT);
            resetBall(Player.RIGHT);
        } else if (ball.getX() > Settings.windowSize[0]) {
            infoBoard.playerScored(Player.LEFT);
            resetBall(Player.LEFT);
        }
        spellkeeper.update();
        tendDelayedEffects();
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
     * Utility functions, called ~1/game loop *
     */

    public void step(int[] p1keys, int[] p2keys) {
        execute(p1keys, playerL);
        execute(p2keys, playerR);

        world.step(Settings.timeStep, Settings.velocityIterations, Settings.positionIterations);

        lastStepTime = (int) System.nanoTime() / 1000000;
    }

    /******************************************
     Methods that create or add  objects
     *****************************************/

    private void makeWalls() {
        float width = Settings.windowMeters[0];
        float height = Settings.windowMeters[1];

        new Wall(width/2, height + 0.001f, 0.001f, width, 0f, false, (char) 0, getWorld(), this);
        new Wall(width/2, 0      - 0.001f, 0.001f, width, 0f, false, (char) 0, getWorld(), this);
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
        return new Paddle(x, Settings.windowMeters[1] / 2, Settings.paddleLength, color, getWorld(), this);
    }

    private void makeBall(int height, int width) {
        ball = new Ball(width / 2, height / 2, Settings.ballRadius, getWorld(),
                true, '1', this);
        ball.getBody().setLinearVelocity(new Vec2(-Settings.serveSpeed, 0));
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
                break;
            case Player.RIGHT:
            default:
                x = ((points[0] + points[6]) / 2);
                y = ((points[1] + points[7]) / 2);
                direction = player.getPaddle().getShape().getNormals()[3];
                break;
        }

        return new Laser(Settings.p2m((int) x), Settings.p2m((int) y), Settings.laserRadius, direction,
                getWorld(), this);
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

    public void addShape(PongShape ps) {
        shapeList.add(ps);
    }


    /******************************************
     Methods that get current objects
     *****************************************/
    public org.jbox2d.dynamics.World getWorld() {
        return world;
    }

    public Player getPlayer(long id){
        return playerL.getId() == id?playerL:playerR;
    }

    public Ball getBall() {
        return ball;
    }

    public Debugger getDebbie() {
        return debbie;
    }

    public InfoBoard getInfoBoard() {
        return infoBoard;
    }

    public int getLastStepTime() {
        return lastStepTime;
    }

    public List<PongShape> getShapeList() {
        return shapeList;
    }

    public long getFrame() {
        return frame;
    }

    public Player getPlayerL() {
        return playerL;
    }

    public Player getPlayerR() {
        return playerR;
    }

    public PongServer getServer() {
        return server;
    }

    public Set<DelayedEffect> getDelayedEffects() {
        return delayedEffects;
    }

    public Spellkeeper getSpellkeeper() {
        return spellkeeper;
    }

    /******************************************
     Methods that modify current objects
     *****************************************/
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

    public void setMana(byte leftMana, byte rightMana) {
        infoBoard.setMana(leftMana, rightMana);
    }


    private void rubberBandRotation(float isClockwise, Paddle paddle){
        /** Getting current angle of body **/
        float currentAngle = paddle.getBody().getAngle();
        float currentVelocity = paddle.getBody().getAngularVelocity();
        if (currentAngle > 180){
            currentAngle -= 360;
        }

        /** Create initial force **/
        float force = 0f;

        /** Paddle Push **/
        force += isClockwise * (Settings.maxPaddleRotateAngle * Settings.paddleSpringConstant);

        /** Rubber Band **/
        force += -currentAngle * (Settings.paddleSpringConstant);

        /** Damper Force **/
        force += -currentVelocity * Settings.paddleDampingConstant;

        paddle.getBody().setAngularVelocity(currentVelocity + force);
    }

    public void tendDelayedEffects() {
        for (DelayedEffect delayedEffect : delayedEffects) {
            delayedEffect.tick();
            if (delayedEffect.ticksRemaining <= 0) {
                delayedEffects.remove(delayedEffect);
            }
        }
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
                    spellkeeper.tryToCast(player, Keyboard.KEY_SPACE);
                    break;
                }
                case Keyboard.KEY_Q: {
                    spellkeeper.tryToCast(player, Keyboard.KEY_Q);
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

    /******************************************
     Methods that remove current  objects
     *****************************************/

    public void removePongShape(PongShape ps) {
        shapeList.remove(ps);
    }


}