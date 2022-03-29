package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;


@Autonomous(name="Blue2 park storage", group="Blue")
@Disabled
public class Blue2_storage_park  extends LinearOpMode {


    static final double COUNTS_PER_MOTOR_REV = 1440;
    static final double DRIVE_GEAR_REDUCTION = 2.0;
    static final double WHEEL_DIAMETER_INCHES = 4.0;
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double DRIVE_SPEED = 0.6;
    static final double TURN_SPEED = 0.5;
    private static final String TFOD_MODEL_ASSET = "FreightFrenzy_BCDM.tflite";
    private static final String[] LABELS = {
            "Ball",
            "Cube",
            "Duck",
            "Marker"
    };
    private static final String VUFORIA_KEY = "AXaixb7/////AAABmerN5p/9iEggoRwnZvCC9QSIKnn7TA3GqAE6H2gihMwnCS4JKHKZUxdqn9dBt5Jg29PHVYcOWrCawh6okXVCLrXEwe2O8JppFCPFxDWfLooTUV0wEUy9a6ZT0LsAg0z9AZYiZSZb3ZNhrGZ6Bu1EZbjJBCEEqWIBbBWTB9U1xF/1SGrAkL735dMCdnHREkDkAHIiAePngb4f0E3Y98BzeOILJTwiYX6oFICFFX7ubHZMgIteHwyXVwBX/lyheirHuGUl0Ffk4ts4XzbN+gHQTBnCI1HxuAi7X2llvPDu71zRRJfTgQB16R+xT27a2xDrNlVwKt+oiOsj0IdmkKwP2vudgVIz3kEE/wydVUQDNjHu";
    Robot robot = new Robot();
    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {


        int state = 0;

        if (state == 0) {
            robot.init(hardwareMap);


            telemetry.addData("Status", "Resetting Encoders");
            telemetry.update();

            robot.motorFL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.motorFR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            robot.motorFL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.motorFR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


            telemetry.addData("Path0", "Starting at %7d :%7d",
                    robot.motorFL.getCurrentPosition(),
                    robot.motorFR.getCurrentPosition());
            telemetry.update();


            waitForStart();
            state = 12;
        }

        //move forward
        if (state == 1) {
            telemetry.addData("State", "1");
            telemetry.update();
            runtime = robot.encoderDrive(1.0, -16.5, -16.5, -16.5, -16.5, 10, runtime);
            while (opModeIsActive() && (robot.motorFL.isBusy() || robot.motorFR.isBusy() || robot.motorBL.isBusy() || robot.motorBR.isBusy())) {
                telemetry.addData("Path1", "Running to %7d :%7d", robot.newLeftTarget, robot.newRightTarget);
                telemetry.addData("Path2", "Running at %7d :%7d",
                        robot.motorFL.getCurrentPosition(),
                        robot.motorFR.getCurrentPosition());
                telemetry.update();
            }

            state = 2;
        }
        //turn
        if (state == 2) {
            telemetry.addData("State", "2");
            telemetry.update();
            runtime = robot.encoderDrive(1, -8, 8, -8, 8, 10, runtime);

            while (opModeIsActive() && (robot.motorFL.isBusy() || robot.motorFR.isBusy() || robot.motorBL.isBusy() || robot.motorBR.isBusy())) {
                telemetry.addData("Path1", "Running to %7d :%7d", robot.newLeftTarget, robot.newRightTarget);
                telemetry.addData("Path2", "Running at %7d :%7d",
                        robot.motorFL.getCurrentPosition(),
                        robot.motorFR.getCurrentPosition());
                telemetry.update();
            }

            state = 3;
        }
        //forward
        if (state == 3) {
            telemetry.addData("State", "3");
            telemetry.update();
            runtime = robot.encoderDrive(1.0, -11, -11, -11, -11, 10, runtime);
            while (opModeIsActive() && (robot.motorFL.isBusy() || robot.motorFR.isBusy() || robot.motorBL.isBusy() || robot.motorBR.isBusy())) {
                telemetry.addData("Path1", "Running to %7d :%7d", robot.newLeftTarget, robot.newRightTarget);
                telemetry.addData("Path2", "Running at %7d :%7d",
                        robot.motorFL.getCurrentPosition(),
                        robot.motorFR.getCurrentPosition());
                telemetry.update();
            }


            state = 4;
        }


        //turn
        if (state == 4) {
            telemetry.addData("State", "4");
            telemetry.update();
            runtime = robot.encoderDrive(1, -6.5, 6.5, -6.5, 6.5, 10, runtime);
            while (opModeIsActive() && (robot.motorFL.isBusy() || robot.motorFR.isBusy() || robot.motorBL.isBusy() || robot.motorBR.isBusy())) {
                telemetry.addData("Path1", "Running to %7d :%7d", robot.newLeftTarget, robot.newRightTarget);
                telemetry.addData("Path2", "Running at %7d :%7d",
                        robot.motorFL.getCurrentPosition(),
                        robot.motorFR.getCurrentPosition());
                telemetry.update();
            }

            state = 5;
        }


        if (state == 5) {
            telemetry.addData("State", "5");
            telemetry.update();
            runtime = robot.encoderDrive(1.0, -9.7, -9.7, -9.7, -9.7, 10, runtime);
            while (opModeIsActive() && (robot.motorFL.isBusy() || robot.motorFR.isBusy() || robot.motorBL.isBusy() || robot.motorBR.isBusy())) {
                telemetry.addData("Path1", "Running to %7d :%7d", robot.newLeftTarget, robot.newRightTarget);
                telemetry.addData("Path2", "Running at %7d :%7d",
                        robot.motorFL.getCurrentPosition(),
                        robot.motorFR.getCurrentPosition());
                telemetry.update();
            }

            state = 6;
        }

        if (state == 6) {
            telemetry.addData("State", "6");
            telemetry.update();
            runtime = robot.wheelSpin(1.0, 4, runtime);
            while (opModeIsActive() &&
                    (robot.duckWheel.isBusy())) {
                telemetry.addData("Duck Wheel", robot.duckWheel.getPower());
                telemetry.update();
            }

            state = 7;
        }


        if (state == 7) {
            telemetry.addData("State", "7");
            telemetry.update();
            runtime = robot.encoderDrive(1, 5, 5, 5, 5, 10, runtime);
            while (opModeIsActive() && (robot.motorFL.isBusy() || robot.motorFR.isBusy() || robot.motorBL.isBusy() || robot.motorBR.isBusy())) {
                telemetry.addData("Path1", "Running to %7d :%7d", robot.newLeftTarget, robot.newRightTarget);
                telemetry.addData("Path2", "Running at %7d :%7d",
                        robot.motorFL.getCurrentPosition(),
                        robot.motorFR.getCurrentPosition());
                telemetry.update();
            }

            state = 8;
        }


        if (state == 8) {
            telemetry.addData("State", "8");
            telemetry.update();
            runtime = robot.armLift(0.4, 400, 10, runtime);
            while (opModeIsActive() &&
                    (robot.arm.isBusy())) {
                telemetry.addData("Arm", robot.arm.getPower());
                telemetry.addData("Arm Position", robot.arm.getCurrentPosition());
                telemetry.update();
            }

            state = 9;
        }

        if (state == 9) {
            telemetry.addData("state", "9");
            telemetry.update();
            runtime = robot.clawMove(robot.closed, 10, runtime);
            while (opModeIsActive() && robot.claw.getPosition() != robot.open) {
                telemetry.addData("Claw", robot.claw.getPosition());
                telemetry.update();
            }

            state = 10;
        }

        if (state == 10) {
            telemetry.addData("state", "10");
            telemetry.update();

            sleep(500);

            runtime = robot.goToImage(1, -5, 5, runtime);
            while (opModeIsActive() && robot.targetFound) {
                telemetry.addData("Target", " %s", robot.targetName);
                telemetry.addData("Range", "%5.1f inches", robot.targetRange);
                telemetry.addData("Bearing", "%3.0f degrees", robot.targetBearing);
            }
        }

        if (state == 11) {
            telemetry.addData("state", "11");
            telemetry.update();
            runtime = robot.encoderDrive(1.0, -6, -6, -6.5, -6.5, 10, runtime);
            while (opModeIsActive() && (robot.motorFL.isBusy() || robot.motorFR.isBusy() || robot.motorBL.isBusy() || robot.motorBR.isBusy())) {
                telemetry.addData("Path1", "Running to %7d :%7d", robot.newLeftTarget, robot.newRightTarget);
                telemetry.addData("Path2", "Running at %7d :%7d",
                        robot.motorFL.getCurrentPosition(),
                        robot.motorFR.getCurrentPosition());
                telemetry.update();
            }

            state = 12;
        }

        if (state == 12) {
            telemetry.addData("state", "12");
            telemetry.update();
            runtime = robot.detectDuck(4,runtime);

            state = 13;
        }

        /**
         * Initialize the Vuforia localization engine.
         */

        if (state == 13) {
            if (robot.tier == 1) {

                telemetry.addData("State", "13");
                telemetry.update();
                runtime = robot.encoderDrive(1, -5, -5, -5, -5, 10, runtime);
                while (opModeIsActive() && (robot.motorFL.isBusy() || robot.motorFR.isBusy() || robot.motorBL.isBusy() || robot.motorBR.isBusy())) {
                    telemetry.addData("Path1", "Running to %7d :%7d", robot.newLeftTarget, robot.newRightTarget);
                    telemetry.addData("Path2", "Running at %7d :%7d",
                            robot.motorFL.getCurrentPosition(),
                            robot.motorFR.getCurrentPosition());
                    telemetry.update();
                }
            }
        }


        sleep(1000);

        telemetry.addData("Path", "Complete");
        telemetry.update();


    }
}