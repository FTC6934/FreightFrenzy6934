package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;


@Autonomous(name="R2 WAREHOUSE", group="Red")
public class R2BlockWarehouse extends LinearOpMode {

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
            telemetry.addData("State", "2");
            telemetry.update();
            runtime = robot.encoderDrive(1.0, -7, -7, -7, -7, 10, runtime);
            while (opModeIsActive() && (robot.motorFL.isBusy() || robot.motorFR.isBusy() || robot.motorBL.isBusy() || robot.motorBR.isBusy())) {
                telemetry.addData("Path1", "Running to %7d :%7d", robot.newLeftTarget, robot.newRightTarget);
                telemetry.addData("Path2", "Running at %7d :%7d",
                        robot.motorFL.getCurrentPosition(),
                        robot.motorFR.getCurrentPosition());
                telemetry.update();
            }

            state = 3;
        }

        // CHECK DUCK IN SPOT 1
        if (state == 3) {
            telemetry.addData("State", "3");
            telemetry.update();
            runtime = robot.detectDuck(2,runtime);
            if (robot.tier == 1){
                state = 4;
            }
            else{
                state = 5;
            }
        }

        // IF DUCK DETECTED, TURN TOWARD HUB
        if (state == 4) {
            telemetry.addData("State", "4");
            telemetry.update();
            runtime = robot.encoderDrive(1.0, -5, 5, -5, 5, 10, runtime);
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
            runtime = robot.encoderDrive(1, -2, 2, -2, 2, 10, runtime);
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

            state = 9;
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
            runtime = robot.encoderDrive(1,6,6,6,6,10,runtime);
            while (opModeIsActive() && (robot.motorFL.isBusy() || robot.motorFR.isBusy() || robot.motorBL.isBusy() || robot.motorBR.isBusy())) {
                telemetry.addData("Path1", "Running to %7d :%7d", robot.newLeftTarget, robot.newRightTarget);
                telemetry.addData("Path2", "Running at %7d :%7d",
                        robot.motorFL.getCurrentPosition(),
                        robot.motorFR.getCurrentPosition());
                telemetry.update();
            }

            state = 12;
        }

        // TURN TOWARD CAROUSEL
        if (state == 12) {
            telemetry.addData("state", "12");
            telemetry.update();

            runtime = robot.encoderDrive(1, 12,-12,12,-12,10,runtime);
            while (opModeIsActive() && (robot.motorFL.isBusy() || robot.motorFR.isBusy() || robot.motorBL.isBusy() || robot.motorBR.isBusy())) {
                telemetry.addData("Path1", "Running to %7d :%7d", robot.newLeftTarget, robot.newRightTarget);
                telemetry.addData("Path2", "Running at %7d :%7d",
                        robot.motorFL.getCurrentPosition(),
                        robot.motorFR.getCurrentPosition());
                telemetry.update();
            }

            state = 13;
        }

        // MOVE TOWARD CAROUSEL
        if (state == 13) {
            telemetry.addData("state", "13");
            telemetry.update();
            runtime = robot.encoderDrive(1.0, -13, -15, -13, -15, 15, runtime);
            while (opModeIsActive() && (robot.motorFL.isBusy() || robot.motorFR.isBusy() || robot.motorBL.isBusy() || robot.motorBR.isBusy())) {
                telemetry.addData("Path1", "Running to %7d :%7d", robot.newLeftTarget, robot.newRightTarget);
                telemetry.addData("Path2", "Running at %7d :%7d",
                        robot.motorFL.getCurrentPosition(),
                        robot.motorFR.getCurrentPosition());
                telemetry.update();
            }

            state = 14;
        }

        // TURN TOWARD CAROUSEL
        if (state == 14) {
            telemetry.addData("state", "14");
            telemetry.update();
            runtime = robot.encoderDrive(1,-2,2,-2,2,10,runtime);
            while (opModeIsActive() && (robot.motorFL.isBusy() || robot.motorFR.isBusy() || robot.motorBL.isBusy() || robot.motorBR.isBusy())) {
                telemetry.addData("Path1", "Running to %7d :%7d", robot.newLeftTarget, robot.newRightTarget);
                telemetry.addData("Path2", "Running at %7d :%7d",
                        robot.motorFL.getCurrentPosition(),
                        robot.motorFR.getCurrentPosition());
                telemetry.update();
            }

            state = 15;
        }

        // TOUCH WHEEL AGAINST CAROUSEL
        if (state == 15) {
            telemetry.addData("state","15");
            telemetry.update();
            runtime = robot.encoderDrive(0.75, -5,-5,-5,-5,10,runtime);

            while (opModeIsActive() && (robot.motorFL.isBusy() || robot.motorFR.isBusy() || robot.motorBL.isBusy() || robot.motorBR.isBusy())) {
                telemetry.addData("Path1", "Running to %7d :%7d", robot.newLeftTarget, robot.newRightTarget);
                telemetry.addData("Path2", "Running at %7d :%7d",
                        robot.motorFL.getCurrentPosition(),
                        robot.motorFR.getCurrentPosition());
                telemetry.update();
            }

            state = 16;
        }

        // SPIN CAROUSEL
        if (state == 16){
            telemetry.addData("state","16");
            telemetry.update();
            runtime = robot.wheelSpin(-1, 4, runtime);

            while (opModeIsActive() &&
                    (robot.duckWheel.isBusy())) {
                telemetry.addData("Duck Wheel", robot.duckWheel.getPower());
                telemetry.update();
            }

            state = 17;
        }

        // BACK AWAY FROM CAROUSEL
        if (state == 17){
            telemetry.addData("state","17");
            telemetry.update();
            runtime = robot.encoderDrive(1, 6,5,6,5,10,runtime);

            while (opModeIsActive() && (robot.motorFL.isBusy() || robot.motorFR.isBusy() || robot.motorBL.isBusy() || robot.motorBR.isBusy())) {
                telemetry.addData("Path1", "Running to %7d :%7d", robot.newLeftTarget, robot.newRightTarget);
                telemetry.addData("Path2", "Running at %7d :%7d",
                        robot.motorFL.getCurrentPosition(),
                        robot.motorFR.getCurrentPosition());
                telemetry.update();
            }

            state = 18;
        }

        // TURN TOWARD WAREHOUSE
        if (state == 18) {
            telemetry.addData("state", "18");
            telemetry.update();
            runtime = robot.encoderDrive(1,13,-13,13,-13,10,runtime);
            while (opModeIsActive() && (robot.motorFL.isBusy() || robot.motorFR.isBusy() || robot.motorBL.isBusy() || robot.motorBR.isBusy())) {
                telemetry.addData("Path1", "Running to %7d :%7d", robot.newLeftTarget, robot.newRightTarget);
                telemetry.addData("Path2", "Running at %7d :%7d",
                        robot.motorFL.getCurrentPosition(),
                        robot.motorFR.getCurrentPosition());
                telemetry.update();
            }

            state = 19;
        }

        // DRIVE INTO WAREHOUSE
        if (state == 19){
            telemetry.addData("state", "19");
            telemetry.update();
            runtime = robot.encoderDrive(1.0, -28, -30, -28, -30, 10, runtime);
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
        if (state == 20) {
            telemetry.addData("State", "20");
            telemetry.update();
            runtime = robot.encoderDrive(1.0, -3, 3, -3, 3, 10, runtime);
            while (opModeIsActive() && (robot.motorFL.isBusy() || robot.motorFR.isBusy() || robot.motorBL.isBusy() || robot.motorBR.isBusy())) {
                telemetry.addData("Path1", "Running to %7d :%7d", robot.newLeftTarget, robot.newRightTarget);
                telemetry.addData("Path2", "Running at %7d :%7d",
                        robot.motorFL.getCurrentPosition(),
                        robot.motorFR.getCurrentPosition());
                telemetry.update();
            }


            state = 22;
        }

        // IF DUCK NOT DETECTED, TURN TOWARD POSITION 3
        if (state == 21) {
            telemetry.addData("State", "5");
            telemetry.update();
            runtime = robot.encoderDrive(1, -2, 2, -2, 2, 10, runtime);
            while (opModeIsActive() && (robot.motorFL.isBusy() || robot.motorFR.isBusy() || robot.motorBL.isBusy() || robot.motorBR.isBusy())) {
                telemetry.addData("Path1", "Running to %7d :%7d", robot.newLeftTarget, robot.newRightTarget);
                telemetry.addData("Path2", "Running at %7d :%7d",
                        robot.motorFL.getCurrentPosition(),
                        robot.motorFR.getCurrentPosition());
                telemetry.update();
            }

            state = 23;
        }

        // MOVE TOWARD HUB
        if (state == 22) {
            telemetry.addData("State", "22");
            telemetry.update();
            runtime = robot.encoderDrive(1.0, -6, -6, -6, -6, 10, runtime);
            while (opModeIsActive() && (robot.motorFL.isBusy() || robot.motorFR.isBusy() || robot.motorBL.isBusy() || robot.motorBR.isBusy())) {
                telemetry.addData("Path1", "Running to %7d :%7d", robot.newLeftTarget, robot.newRightTarget);
                telemetry.addData("Path2", "Running at %7d :%7d",
                        robot.motorFL.getCurrentPosition(),
                        robot.motorFR.getCurrentPosition());
                telemetry.update();
            }

            state = 24;
        }

        // CHECK DUCK IN POSITION 3
        if (state == 23) {
            telemetry.addData("State", "23");
            telemetry.update();
            runtime = robot.detectDuck(2,runtime);
            state = 36;

        }

        // LIFT ARM TO LEVEL 2
        if (state == 24) {
            telemetry.addData("State", "24");
            telemetry.update();
            runtime = robot.armLift(0.5, 700, 10, runtime);
            while (opModeIsActive() &&
                    (robot.arm.isBusy())) {
                telemetry.addData("Arm", robot.arm.getPower());
                telemetry.addData("Arm Position", robot.arm.getCurrentPosition());
                telemetry.update();
            }

            state = 25;
        }

        // MOVE TO LEVEL 2
        if (state == 25) {
            telemetry.addData("state", "25");
            telemetry.update();
            runtime = robot.encoderDrive(1.0, -4, -4, -4, -4, 10, runtime);
            while (opModeIsActive() && (robot.motorFL.isBusy() || robot.motorFR.isBusy() || robot.motorBL.isBusy() || robot.motorBR.isBusy())) {
                telemetry.addData("Path1", "Running to %7d :%7d", robot.newLeftTarget, robot.newRightTarget);
                telemetry.addData("Path2", "Running at %7d :%7d",
                        robot.motorFL.getCurrentPosition(),
                        robot.motorFR.getCurrentPosition());
                telemetry.update();
            }

            state = 26;
        }

        // DROP BLOCK
        if (state == 26) {
            telemetry.addData("state", "26");
            telemetry.update();
            runtime = robot.clawMove(robot.open, 10, runtime);
            while (opModeIsActive() && robot.claw.getPosition() != robot.open) {
                telemetry.addData("Claw", robot.claw.getPosition());
                telemetry.update();
            }

            state = 27;
        }

        // BACK AWAY FROM HUB
        if (state == 27){
            telemetry.addData("state", "27");
            telemetry.update();
            runtime = robot.encoderDrive(1,6,6,6,6,10,runtime);
            while (opModeIsActive() && (robot.motorFL.isBusy() || robot.motorFR.isBusy() || robot.motorBL.isBusy() || robot.motorBR.isBusy())) {
                telemetry.addData("Path1", "Running to %7d :%7d", robot.newLeftTarget, robot.newRightTarget);
                telemetry.addData("Path2", "Running at %7d :%7d",
                        robot.motorFL.getCurrentPosition(),
                        robot.motorFR.getCurrentPosition());
                telemetry.update();
            }

            state = 28;
        }

        // TURN TOWARD CAROUSEL
        if (state == 28) {
            telemetry.addData("state", "28");
            telemetry.update();

            runtime = robot.encoderDrive(1, 12,-12,12,-12,10,runtime);
            while (opModeIsActive() && (robot.motorFL.isBusy() || robot.motorFR.isBusy() || robot.motorBL.isBusy() || robot.motorBR.isBusy())) {
                telemetry.addData("Path1", "Running to %7d :%7d", robot.newLeftTarget, robot.newRightTarget);
                telemetry.addData("Path2", "Running at %7d :%7d",
                        robot.motorFL.getCurrentPosition(),
                        robot.motorFR.getCurrentPosition());
                telemetry.update();
            }

            state = 29;
        }

        // MOVE TOWARD CAROUSEL
        if (state == 29) {
            telemetry.addData("state", "29");
            telemetry.update();
            runtime = robot.encoderDrive(1.0, -13, -15, -13, -15, 15, runtime);
            while (opModeIsActive() && (robot.motorFL.isBusy() || robot.motorFR.isBusy() || robot.motorBL.isBusy() || robot.motorBR.isBusy())) {
                telemetry.addData("Path1", "Running to %7d :%7d", robot.newLeftTarget, robot.newRightTarget);
                telemetry.addData("Path2", "Running at %7d :%7d",
                        robot.motorFL.getCurrentPosition(),
                        robot.motorFR.getCurrentPosition());
                telemetry.update();
            }

            state = 30;
        }

        // TURN TOWARD CAROUSEL
        if (state == 30) {
            telemetry.addData("state", "30");
            telemetry.update();
            runtime = robot.encoderDrive(1,-2,2,-2,2,10,runtime);
            while (opModeIsActive() && (robot.motorFL.isBusy() || robot.motorFR.isBusy() || robot.motorBL.isBusy() || robot.motorBR.isBusy())) {
                telemetry.addData("Path1", "Running to %7d :%7d", robot.newLeftTarget, robot.newRightTarget);
                telemetry.addData("Path2", "Running at %7d :%7d",
                        robot.motorFL.getCurrentPosition(),
                        robot.motorFR.getCurrentPosition());
                telemetry.update();
            }

            state = 31;
        }

        //TOUCH WHEEL AGAINST CAROUSEL
        if (state == 31) {
            telemetry.addData("state","31");
            telemetry.update();
            runtime = robot.encoderDrive(1, -5,-5,-5,-5,10,runtime);

            while (opModeIsActive() && (robot.motorFL.isBusy() || robot.motorFR.isBusy() || robot.motorBL.isBusy() || robot.motorBR.isBusy())) {
                telemetry.addData("Path1", "Running to %7d :%7d", robot.newLeftTarget, robot.newRightTarget);
                telemetry.addData("Path2", "Running at %7d :%7d",
                        robot.motorFL.getCurrentPosition(),
                        robot.motorFR.getCurrentPosition());
                telemetry.update();
            }

            state = 32;
        }

        // SPIN CAROUSEL
        if (state == 32){
            telemetry.addData("state","32");
            telemetry.update();
            runtime = robot.wheelSpin(-1, 4, runtime);

            while (opModeIsActive() &&
                    (robot.duckWheel.isBusy())) {
                telemetry.addData("Duck Wheel", robot.duckWheel.getPower());
                telemetry.update();
            }

            state = 32;
        }

        // BACK AWAY FROM CAROUSEL
        if (state == 33){
            telemetry.addData("state","33");
            telemetry.update();
            runtime = robot.encoderDrive(1, 6,4,6,4,10,runtime);

            while (opModeIsActive() && (robot.motorFL.isBusy() || robot.motorFR.isBusy() || robot.motorBL.isBusy() || robot.motorBR.isBusy())) {
                telemetry.addData("Path1", "Running to %7d :%7d", robot.newLeftTarget, robot.newRightTarget);
                telemetry.addData("Path2", "Running at %7d :%7d",
                        robot.motorFL.getCurrentPosition(),
                        robot.motorFR.getCurrentPosition());
                telemetry.update();
            }

            state = 34;
        }

        // TURN TOWARD BARCODE
        // TURN TOWARD WAREHOUSE
        if (state == 34) {
            telemetry.addData("state", "34");
            telemetry.update();
            runtime = robot.encoderDrive(1,13,-13,13,-13,10,runtime);
            while (opModeIsActive() && (robot.motorFL.isBusy() || robot.motorFR.isBusy() || robot.motorBL.isBusy() || robot.motorBR.isBusy())) {
                telemetry.addData("Path1", "Running to %7d :%7d", robot.newLeftTarget, robot.newRightTarget);
                telemetry.addData("Path2", "Running at %7d :%7d",
                        robot.motorFL.getCurrentPosition(),
                        robot.motorFR.getCurrentPosition());
                telemetry.update();
            }

            state = 35;
        }

        // DRIVE INTO WAREHOUSE
        if (state == 35){
            telemetry.addData("state", "35");
            telemetry.update();
            runtime = robot.encoderDrive(1.0, -28, -30, -28, -30, 10, runtime);
            while (opModeIsActive() && (robot.motorFL.isBusy() || robot.motorFR.isBusy() || robot.motorBL.isBusy() || robot.motorBR.isBusy())) {
                telemetry.addData("Path1", "Running to %7d :%7d", robot.newLeftTarget, robot.newRightTarget);
                telemetry.addData("Path2", "Running at %7d :%7d",
                        robot.motorFL.getCurrentPosition(),
                        robot.motorFR.getCurrentPosition());
                telemetry.update();
            }

            state = -1;
        }

        // IF DUCK DETECTED OR NOT, TURN TOWARD HUB
        if (state == 36) {
            telemetry.addData("State", "36");
            telemetry.update();
            runtime = robot.encoderDrive(1.0, -1, 1, -1, 1, 10, runtime);
            while (opModeIsActive() && (robot.motorFL.isBusy() || robot.motorFR.isBusy() || robot.motorBL.isBusy() || robot.motorBR.isBusy())) {
                telemetry.addData("Path1", "Running to %7d :%7d", robot.newLeftTarget, robot.newRightTarget);
                telemetry.addData("Path2", "Running at %7d :%7d",
                        robot.motorFL.getCurrentPosition(),
                        robot.motorFR.getCurrentPosition());
                telemetry.update();
            }


            state = 37;
        }

        // MOVE TOWARD HUB
        if (state == 37) {
            telemetry.addData("State", "37");
            telemetry.update();
            runtime = robot.encoderDrive(1.0, -6, -6, -6, -6, 10, runtime);
            while (opModeIsActive() && (robot.motorFL.isBusy() || robot.motorFR.isBusy() || robot.motorBL.isBusy() || robot.motorBR.isBusy())) {
                telemetry.addData("Path1", "Running to %7d :%7d", robot.newLeftTarget, robot.newRightTarget);
                telemetry.addData("Path2", "Running at %7d :%7d",
                        robot.motorFL.getCurrentPosition(),
                        robot.motorFR.getCurrentPosition());
                telemetry.update();
            }

            state = 38;
        }

        // LIFT ARM TO LEVEL 3
        if (state == 38) {
            telemetry.addData("State", "38");
            telemetry.update();
            runtime = robot.armLift(0.5, 1100, 10, runtime);
            while (opModeIsActive() &&
                    (robot.arm.isBusy())) {
                telemetry.addData("Arm", robot.arm.getPower());
                telemetry.addData("Arm Position", robot.arm.getCurrentPosition());
                telemetry.update();
            }

            state = 39;
        }

        // MOVE TO LEVEL 3
        if (state == 39) {
            telemetry.addData("state", "39");
            telemetry.update();
            runtime = robot.encoderDrive(1.0, -4, -4, -4, -4, 10, runtime);
            while (opModeIsActive() && (robot.motorFL.isBusy() || robot.motorFR.isBusy() || robot.motorBL.isBusy() || robot.motorBR.isBusy())) {
                telemetry.addData("Path1", "Running to %7d :%7d", robot.newLeftTarget, robot.newRightTarget);
                telemetry.addData("Path2", "Running at %7d :%7d",
                        robot.motorFL.getCurrentPosition(),
                        robot.motorFR.getCurrentPosition());
                telemetry.update();
            }

            state = 40;
        }

        // DROP BLOCK
        if (state == 40) {
            telemetry.addData("state", "40");
            telemetry.update();
            runtime = robot.clawMove(robot.open, 10, runtime);
            while (opModeIsActive() && robot.claw.getPosition() != robot.open) {
                telemetry.addData("Claw", robot.claw.getPosition());
                telemetry.update();
            }

            state = 41;
        }

        // BACK AWAY FROM HUB
        if (state == 41){
            telemetry.addData("state", "41");
            telemetry.update();
            runtime = robot.encoderDrive(1,6,6,6,6,10,runtime);
            while (opModeIsActive() && (robot.motorFL.isBusy() || robot.motorFR.isBusy() || robot.motorBL.isBusy() || robot.motorBR.isBusy())) {
                telemetry.addData("Path1", "Running to %7d :%7d", robot.newLeftTarget, robot.newRightTarget);
                telemetry.addData("Path2", "Running at %7d :%7d",
                        robot.motorFL.getCurrentPosition(),
                        robot.motorFR.getCurrentPosition());
                telemetry.update();
            }

            state = 42;
        }

        // TURN TOWARD CAROUSEL
        if (state == 42) {
            telemetry.addData("state", "42");
            telemetry.update();

            runtime = robot.encoderDrive(1, 12,-12,12,-12,10,runtime);
            while (opModeIsActive() && (robot.motorFL.isBusy() || robot.motorFR.isBusy() || robot.motorBL.isBusy() || robot.motorBR.isBusy())) {
                telemetry.addData("Path1", "Running to %7d :%7d", robot.newLeftTarget, robot.newRightTarget);
                telemetry.addData("Path2", "Running at %7d :%7d",
                        robot.motorFL.getCurrentPosition(),
                        robot.motorFR.getCurrentPosition());
                telemetry.update();
            }

            state = 43;
        }

        // MOVE TOWARD CAROUSEL
        if (state == 43) {
            telemetry.addData("state", "43");
            telemetry.update();
            runtime = robot.encoderDrive(1.0, -13, -15, -13, -15, 15, runtime);
            while (opModeIsActive() && (robot.motorFL.isBusy() || robot.motorFR.isBusy() || robot.motorBL.isBusy() || robot.motorBR.isBusy())) {
                telemetry.addData("Path1", "Running to %7d :%7d", robot.newLeftTarget, robot.newRightTarget);
                telemetry.addData("Path2", "Running at %7d :%7d",
                        robot.motorFL.getCurrentPosition(),
                        robot.motorFR.getCurrentPosition());
                telemetry.update();
            }

            state = 44;
        }

        // TURN TOWARD CAROUSEL
        if (state == 44) {
            telemetry.addData("state", "44");
            telemetry.update();
            runtime = robot.encoderDrive(1,-2,2,-2,2,10,runtime);
            while (opModeIsActive() && (robot.motorFL.isBusy() || robot.motorFR.isBusy() || robot.motorBL.isBusy() || robot.motorBR.isBusy())) {
                telemetry.addData("Path1", "Running to %7d :%7d", robot.newLeftTarget, robot.newRightTarget);
                telemetry.addData("Path2", "Running at %7d :%7d",
                        robot.motorFL.getCurrentPosition(),
                        robot.motorFR.getCurrentPosition());
                telemetry.update();
            }

            state = 45;
        }

        //TOUCH WHEEL AGAINST CAROUSEL
        if (state == 45) {
            telemetry.addData("state","45");
            telemetry.update();
            runtime = robot.encoderDrive(1, -5,-5,-5,-5,10,runtime);

            while (opModeIsActive() && (robot.motorFL.isBusy() || robot.motorFR.isBusy() || robot.motorBL.isBusy() || robot.motorBR.isBusy())) {
                telemetry.addData("Path1", "Running to %7d :%7d", robot.newLeftTarget, robot.newRightTarget);
                telemetry.addData("Path2", "Running at %7d :%7d",
                        robot.motorFL.getCurrentPosition(),
                        robot.motorFR.getCurrentPosition());
                telemetry.update();
            }

            state = 46;
        }

        // SPIN CAROUSEL
        if (state == 46){
            telemetry.addData("state","46");
            telemetry.update();
            runtime = robot.wheelSpin(-1, 4, runtime);

            while (opModeIsActive() &&
                    (robot.duckWheel.isBusy())) {
                telemetry.addData("Duck Wheel", robot.duckWheel.getPower());
                telemetry.update();
            }

            state = 47;
        }

        // BACK AWAY FROM CAROUSEL
        if (state == 47){
            telemetry.addData("state","47");
            telemetry.update();
            runtime = robot.encoderDrive(1, 5,5,5,5,10,runtime);

            while (opModeIsActive() && (robot.motorFL.isBusy() || robot.motorFR.isBusy() || robot.motorBL.isBusy() || robot.motorBR.isBusy())) {
                telemetry.addData("Path1", "Running to %7d :%7d", robot.newLeftTarget, robot.newRightTarget);
                telemetry.addData("Path2", "Running at %7d :%7d",
                        robot.motorFL.getCurrentPosition(),
                        robot.motorFR.getCurrentPosition());
                telemetry.update();
            }

            state = 48;
        }

        // TURN TOWARD BARCODE
        // TURN TOWARD WAREHOUSE
        if (state == 48) {
            telemetry.addData("state", "48");
            telemetry.update();
            runtime = robot.encoderDrive(1,13,-13,13,-13,10,runtime);
            while (opModeIsActive() && (robot.motorFL.isBusy() || robot.motorFR.isBusy() || robot.motorBL.isBusy() || robot.motorBR.isBusy())) {
                telemetry.addData("Path1", "Running to %7d :%7d", robot.newLeftTarget, robot.newRightTarget);
                telemetry.addData("Path2", "Running at %7d :%7d",
                        robot.motorFL.getCurrentPosition(),
                        robot.motorFR.getCurrentPosition());
                telemetry.update();
            }

            state = 49;
        }

        // DRIVE INTO WAREHOUSE
        if (state == 49){
            telemetry.addData("state", "49");
            telemetry.update();
            runtime = robot.encoderDrive(1.0, -28, -30, -28, -30, 10, runtime);
            while (opModeIsActive() && (robot.motorFL.isBusy() || robot.motorFR.isBusy() || robot.motorBL.isBusy() || robot.motorBR.isBusy())) {
                telemetry.addData("Path1", "Running to %7d :%7d", robot.newLeftTarget, robot.newRightTarget);
                telemetry.addData("Path2", "Running at %7d :%7d",
                        robot.motorFL.getCurrentPosition(),
                        robot.motorFR.getCurrentPosition());
                telemetry.update();
            }

            state = -1;
        }

        // END OF CODE
        if (state == -1){
            telemetry.addData("Path", "Complete");
            telemetry.update();
            sleep(1000);
        }

    }
}
