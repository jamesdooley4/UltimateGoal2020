package org.firstinspires.ftc.teamcode;

import com.technototes.control.gamepad.GamepadStick;
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
    private final Robot robot;

    /** Gamepads
     *
     */
    private final CommandGamepad driverGamepad;

    /** Buttons for intake
     * mainbutton toggles between intaking in and off, and spit just extakes
     */
    private final CommandButton intakeMainButton, intakeSpitButton;

    private final CommandButton firePrepButton;
    private final CommandAxis fireAxis;

    private final CommandButton powerButton;

    private final double DEFAULT_SHOOTER_VELOCITY = 1350;
    private double lastSetShooterVelocity = DEFAULT_SHOOTER_VELOCITY;

    private final GamepadStick driveLStick, driveRStick;

    private final CommandButton resetGyroButton;

    public OperatorInterface(CommandGamepad driver, Robot r){

        //instantiate objects
        driverGamepad = driver;
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
        driverGamepad.dpadUp.whenPressed(new ShooterSetSpeedCommand(robot.shooterSubsystem, ()->lastSetShooterVelocity+=100));
        driverGamepad.dpadDown.whenPressed(new ShooterSetSpeedCommand(robot.shooterSubsystem, ()->lastSetShooterVelocity-=100));

        driveLStick = driverGamepad.leftStick;
        driveRStick = driverGamepad.rightStick;


        resetGyroButton = driverGamepad.rightStickButton;

        //intake commands
        intakeMainButton.whenPressed(new IntakeInCommand(robot.intakeSubsystem));

        intakeSpitButton.whenPressed(new IntakeOutCommand(robot.intakeSubsystem))
                .whenReleased(new IntakeStopCommand(robot.intakeSubsystem));

        powerButton.whilePressed(new ShooterSetFlapCommand(robot.shooterSubsystem, ()->0.1));

        firePrepButton.whenPressed(
                new ParallelCommandGroup(
                        new ShooterSetFlapCommand(robot.shooterSubsystem, ()->0.65),
                        new ShooterSetSpeedCommand(robot.shooterSubsystem, ()->lastSetShooterVelocity = DEFAULT_SHOOTER_VELOCITY)));
        fireAxis.whenPressed(new SendOneRingToShooterCommand(robot.indexSubsystem));

        driverGamepad.leftStickButton.whilePressed(new ShooterStopCommand(robot.shooterSubsystem));
        //drive command
        resetGyroButton.whileReleased(new DriveCommand(robot.drivebaseSubsystem, driveLStick, driveRStick));
        resetGyroButton.whenPressed(new ResetGyroCommand(robot.drivebaseSubsystem));
    }
}
