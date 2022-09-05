package org.firstinspires.ftc.teamcode.commands.index;

import com.technototes.library.command.WaitCommand;

import org.firstinspires.ftc.teamcode.subsystems.IndexSubsystem;

import java.util.function.DoubleSupplier;

public class ArmSetPositionCommand extends WaitCommand {
    public IndexSubsystem subsystem;
    public DoubleSupplier supplier;
    public double curr;
    public ArmSetPositionCommand(IndexSubsystem sub, DoubleSupplier sup) {
        super(0.1);
        subsystem = sub;
        supplier = sup;
    }

    @Override
    public void init() {
        curr = supplier.getAsDouble();
        subsystem.setPosition(curr);
    }

}
