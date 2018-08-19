
import com.leikr.core.LeikrEngine; // Required for extending LeikrEngine

class LeikrGame extends LeikrEngine{

	def x;
	def y;
	def id;
	
	def Player = [:];
	
    def void create(){
        super.create();// Very important for initializing core engine variables.
        println("Hello, World! From the LeikrGame script.");
       loadMap(1);
       x = 0;
       y = 0;
       
       Player.x = 32;
       Player.y = 32;
       Player.spriteId = 4;
    }
   
   	def movePlayer(){
   		int rightX = (Player.x +8)/8;
   		int leftX = (Player.x-1)/8;
   		int upY = (Player.y+8)/8;
   		int downY = (Player.y-1)/8;
   		
   		int y = Player.y/8;
   		int x = Player.x/8;
   		
   		
   		def tileIdRight = getCellTileId(rightX, y);
   		def tileIdLeft = getCellTileId(leftX, y);
   		def tileIdUp = getCellTileId(x, upY);
   		def tileIdDown = getCellTileId(x, downY);
   		
   		if(rightKeyPressed() || rightBtnPressed()){
   			if(tileIdRight != 3){
   				Player.x++;
			}
   		}
   		if(leftKeyPressed() || leftBtnPressed()){
   			if(tileIdLeft != 3){
   				Player.x--;
   			}
   		}
   		if(upKeyPressed() || upBtnPressed()){
   			if(tileIdUp != 3){
   				Player.y++;
			}
   		}
   		if(downKeyPressed() || downBtnPressed()){
   			if(tileIdDown != 3){
   				Player.y--;
   			}
   		}
   		drawText(Integer.toString(Player.x), 100, 150, 9 );
   		drawText(Integer.toString(tileIdRight), 100, 200, 9 );
   	}
   
    def void render(){
		id = getCellTileId(x,y);
		
		drawText(Integer.toString(id), 100, 100, 9 );
		
		movePlayer();
		drawSprite(Player.spriteId, Player.x, Player.y);
		
    }
    
}
