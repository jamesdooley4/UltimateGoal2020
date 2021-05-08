package org.firstinspires.ftc.teamcode.commands.turret;


import com.technototes.library.command.Command;
import com.technototes.library.hardware.servo.Servo;
import com.technototes.library.subsystem.Subsystem;

import org.firstinspires.ftc.teamcode.subsystems.TurretSubsystem;

public class TurretRotateRightCommand extends Command {
    public double start;
    public TurretSubsystem subsystem;

    public TurretRotateRightCommand(TurretSubsystem sub){
        addRequirements(sub);
        subsystem = sub;


    }

    @Override
    public void init() {
        start = subsystem.getTurretPosition();
    }

    public void execute(){


        subsystem.setServo1(start+ Math.pow(commandRuntime.seconds(), 3));




    }

}
