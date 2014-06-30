package pong;

public class DestroyBall extends DelayedEffect {
	Ball ball;
	
	public DestroyBall(Ball ball, int timeout) {
		this.ticksRemaining=timeout;
		this.ball=ball;
	}
	
	@Override
	boolean timeToAct() {
		return ticksRemaining==0;
	}

	@Override
	void takeAction() {
		ball.destroy();
	}

}
