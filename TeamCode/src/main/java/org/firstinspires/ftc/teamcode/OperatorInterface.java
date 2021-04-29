package org.firstinspires.ftc.teamcode;

import com.technototes.control.gamepad.GamepadStick;
import com.technototes.library.command.InstantCommand;
import com.technototes.library.command.ParallelCommandGroup;
import com.technototes.library.command.SequentialCommandGroup;
import com.technototes.library.command.WaitCommand;
import com.technototes.library.control.gamepad.CommandAxis;
import com.technototes.library.control.gamepad.CommandButton;
import com.technototes.library.control.gamepad.CommandGamepad;

import org.firstinspires.ftc.teamcode.commands.autonomous.SendOneRingToShooterCommand;
import org.firstinspires.ftc.teamcode.commands.drivebase.DriveCommand;
import org.firstinspires.ftc.teamcode.commands.drivebase.ResetGyroCommand;
import org.firstinspires.ftc.teamcode.commands.drivebase.VisionAlignCommand;
import org.firstinspires.ftc.teamcode.commands.index.IndexPivotDownCommand;
import org.firstinspires.ftc.teamcode.commands.index.IndexPivotUpCommand;
import org.firstinspires.ftc.teamcode.commands.intake.IntakeInCommand;
import org.firstinspires.ftc.teamcode.commands.intake.IntakeOutCommand;
import org.firstinspires.ftc.teamcode.commands.intake.IntakeStopCommand;
import org.firstinspires.ftc.teamcode.commands.shooter.ShooterSetFlapCommand;
import org.firstinspires.ftc.teamcode.commands.shooter.ShooterSetSpeedCommand;
import org.firstinspires.ftc.teamcode.commands.shooter.ShooterStopCommand;
import org.firstinspires.ftc.teamcode.commands.wobble.WobbleCloseThenRaiseCommand;
import org.firstinspires.ftc.teamcode.commands.wobble.WobbleLowerThenOpenCommand;

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

    public CommandButton testButton, wobbleArmButton;

    public CommandButton firePrepButton;
    public CommandAxis fireAxis;

    public CommandButton powerButton;
    public CommandAxis powerAxis;

    public GamepadStick driveLStick, driveRStick;
    //public CommandButton turboModeButton, snailModeButton;

    public CommandButton resetGyroButton;

    public OperatorInterface(CommandGamepad driver, CommandGamepad codriver, Robot r){

        //instantiate objects
        driverGamepad = driver;
        codriverGamepad = codriver;
        robot = r;

        //set buttons
        intakeMainButton = driverGamepad.a;
        intakeSpitButton = driverGamepad.b;

        testButton = driverGamepad.x;
        wobbleArmButton = driverGamepad.y;

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


        wobbleArmButton.whenToggled(new WobbleLowerThenOpenCommand(robot.wobbleSubsystem))
                .whenInverseToggled(new WobbleCloseThenRaiseCommand(robot.wobbleSubsystem));

        //intake commands
        intakeMainButton.whenPressed(new IntakeInCommand(robot.intakeSubsystem));

        intakeSpitButton.whenPressed(new IntakeOutCommand(robot.intakeSubsystem))
                .whenReleased(new IntakeStopCommand(robot.intakeSubsystem));

        powerButton.whilePressed(new ShooterSetFlapCommand(robot.shooterSubsystem, ()->0.23));

        firePrepButton.whenPressed(new ParallelCommandGroup(
                new InstantCommand(()->robot.drivebaseSubsystem.speed = 0.5),
                new ShooterSetSpeedCommand(robot.shooterSubsystem, ()->0.8),
                new ShooterSetFlapCommand(robot.shooterSubsystem, ()->0.47),
                new SequentialCommandGroup(new IntakeInCommand(robot.intakeSubsystem), new WaitCommand(0.4), new IntakeStopCommand(robot.intakeSubsystem))))
                .schedule(()->fireAxis.getAsBoolean()&&firePrepButton.getAsBoolean(), new SendOneRingToShooterCommand(robot.indexSubsystem, ()->1-fireAxis.getAsDouble()))   //new IndexPivotDownCommand(robot.indexSubsystem))
                .whenReleased(new ParallelCommandGroup(
                        new InstantCommand(()->robot.drivebaseSubsystem.speed = 1),
                        new ShooterStopCommand(robot.shooterSubsystem)));

        //drive command
        testButton.whileReleased(new DriveCommand(robot.drivebaseSubsystem, driveLStick, driveRStick))
                .whilePressed(new VisionAlignCommand(robot.drivebaseSubsystem, robot.visionSubsystem));

        resetGyroButton.whenPressed(new ResetGyroCommand(robot.drivebaseSubsystem));



    }
}
