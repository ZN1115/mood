import edit_distance
import search
import numpy as np
import os

desktop_path = "./images/" #建立c.txt的位置

def text_cw(name,msg):
    
    folder = os.path.exists(desktop_path)
    #判斷結果
    if not folder:
        #如果不存在，則建立新目錄
        os.makedirs(desktop_path)
        print('-----建立成功-----')
    #else:
        #shutil.rmtree(desktop_path)
        #os.makedirs(desktop_path)
    full_path = desktop_path + name + '.txt'
    file = open(full_path, 'w' ,encoding='UTF-8')
    for i in range(5):
        file.write(msg[i][0])
        file.write('\n')
    #file.write(msg)
    file.close()

mainsun=[]
def init():
    res=search.init()
    ree=edit_distance.init()
    '''for i in range(len(res)):
        mainsun.append('%.2f'%float(res[i]*ree[i])) #取小數後2位
    print(mainsun) #害怕，悲傷，憂鬱，焦慮，生氣'''
    scared=('%.2f'%float(res[0]*ree[0]))
    sad=('%.2f'%float(res[1]*ree[1]))
    melancholy=str(res[2]/2)
    anxiety=('%.2f'%float(res[3]*ree[3]))
    angry=('%.2f'%float(res[4]*ree[4]))
    value=np.array([["生氣:"+angry]
                ,["悲傷:"+sad]
                ,["焦慮:"+anxiety]
                ,["害怕:"+scared]
                ,["自殺:"+melancholy]])
    text_cw('c',value)               
if __name__ == '__main__':
    init()