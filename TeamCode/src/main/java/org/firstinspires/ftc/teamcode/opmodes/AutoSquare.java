package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.technototes.library.command.CommandScheduler;
import com.technototes.library.command.InstantCommand;
import com.technototes.library.command.SequentialCommandGroup;
import com.technototes.library.command.WaitCommand;
import com.technototes.library.structure.CommandOpMode;

import org.firstinspires.ftc.teamcode.Robot;

@Autonomous(name="drive square")
public class AutoSquare extends CommandOpMode {

    private Robot robot;

    @Override
    public void uponInit() {
        robot = new Robot();
    }

    @Override
    public void uponStart() {
        CommandScheduler.getInstance().schedule(
                new SequentialCommandGroup(
                        new InstantCommand(() -> robot.drivebaseSubsystem.setWeightedDrivePower(new Pose2d(1, 0, 0))),
                        new WaitCommand(1),
                        new InstantCommand(() -> robot.drivebaseSubsystem.setWeightedDrivePower(new Pose2d(0, 1, 90))),
                        new WaitCommand(1),
                        new InstantCommand(() -> robot.drivebaseSubsystem.setWeightedDrivePower(new Pose2d(-1, 0, 180))),
                        new WaitCommand(1),
                        new InstantCommand(() -> robot.drivebaseSubsystem.setWeightedDrivePower(new Pose2d(0, -1, 270))),
                        new WaitCommand(1),
                        new InstantCommand(() -> robot.drivebaseSubsystem.setWeightedDrivePower(new Pose2d(0,0,0))),
                        new WaitCommand(1)
                )
        );
    }
}
