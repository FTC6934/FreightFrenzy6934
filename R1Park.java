package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;


@Autonomous(name="R1 PARK", group="Blue")
public class R1Park extends LinearOpMode {
    Robot robot = new Robot();
    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {


        int state = 0;

        // INITIALIZE
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
            state = 1;
        }

        // GRAB PRE LOAD
        if (state == 1){
            telemetry.addData("State", "1");
            telemetry.update();
            runtime = robot.clawMove(robot.closed, 3, runtime);
            while (opModeIsActive() && robot.claw.getPosition() != robot.closed){
                telemetry.addData("Claw Position",robot.claw.getPosition());
                telemetry.update();
            }

            state = 2;
        }

        // DRIVE UP TO BARCODE
        if (state == 2) {
            telemetry.addData("State", "1");
            telemetry.update();
            runtime = robot.encoderDrive(1.0, -14, -14, -14, -14, 10, runtime);
            while (opModeIsActive() && (robot.motorFL.isBusy() || robot.motorFR.isBusy() || robot.motorBL.isBusy() || robot.motorBR.isBusy())) {
                telemetry.addData("Path1", "Running to %7d :%7d", robot.newLeftTarget, robot.newRightTarget);
                telemetry.addData("Path2", "Running at %7d :%7d",
                        robot.motorFL.getCurrentPosition(),
                        robot.motorFR.getCurrentPosition());
                telemetry.update();
            }

            state = 8;
        }

        // CHECK DUCK IN SPOT 1
        if (state == 3) {
            telemetry.addData("State", "3");
            telemetry.update();
            runtime = robot.detectDuck(2,runtime);
            if (robot.tier == 1){
                state = 14;
            }
            else{
                state = 15;
            }
        }

        // IF DUCK DETECTED, TURN TOWARD HUB
        if (state == 4) {
            telemetry.addData("State", "4");
            telemetry.update();
            runtime = robot.encoderDrive(1.0, -8, 8, -8, 8, 10, runtime);
            while (opModeIsActive() && (robot.motorFL.isBusy() || robot.motorFR.isBusy() || robot.motorBL.isBusy() || robot.motorBR.isBusy())) {
                telemetry.addData("Path1", "Running to %7d :%7d", robot.newLeftTarget, robot.newRightTarget);
                telemetry.addData("Path2", "Running at %7d :%7d",
                        robot.motorFL.getCurrentPosition(),
                        robot.motorFR.getCurrentPosition());
                telemetry.update();
            }


            state = 6;
        }

        // IF DUCK NOT DETECTED, TURN TOWARD POSITION 2
        if (state == 5) {
            telemetry.addData("State", "5");
            telemetry.update();
            runtime = robot.encoderDrive(1, -4, 4, -4, 4, 10, runtime);
            while (opModeIsActive() && (robot.motorFL.isBusy() || robot.motorFR.isBusy() || robot.motorBL.isBusy() || robot.motorBR.isBusy())) {
                telemetry.addData("Path1", "Running to %7d :%7d", robot.newLeftTarget, robot.newRightTarget);
                telemetry.addData("Path2", "Running at %7d :%7d",
                        robot.motorFL.getCurrentPosition(),
                        robot.motorFR.getCurrentPosition());
                telemetry.update();
            }

            state = 7;
        }

        // MOVE TOWARD HUB
        if (state == 6) {
            telemetry.addData("State", "6");
            telemetry.update();
            runtime = robot.encoderDrive(1.0, -7, -7, -7, -7, 10, runtime);
            while (opModeIsActive() && (robot.motorFL.isBusy() || robot.motorFR.isBusy() || robot.motorBL.isBusy() || robot.motorBR.isBusy())) {
                telemetry.addData("Path1", "Running to %7d :%7d", robot.newLeftTarget, robot.newRightTarget);
                telemetry.addData("Path2", "Running at %7d :%7d",
                        robot.motorFL.getCurrentPosition(),
                        robot.motorFR.getCurrentPosition());
                telemetry.update();
            }

            state = 8;
        }

        // CHECK DUCK IN POSITION 2
        if (state == 7) {
            telemetry.addData("State", "7");
            telemetry.update();
            runtime = robot.detectDuck(2,runtime);
            if (robot.tier == 1){
                state = 20;
            }
            else{
                state = 21;
            }

        }

        // LIFT ARM TO LEVEL 1
        if (state == 8) {
            telemetry.addData("State", "8");
            telemetry.update();
            runtime = robot.armLift(0.5, 350, 10, runtime);
            while (opModeIsActive() &&
                    (robot.arm.isBusy())) {
                telemetry.addData("Arm", robot.arm.getPower());
                telemetry.addData("Arm Position", robot.arm.getCurrentPosition());
                telemetry.update();
            }

            state = 12;
        }

        // MOVE TO LEVEL 1
        if (state == 9) {
            telemetry.addData("state", "9");
            telemetry.update();
            runtime = robot.encoderDrive(1.0, -4, -4, -4, -4, 10, runtime);
            while (opModeIsActive() && (robot.motorFL.isBusy() || robot.motorFR.isBusy() || robot.motorBL.isBusy() || robot.motorBR.isBusy())) {
                telemetry.addData("Path1", "Running to %7d :%7d", robot.newLeftTarget, robot.newRightTarget);
                telemetry.addData("Path2", "Running at %7d :%7d",
                        robot.motorFL.getCurrentPosition(),
                        robot.motorFR.getCurrentPosition());
                telemetry.update();
            }

            state = 10;
        }

        // DROP BLOCK
        if (state == 10) {
            telemetry.addData("state", "10");
            telemetry.update();
            runtime = robot.clawMove(robot.open, 10, runtime);
            while (opModeIsActive() && robot.claw.getPosition() != robot.open) {
                telemetry.addData("Claw", robot.claw.getPosition());
                telemetry.update();
            }

            state = 11;
        }

        // BACK AWAY FROM HUB
        if (state == 11){
            telemetry.addData("state", "11");
            telemetry.update();
            runtime = robot.encoderDrive(1,6,3,6,3,10,runtime);
            while (opModeIsActive() && (robot.motorFL.isBusy() || robot.motorFR.isBusy() || robot.motorBL.isBusy() || robot.motorBR.isBusy())) {
                telemetry.addData("Path1", "Running to %7d :%7d", robot.newLeftTarget, robot.newRightTarget);
                telemetry.addData("Path2", "Running at %7d :%7d",
                        robot.motorFL.getCurrentPosition(),
                        robot.motorFR.getCurrentPosition());
                telemetry.update();
            }

            state = 12;
        }

        // TURN TOWARD WAREHOUSE
        if (state == 12){
            telemetry.addData("state", "12");
            telemetry.update();
            runtime = robot.encoderDrive(1,-8,8,-8,8,10,runtime);
            while (opModeIsActive() && (robot.motorFL.isBusy() || robot.motorFR.isBusy() || robot.motorBL.isBusy() || robot.motorBR.isBusy())) {
                telemetry.addData("Path1", "Running to %7d :%7d", robot.newLeftTarget, robot.newRightTarget);
                telemetry.addData("Path2", "Running at %7d :%7d",
                        robot.motorFL.getCurrentPosition(),
                        robot.motorFR.getCurrentPosition());
                telemetry.update();
            }

            state = 13;
        }

        // DRIVE INTO WAREHOUSE
        if (state == 13){
            telemetry.addData("state", "13");
            telemetry.update();
            runtime = robot.encoderDrive(1,-25,-25,-25,-25,10,runtime);
            while (opModeIsActive() && (robot.motorFL.isBusy() || robot.motorFR.isBusy() || robot.motorBL.isBusy() || robot.motorBR.isBusy())) {
                telemetry.addData("Path1", "Running to %7d :%7d", robot.newLeftTarget, robot.newRightTarget);
                telemetry.addData("Path2", "Running at %7d :%7d",
                        robot.motorFL.getCurrentPosition(),
                        robot.motorFR.getCurrentPosition());
                telemetry.update();
            }

            state = -1;
        }

        // IF DUCK DETECTED, TURN TOWARD HUB
        if (state == 14) {
            telemetry.addData("State", "14");
            telemetry.update();
            runtime = robot.encoderDrive(1.0, -6, 6, -6, 6, 10, runtime);
            while (opModeIsActive() && (robot.motorFL.isBusy() || robot.motorFR.isBusy() || robot.motorBL.isBusy() || robot.motorBR.isBusy())) {
                telemetry.addData("Path1", "Running to %7d :%7d", robot.newLeftTarget, robot.newRightTarget);
                telemetry.addData("Path2", "Running at %7d :%7d",
                        robot.motorFL.getCurrentPosition(),
                        robot.motorFR.getCurrentPosition());
                telemetry.update();
            }


            state = 16;
        }

        // IF DUCK NOT DETECTED, TURN TOWARD POSITION 2
        if (state == 15) {
            telemetry.addData("State", "15");
            telemetry.update();
            runtime = robot.encoderDrive(1, -2, 2, -2, 2, 10, runtime);
            while (opModeIsActive() && (robot.motorFL.isBusy() || robot.motorFR.isBusy() || robot.motorBL.isBusy() || robot.motorBR.isBusy())) {
                telemetry.addData("Path1", "Running to %7d :%7d", robot.newLeftTarget, robot.newRightTarget);
                telemetry.addData("Path2", "Running at %7d :%7d",
                        robot.motorFL.getCurrentPosition(),
                        robot.motorFR.getCurrentPosition());
                telemetry.update();
            }

            state = 17;
        }

        // MOVE TOWARD HUB
        if (state == 16) {
            telemetry.addData("State", "16");
            telemetry.update();
            runtime = robot.encoderDrive(1.0, -7, -7, -7, -7, 10, runtime);
            while (opModeIsActive() && (robot.motorFL.isBusy() || robot.motorFR.isBusy() || robot.motorBL.isBusy() || robot.motorBR.isBusy())) {
                telemetry.addData("Path1", "Running to %7d :%7d", robot.newLeftTarget, robot.newRightTarget);
                telemetry.addData("Path2", "Running at %7d :%7d",
                        robot.motorFL.getCurrentPosition(),
                        robot.motorFR.getCurrentPosition());
                telemetry.update();
            }

            state = 18;
        }

        // CHECK DUCK IN POSITION 3
        if (state == 17) {
            telemetry.addData("State", "17");
            telemetry.update();
            runtime = robot.detectDuck(2,runtime);
            state = 24;
        }

        // LIFT ARM TO LEVEL 2
        if (state == 18) {
            telemetry.addData("State", "18");
            telemetry.update();
            runtime = robot.armLift(0.5, 700, 10, runtime);
            while (opModeIsActive() &&
                    (robot.arm.isBusy())) {
                telemetry.addData("Arm", robot.arm.getPower());
                telemetry.addData("Arm Position", robot.arm.getCurrentPosition());
                telemetry.update();
            }

            state = 19;
        }

        // MOVE TO LEVEL 2
        if (state == 19) {
            telemetry.addData("state", "19");
            telemetry.update();
            runtime = robot.encoderDrive(1.0, -4, -4, -4, -4, 10, runtime);
            while (opModeIsActive() && (robot.motorFL.isBusy() || robot.motorFR.isBusy() || robot.motorBL.isBusy() || robot.motorBR.isBusy())) {
                telemetry.addData("Path1", "Running to %7d :%7d", robot.newLeftTarget, robot.newRightTarget);
                telemetry.addData("Path2", "Running at %7d :%7d",
                        robot.motorFL.getCurrentPosition(),
                        robot.motorFR.getCurrentPosition());
                telemetry.update();
            }

            state = 20;
        }

        // DROP BLOCK
        if (state == 20) {
            telemetry.addData("state", "20");
            telemetry.update();
            runtime = robot.clawMove(robot.open, 10, runtime);
            while (opModeIsActive() && robot.claw.getPosition() != robot.open) {
                telemetry.addData("Claw", robot.claw.getPosition());
                telemetry.update();
            }

            state = 21;
        }

        // BACK AWAY FROM HUB
        if (state == 21){
            telemetry.addData("state", "21");
            telemetry.update();
            runtime = robot.encoderDrive(1,6,3,6,3,10,runtime);
            while (opModeIsActive() && (robot.motorFL.isBusy() || robot.motorFR.isBusy() || robot.motorBL.isBusy() || robot.motorBR.isBusy())) {
                telemetry.addData("Path1", "Running to %7d :%7d", robot.newLeftTarget, robot.newRightTarget);
                telemetry.addData("Path2", "Running at %7d :%7d",
                        robot.motorFL.getCurrentPosition(),
                        robot.motorFR.getCurrentPosition());
                telemetry.update();
            }

            state = 22;
        }

        // TURN TOWARD WAREHOUSE
        if (state == 22){
            telemetry.addData("state", "22");
            telemetry.update();
            runtime = robot.encoderDrive(1,6,-6,6,-6,10,runtime);
            while (opModeIsActive() && (robot.motorFL.isBusy() || robot.motorFR.isBusy() || robot.motorBL.isBusy() || robot.motorBR.isBusy())) {
                telemetry.addData("Path1", "Running to %7d :%7d", robot.newLeftTarget, robot.newRightTarget);
                telemetry.addData("Path2", "Running at %7d :%7d",
                        robot.motorFL.getCurrentPosition(),
                        robot.motorFR.getCurrentPosition());
                telemetry.update();
            }

            state = 23;
        }

        // DRIVE INTO WAREHOUSE
        if (state == 23){
            telemetry.addData("state", "23");
            telemetry.update();
            runtime = robot.encoderDrive(1,-15,-15,-15,-15,10,runtime);
            while (opModeIsActive() && (robot.motorFL.isBusy() || robot.motorFR.isBusy() || robot.motorBL.isBusy() || robot.motorBR.isBusy())) {
                telemetry.addData("Path1", "Running to %7d :%7d", robot.newLeftTarget, robot.newRightTarget);
                telemetry.addData("Path2", "Running at %7d :%7d",
                        robot.motorFL.getCurrentPosition(),
                        robot.motorFR.getCurrentPosition());
                telemetry.update();
            }

            state = -1;
        }

        // IF DUCK DETECTED, TURN TOWARD HUB
        if (state == 24) {
            telemetry.addData("State", "24");
            telemetry.update();
            runtime = robot.encoderDrive(1.0, -2, 2, -2, 2, 10, runtime);
            while (opModeIsActive() && (robot.motorFL.isBusy() || robot.motorFR.isBusy() || robot.motorBL.isBusy() || robot.motorBR.isBusy())) {
                telemetry.addData("Path1", "Running to %7d :%7d", robot.newLeftTarget, robot.newRightTarget);
                telemetry.addData("Path2", "Running at %7d :%7d",
                        robot.motorFL.getCurrentPosition(),
                        robot.motorFR.getCurrentPosition());
                telemetry.update();
            }

            state = 25;
        }

        // MOVE TOWARD HUB
        if (state == 25) {
            telemetry.addData("State", "25");
            telemetry.update();
            runtime = robot.encoderDrive(1.0, -5, -5, -5, -5, 10, runtime);
            while (opModeIsActive() && (robot.motorFL.isBusy() || robot.motorFR.isBusy() || robot.motorBL.isBusy() || robot.motorBR.isBusy())) {
                telemetry.addData("Path1", "Running to %7d :%7d", robot.newLeftTarget, robot.newRightTarget);
                telemetry.addData("Path2", "Running at %7d :%7d",
                        robot.motorFL.getCurrentPosition(),
                        robot.motorFR.getCurrentPosition());
                telemetry.update();
            }

            state = 26;
        }

        // LIFT ARM TO LEVEL 3
        if (state == 26) {
            telemetry.addData("State", "26");
            telemetry.update();
            runtime = robot.armLift(0.5, 1100, 10, runtime);
            while (opModeIsActive() &&
                    (robot.arm.isBusy())) {
                telemetry.addData("Arm", robot.arm.getPower());
                telemetry.addData("Arm Position", robot.arm.getCurrentPosition());
                telemetry.update();
            }

            state = 27;
        }

        // MOVE TO LEVEL 3
        if (state == 27) {
            telemetry.addData("state", "27");
            telemetry.update();
            runtime = robot.encoderDrive(1.0, -4, -4, -4, -4, 10, runtime);
            while (opModeIsActive() && (robot.motorFL.isBusy() || robot.motorFR.isBusy() || robot.motorBL.isBusy() || robot.motorBR.isBusy())) {
                telemetry.addData("Path1", "Running to %7d :%7d", robot.newLeftTarget, robot.newRightTarget);
                telemetry.addData("Path2", "Running at %7d :%7d",
                        robot.motorFL.getCurrentPosition(),
                        robot.motorFR.getCurrentPosition());
                telemetry.update();
            }

            state = 28;
        }

        // DROP BLOCK
        if (state == 28) {
            telemetry.addData("state", "28");
            telemetry.update();
            runtime = robot.clawMove(robot.open, 10, runtime);
            while (opModeIsActive() && robot.claw.getPosition() != robot.open) {
                telemetry.addData("Claw", robot.claw.getPosition());
                telemetry.update();
            }

            state = 29;
        }

        // BACK AWAY FROM HUB
        if (state == 29){
            telemetry.addData("state", "29");
            telemetry.update();
            runtime = robot.encoderDrive(1,6,3,6,3,10,runtime);
            while (opModeIsActive() && (robot.motorFL.isBusy() || robot.motorFR.isBusy() || robot.motorBL.isBusy() || robot.motorBR.isBusy())) {
                telemetry.addData("Path1", "Running to %7d :%7d", robot.newLeftTarget, robot.newRightTarget);
                telemetry.addData("Path2", "Running at %7d :%7d",
                        robot.motorFL.getCurrentPosition(),
                        robot.motorFR.getCurrentPosition());
                telemetry.update();
            }

            state = 30;
        }

        // TURN TOWARD WAREHOUSE
        if (state == 30){
            telemetry.addData("state", "30");
            telemetry.update();
            runtime = robot.encoderDrive(1,6,-6,6,-6,10,runtime);
            while (opModeIsActive() && (robot.motorFL.isBusy() || robot.motorFR.isBusy() || robot.motorBL.isBusy() || robot.motorBR.isBusy())) {
                telemetry.addData("Path1", "Running to %7d :%7d", robot.newLeftTarget, robot.newRightTarget);
                telemetry.addData("Path2", "Running at %7d :%7d",
                        robot.motorFL.getCurrentPosition(),
                        robot.motorFR.getCurrentPosition());
                telemetry.update();
            }

            state = 31;
        }

        // DRIVE INTO WAREHOUSE
        if (state == 31){
            telemetry.addData("state", "31");
            telemetry.update();
            runtime = robot.encoderDrive(1,-15,-15,-15,-15,10,runtime);
            while (opModeIsActive() && (robot.motorFL.isBusy() || robot.motorFR.isBusy() || robot.motorBL.isBusy() || robot.motorBR.isBusy())) {
                telemetry.addData("Path1", "Running to %7d :%7d", robot.newLeftTarget, robot.newRightTarget);
                telemetry.addData("Path2", "Running at %7d :%7d",
                        robot.motorFL.getCurrentPosition(),
                        robot.motorFR.getCurrentPosition());
                telemetry.update();
            }

            state = -1;
        }
    }

}
