package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.technototes.library.hardware.HardwareDevice;
import com.technototes.library.hardware.motor.EncodedMotor;
import com.technototes.library.hardware.motor.EncodedMotorGroup;
import com.technototes.library.hardware.motor.Motor;
import com.technototes.library.hardware.motor.MotorGroup;

import com.technototes.library.hardware.servo.Servo;
import com.technototes.library.hardware.servo.ServoGroup;
import com.technototes.logger.Loggable;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;

import com.technototes.library.hardware.sensor.IMU;

/** Class for the hardware devices of the robot
 *
 */
public class Hardware implements Loggable {

    //drivebase
    public EncodedMotor<DcMotorEx> flDriveMotor;
    public EncodedMotor<DcMotorEx> frDriveMotor;
    public EncodedMotor<DcMotorEx> rlDriveMotor;
    public EncodedMotor<DcMotorEx> rrDriveMotor;

    public IMU imu;

    //index
    public Servo indexArmServo;

    //intake
    public Motor intakeMotor1;
    public Motor intakeMotor2;
    public MotorGroup intakeMotorGroup;

    //shooter
    public EncodedMotor<DcMotorEx> shooterMotor;

    public Servo shooterFlapServo;

    public Hardware(){
        flDriveMotor = new EncodedMotor<>("flMotor");
        frDriveMotor = new EncodedMotor<>("frMotor");
        rlDriveMotor = new EncodedMotor<>("rlMotor");
        rrDriveMotor = new EncodedMotor<>("rrMotor");

        imu = new IMU("imu");

        indexArmServo = new Servo("indexarm");

        intakeMotor1 = new Motor<>("intake1");
        intakeMotor2 = new Motor<>("intake2").invert();
        //TODO fix this warning
        intakeMotorGroup = new MotorGroup(intakeMotor1, intakeMotor2);

        shooterMotor = new EncodedMotor<DcMotorEx>("shooter1").invert();

        shooterFlapServo = new Servo("flapservo");
    }

}
