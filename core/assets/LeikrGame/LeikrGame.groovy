
import com.leikr.core.LeikrEngine; // Required for extending LeikrEngine

class LeikrGame extends LeikrEngine{

    int ballX;
    int ballY;

    def ballSpeedY;
    def ballSpeedX;

    def void create(){
        super.create();// Very important for initializing core engine variables.
        println("Hello, World! From the LeikrGame script.");
        
        ballWidth = 80;
        ballHeight = 80;
        
        ballY = 0;
        ballX = getScreenWidth() / 2;
        
        ballSpeedY = 1;
        ballSpeedX = 1;
        
        //setBackgroundColor("GREEN");
    }
   
    def void moveBall(){
        if(ballY <= 15){
            ballSpeedY = 1;
        }
        if(ballY >= getScreenHeight()-15){
            ballSpeedY = -1;
        }
        if(ballX <= 15){
            ballSpeedX = 1;
        }
        if(ballX >= getScreenWidth()-15){
            ballSpeedX = -1;
        }
        ballY = ballY + ballSpeedY;
        ballX = ballX + ballSpeedX;
    }

    def void render(){
        
        
        moveBall();
        drawCircle(ballX, ballY, 15, 4, "FILLED");
        drawLine(10, 10, 130, 80, 6);
        drawPalette(5, 50, 10, 10);
        drawColor(0, 10, 10);
        drawColor(5, 20, 20);
        
    }
    
}
