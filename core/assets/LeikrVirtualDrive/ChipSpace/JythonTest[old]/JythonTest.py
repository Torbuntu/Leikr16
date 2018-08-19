from com.leikr.core import LeikrEngine

class JythonTest(LeikrEngine):
    
    def __init__(self):
        super(JythonTest, self).__init__()
        self.x = 0
    #
    
    def create(self):
        super(JythonTest, self).create()
        print("hey")
        
    #
        
    def render(self):
                
        if(self.rightKeyPressed()):
            self.x = self.x + 5
            
            
        if(self.leftKeyPressed()):
            self.x = self.x - 5
            
            
        self.drawSprite(8, self.x, 20)
    #
