import java.util.Date;

public class PID {
	final private int P_ON_E = 1;
	final private int REVERSE  = 1;
	public double dispKp;				// * we'll hold on to the tuning parameters in user-entered 
	public double dispKi;				//   format for display purposes
	public double dispKd;				//
    
	private double kp;                  // * (P)roportional Tuning Parameter
	private double ki;                  // * (I)ntegral Tuning Parameter
	private double kd;                  // * (D)erivative Tuning Parameter

	private int controllerDirection;
	public int pOn;
 
	private long lastTime;
	private double outputSum, lastInput;

	public void setControllerDirection(int Direction) {
		if(inAuto && Direction !=controllerDirection){
			  kp = (0 - kp);
		      ki = (0 - ki);
		      kd = (0 - kd);
		}
		
		this.controllerDirection = Direction;
	}

	private long SampleTime;
	private double outMin, outMax;
	private boolean inAuto, pOnE;
	
	public PID(double Kp, double Ki, double Kd, int POn, int ControllerDirection) {
	    inAuto = false;

	    this.setOutputLimits(0, 255);				//default output limit corresponds to
													//the arduino pwm limits

	    SampleTime = 250;							//default Controller Sample Time is 0.25 seconds

	    this.setControllerDirection(ControllerDirection);
	    this.setTunings(Kp, Ki, Kd, POn);

	    lastTime = new Date().getTime()-SampleTime;
	}
	
	public double compute(double inputCom, double setPointCom)
	{
	   if(!inAuto) return 0;
	   long now = new Date().getTime();
	   long timeChange = (now - lastTime);
	   if(timeChange>=SampleTime)
	   {
	      /*Compute all the working error variables*/
	      double input = inputCom;
	      double error = setPointCom - input;
	      double dInput = (input - lastInput);
	      outputSum+= (ki * error);

	      /*Add Proportional on Measurement, if P_ON_M is specified*/
	      if(!pOnE) outputSum-= kp * dInput;

	      if(outputSum > outMax) outputSum= outMax;
	      else if(outputSum < outMin) outputSum= outMin;

	      /*Add Proportional on Error, if P_ON_E is specified*/
		   double output;
	      if(pOnE) output = kp * error;
	      else output = 0;

	      /*Compute Rest of PID Output*/
	      output += outputSum - kd * dInput;

		  if(output > outMax) output = outMax;
	      else if(output < outMin) output = outMin;

	      /*Remember some variables for next time*/
	      lastInput = input;
	      lastTime = now;
		    return output;
	   }
	   else return 0;
	}
	
	
	public void setTunings(double Kp, double Ki, double Kd, int POn)
	{
	   if (Kp<0 || Ki<0 || Kd<0) return;

	   pOn = POn;
	   pOnE = POn == P_ON_E;

	   dispKp = Kp; dispKi = Ki; dispKd = Kd;

	   double SampleTimeInSec = ((double)SampleTime)/1000;
	   kp = Kp;
	   ki = Ki * SampleTimeInSec;
	   kd = Kd / SampleTimeInSec;

	  if(controllerDirection == REVERSE)
	   {
	      kp = (0 - kp);
	      ki = (0 - ki);
	      kd = (0 - kd);
	   }
	}
	
	public void setMode(int mode)
	{
	    boolean newAuto = (mode == 1);
	    if(newAuto && !inAuto)
	    {  /*we just went from manual to auto*/
	        this.Initialize();
	    }
	    inAuto = newAuto;
	}
	
	public void Initialize() {
	   outputSum = myOutput;
	   lastInput = myInput;
	   if(outputSum > outMax) outputSum = outMax;
	   else if(outputSum < outMin) outputSum = outMin;
	}
	
	public void setOutputLimits(double Min, double Max) {
	   if(Min >= Max) return;
	   outMin = Min;
	   outMax = Max;
	}
	
	public void setSampleTime(int NewSampleTime) {
	   if (NewSampleTime > 0)
	   {
	      double ratio  = (double)NewSampleTime
	                      / (double)SampleTime;
	      ki *= ratio;
	      kd /= ratio;
	      SampleTime = (long)NewSampleTime;
	   }
	}
}
