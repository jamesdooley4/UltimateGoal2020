package org.firstinspires.ftc.teamcode.subsystems;

import com.technototes.library.hardware.servo.Servo;
import com.technototes.library.subsystem.servo.ServoSubsystem;
import com.technototes.logger.Stated;

public class TurretSubsystem extends ServoSubsystem implements Stated<String> {

    public Servo TurretServo;


    public TurretSubsystem( Servo s){

        TurretServo = s;




    }

    public void setServo1(double val){
        TurretServo.setPosition(val);


    }
    public void changeBy(double change){

        TurretServo.setPosition(TurretServo.getPosition()+change);


    }


    public String getState() {
        return "TURRET: "+TurretServo.getPosition();
    }

    public double getTurretPosition(){

        return TurretServo.getPosition();

    }


}
