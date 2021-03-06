package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Tele extends OpMode {

    DcMotor fr;
    DcMotor fl;
    DcMotor br;
    DcMotor bl;

    DcMotor shooter;
    Servo ramp;

    DcMotor intake;
    DcMotor transfer;

    Servo elevator;
    public int level = 0;

    float x1, x2, y1, y2;

    public void drive(float x1, float y1, float x2) {

        if (gamepad1.left_trigger > 0.5) {
            if (Math.abs(x1) < 0.15) {
                x1 = 0;
            } else {
                x1 *= -(float) 1.5;
            }
            y1 *= -1;
        }

        float frontLeft = y1 + x1 + x2;
        float frontRight = y1 - x1 - x2;
        float backLeft = y1 - x1 + x2;
        float backRight = y1 + x1 - x2;

        frontLeft = Range.clip(frontLeft, -1, 1);
        frontRight = Range.clip(frontRight, -1, 1);
        backLeft = Range.clip(backLeft, -1, 1);
        backRight = Range.clip(backRight, -1, 1);

        fl.setPower(frontLeft);
        fr.setPower(frontRight);
        bl.setPower(backLeft);
        br.setPower(backRight);
    }

    @Override
    public void init() {
        fr = hardwareMap.dcMotor.get("frontright");
        fl = hardwareMap.dcMotor.get("frontleft");
        br = hardwareMap.dcMotor.get("backright");
        bl = hardwareMap.dcMotor.get("backleft");

        bl.setDirection(DcMotorSimple.Direction.REVERSE);
        fl.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    @Override
    public void loop() {
        x1 = gamepad1.left_stick_x;
        y1 = gamepad1.left_stick_y;
        x2 = gamepad1.right_stick_x;
        y2 = gamepad1.right_stick_y;

        double joystickLeftDistance = Math.pow(x1, 2) + Math.pow(y1, 2);
        if (joystickLeftDistance < 0.9)
        {
            x1 = x1/2;
            y1 = y1/2;
        }
        double joystickRightDistance = Math.pow(x2, 2) + Math.pow(y2, 2);
        if (joystickRightDistance < 0.9)
        {
            x2 = x2/2;
        }

        if(gamepad2.dpad_left){
            ramp.setPosition(0); //0 placeholder (degrees: 20.97)
        }
        if(gamepad2.dpad_up){
            ramp.setPosition(0); //0 placeholder (degrees: 30.25)
        }
        if(gamepad2.dpad_right){
            ramp.setPosition(0); //0 placeholder (degrees: 24.23)
        }
        if(gamepad2.dpad_down){
            ramp.setPosition(0); //0 placeholder (degrees: 15.82)
        }

        if(gamepad2.right_trigger > 0.2){
            shooter.setPower(1);
        }

        if(gamepad1.right_trigger > 0.2){ //in
            intake.setPower(1);
            transfer.setPower(1);
        }
        if(gamepad1.right_bumper){//out
            intake.setPower(-1);
            transfer.setPower(-1);
        }

        if(gamepad2.left_trigger > 0.8){
            boolean keepGoing = true;
            switch(level){
                case 0:
                    if(elevator.getPosition() != 0)
                        keepGoing = false;
                case 1:
                    if(elevator.getPosition() != 0.5)
                        keepGoing = false;
                case 2:
                    if(elevator.getPosition() != 0.75)
                        keepGoing = false;
                case 3:
                    if(elevator.getPosition() != 1)
                        keepGoing = false;
            }

            if (keepGoing)
                elevate();
        }
    }

    public void elevate(){
        level++;
        if(level > 3)
            level = 0;

        switch(level){
            case 0:
                elevator.setPosition(0);//bottom elevator position
            case 1:
                elevator.setPosition(0.5);//shoot first ring
            case 2:
                elevator.setPosition(0.75);//shoot second ring
            case 3:
                elevator.setPosition(1);//shoot third ring
        }
    }
}
