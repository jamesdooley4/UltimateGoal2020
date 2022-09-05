package org.firstinspires.ftc.teamcode;

import com.technototes.library.hardware.HardwareDevice;
import com.technototes.logger.Color;
import com.technototes.logger.Log;
import com.technototes.logger.Loggable;

import org.firstinspires.ftc.teamcode.subsystems.IndexSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.DrivebaseSubsystem;

/** Class for the subsystems on the robot
 *
 */
public class Robot implements Loggable {
    public Hardware hardware;

    //drivebase
    public DrivebaseSubsystem drivebaseSubsystem;

    //index
    @Log(name = "Index", index = 4, color = Color.ORANGE, entryColor = Color.LIGHT_GRAY)
    public IndexSubsystem indexSubsystem;

    //intake not logged with autonomous opmode
    @Log(name = "Intake", index = 1, color = Color.BLUE, entryColor = Color.LIGHT_GRAY)
    public IntakeSubsystem intakeSubsystem;

    //shooter logged with number/progress bar
    //@Log.NumberBar(name = "Shooter", index =  2, completeBarColor = Color.GREEN, min = 0, max=2000)
    @Log(name = "shooter, index = 2")
    public ShooterSubsystem shooterSubsystem;

    //voltage displayed in yellow to catch driver's eye
    @Log.Number(name="VOLTAGE", index = 0, color = Color.YELLOW, numberColor = Color.LIGHT_GRAY)
    public double getVoltage(){
        double d = HardwareDevice.hardwareMap.voltageSensor.iterator().next().getVoltage();
        return d;
    }

    public Robot(boolean stack){
        hardware = new Hardware();

        drivebaseSubsystem = new DrivebaseSubsystem(hardware.flDriveMotor, hardware.frDriveMotor, hardware.rlDriveMotor, hardware.rrDriveMotor, hardware.imu);

        indexSubsystem = new IndexSubsystem(hardware.indexArmServo);

        intakeSubsystem = new IntakeSubsystem(hardware.intakeMotorGroup);

        shooterSubsystem = new ShooterSubsystem(hardware.shooterMotor, hardware.shooterFlapServo);
    }
    public Robot(){
        this(false);
    }
}
