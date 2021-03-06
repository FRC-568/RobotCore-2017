package org.usfirst.frc.team568.robot.commands;

import org.usfirst.frc.team568.robot.subsystems.Shooter;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class BallShooterandMixer extends Command {
	Shooter shooter;

	public BallShooterandMixer() {
		// TODO Auto-generated constructor stub
		shooter = new Shooter();
	}

	@Override
	protected void initialize() {
		shooter.shootMotor.set(-(9.5 / 12));
		Timer.delay(.5);
	}

	@Override
	protected void execute() {
		// Timer.delay(1);
		shooter.ballWranglerIn.set(true);
		shooter.ballWranglerOut.set(false);
		Timer.delay(250);
		shooter.gate.setAngle(50);
		Timer.delay(250);
		shooter.ballWranglerIn.set(false);
		shooter.ballWranglerOut.set(true);
		Timer.delay(250);
		shooter.gate.setAngle(0);
		Timer.delay(250);

	}

	@Override
	protected boolean isFinished() {
		return false;
	}

	@Override
	protected void end() {
		shooter.shootMotor.set(0);
	}

	@Override
	protected void interrupted() {

		// TODO Auto-generated method stub

	}

}
