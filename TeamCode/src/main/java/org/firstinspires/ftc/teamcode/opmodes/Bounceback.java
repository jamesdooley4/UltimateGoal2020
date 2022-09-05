package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.technototes.library.command.CommandScheduler;
import com.technototes.library.command.InstantCommand;
import com.technototes.library.command.SequentialCommandGroup;
import com.technototes.library.structure.CommandOpMode;
import com.technototes.logger.Loggable;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.commands.GetStackSizeCommand;
import org.firstinspires.ftc.teamcode.commands.StrafeCommand;
import org.firstinspires.ftc.teamcode.commands.autonomous.AimAndShootCommand;
import org.firstinspires.ftc.teamcode.commands.autonomous.AutoState;
import org.firstinspires.ftc.teamcode.commands.autonomous.BouncebackCommand;
import org.firstinspires.ftc.teamcode.commands.autonomous.DeliverFirstWobble3Command;
import org.firstinspires.ftc.teamcode.commands.autonomous.DeliverSecondWobble3Command;
import org.firstinspires.ftc.teamcode.commands.autonomous.IntakeStackCommand;
import org.firstinspires.ftc.teamcode.commands.autonomous.ObtainSecondWobble3Command;
import org.firstinspires.ftc.teamcode.commands.autonomous.ParkCommand;
import org.firstinspires.ftc.teamcode.commands.autonomous.PathToShootCommand;
import org.firstinspires.ftc.teamcode.commands.autonomous.PowershotCommand;
import org.firstinspires.ftc.teamcode.commands.intake.IntakeInCommand;
import org.firstinspires.ftc.teamcode.commands.intake.IntakeStopCommand;
import org.firstinspires.ftc.teamcode.commands.shooter.ShooterSetSpeedCommand;
import org.firstinspires.ftc.teamcode.commands.wobble.WobbleLowerAndOpenCommand;
import org.firstinspires.ftc.teamcode.commands.wobble.WobbleRaiseCommand;
import org.firstinspires.ftc.teamcode.subsystems.WobbleSubsystem;

@Autonomous(name = "Bounceback")
public class Bounceback extends CommandOpMode implements Loggable {
    /**
     * The robot
     */
    public Robot robot;

    public AutoState state;

    @Override
    public void uponInit() {
        CommandScheduler.resetScheduler();
        robot = new Robot(true);
        robot.wobbleSubsystem.setClawPosition(WobbleSubsystem.ClawPosition.CLOSED);
        robot.wobbleSubsystem.setArmPosition(WobbleSubsystem.ArmPosition.RAISED);
        state = new AutoState(AutoState.Team.RED);
        state.setStackSize(AutoState.StackSize.FOUR);
        CommandScheduler.getInstance().scheduleForState(new GetStackSizeCommand(robot.visionStackSubsystem, state),
                () -> true, OpModeState.INIT);
    }

    @Override
    public void uponStart() {
        robot.turretSubsystem.raise();
        robot.turretSubsystem.setTurretPosition(1);
        CommandScheduler.getInstance().schedule(
                new SequentialCommandGroup(
                                //Powershots and bounceback(3->3 rings in bot)
                                new ShooterSetSpeedCommand(robot.shooterSubsystem, () -> 800),
                                new PowershotCommand(robot.drivebaseSubsystem, robot.shooterSubsystem, robot.intakeSubsystem, robot.indexSubsystem, robot.turretSubsystem, state),
                                new ShooterSetSpeedCommand(robot.shooterSubsystem, () -> 1),
                                new BouncebackCommand(robot.drivebaseSubsystem, robot.shooterSubsystem, robot.intakeSubsystem, robot.indexSubsystem, robot.turretSubsystem, state),

                                //First wobble(3->3 rings in bot)
//                                new StrafeCommand(robot.drivebaseSubsystem, state.correctedFirstWobbleDropPos()),
//                                new WobbleLowerAndOpenCommand(robot.wobbleSubsystem),

                                //go shoot bouncebacks(3->0 rings in bot)
                                new PathToShootCommand(robot.drivebaseSubsystem, robot.shooterSubsystem, robot.intakeSubsystem, robot.turretSubsystem, state),
                                new IntakeStopCommand(robot.intakeSubsystem),
                                new AimAndShootCommand(robot.intakeSubsystem, robot.indexSubsystem, robot.turretSubsystem, robot.visionAimSubsystem, robot.shooterSubsystem),
                                new ShooterSetSpeedCommand(robot.shooterSubsystem, () -> 1),

                                //Pay attention ya goof
                                new InstantCommand(this::terminate),
                                //Did you pay attention?

                                //Intake the stack(0->0 rings in bot, 0 on the field)
                                new IntakeStackCommand(robot.drivebaseSubsystem, robot.intakeSubsystem, state),

                                //Get the second wobble
                                new ObtainSecondWobble3Command(robot.drivebaseSubsystem, robot.wobbleSubsystem, state),

                                //new IntakeInCommand(robot.intakeSubsystem),
                                new ShooterSetSpeedCommand(robot.shooterSubsystem, () -> 800),
                                new PathToShootCommand(robot.drivebaseSubsystem, robot.shooterSubsystem, robot.intakeSubsystem, robot.turretSubsystem, state)
                                        .with(new WobbleRaiseCommand(robot.wobbleSubsystem)),

                                new AimAndShootCommand(robot.intakeSubsystem, robot.indexSubsystem, robot.turretSubsystem, robot.visionAimSubsystem, robot.shooterSubsystem)
                                        .with(new IntakeStopCommand(robot.intakeSubsystem)),
                                new ShooterSetSpeedCommand(robot.shooterSubsystem, () -> 0)
                                        .with(new DeliverSecondWobble3Command(robot.drivebaseSubsystem, robot.wobbleSubsystem, state)
                                                .with(new InstantCommand(() -> robot.turretSubsystem.setTurretPosition(1)))),
                                new ParkCommand(robot.drivebaseSubsystem, robot.wobbleSubsystem, state),
                                new InstantCommand(this::terminate)
                )
        );

    }
}
