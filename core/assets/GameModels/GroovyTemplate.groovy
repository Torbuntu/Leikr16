
import com.leikr.core.LeikrEngine; // Required for extending LeikrEngine

class GAME_NAME extends LeikrEngine{

    def void create(){
        super.create();// Very important for initializing core engine variables.
        println("Hello, World! From the LeikrGame script.");       
    }
  
    def void render(){
        rect(0,0,320,240,5, "filled");
    }
}