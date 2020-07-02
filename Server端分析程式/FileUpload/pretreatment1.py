import wave
import matplotlib.pyplot as pl
import numpy as np
import math
import os
import speech_recognition
from pydub import AudioSegment
from googletrans import Translator
import sys
import shutil


def CheckStatus(fileName, Need_Delete):
    """
    input : 檔案路徑和名稱
    output : 呼叫下一位 (TC Project suit)
    target:
    TC 動作做完的時候會寫出一個檔案根Python 說"老子我做完了你可以叫下一個Project" 開始做了
    如果Need_Delete 是TRUE的話發現檔案存在就將其刪除
    """
    if Need_Delete == True:
        if os.path.exists(fileName):
            os.remove(fileName)

#計算分貝
def calVolumeDB(waveData, frameSize, overLap):
    wlen = len(waveData)
    step = frameSize - overLap
    frameNum = int(math.ceil(wlen*1.0/step))
    volume = np.zeros((frameNum,1))
    for i in range(frameNum):
        curFrame = waveData[np.arange(i*step,min(i*step+frameSize,wlen))]
        curFrame = curFrame - np.mean(curFrame) # zero-justified
        volume[i] = 10*np.log10(np.sum(curFrame*curFrame))
    return volume
#計算分貝
def calVolume(waveData, frameSize, overLap):
    wlen = len(waveData)
    step = frameSize - overLap
    frameNum = int(math.ceil(wlen*1.0/step))
    volume = np.zeros((frameNum,1))
    for i in range(frameNum):
        curFrame = waveData[np.arange(i*step,min(i*step+frameSize,wlen))]
        curFrame = curFrame - np.median(curFrame) # zero-justified
        volume[i] = np.sum(np.abs(curFrame))/10000
    return volume
#計算過零率
def ZeroCR(waveData,frameSize,overLap):
    wlen = len(waveData)
    step = frameSize - overLap
    frameNum = math.ceil(wlen/step)
    zcr = np.zeros((frameNum,1))
    for i in range(frameNum):
        curFrame = waveData[np.arange(i*step,min(i*step+frameSize,wlen))]
        #To avoid DC bias, usually we need to perform mean subtraction on each frame
        #ref: http://neural.cs.nthu.edu.tw/jang/books/audiosignalprocessing/basicFeatureZeroCrossingRate.asp
        curFrame = curFrame - np.mean(curFrame) # zero-justified
        zcr[i] = sum(curFrame[0:-1]*curFrame[1::]<=0)
    return zcr
#計算沒說話的間隔
def Unvoise(timestart,timeend,time2,zcr1,nframes,ts):
    voise=np.zeros((ts,1))
    for i in range(0,ts):
        start=0
        end=0
        for j in range(0,nframes):
            if(time2[j]==timestart[i]):
                start=1
            elif(time2[j]==timeend[i]):
                start=0
                if(zcr1[j]>=70):
                    end=end+1
                else:
                    end=end-1
                break
            if(start==1):
                if(zcr1[j]>=70):
                    end=end+1
                else:
                    end=end-1
        if(end>=0):
            voise[i]=1
        else:
            voise[i]=0
    return voise

#取得切割音檔
def Loudvoise(timestart,timeend,time3,data3,ts,xx):
    loudvoise=np.zeros((ts,1))
    for i in range(0,ts):
        start=0
        startt=0
        end=0
        big=0
        small=100
        varr=0
        for j in range(0,xx):
            if(time3[j]==timestart[i]):
                start=1
            elif(time3[j]==timeend[i]):
                startt=startt+1
                end=end+data3[j]
                if(data3[j]>=big):
                    big=data3[j]
                if(data3[j]<=small):
                    small=data3[j]
                start=0
                
                break
            if(start==1):
                startt=startt+1
                end=end+data3[j]
                if(data3[j]>=big):
                    big=data3[j]
                if(data3[j]<=small):
                    small=data3[j]
        end=end/startt
        for j in range(0,xx):
            if(time3[j]==timestart[i]):
                start=1
            elif(time3[j]==timeend[i]):
                start=0
                break
            if(start==1):
                varr=varr+(data3[i]-end)*(data3[i]-end)
        varr=varr/startt        
        #print("________________________________")
        #print("開始說話時間")
        #print(timestart[i])
        #print("結束說話時間")
        #print(timeend[i])
        #print("說話時間長度")
        #print(timeend[i]-timestart[i])
        #print("平均音量")
        #print(end)
        totalvolume.append(end)
        #print("變異數")
        #print(varr)
        totalvar.append(varr)
        if(i<ts):
            #print("沉默時間")
            #print(timestart[i+1]-timeend[i])
            totalnospeak.append(timestart[i+1]-timeend[i])
        #print("取點個數")
        #print(startt)
        if(end>=0):
            voise[i]=1
        else:
            voise[i]=0
    return voise

CheckStatus('.\\images\\ok.txt',True)#檢查是否做完情緒音檔預處理

# ============ test the algorithm =============
# read wave file and get parameters.
outwav='./images/test.wav'
fw = wave.open(outwav,'r')
params = fw.getparams()
print(params)
nchannels, sampwidth, framerate, nframes = params[:4]
strData = fw.readframes(nframes)
waveData = np.frombuffer(strData, dtype=np.int16)
#waveData = waveData*1.0/max(abs(waveData))  # normalization
fw.close()

waveData2=np.zeros((nframes//128,1))
waveData2.setflags(write=1)

data3=np.zeros((nframes//128,1))
data3.setflags(write=1)
time3=np.zeros((nframes//128,1))
time3.setflags(write=1)
timestart=np.zeros((nframes//128,1))
timestart.setflags(write=1)
timeend=np.zeros((nframes//128,1))
timeend.setflags(write=1)
# calculate volume
frameSize = 256
overLap = 128
volume12 = calVolume(waveData,frameSize,overLap)
zcr1 = ZeroCR(waveData,frameSize,overLap)
#plot the wave
time = np.arange(0, nframes)*(1.0/framerate)
time2 = np.arange(0, len(volume12))*(frameSize-overLap)*1.0/framerate
#print(volume12)
#記錄簡化後的數值
data3[0]=volume12[0]
time3[0]=time2[0]
xx=1

totalvolume=[]
totalvar=[]
totalnospeak=[]
totalwords=[]
totaltalkspeed=[]

totalvolume1=0
totalvar1=0
totalwords1=0
totaltalkspeed1=0
totalnospeak1=0

#求斜率
for i in range(1,nframes//128):
    waveData2[i-1]=volume12[i]-volume12[i-1]

#比較斜率，如果在斜率1在斜率0的誤差範圍內，兩斜線中間的點不存放在新的陣列裡 
for i in range(1,nframes//128):
    if(waveData2[i]>=waveData2[i-1]+0.25):
        data3[xx]=volume12[i]
        time3[xx]=time2[i]
        xx=xx+1
    elif(waveData2[i]<waveData2[i-1]-0.25):
        data3[xx]=volume12[i]
        time3[xx]=time2[i]
        xx=xx+1

data3[xx]=volume12[-1]
time3[xx]=time2[-1]

timepoint=0
xx=xx+1
ts=0
te=0
for i in range(xx,nframes//128):
    data3[i]=data3[xx-1]
    time3[i]=time3[xx-1]
#print(nframes//128)
#print(xx)


#找出聲音開始與結束時間
for i in range(0,xx):
    if(data3[i]>=max(data3)*0.05+min(data3)*5.0):
        if(timepoint==0):
            timestart[ts]=time3[i-1]
            ts=ts+1
            timepoint=1
    if(data3[i]<max(data3)*0.05+min(data3)*5.0):
        if(timepoint==1):
            timeend[te]=time3[i]
            te=te+1
            timepoint=0
    if(i==xx):
        if(timepoint==1):
            timeend[te]=time3[i]
            te=te+1
            timepoint=0
#print(max(data3)*0.05+min(data3)*5.0)
#print(ts)


        
#間隔太近的兩段時間合併
timestartdelete=[]
timeenddelete=[]

for i in range(1,ts):
    if(timestart[i]-timeend[i-1]<0.3):
        timestartdelete.append(i)
        timeenddelete.append(i-1)
        te=te-1
timestart=np.delete(timestart,timestartdelete)
timeend=np.delete(timeend,timeenddelete)
ts=te

#時間太短刪除
timelongdelete=[]

for i in range(0,ts):
    if(timeend[i]-timestart[i]<0.3):
        timelongdelete.append(i)
        te=te-1
timestart=np.delete(timestart,timelongdelete)
timeend=np.delete(timeend,timelongdelete)
ts=te

#算每段時間的過零率，如果超出標準，則把他認為是氣音刪除
voise=Unvoise(timestart,timeend,time2,zcr1,nframes,ts)
#print(voise)
voisedelete=[]
for i in range(0,ts):
    if(voise[i]>0):
        voisedelete.append(i)
        te=te-1
timestart=np.delete(timestart,voisedelete)
timeend=np.delete(timeend,voisedelete)
ts=te

bigvoise=Loudvoise(timestart,timeend,time3,data3,ts,xx)

print("------------------------------------------------------------------")

endd=0

for i in range(0,xx):
    endd=endd+data3[i]
endd=endd/xx
var=0
for i in range(0,xx):
    var=var+(data3[i]-endd)*(data3[i]-endd)
var=var/xx

#建文字檔並寫入文字
def text_cw(name, msg,cw,t):
    if(cw==0):
        desktop_path = ".\\images\\2\\all\\"
    elif(cw==1):
        desktop_path = ".\\images\\2\\cut\\"
    elif(cw==2):
        desktop_path = ".\\images\\3\\all\\"
    elif(cw==3):
        desktop_path = ".\\images\\3\\cut\\"
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
    file.write(msg)
    file.close()
    
def text_cw1(name, msg,cw,t):
    if(cw==4):
        desktop_path = ".\\images\\1\\all\\"
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
    if(cw==4):
        for es in range(7):
            file.write(msg[es])
            file.write('\n')
    file.close()
    

translator = Translator()

#整段音檔轉文字檔
desktop_path = ".\\"
e =speech_recognition.Recognizer()
with speech_recognition.AudioFile(outwav) as source:
    e.adjust_for_ambient_noise(source,duration=0)
    audio=e.record(source)
txt=e.recognize_google(audio,language='zh-TW')
txt1=translator.translate(txt,src = "zh-TW",dest="en")
#print(len(txt))
totalwords.append(len(txt))
cw=0
text_cw("mood", txt,cw,0)
cw=2
text_cw("mood", str(txt1.text),cw,0)


#切割音檔並轉換為文字
sound = AudioSegment.from_mp3(outwav)
for i in range(ts):
    start_time = int(timestart[i])
    stop_time = math.ceil(timeend[i])
    #print ("time:",start_time,"~",stop_time)
    start_time = start_time*1000
    stop_time = stop_time*1000
    word = sound[start_time:stop_time]
    desktop_path = ".\\images\\1\\voise\\"
    folder = os.path.exists(desktop_path)
    #判斷結果
    if not folder:
        #如果不存在，則建立新目錄
        os.makedirs(desktop_path)
        #print('-----建立成功-----')
    elif(i==0):
        shutil.rmtree(desktop_path)
        os.makedirs(desktop_path)
    word.export(desktop_path +'mood%s.wav' %i,format="wav")
    
    #print("轉換語音檔為文字")
    e =speech_recognition.Recognizer()
    with speech_recognition.AudioFile(desktop_path +'mood%s.wav' %i) as source:
        e.adjust_for_ambient_noise(source,duration=0)
        audio=e.record(source)
    txt=e.recognize_google(audio,language='zh-TW')
    
    #print(len(txt))
    totalwords.append(len(txt))
    totaltalkspeed.append(len(txt)/(timeend[i]-timestart[i]))
    t=str(i)
    cw=1
    text_cw("mood"+t, txt,cw,i)

if(len(totalvolume)>0):   
    for i in range(0,len(totalvolume)):
        #print("------------------")
        #print("斷句音量平均",totalvolume[i])
        totalvolume1=totalvolume1+totalvolume[i]
    totalvolume1=totalvolume1/len(totalvolume)
if(len(totalvar)>0):
    for i in range(0,len(totalvar)):
        #print("斷句變異數",totalvar[i])
        totalvar1=totalvar1+totalvar[i]
    totalvar1=totalvar1/len(totalvar)
if(len(totaltalkspeed)>0):
    for i in range(0,len(totaltalkspeed)):
        #print("斷句語速",totaltalkspeed[i])
        totaltalkspeed1=totaltalkspeed1+totaltalkspeed[i]
    totaltalkspeed1=totaltalkspeed1/len(totaltalkspeed)
if(len(totalnospeak)>1):
    for i in range(0,len(totalnospeak)-1):
        totalnospeak1=totalnospeak1+totalnospeak[i]
    totalnospeak1=totalnospeak1/(len(totalnospeak)-1)
if(len(totalwords)>0):
    for i in range(0,len(totalwords)):
        totalwords1=totalwords1+totalwords[i]
    totalwords1=totalwords1/2







print("斷句音量平均:",totalvolume1)
print("斷句變異數:",totalvar1)
print("斷句語速:",totaltalkspeed1)
print("斷句沉默時間:",totalnospeak1)

print("_____________________________")
print("總說話時間長度",time3[-1])
print("文章音量平均",endd)
print("文章變異數",var)
print("文章總字數",totalwords1)
print("文章語速",totalwords1/time3[-1])

cw=4
aa=np.array([ "文章音量平均:"+str(endd).replace('[','').replace(']','')
            ,"文章變異數:"+str(var).replace('[','').replace(']','') 
            ,"文章語速:"+str(totalwords1/time3[-1]).replace('[','').replace(']','')
            ,"斷句音量平均:"+str(totalvolume1).replace('[','').replace(']','')
            ,"斷句音量變異數:"+str(totalvar1).replace('[','').replace(']','')
            ,"斷句語速:"+str(totaltalkspeed1)
            ,"斷句沉默時間:"+str(totalnospeak1)])
            
            
text_cw1("mood",aa,cw,0)

fp = open(".\\images\\ok.txt",'w')


#print("斷句情緒分數比例",score[0],score[1],score[2],score[3])

#print("誤差值",error)
#print("可信度",credibility)