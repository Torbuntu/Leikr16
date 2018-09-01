from com.leikr.core import LeikrEngine

class GAME_NAME(LeikrEngine):
    
    def __init__(self):
        super(GAME_NAME, self).__init__()

    #
    
    def create(self):
        super(GAME_NAME, self).create()
        print("Hello, World! From the LeikrGame script.")
        
    #
        
    def render(self, delta=None):
        self.drawRect(0,0,320,240,5, "filled");     
      
    #