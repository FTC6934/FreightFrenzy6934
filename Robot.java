package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

public class Robot
{

    // MOTOR VARIABLE
    public DcMotor motorBL = null;
    public DcMotor motorBR = null;
    public DcMotor motorFL = null;
    public DcMotor motorFR = null;
    public DcMotor duckWheel=null;
    public DcMotor arm = null;
    public Servo claw = null;

    //HARDWARE MAP
    HardwareMap hwMap           =  null;

    //SERVO VARIABLES
    public double open = 0.05;
    public double closed = 0.6;
    private double position;

    //ENCODER VARIABLES
    static final double     COUNTS_PER_MOTOR_REV    = 1440 ;    // eg: TETRIX Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = 2.0 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double     DRIVE_SPEED             = 0.6;
    static final double     TURN_SPEED              = 0.5;

    public int newLeftTarget;
    public int newRightTarget;
    public int newLeftBackTarget;
    public int newRightBackTarget;

    // VUFORIA VARIABLES
    final double DESIRED_DISTANCE = 2; //  this is how close the camera should get to the target (inches)
    //  The GAIN constants set the relationship between the measured position error,
    //  and how much power is applied to the drive motors.  Drive = Error * Gain
    //  Make these values smaller for smoother control.
    final double SPEED_GAIN =   0.05 ;   //  Speed Control "Gain". eg: Ramp up to 50% power at a 25 inch error.   (0.50 / 25.0)
    final double TURN_GAIN  =   0.01 ;   //  Turn Control "Gain".  eg: Ramp up to 25% power at a 25 degree error. (0.25 / 25.0)

    final double MM_PER_INCH = 25.40 ;   //  Metric conversion

    private static final String VUFORIA_KEY =
            "AXaixb7/////AAABmerN5p/9iEggoRwnZvCC9QSIKnn7TA3GqAE6H2gihMwnCS4JKHKZUxdqn9dBt5Jg29PHVYcOWrCawh6okXVCLrXEwe2O8JppFCPFxDWfLooTUV0wEUy9a6ZT0LsAg0z9AZYiZSZb3ZNhrGZ6Bu1EZbjJBCEEqWIBbBWTB9U1xF/1SGrAkL735dMCdnHREkDkAHIiAePngb4f0E3Y98BzeOILJTwiYX6oFICFFX7ubHZMgIteHwyXVwBX/lyheirHuGUl0Ffk4ts4XzbN+gHQTBnCI1HxuAi7X2llvPDu71zRRJfTgQB16R+xT27a2xDrNlVwKt+oiOsj0IdmkKwP2vudgVIz3kEE/wydVUQDNjHu";

    VuforiaLocalizer vuforia    = null;
    OpenGLMatrix targetPose     = null;
    String targetName           = "";

    // CAMERA OFFSET
    public double xOffset = 5;
    public double yOffset = 3;

    public VuforiaTrackables targetsFreightFrenzy = null;

    public boolean targetFound     = false;    // Set to true when a target is detected by Vuforia
    public double  targetRange     = 0;        // Distance from camera to target in Inches
    public double  targetBearing   = 0;        // Robot Heading, relative to target.  Positive degrees means target is to the right.
    public double  drive           = 0;        // Desired forward power (-1 to +1)
    public double  turn            = 0;        // Desired turning power (-1 to +1)

    // TENSOR FLOW VARIABLES
    private TFObjectDetector tfod;
    private static final String TFOD_MODEL_ASSET = "FreightFrenzy_BCDM.tflite";
    private static final String[] LABELS = {
            "Ball",
            "Cube",
            "Duck",
            "Marker"
    };

    public int tier;

    public Robot(){

    }

    // WHEN INIT IS PRESSED
    public void init(HardwareMap ahwMap) {


        hwMap = ahwMap;

        // INITIALIZE MOTORS
        motorBL = hwMap.get(DcMotor.class, "Back Left");
        motorBR = hwMap.get(DcMotor.class, "Back Right");
        motorFL = hwMap.get(DcMotor.class, "Front Left");
        motorFR = hwMap.get(DcMotor.class, "Front Right");
        // REVERSE DIRECTION SO CCW IS POSITIVE
        motorBR.setDirection(DcMotorSimple.Direction.REVERSE);
        motorFR.setDirection(DcMotorSimple.Direction.REVERSE);
        duckWheel = hwMap.get(DcMotor.class, "Duck Wheel");
        arm = hwMap.get(DcMotor.class, "Arm");
        claw = hwMap.get(Servo.class, "Claw");

        motorFL.setPower(0);
        motorFR.setPower(0);
        motorBL.setPower(0);
        motorBR.setPower(0);
        duckWheel.setPower(0);
        arm.setPower(0);

        //INITIALIZE ENCODERS
        motorBL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorBR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorBL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorBR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorBL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorBR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorFL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorFR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorFL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorFR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorFL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorFR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        duckWheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        // PREVENT THE MOTORS FROM SPINNING WHEN STOPPING
        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorBR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorFR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorFL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorBL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // SET CLAW INTO OPEN POSITION
        claw.setPosition(open);

        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         * To get an on-phone camera preview, use the code below.
         * If no camera preview is desired, use the parameter-less constructor instead (commented out below).
         */
        int cameraMonitorViewId = hwMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hwMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        // VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;

        // Turn off Extended tracking.  Set this true if you want Vuforia to track beyond the target.
        parameters.useExtendedTracking = false;

        // Connect to the camera we are to use.  This name must match what is set up in Robot Configuration
        parameters.cameraName = hwMap.get(WebcamName.class, "Webcam 1");
        this.vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Load the trackable objects from the Assets file, and give them meaningful names
        targetsFreightFrenzy = this.vuforia.loadTrackablesFromAsset("FreightFrenzy");
        targetsFreightFrenzy.get(0).setName("Blue Storage");
        targetsFreightFrenzy.get(1).setName("Blue Alliance Wall");
        targetsFreightFrenzy.get(2).setName("Red Storage");
        targetsFreightFrenzy.get(3).setName("Red Alliance Wall");

        // Start tracking targets in the background
        targetsFreightFrenzy.activate();

    }

    public ElapsedTime encoderDrive(double speed,
                             double leftInches, double rightInches, double leftBackInches, double rightBackInches,
                             double timeoutS, ElapsedTime rt) {
        int newLeftTarget;
        int newRightTarget;
        int newLeftBackTarget;
        int newRightBackTarget;

            // Determine new target position, and pass to motor controller
            newLeftTarget = motorFL.getCurrentPosition() + (int)(leftInches * COUNTS_PER_INCH);
            newRightTarget = motorFR.getCurrentPosition() + (int)(rightInches * COUNTS_PER_INCH);
            newLeftBackTarget = motorBL.getCurrentPosition() + (int)(leftBackInches * COUNTS_PER_INCH);
            newRightBackTarget = motorBR.getCurrentPosition() + (int)(rightBackInches * COUNTS_PER_INCH);


            motorFL.setTargetPosition(newLeftTarget);
            motorFR.setTargetPosition(newRightTarget);
            motorBL.setTargetPosition(newLeftBackTarget);
            motorBR.setTargetPosition(newRightBackTarget);

            // Turn On RUN_TO_POSITION
            motorFL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motorFR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motorBL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motorBR.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            rt.reset();
            motorFL.setPower(Math.abs(speed));
            motorFR.setPower(Math.abs(speed));
            motorBL.setPower(Math.abs(speed));
            motorBR.setPower(Math.abs(speed));


            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while ((rt.seconds() < timeoutS) &&
                    (motorFL.isBusy() && motorFR.isBusy())) {
                /*
                // Display it for the driver.
                telemetry.addData("Path1",  "Running to %7d :%7d", newLeftTarget,  newRightTarget);
                telemetry.addData("Path2",  "Running at %7d :%7d",
                        motorFL.getCurrentPosition(),
                        motorFR.getCurrentPosition());
                telemetry.update();
                 */
            }

            // Stop all motion;
            motorFL.setPower(0);
            motorFR.setPower(0);
            motorBL.setPower(0);
            motorBR.setPower(0);
            duckWheel.setPower(0);

            // Turn off RUN_TO_POSITION
            // this resets run to position settings
            motorFL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            motorFR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            motorBL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            motorBR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            duckWheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            //  sleep(250);   // optional pause after each move

            return rt;
        }

    public ElapsedTime wheelSpin(double speed, double timeOut, ElapsedTime rt) {


            rt.reset();
            while ((rt.seconds() < timeOut)) {

                duckWheel.setPower(speed);
            }

            duckWheel.setPower(0);

        return rt;
    }

    public ElapsedTime armLift(double power, int position, double timeOut, ElapsedTime rt){
        rt.reset();

        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        arm.setTargetPosition(position);
        arm.setPower(power);
        arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        while ((rt.seconds() < timeOut) && arm.isBusy()) {
        }

        arm.setPower(0);

        return rt;
    }

    public ElapsedTime clawMove(double position, double timeOut, ElapsedTime rt){
        rt.reset();
        claw.setPosition(position);

        return rt;
    }

    public ElapsedTime goToImage(double speed, double distance, double timeOut, ElapsedTime rt){

        rt.reset();
        // Look for first visible target, and save its pose.
        targetFound = false;


        for (VuforiaTrackable trackable : targetsFreightFrenzy)
        {
            if (((VuforiaTrackableDefaultListener) trackable.getListener()).isVisible())
            {
                targetPose = ((VuforiaTrackableDefaultListener)trackable.getListener()).getVuforiaCameraFromTarget();

                // if we have a target, process the "pose" to determine the position of the target relative to the robot.
                if (targetPose != null)
                {
                    targetFound = true;
                    targetName  = trackable.getName();
                    VectorF trans = targetPose.getTranslation();

                    // Extract the X & Y components of the offset of the target relative to the robot
                    double targetX = (trans.get(0) / MM_PER_INCH) - xOffset; // Image X axis
                    double targetY = (trans.get(2) / MM_PER_INCH) - yOffset; // Image Z axis

                    // target range is based on distance from robot position to origin (right triangle).
                    targetRange = Math.hypot(targetX, targetY);

                    // target bearing is based on angle formed between the X axis and the target range line
                    targetBearing = Math.toDegrees(Math.asin(targetX / targetRange));

                    break;  // jump out of target tracking loop if we find a target.
                }
            }
        }

        // Tell the driver what we see, and what to do.
        if (targetFound) {
            //telemetry.addData(">","HOLD Left-Bumper to Drive to Target\n");
            //telemetry.addData("Target", " %s", targetName);
            //telemetry.addData("Range",  "%5.1f inches", targetRange);
            //telemetry.addData("Bearing","%3.0f degrees", targetBearing);

            double  rangeError   = (targetRange - DESIRED_DISTANCE);
            double  headingError = targetBearing;

            // Use the speed and turn "gains" to calculate how we want the robot to move.
            drive = rangeError * SPEED_GAIN;
            turn  = headingError * TURN_GAIN ;

            double leftPower    = Range.clip(drive + turn, -1.0, 1.0) ;
            double rightPower   = Range.clip(drive - turn, -1.0, 1.0) ;
            motorFL.setPower(-leftPower);
            motorFR.setPower(-rightPower);
            motorBL.setPower(-leftPower);
            motorBR.setPower(-rightPower);

        }

        else {
            encoderDrive(speed, distance, distance, distance, distance, timeOut, rt);
        }

        return rt;
    }

    public ElapsedTime detectDuck(double time, ElapsedTime rt){
        /* Note: This sample uses the all-objects Tensor Flow model (FreightFrenzy_BCDM.tflite), which contains
         * the following 4 detectable objects
         *  0: Ball,
         *  1: Cube,
         *  2: Duck,
         *  3: Marker (duck location tape marker)
         *
         *  Two additional model assets are available which only contain a subset of the objects:
         *  FreightFrenzy_BC.tflite  0: Ball,  1: Cube
         *  FreightFrenzy_DM.tflite  0: Duck,  1: Marker
         */


        // The TFObjectDetector uses the camera frames from the VuforiaLocalizer, so we create that
        // first.
        //initVuforia();
        rt.reset();
        initTfod();

        /**
         * Activate TensorFlow Object Detection before we wait for the start command.
         * Do it here so that the Camera Stream window will have the TensorFlow annotations visible.
         **/
        if (tfod != null) {
            tfod.activate();

            // The TensorFlow software will scale the input images from the camera to a lower resolution.
            // This can result in lower detection accuracy at longer distances (> 55cm or 22").
            // If your target is at distance greater than 50 cm (20") you can adjust the magnification value
            // to artificially zoom in to the center of image.  For best results, the "aspectRatio" argument
            // should be set to the value of the images used to create the TensorFlow Object Detection model
            // (typically 16/9).
            tfod.setZoom(2.5, 16.0/9.0);
        }

        /** Wait for the game to begin */
        //telemetry.addData(">", "Press Play to start op mode");
        //telemetry.update();

        //sleep(1000);


            while (rt.seconds() < time) {
                if (tfod != null) {
                    // getUpdatedRecognitions() will return null if no new information is available since
                    // the last time that call was made.
                    List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                    if (updatedRecognitions != null) {
                        //telemetry.addData("# Object Detected", updatedRecognitions.size());
                        // step through the list of recognitions and d
                        // display boundary info.

                        int i = 0;
                        for (Recognition recognition : updatedRecognitions) {
                            //telemetry.addData(String.format("label (%d)", i), recognition.getLabel());
                            //telemetry.addData(String.format("  left,top (%d)", i), "%.03f , %.03f",
                                    //recognition.getLeft(), recognition.getTop());
                            //telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
                                    //recognition.getRight(), recognition.getBottom());
                            i++;

                            if(recognition.getLabel().equals("Duck")){
                                tier = 1;
                            }
                        }

                    }
                }
            }
        return rt;
    }

    private void initTfod() {
        int tfodMonitorViewId = hwMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hwMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.8f;
        tfodParameters.isModelTensorFlow2 = true;
        tfodParameters.inputSize = 320;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABELS);
    }
}