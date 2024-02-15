package frc.robot;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import java.lang.Math;
import java.lang.reflect.Array;

import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;

public class limeslight extends SubsystemBase {


    @Override
    public void periodic() {
       
    }
    NetworkTable limelightBack; // table for the limelight

    NetworkTableEntry tx; // Table for the horixontal rotation in degrees (-27 to 27)
    NetworkTableEntry ty; // Table for the verticle rotation in degrees (?? - ??)
    NetworkTableEntry ta; // Table for the area the target takes up
    NetworkTableEntry ts; // Table for the skew/rotation of target NOTE: define better
    NetworkTableEntry tv; // Table to see if there are valid targets
    NetworkTableEntry tl; /// Table for latency/delay before data transfer
    NetworkTableEntry tid; // Table for the ID of target

    NetworkTableEntry tshort; // Table for short side length
    NetworkTableEntry tlong; // Table for long side length
    NetworkTableEntry thoriz; // Table for width
    NetworkTableEntry tvert; // Table for height
    NetworkTableEntry ledMode; // Table to set blinking LEDS
    NetworkTableEntry camMode; // Table to set camera mode
    NetworkTableEntry pipeline; // Table to switch pipelines
    NetworkTableEntry solvePNP; // Table to give position in 3D space based on camera
    NetworkTableEntry botpose; // I'll be honest, I have no clue what this does. 

    public void DebugMethodSingle() {
        var tab = Shuffleboard.getTab("Driver Diagnostics");
        tab.addBoolean("Shooter Running", () -> true);
    }

    public limeslight() {
        // instantiate the tables (basically make the tables real)
        limelightBack = NetworkTableInstance.getDefault().getTable("limelight");
        // creates each table
        tx = limelightBack.getEntry("tx");
        ty = limelightBack.getEntry("ty");
        ta = limelightBack.getEntry("ta");
        ts = limelightBack.getEntry("ts");
        tv = limelightBack.getEntry("tv");
        tl = limelightBack.getEntry("tl");
        tid = limelightBack.getEntry("tid");

        tshort = limelightBack.getEntry("tshort");
        tlong = limelightBack.getEntry("tlong");
        thoriz = limelightBack.getEntry("thoriz");
        tvert = limelightBack.getEntry("tvert");
        ledMode = limelightBack.getEntry("ledMode");
        camMode = limelightBack.getEntry("camMode");
        pipeline = limelightBack.getEntry("pipeline");
        solvePNP = limelightBack.getEntry("solvePNP");
        botpose = limelightBack.getEntry("botpose");

    }

    // vison constants
    double CAMERA_HEIGHT = 48.006; //taz 1 on green cart, centimeters

    public double getArea() {

        return ta.getDouble(0.0);

    }

    public double getSkew() {
        return ts.getDouble(0.0);
    }

    public double getID(){
        return tid.getDouble(-1.0);
    }

    // trinary system 
    // -1 = degree to negative NOTE: change to left/right when is figured out
    // 0 = head on
    // 1 = degree to positive 
    public double getAngleFacingTag(){ //get better name
        return tx.getDouble(0.0);
    }
    
    /*
     * notes for creating function for degrees
     * -5 to 5 good, anything outside is bad
     * turn negative of degree input
     */

    public boolean checkTarget() {
        var t = tv.getDouble(0.0);
        if (t == 1.0) {
            return true;
        } else {
            return false;
        }
    }

    
    // checks how "off" the robot is to the target in degrees
    // basically how many degrees it needs to turn to be facing the target
    public int checkSkew() { // change to range instead of one int once able to test + determine range
        if (checkTarget()) {
            if ((getSkew() < 1 && getSkew() > -1) || getSkew() < 88) {
                return 0; // skew is on target
            } else if (getSkew() > 45) {
                return 1; // turn right skew amount of angles (?)
            } else {
                return -1; // turn left skew-45 angles
            }
        } else {
            return 2;
        }
    }

    //returns the height of the target april tag in centimeters
    public int APRIL_TAG_HEIGHT(){
        double tagID = getID();
        if((tagID == 1.0) || (tagID == 2.0) || (tagID == 5.0) || (tagID == 6.0) || (tagID == 9.0) || (tagID == 10.0)){
            return 122; //tags 1,2,5,6,9 and 10 have a height of 122
        } else if
        ((tagID == 3.0)||(tagID == 4.0)||(tagID == 7.0)||(tagID == 8.0)){
            return 132;
        } else if
        ((tagID == 11.0)||(tagID == 12.0)||(tagID == 13.0)||(tagID == 14.0)||(tagID == 15.0)||(tagID == 16.0)){
            return 121;
        } else {
            return 1;
        }
    }

    public double DISTANCE_CALCULATIONS(){

        double angleToGoalRadians = (ty.getDouble(0.0)) * (3.14159/180.0);

        return (APRIL_TAG_HEIGHT() - CAMERA_HEIGHT) / Math.tan(angleToGoalRadians);

    }

}
// end of class