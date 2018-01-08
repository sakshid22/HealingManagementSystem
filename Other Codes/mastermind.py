from pygame import *
from random import randint


scr = display.set_mode((280,550))

mmbg    = image.load('MMbg.jpg')
palette = image.load('palette.png')
myst    = image.load('myst.png')

palet_rect  = Rect(20,440,240,30)
submit_rect = Rect(20,475,240,30)
delete_rect = Rect(20,510,240,30)

palet_mask = mask.from_surface(palette)


def play():
    
    def submit(value):
        try:
            a,b = zip(*[(a,b) for a,b in zip(mastermind,value) if a!=b])
            a   = list(a)
        except: return 4,0
        for i in b:
            try: a.remove(i)
            except: continue
        return 4-len(b),len(b)-len(a)
        

    display.set_caption('MasterMind')
    scr.blit(mmbg,(0,0))
    scr.blit(palette,palet_rect)
    display.flip()
    
    mastermind = [randint(0,7),randint(0,7),randint(0,7),randint(0,7)]
    
    for ligne in range(10):
        r = scr.blit(myst,(50,ligne*35+70))
        display.update(r)
        combi = []
        while 1:
            ev = event.wait()
            if ev.type == MOUSEBUTTONUP:
                if len(combi)<4:
                    if palet_rect.collidepoint(ev.pos):
                        x,y = ev.pos
                        x -= palet_rect.x
                        y -= palet_rect.y
                        if palet_mask.get_at((x,y)):
                            combi.append(x//30)
                            r = scr.blit(palette,(len(combi)*30+20,ligne*35+70),(x//30*30,0,30,30))
                            display.update(r)
                elif submit_rect.collidepoint(ev.pos):
                    placed,misplaced = submit(combi)
                    for e,c in enumerate([(255,0,0)]*placed+[(255,255,255)]*misplaced):
                        r = draw.circle(scr,c,(190+e*10,ligne*35+85),2,0)
                        display.update(r)
                    break
                if delete_rect.collidepoint(ev.pos):
                    combi = []
                    scr.blit(mmbg,(50,ligne*35+70),(50,ligne*35+70,120,30))
                    scr.blit(myst,(50,ligne*35+70))
                    display.flip()
            elif ev.type == QUIT:
                exit()
        if placed == 4:
            display.set_caption('GAGNE !')
            break
    else:
        display.set_caption('PERDU !')
        
    for e,c in enumerate(mastermind):
        scr.blit(palette,(80+e*30,20),(c*30,0,30,30))
        display.flip()


play()
while 1:
    ev = event.wait()
    if ev.type == QUIT: break
    elif ev.type == MOUSEBUTTONUP: play()

    



    
