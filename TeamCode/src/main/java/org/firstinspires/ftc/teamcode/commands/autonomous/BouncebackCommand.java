package org.firstinspires.ftc.teamcode.commands.autonomous;

import android.util.Pair;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.technototes.library.command.ParallelCommandGroup;
import com.technototes.library.command.ParallelRaceGroup;
import com.technototes.library.command.SequentialCommandGroup;
import com.technototes.library.command.WaitCommand;

import org.firstinspires.ftc.teamcode.commands.StrafeCommand;
import org.firstinspires.ftc.teamcode.commands.TrajectoryCommand;
import org.firstinspires.ftc.teamcode.commands.intake.IntakeInCommand;
import org.firstinspires.ftc.teamcode.commands.intake.IntakeStopCommand;
import org.firstinspires.ftc.teamcode.commands.shooter.ShooterSetSpeed2Command;
import org.firstinspires.ftc.teamcode.commands.shooter.ShooterStopCommand;
import org.firstinspires.ftc.teamcode.subsystems.DrivebaseSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IndexSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.TurretSubsystem;

public class BouncebackCommand extends SequentialCommandGroup {
    public BouncebackCommand(DrivebaseSubsystem d, ShooterSubsystem s, IntakeSubsystem i, IndexSubsystem ix, TurretSubsystem t, AutoState st) {
        super(
                //bounceback time
                new SequentialCommandGroup(
                        new ParallelCommandGroup(
                                new StrafeCommand(d, st.correctedPos(115, 50, -60)),
                                new ShooterStopCommand(s),
                                new IntakeInCommand(i)
                        ),
//                        new StrafeCommand(d, st.correctedPos(115, -10, -90)),
//                        new StrafeCommand(d, st.correctedPos(110, -5, -120)),
//                        new StrafeCommand(d, st.correctedPos(80, -5, 0))
                        new TrajectoryCommand(d,
                                new Pair<>(st.correctedTan(-100.0), st.correctedPos(105, 0, 0)),
                                new Pair<>(st.correctedTan(-140.0), st.correctedPos(80, 5, 0))
                        )
                )
        );
    }
}
