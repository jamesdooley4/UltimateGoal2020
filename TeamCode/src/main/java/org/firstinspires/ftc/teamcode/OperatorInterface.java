package org.firstinspires.ftc.teamcode;

import com.technototes.control.gamepad.GamepadStick;
import com.technototes.library.command.InstantCommand;
import com.technototes.library.command.ParallelCommandGroup;
import com.technototes.library.control.gamepad.CommandAxis;
import com.technototes.library.control.gamepad.CommandButton;
import com.technototes.library.control.gamepad.CommandGamepad;

import org.firstinspires.ftc.teamcode.commands.autonomous.SendOneRingToShooterCommand;
import org.firstinspires.ftc.teamcode.commands.drivebase.DriveCommand;
import org.firstinspires.ftc.teamcode.commands.drivebase.ResetGyroCommand;
import org.firstinspires.ftc.teamcode.commands.intake.IntakeInCommand;
import org.firstinspires.ftc.teamcode.commands.intake.IntakeOutCommand;
import org.firstinspires.ftc.teamcode.commands.intake.IntakeStopCommand;
import org.firstinspires.ftc.teamcode.commands.shooter.ShooterSetFlapCommand;
import org.firstinspires.ftc.teamcode.commands.shooter.ShooterSetSpeedCommand;
import org.firstinspires.ftc.teamcode.commands.shooter.ShooterStopCommand;

/** Class for driver controls
 *
 */
public class OperatorInterface {
    /** The robot
     *
     */
    public Robot robot;

    /** Gamepads
     *
     */
    public CommandGamepad driverGamepad, codriverGamepad;

    /** Buttons for intake
     * mainbutton toggles between intaking in and off, and spit just extakes
     */
    public CommandButton intakeMainButton, intakeSpitButton;

    public CommandButton firePrepButton;
    public CommandAxis fireAxis;

    public CommandButton powerButton;
    public CommandAxis powerAxis;

    public GamepadStick driveLStick, driveRStick;

    public CommandButton resetGyroButton;

    public OperatorInterface(CommandGamepad driver, CommandGamepad codriver, Robot r){

        //instantiate objects
        driverGamepad = driver;
        codriverGamepad = codriver;
        robot = r;

        //set buttons
        intakeMainButton = driverGamepad.a;
        intakeSpitButton = driverGamepad.b;

        //testButton = driverGamepad.x;

        firePrepButton = driverGamepad.leftBumper;
        fireAxis = driverGamepad.leftTrigger;
        //to actually fire
        fireAxis.setTriggerThreshold(0.1);

        powerButton = driverGamepad.rightBumper;
        powerAxis = driverGamepad.rightTrigger;
        powerAxis.setTriggerThreshold(0.1);

        driveLStick = driverGamepad.leftStick;
        driveRStick = driverGamepad.rightStick;


        resetGyroButton = driverGamepad.rightStickButton;

        //intake commands
        intakeMainButton.whenPressed(new IntakeInCommand(robot.intakeSubsystem));

        intakeSpitButton.whenPressed(new IntakeOutCommand(robot.intakeSubsystem))
                .whenReleased(new IntakeStopCommand(robot.intakeSubsystem));

        powerButton.whilePressed(new ShooterSetFlapCommand(robot.shooterSubsystem, ()->0));

        //2600 max tps
        //lower flap to aim it higher
        firePrepButton.whenPressed(new ShooterSetFlapCommand(robot.shooterSubsystem, ()->0.88))
                .whilePressed(new ShooterSetSpeedCommand(robot.shooterSubsystem, ()->1350));
        firePrepButton.whenPressed(new ParallelCommandGroup(
                new InstantCommand(()->robot.drivebaseSubsystem.speed = 0.7)
                ))//new SequentialCommandGroup(new IntakeInCommand(robot.intakeSubsystem), new WaitCommand(0.4), new IntakeStopCommand(robot.intakeSubsystem))))
                .schedule(()->fireAxis.getAsBoolean()&&firePrepButton.getAsBoolean() && robot.shooterSubsystem.getVelocity()>=1300, new SendOneRingToShooterCommand(robot.indexSubsystem, ()->1-fireAxis.getAsDouble()))  //new IndexPivotDownCommand(robot.indexSubsystem))
                .whenReleased(new ParallelCommandGroup(
                        new InstantCommand(()->robot.drivebaseSubsystem.speed = 1)
                        //new ShooterSetSpeed2Command(robot.shooterSubsystem, ()->1350),
                ));


        driverGamepad.leftStickButton.whilePressed(new ShooterStopCommand(robot.shooterSubsystem));
        //drive command
        resetGyroButton.whileReleased(new DriveCommand(robot.drivebaseSubsystem, driveLStick, driveRStick));
        resetGyroButton.whenPressed(new ResetGyroCommand(robot.drivebaseSubsystem));
    }
}
