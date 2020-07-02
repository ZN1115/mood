# -*- coding: utf-8 -*-
"""
Created on Wed Oct 23 01:19:49 2019

@author: top
"""
import os
import numpy as np
import time

def finalanalysis(bmood,cmood,dmood):
    finalmood=[0,0,0,0]
    for i in range(4):
        finalmood[i]=(bmood[i]/100)+cmood[i]
        finalmood[i]=('%.2f'%finalmood[i])
    
    finalmood.append(dmood[0])
    return finalmood

def text_cw(name, msg):
    desktop_path = ".\\images\\"
    folder = os.path.exists(desktop_path)
    #判斷結果
    if not folder:
        #如果不存在，則建立新目錄
        os.makedirs(desktop_path)
        print('-----建立成功-----')
    #elif(t==0):
        #shutil.rmtree(desktop_path)
        #os.makedirs(desktop_path)
    full_path = desktop_path + name + '.txt'
    file = open(full_path, 'w',encoding='UTF-8')
    for es in range(7):
        file.write(msg[es])
        file.write('\n')
    file.close()

f = open('./images/b.txt','r',encoding='utf-8')
bmood = []
line = f.readlines

for line in f:
    line = ''.join(line).strip('\n')
    bmood.append(line)

for i in range(4): 
    bmood[i]=float(bmood[i][3:])

#for i in range(len(bmood)):  
    #print(bmood[i])
f.close()

openc=0
f = open('./images/c.txt','r',encoding='utf-8')
cmood = []
line = f.readlines

for line in f:
    line = ''.join(line).strip('\n')
    cmood.append(line)

for i in range(5): 
    cmood[i]=float(cmood[i][3:])
    if(cmood[i]>openc):
        openc=cmood[i]
if(openc>1):
    for i in range(5):
        cmood[i]=cmood[i]/openc
cmood[4]=('%.2f'%cmood[4])
#for i in range(len(cmood)):  
    #print(cmood[i])
f.close()

f = open('./images/d.txt','r',encoding='utf-8')
dmood = []
line = f.readlines
for line in f:
    line = ''.join(line).strip('\n')
    dmood.append(line[3:])

f.close()


finalmood=finalanalysis(bmood,cmood,dmood)
print("生氣:"+str(finalmood[0]))
print("悲傷:"+str(finalmood[1]))
print("焦慮:"+str(finalmood[2]))
print("害怕:"+str(finalmood[3]))
print("自殺:"+str(cmood[4]))
print("分數:"+str(finalmood[4]))


localtime=time.localtime(time.time())
aa=np.array(["生氣:"+str(finalmood[0])
            ,"悲傷:"+str(finalmood[1])
            ,"焦慮:"+str(finalmood[2])
            ,"害怕:"+str(finalmood[3])
            ,"自殺:"+str(cmood[4])
            ,"分數:"+str(finalmood[4])
            ,"時間:"+str(localtime[1])+"/"+str(localtime[2])+"-"+str(localtime[3])+"點"+str(localtime[4])+"分"])

#print("時間:"+str(localtime[1])+"/"+str(localtime[2])+"-"+str(localtime[3])+"點"+str(localtime[4])+"分")

text_cw("test",aa)
