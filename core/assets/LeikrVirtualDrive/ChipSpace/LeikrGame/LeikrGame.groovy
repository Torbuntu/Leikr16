
import com.leikr.core.LeikrEngine; // Required for extending LeikrEngine

class LeikrGame extends LeikrEngine{

	def x;
	def y;
	def id;

	def p = [:];

    def void create(){
        super.create();// Very important for initializing core engine variables.
        println("Hello, World! From the LeikrGame script.");
       loadMap(1);
       x = 0;
       y = 0;

       p.x = 24;
       p.y = 24;
       p.spriteId = 4;
       p.vx = 0;
       p.vy = 0;
    }

    def solid(x, y){
    	int nx = x/8;
    	int ny = y/8;
    	println(getCellTileId(nx,ny));
    	return getCellTileId(nx,ny) == 3;
    }

   	def movep(){

   		if(rightKeyPressed() || rightBtnPressed()){
			p.vx = 1;
   		}
   		else if(leftKeyPressed() || leftBtnPressed()){
   			p.vx = -1;
   		}else{
			p.vx = 0;
		}

		if( solid(p.x+p.vx,p.y+p.vy) || solid(p.x+7+p.vx,p.y+p.vy) || solid(p.x+p.vx,p.y-7+p.vy) || solid(p.x+7+p.vx,p.y-7+p.vy)){
			p.vx = 0;
		}

		if (solid(p.x+p.vx,p.y-2+p.vy) || solid(p.x+7+p.vx,p.y-2+p.vy) ){
			p.vy=0;
		}
		else{
			p.vy=p.vy-1;
		}

   		if(p.vy == 0 && (upKeyPressed() || upBtnPressed())){
	   		p.vy = 1;
   		}

		if (p.vy > 0 && (solid(p.x+p.vx,p.y+p.vy) || solid(p.x+7+p.vx,p.y+p.vy))){
			p.vy=0;
		}



   		p.x = p.vx + p.x;
   		p.y = p.vy + p.y;
   	}

    def void render(){
		id = getCellTileId(x,y);

		drawText(Integer.toString(id), 100, 100, 9 );

		movep();
		drawSprite(p.spriteId, p.x, p.y);
		setCamera(p.x, p.y);
		//setCamera(0,0);

    }

}
