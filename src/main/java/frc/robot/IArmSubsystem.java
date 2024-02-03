package frc.robot;

public interface IArmSubsystem {
    void SetToPosition(int setPoint);
    int GetPosition();
}