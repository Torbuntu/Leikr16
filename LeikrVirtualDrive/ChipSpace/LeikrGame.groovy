
import com.leikr.core.LeikrEngine; // Required for extending LeikrEngine

class LeikrGame extends LeikrEngine{
    
    int pOneScore = 0;
    int pTwoScore = 0;    

    def style = "Filled";

    int playerOneX = 5;
    int playerOneY = getScreenHeight()/2;
    int playerOneHeight = 35;

    int playerTwoX = getScreenWidth() - 10;
    int playerTwoY = getScreenHeight()/2;
    int playerTwoHeight = 35;

    int ballX = getScreenWidth() / 2;
    int ballY = getScreenHeight() / 2;
    int ballSpeedX = 0;
    int ballSpeedY = 0;
    
    int textPosX = 25;
    int textPosY = getScreenHeight() - 12;
    
    def playing = false;
    def gameOver = true;
    
    def winner = "";
    def notStart = true;
    
    
    int centerLineX = getScreenWidth() / 2;
    

    def void playerOneMove(){
        if(downKeyPressed()){
            if(playerTwoY > 0){
                playerTwoY -= 1;
            }              
        }
        if(upKeyPressed()){
            if(playerTwoY < getScreenHeight()-playerTwoHeight){
                playerTwoY += 1;
            }
        }
    }
    
    def playerTwoMove(){
        if(zKeyPressed()){
            if(playerOneY > 0){
                playerOneY -= 1;
            }              
        }
        if(xKeyPressed()){
            if(playerOneY < getScreenHeight()-playerOneHeight){
                playerOneY += 1;
            }
        }
    }

    def checkYbounds(){
        if(ballY <= 7){
            ballSpeedY = ballSpeedY * -1;
        }
        if(ballY >= getScreenHeight()-7){
            ballSpeedY = ballSpeedY * -1;
        }
    }

    def checkBallCollisionPlayerOne(){
        if(ballSpeedX < 0 && ballX <= 11){
            if(ballY >= playerOneY && ballY <= playerOneY + 35 ){
                ballSpeedX = ballSpeedX * -1;
                pOneScore++;
            }else{
                ballSpeedY = 0;
                ballSpeedX = 0;
                playing = false;
                gameOver = true;
                winner = "Player 2";
            }
        }
        
    }
    
    def checkBallCollisionPlayerTwo(){
        if(ballSpeedX > 0 && ballX >= getScreenWidth() - 11){
            if(ballY >= playerTwoY && ballY <= playerTwoY + 35){

                ballSpeedX = ballSpeedX * -1;
                pTwoScore++;
            }else{
                ballSpeedY = 0;
                ballSpeedX = 0;
                playing = false;
                gameOver = true;
                winner = "Player 1";
            }
        }
        
    }

    def ballMove(){
        if(ballSpeedX == 0 && ballSpeedY == 0){
            if(pOneScore > pTwoScore){
                ballSpeedX = 1;
                ballSpeedY = 1;
            }else{
                ballSpeedX = -1;
                ballSpeedY = -1;
            }
        }
        checkYbounds();

        checkBallCollisionPlayerOne();
        checkBallCollisionPlayerTwo();
        
        ballX += ballSpeedX;
        ballY += ballSpeedY;
    }


    def void create(){
        super.create();// Very important for initializing core engine variables.
        println("Hello, World! From the LeikrGame script.");
    }
   
    

    def void render(){
        drawRect(0, 0, 2, getScreenHeight(), "WHITE", "Filled");
        drawRect(getScreenWidth()-2, 0, 2, getScreenHeight(), "WHITE", "Filled");
        drawRect(0, 0, getScreenWidth(), 2, "WHITE", "Filled");
        drawRect(0, getScreenHeight()-2, getScreenWidth(), 2, "WHITE", "Filled");
        playerOneMove();
        playerTwoMove();
        if(playing){
            ballMove();
        }
        if(gameOver){
            if(notStart){
                drawText("Press space to play.", 30, 92);
                
            }else{
                drawText("Press space to play again.", 30, 92);
                drawText(winner+" Wins!", 50, 100);

            }
        }else{
            drawRect(centerLineX-1, 0, 2, getScreenHeight(), "WHITE", "Filled");
        }
        if(spaceKeyPressed()){
            if(notStart){
                notStart = false;
                playing = true;
            }
            pOneScore = 0;
            pTwoScore = 0;
            ballX = getScreenWidth() / 2;
            ballY = getScreenHeight() / 2;
            gameOver = false;
            playing = true;
            ballMove();
        }
        drawText("P1 Score: "+pOneScore, textPosX, textPosY);
        drawText("P2 Score: "+pTwoScore, textPosX+120, textPosY);
        drawCircle(ballX, ballY, 5, "BLUE", style);

        drawRect(playerOneX, playerOneY, 5, playerOneHeight, "WHITE", style);
        drawRect(playerTwoX, playerTwoY, 5, playerTwoHeight, "WHITE", style);
        
    }
    
}
