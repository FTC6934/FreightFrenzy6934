package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="TeleOp Freight Frenzy")
public class TeleOp2021 extends OpMode{

    public DcMotor motorBL = null;
    public DcMotor motorBR = null;
    public DcMotor motorFL = null;
    public DcMotor motorFR = null;
    public DcMotor duckWheel = null;
    public DcMotorEx arm = null;
    public Servo claw = null;
    //public Servo claw2 = null;

    private double open = 0.05;
    private double closed = 0.6;
    private double position;

    private int armPosition;

    public void init() {
        motorBL = hardwareMap.get(DcMotor.class, "Back Left");
        motorBL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorBL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorBR = hardwareMap.get(DcMotor.class, "Back Right");
        motorFL = hardwareMap.get(DcMotor.class, "Front Left");
        motorFR = hardwareMap.get(DcMotor.class, "Front Right");
        motorBL.setDirection(DcMotorSimple.Direction.REVERSE);
        motorFL.setDirection(DcMotorSimple.Direction.REVERSE);
        duckWheel = hardwareMap.get(DcMotor.class, "Duck Wheel");

        // this code ......sets arm encoders
        arm = hardwareMap.get(DcMotorEx.class, "Arm");
        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        arm.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

        claw = hardwareMap.get(Servo.class, "Claw");
        claw.setPosition(open);
        //claw2 = hardwareMap.get(Servo.class, "Claw2");
    }

    public void start()
    {

    }

    public void loop()
    {
        //double leftStickX = gamepad1.left_stick_x;
        double leftY = -gamepad1.left_stick_y;
        double rightX = gamepad1.right_stick_x;
        //double r = Math.hypot(leftStickX, leftStickY);
        //double robotAngle = Math.atan2(-leftStickY,leftStickX) - Math.PI / 4;
        motorFL.setPower(leftY + rightX);
        motorFR.setPower(leftY - rightX);
        motorBL.setPower(leftY + rightX);
        motorBR.setPower(leftY - rightX);

        //duck wheel
        if (gamepad1.b)
        {
            duckWheel.setPower(1);
        }
        else if (gamepad1.a)
        {
            duckWheel.setPower(-1);
        }
        else
        {
            duckWheel.setPower(0);
        }

        // arm
        // UP
        if (gamepad2.right_stick_y < 0 && arm.getCurrentPosition()<2000)
        {
            arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            //arm.setVelocity(-gamepad2.right_stick_y*400);
            arm.setPower(0.5*-gamepad2.right_stick_y);
            armPosition = arm.getCurrentPosition();
        }
        // DOWN
        else if (gamepad2.right_stick_y > 0 && arm.getCurrentPosition()>0)
        {
            arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            //arm.setVelocity(-gamepad2.right_stick_y*400);
            arm.setPower(0.5*-gamepad2.right_stick_y);
            armPosition = arm.getCurrentPosition();
        }
        else
        {

            arm.setTargetPosition(armPosition);
            arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }

        // claw`
        if (gamepad2.b)
        {
            claw.setPosition(closed);
        }
        else if (gamepad2.a)
        {
            claw.setPosition(open);
        }


        if(gamepad2.x)
        {
            claw.setPosition(0.4);
        }
        else if (gamepad2.y)
        {
            claw.setPosition(0.2);
        }


        telemetry.addData("Motor Speed BL", motorBL.getPower());
        telemetry.addData("PositionBL: ", motorBL.getCurrentPosition());
        telemetry.addData("Motor Speed BR", motorBR.getPower());
        telemetry.addData("Motor Speed FL", motorFL.getPower());
        telemetry.addData("Motor Speed FR", motorFR.getPower());
        telemetry.addData("Motor Speed Duck Wheel", duckWheel.getPower());
        telemetry.addData("power", arm.getPower());
        telemetry.addData("position", arm.getCurrentPosition());
        telemetry.addData("Claw Position", claw.getPosition());
        //telemetry.addData("Claw2 Position", claw2.getPosition());
    }

}