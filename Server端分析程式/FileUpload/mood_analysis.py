# -*- coding: utf-8 -*-
"""
Created on Tue Nov 26 01:04:09 2019

@author: top
"""

# -*- coding: utf-8 -*-
import os
import numpy as np
"""
Created on Sun Oct 20 00:09:07 2019

@author: top
"""

#斷句音檔比對    
def PK(mood,normal):
    score=[0,0,0,0]
    angryscore=[0,0,0]
    scarescore=[0,0,0]
    sadscore=[0,0,0]
    anxiety=[0,0,0]
    normalsmallvolume=normal[3]
    normalbigvolume=normal[3]+6.7
    normalsmallvar=normal[4]-308
    normalbigvar=normal[4]+308
    normalbigtalkspeed=normal[5]+1.6
    normalsmalltalkspeed=normal[5]+0.4
    
    angrysmallvolume=normalbigvolume+2.26
    angrybigvolume=angrysmallvolume+24.49
    angrysmallvar=normalbigvar+500
    angrybigvar=angrysmallvar+1000
    angrybigtalkspeed=normalbigtalkspeed+2.8
    angrysmalltalkspeed=normalsmalltalkspeed+0.3
    
    sadsmallvolume=normalsmallvolume-8.21
    sadbigvolume=normalsmallvolume-2
    sadsmallvar=normalsmallvar-584
    sadbigvar=normalsmallvar+30
    sadbigtalkspeed=normalbigtalkspeed-0.3
    sadsmalltalkspeed=normalsmalltalkspeed-1.7
    
    scaresmallvolume=normalsmallvolume-7
    scarebigvolume=normalbigvolume-4.3
    scaresmallvar=normalsmallvar+120
    scarebigvar=normalbigvar+500
    scarebigtalkspeed=normalbigtalkspeed-0.3
    scaresmalltalkspeed=normalsmalltalkspeed-0.3
    
    ansmallvolume=normalsmallvolume-5.41
    anbigvolume=normalbigvolume
    ansmallvar=normalsmallvar-386
    anbigvar=normalbigvar+400
    anbigtalkspeed=normalbigtalkspeed+1.1
    ansmalltalkspeed=normalsmalltalkspeed+0.1

    #angry
    if(mood[3]>=angrybigvolume):
        angryscore[0]=angryscore[0]+1
    elif(angrybigvolume>mood[3]>angrysmallvolume):
        angryscore[0]=(mood[3]-angrysmallvolume)/24.49
    if(mood[4]>=angrybigvar):
        angryscore[1]=angryscore[1]+1
    elif(angrybigvar>mood[4]>angrysmallvar):
        angryscore[1]=(mood[4]-angrysmallvar)/1000
    if(mood[5]>=angrybigtalkspeed):
        angryscore[2]=angryscore[2]+1
    elif(angrybigtalkspeed>mood[5]>angrysmalltalkspeed):
        angryscore[2]=(mood[5]-angrysmalltalkspeed)/3.7
    score[0]=(angryscore[0]+angryscore[1]+angryscore[2])/3
    print("生氣平均音量分數:  "+str(angryscore[0]))
    print("生氣音量變異數分數:"+str(angryscore[1]))
    print("生氣語速分數:      "+str(angryscore[2]))
    print("----------------------------------------")
    
    
    #sad
    if(mood[3]<=sadsmallvolume):
        sadscore[0]=sadscore[0]+1
    elif(sadbigvolume>mood[3]>sadsmallvolume):
        sadscore[0]=(sadbigvolume-mood[3])/6.9
    if(mood[4]<=sadsmallvar):
        sadscore[1]=sadscore[1]+1
    elif(sadbigvar>mood[4]>sadsmallvar):
        sadscore[0]=(sadbigvar-mood[4])/516
    if(mood[5]<=sadsmalltalkspeed):
        sadscore[2]=sadscore[2]+1
    elif(sadbigtalkspeed>mood[5]>sadsmalltalkspeed):
        sadscore[2]=(sadbigtalkspeed-mood[5])/2.6
    score[1]=(sadscore[0]+sadscore[1]+sadscore[2])/3
    print("難過平均音量分數:  "+str(sadscore[0]))
    print("難過音量變異數分數:"+str(sadscore[1]))
    print("難過語速分數:      "+str(sadscore[2]))
    print("----------------------------------------")
    
    #anxiety
    if(mood[3]<=ansmallvolume):
        anxiety[0]=anxiety[0]+1
    elif(anbigvolume>mood[3]>ansmallvolume):
        anxiety[0]=(anbigvolume-mood[3])/12.11
    if(mood[4]>=anbigvar):
        anxiety[1]=anxiety[1]+1
    elif(ansmallvar<mood[4]<anbigvar):
        anxiety[1]=(mood[4]-ansmallvar)/1436
    if(mood[5]>=anbigtalkspeed):
        anxiety[2]=anxiety[2]+1
    elif(ansmalltalkspeed<mood[5]<anbigtalkspeed):
        anxiety[2]=(mood[5]-ansmalltalkspeed)/2.2
    score[2]=(anxiety[0]+anxiety[1]+anxiety[2])/3
    print("焦慮平均音量分數:  "+str(anxiety[0]))
    print("焦慮音量變異數分數:"+str(anxiety[1]))
    print("焦慮語速分數:      "+str(anxiety[2]))
    print("----------------------------------------")
        
        
    #scare
    if(mood[3]<=scaresmallvolume):
        scarescore[0]=scarescore[0]+1
    elif(scaresmallvolume<mood[3]<scarebigvolume):
        scarescore[0]=(normalbigvolume-mood[3])/9.3
    if(mood[4]>=scarebigvar):
        scarescore[1]=scarescore[1]+1
    elif(scarebigvar>mood[4]>scaresmallvar):
        scarescore[1]=(mood[4]-scaresmallvar)/1184
    if(mood[5]<=scaresmalltalkspeed):
        scarescore[2]=scarescore[2]+1
    elif(scarebigtalkspeed>mood[5]>scaresmalltalkspeed):
        scarescore[2]=(normalbigtalkspeed-mood[5])/1.2
    score[3]=(scarescore[0]+scarescore[1]+scarescore[2])/3
    print("害怕平均音量分數:  "+str(scarescore[0]))
    print("害怕音量變異數分數:"+str(scarescore[1]))
    print("害怕語速分數:      "+str(scarescore[2]))
    print("----------------------------------------")
    
    return score

def final(totalscore,score):
    finalscore=[0,0,0,0,0]
    for i in range(0,5):
        finalscore[i]=(totalscore[i]+score[i])/200
    return finalscore

def errorcount(totalscore,score):
    error=0
    for i in range(0,5):
        if(totalscore[i]-score[i]>0):
            error=error+totalscore[i]-score[i]
        else:
            error=error+score[i]-totalscore[i]
    error=error/200
    return error

#存檔
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
    for es in range(4):
        file.write(msg[es])
        file.write('\n')
    file.close()

#讀情緒數值
f = open('./images/1/all/mood.txt','r',encoding='utf-8')
mood = []
line = f.readlines

for line in f:
    line = ''.join(line).strip('\n')
    mood.append(line)
    
mood[0]=float(mood[0][7:])
mood[1]=float(mood[1][6:])
mood[2]=float(mood[2][5:])
mood[3]=float(mood[3][7:])
mood[4]=float(mood[4][8:])
mood[5]=float(mood[5][5:])
mood[6]=float(mood[6][7:])

#for i in range(len(mood)):  
    #print(mood[i])

f.close()

#讀常模數值
f = open('./images/a.txt','r',encoding='utf-8')
normal = []
line = f.readlines

for line in f:
    line = ''.join(line).strip('\n')
    normal.append(line)
    
normal[0]=float(normal[0][7:])
normal[1]=float(normal[1][8:])
normal[2]=float(normal[2][5:])
normal[3]=float(normal[3][7:])
normal[4]=float(normal[4][8:])
normal[5]=float(normal[5][5:])
normal[6]=float(normal[6][7:])
    
#for i in range(len(normal)):
    #print(normal[i])
    
f.close()

finalscore=PK(mood,normal)
#print("斷句情緒分數比例",score[0],score[1],score[2],score[3],score[4])
for aa in range(4):
    finalscore[aa]=('%.2f'%float(finalscore[aa]))

print("生氣:",finalscore[0])
print("悲傷:",finalscore[1])
print("焦慮:",finalscore[2])
print("害怕:",finalscore[3])

aa=np.array(["生氣:"+str(finalscore[0])
            ,"悲傷:"+str(finalscore[1])
            ,"焦慮:"+str(finalscore[2])
            ,"害怕:"+str(finalscore[3])])

text_cw("b",aa)
'''
totalscore=totalPK(mood,normal)
#print("文章情緒分數比例",totalscore[0],totalscore[1],totalscore[2],totalscore[3],totalscore[4])




finalscore=final(totalscore,score)

for aa in range(5):
    finalscore[aa]=('%.2f'%float(finalscore[aa]))





error=errorcount(totalscore,score)
error=('%.2f'%error)
print("誤差值",error)


'''