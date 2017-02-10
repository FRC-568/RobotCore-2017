package org.usfirst.frc.team568.robot.commands;

import org.usfirst.frc.team568.robot.subsystems.GearBox;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoOne extends CommandGroup {

	public AutoOne(GearBox gearBox) {
		// addSequential(gearBox.closeCommand());
		addSequential(new Drive(100, .2));
		// addSequential(new Turn(90));
		//
		addSequential(gearBox.openCommand());
	}

}
