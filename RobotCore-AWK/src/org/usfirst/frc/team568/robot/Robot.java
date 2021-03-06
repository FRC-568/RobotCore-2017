package org.usfirst.frc.team568.robot;

import org.usfirst.frc.team568.grip.VisionProcessor;
import org.usfirst.frc.team568.robot.commands.AutoFour;
import org.usfirst.frc.team568.robot.commands.AutoOne;
import org.usfirst.frc.team568.robot.commands.AutoThree;
import org.usfirst.frc.team568.robot.commands.AutoTwo;
import org.usfirst.frc.team568.robot.commands.ClimbWithWinch;
import org.usfirst.frc.team568.robot.commands.Shoot;
import org.usfirst.frc.team568.robot.commands.UnClimb;
import org.usfirst.frc.team568.robot.subsystems.DriveTrain;
import org.usfirst.frc.team568.robot.subsystems.GearBox;
import org.usfirst.frc.team568.robot.subsystems.ReferenceFrame2;
import org.usfirst.frc.team568.robot.subsystems.RopeCollector;
import org.usfirst.frc.team568.robot.subsystems.Shooter;
import org.usfirst.frc.team568.robot.subsystems.VisionTargetTracker;
import org.usfirst.frc.team568.robot.subsystems.WinchClimber;

import com.analog.adis16448.frc.ADIS16448_IMU;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {

	int session;
	Timer time;
	// public double whichOne;
	// public boolean over;

	public double speed;
	public double pressure;

	// public double EncoderValue;
	Command autonomousCommand;

	protected static Robot instance;
	public OI oi;
	public DriveTrain driveTrain;
	public ReferenceFrame2 referanceFrame2;
	public ADIS16448_IMU imu;
	public VisionTargetTracker gearLiftTracker;
	public GearBox gearBox;
	public RopeCollector ropeCollector;
	public WinchClimber winchClimber;
	public Shooter shooter;
	public Compressor compressor;
	public VisionTargetTracker gearTracker;
	public ControllerButtons buttons;
	public VisionProcessor visionProcessor;

	public Robot() {
		instance = this;
		oi = new OI();
		gearTracker = new VisionTargetTracker(1); // Camera 1
		visionProcessor = new VisionProcessor(1); // Camera 1
		driveTrain = new DriveTrain();
		winchClimber = new WinchClimber();
		shooter = new Shooter();
		gearBox = new GearBox();
		ropeCollector = new RopeCollector();
		gearLiftTracker = new VisionTargetTracker();
		referanceFrame2 = new ReferenceFrame2();
		time = new Timer();
		imu = new ADIS16448_IMU();
		compressor = new Compressor();

	}

	@Override
	public void robotInit() {

		imu.reset();
		imu.calibrate();
		referanceFrame2.reset();
		referanceFrame2.calabrateGyro();

		SmartDashboard.putNumber("Autonomous #", 1);

		oi.shoot.whileHeld(new Shoot());
		oi.climb.whileHeld(new ClimbWithWinch());
		oi.inverseClimb.whileHeld(new UnClimb());
		oi.openGearBox.whenPressed(gearBox.openCommand());
		oi.closeGearBox.whenPressed(gearBox.closeCommand());
		oi.openRopeClamp.whenPressed(ropeCollector.openCommand());
		oi.closeRopeClamp.whenPressed(ropeCollector.closeCommand());

		compressor.start();

		// SmartDashboard.putNumber("Kp", .15);

		/*
		 * //System.out.println("Robot Init"); //referanceFrame2.reset();
		 * //referanceFrame2.start(); //referanceFrame2.calabrateGyro();
		 * //imu.reset(); //imu.calibrate();
		 * //SmartDashboard.putBoolean("Forward?", true);
		 * //SmartDashboard.putNumber("Time?", 10);
		 * //SmartDashboard.putNumber("Speed", .60); //SmartDashboard.putNumber(
		 * "Autonomous #", 1); //SmartDashboard.putString("Event:", "Robot init"
		 * ); //SmartDashboard.putNumber("Degrees", 90);
		 * //SmartDashboard.putNumber("Count", 0);
		 */
	}

	@Override
	public void disabledInit() {
		visionProcessor.stop();
	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void autonomousInit() {
		referanceFrame2.motorEncoder.reset();
		imu.reset();

		if (SmartDashboard.getNumber("Autonomous #", 0) == 1) {
			autonomousCommand = new AutoOne();
		} else if (SmartDashboard.getNumber("Autonomous #", 0) == 2) {
			autonomousCommand = new AutoTwo();
		} else if (SmartDashboard.getNumber("Autonomous #", 0) == 3) {
			autonomousCommand = new AutoThree();
		} else if (SmartDashboard.getNumber("Autonomous #", 0) == 4) {
			autonomousCommand = new AutoFour();
		}
		referanceFrame2.reset();
		visionProcessor.start();
		autonomousCommand.start();

	}

	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
		SmartDashboard.putNumber("MotorEncoderTicks:", referanceFrame2.motorEncoder.get());
		SmartDashboard.putNumber("Frames", visionProcessor.processingTime);
	}

	@Override
	public void teleopInit() {
		if (autonomousCommand != null) {
			autonomousCommand.cancel();
		}

		referanceFrame2.motorEncoder.reset();
		imu.reset();
		referanceFrame2.reset();
		visionProcessor.stop();
		gearBox.close();
	}

	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
		if (oi.joyStick2.getRawButton(ControllerButtons.A)) {
			shooter.ballWranglerIn.set(true);
			shooter.ballWranglerOut.set(false);
		} else {
			shooter.ballWranglerIn.set(false);
			shooter.ballWranglerOut.set(true);

		}

		SmartDashboard.putNumber("MotorEncoderTicks:", referanceFrame2.motorEncoder.get());
		SmartDashboard.putNumber("GYRO", referanceFrame2.getAngle());
		// SmartDashboard.putNumber("MotorAmpage", driveTrain.leftFront.)

	}

	@Override
	public void testPeriodic() {

	}

	public static Robot getInstance() {
		return instance;
	}
}
