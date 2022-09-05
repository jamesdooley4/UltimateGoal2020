package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.technototes.library.command.CommandScheduler;
import com.technototes.library.command.InstantCommand;
import com.technototes.library.hardware.motor.EncodedMotor;
import com.technototes.library.hardware.motor.EncodedMotorGroup;
import com.technototes.library.hardware.servo.Servo;
import com.technototes.library.structure.CommandOpMode;
import com.technototes.logger.Log;
import com.technototes.logger.LogConfig;
import com.technototes.logger.Loggable;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.OperatorInterface;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.commands.TurnCommand;
import org.firstinspires.ftc.teamcode.commands.drivebase.DriveCommand;
import org.firstinspires.ftc.teamcode.commands.index.ArmSetPositionCommand;
import org.firstinspires.ftc.teamcode.commands.shooter.ShooterSetFlapCommand;
import org.firstinspires.ftc.teamcode.commands.shooter.ShooterSetSpeedCommand;
//@Disabled
@TeleOp(name = "shooter config")
public class ShooterConfigOpMode extends CommandOpMode implements Loggable {
    @LogConfig.Disabled
    public Robot robot;

    // Shooter motor doesn't keep constant velocity, so store the last set shooter velocity value locally
    private double lastSetShooterVelocity = 0;

    private final double DEFAULT_INDEX_POSITION = 1.0;
    private final double DEFAULT_FLAP_POSITION = 1.0;
    private final double DEFAULT_SHOOTER_VELOCITY = 0.0;

    @Override
    public void uponInit() {
        robot = new Robot();

        robot.shooterSubsystem.setVelocity(DEFAULT_SHOOTER_VELOCITY);
        robot.shooterSubsystem.setFlapPosition(DEFAULT_FLAP_POSITION);
        robot.indexSubsystem.setPosition(DEFAULT_INDEX_POSITION);

        driverGamepad.dpadUp.whilePressed(new ShooterSetSpeedCommand(robot.shooterSubsystem, ()->lastSetShooterVelocity+=10));
        driverGamepad.dpadDown.whilePressed(new ShooterSetSpeedCommand(robot.shooterSubsystem, ()->lastSetShooterVelocity-=10));
        driverGamepad.a.whenPressed(new ShooterSetSpeedCommand(robot.shooterSubsystem, ()->lastSetShooterVelocity=DEFAULT_SHOOTER_VELOCITY));

        driverGamepad.dpadLeft.whilePressed(new ShooterSetFlapCommand(robot.shooterSubsystem, ()->robot.shooterSubsystem.getFlapPosition()+0.05));
        driverGamepad.dpadRight.whilePressed(new ShooterSetFlapCommand(robot.shooterSubsystem, ()->robot.shooterSubsystem.getFlapPosition()-0.05));
        driverGamepad.b.whenPressed(new ShooterSetFlapCommand(robot.shooterSubsystem, ()->DEFAULT_FLAP_POSITION));

        driverGamepad.leftBumper.whilePressed(new ArmSetPositionCommand(robot.indexSubsystem, ()->robot.indexSubsystem.getPosition()+0.05));
        driverGamepad.rightBumper.whilePressed(new ArmSetPositionCommand(robot.indexSubsystem, ()->robot.indexSubsystem.getPosition()-0.05));
        driverGamepad.x.whenPressed(new ArmSetPositionCommand(robot.indexSubsystem, ()->DEFAULT_INDEX_POSITION));
    }

    @Log.Number(name = "shooterSpeed", index = 0)
    public double shooter(){
        System.out.print("  sp "+ robot.shooterSubsystem.motor1.getDevice().getVelocity());

        return robot.shooterSubsystem.getVelocity();
    }
    @Log.Number(name = "shooterFlap", index = 1)
    public double flap(){
        System.out.print("  fl "+ robot.shooterSubsystem.getFlapPosition());
        return robot.shooterSubsystem.getFlapPosition();
    }
    @Log.Number(name = "indexArm", index = 2)
    public double arm(){
        System.out.print("  fl "+ robot.indexSubsystem.getPosition());
        return robot.indexSubsystem.getPosition();
    }

    @Override
    public void universalLoop() {
    }
}
