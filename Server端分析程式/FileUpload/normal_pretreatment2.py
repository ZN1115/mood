import wave
import numpy as np
import math
import os
import speech_recognition
from pydub import AudioSegment


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
        if(i<ts-1):
            #print("沉默時間")
            #print(timestart[i+1]-timeend[i])
            totalnospeak.append(timestart[i+1]-timeend[i])
        #print("取點個數")
        #print(startt)
        #print("最大音量")
        #print(big)
        #print("最小音量")
        #print(small)
        if(end>=0):
            voise[i]=1
        else:
            voise[i]=0
    return voise

# ============ test the algorithm =============
# read wave file and get parameters.
normal1=[]
normal11=[]
normal2=[]
normal22=[]
normal3=[]
normal33=[]
check_normal1=0
check_normal2=0
check_normal3=0

for ii in range(0,3):
    if(ii==0):
        outwav='./images/normal1.wav'
    elif(ii==1):
        outwav='./images/normal2.wav'
    elif(ii==2):
        outwav='./images/normal3.wav'
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
    #整段音檔轉文字檔
    
    desktop_path = "./images/"
    e =speech_recognition.Recognizer()
    if(ii==0):
        with speech_recognition.AudioFile(desktop_path + 'normal1.wav') as source:
            e.adjust_for_ambient_noise(source,duration=0)
            audio=e.record(source)
    elif(ii==1):
        with speech_recognition.AudioFile(desktop_path + 'normal2.wav') as source:
            e.adjust_for_ambient_noise(source,duration=0)
            audio=e.record(source)
    elif(ii==2):
        with speech_recognition.AudioFile(desktop_path + 'normal3.wav') as source:
            e.adjust_for_ambient_noise(source,duration=0)
            audio=e.record(source)
    txt=e.recognize_google(audio,language='zh-TW')
    #print(len(txt))
    totalwords.append(len(txt))
    #totaltalkspeed.append(len(txt)/(timeend[i]-timestart[i]))
    cw=0
    


    #切割音檔並轉換為文字
    if(ii==0):
        sound = AudioSegment.from_mp3('./images/normal1.wav')
    elif(ii==1):
        sound = AudioSegment.from_mp3('./images/normal2.wav')
    elif(ii==2):
        sound = AudioSegment.from_mp3('./images/normal3.wav')
    for i in range(ts):
        start_time = int(timestart[i])
        stop_time = math.ceil(timeend[i])
        #print ("time:",start_time,"~",stop_time)
        start_time = start_time*1000
        stop_time = stop_time*1000
        word = sound[start_time:stop_time]
        desktop_path = "./images/常模音檔/斷句音檔/"
        folder = os.path.exists(desktop_path)
        #判斷結果
        if not folder:
            #如果不存在，則建立新目錄
            os.makedirs(desktop_path)
            #print('-----建立成功-----')
        word.export(desktop_path +'常模%s.wav' %i,format="wav")
        
        #print("轉換語音檔為文字")
        e =speech_recognition.Recognizer()
        with speech_recognition.AudioFile(desktop_path +'常模%s.wav' %i) as source:
            e.adjust_for_ambient_noise(source,duration=0)
            audio=e.record(source)
        txt=e.recognize_google(audio,language='zh-TW')
        #print(len(txt))
        totalwords.append(len(txt))
        totaltalkspeed.append(len(txt)/(timeend[i]-timestart[i]))
        
        t=str(i)
        totalvolume11=0
        totalvar11=0
        totaltalkspeed11=0
        totalnospeak11=0
        if(len(totalvolume)>0):
            for i in range(0,len(totalvolume)):
                totalvolume11=totalvolume11+totalvolume[i]
            totalvolume11=totalvolume11/len(totalvolume)
        if(len(totalvar)>0):
            for i in range(0,len(totalvar)):
                totalvar11=totalvar11+totalvar[i]
            totalvar11=totalvar11/len(totalvar)
        if(len(totaltalkspeed)>0):
            for i in range(0,len(totaltalkspeed)):
                totaltalkspeed11=totaltalkspeed11+totaltalkspeed[i]
            totaltalkspeed11=totaltalkspeed11/len(totaltalkspeed)
        if(len(totalnospeak)>1):
            for i in range(0,len(totalnospeak)-1):
                totalnospeak11=totalnospeak11+totalnospeak[i]
            totalnospeak11=totalnospeak11/(len(totalnospeak)-1)
        
    check_normal1=0
    check_normal2=0
    check_normal3=0
    if(ii==0):
        normal1.append(endd)
        normal1.append(var)
        normal1.append(len(txt)/time3[-1])
        normal11.append(totalvolume11)
        normal11.append(totalvar11)
        normal11.append(totaltalkspeed11)
        normal11.append(totalnospeak11)
        for ch in range(0,3):
            if(normal1[ch]==0):
                check_normal1=check_normal1+1
        for ch in range(0,4):
            if(normal11[ch]==0):
                check_normal1=check_normal1+1
    elif(ii==1):
        normal2.append(endd)
        normal2.append(var)
        normal2.append(len(txt)/time3[-1])
        normal22.append(totalvolume11)
        normal22.append(totalvar11)
        normal22.append(totaltalkspeed11)
        normal22.append(totalnospeak11)
        for ch in range(0,3):
            if(normal2[ch]==0):
                check_normal2=check_normal2+1
        for ch in range(0,4):
            if(normal22[ch]==0):
                check_normal2=check_normal2+1
        
    elif(ii==2):
        normal3.append(endd)
        normal3.append(var)
        normal3.append(len(txt)/time3[-1])
        normal33.append(totalvolume11)
        normal33.append(totalvar11)
        normal33.append(totaltalkspeed11)
        normal33.append(totalnospeak11)
        for ch in range(0,3):
            if(normal3[ch]==0):
                check_normal3=check_normal3+1
        for ch in range(0,4):
            if(normal33[ch]==0):
                check_normal3=check_normal3+1
    
nnormal=[]
normall=[]   
if(check_normal1==7):
    for i in range(0,3):
        normall.append((normal2[i]+normal3[i])/2)
        
    for i in range(0,4):
        nnormal.append((normal22[i]+normal33[i])/3)
elif(check_normal2==7):
    for i in range(0,3):
        normall.append((normal1[i]+normal3[i])/2)
        
    for i in range(0,4):
        nnormal.append((normal11[i]+normal33[i])/2)
elif(check_normal3==7):
    for i in range(0,3):
        normall.append((normal1[i]+normal2[i])/2)
        
    for i in range(0,4):
        nnormal.append((normal11[i]+normal22[i])/2)
elif(check_normal3==7 and check_normal1==7):
    for i in range(0,3):
        normall.append(normal2[i])
        
    for i in range(0,4):
        nnormal.append(normal22[i])
elif(check_normal2==7 and check_normal1==7):
    for i in range(0,3):
        normall.append(normal3[i])
        
    for i in range(0,4):
        nnormal.append(normal33[i])
elif(check_normal2==7 and check_normal3==7):
    for i in range(0,3):
        normall.append(normal1[i])
        
    for i in range(0,4):
        nnormal.append(normal11[i])
elif(check_normal2==7 and check_normal3==7 and check_normal3==7):
    print("請重錄")

else:
    for i in range(0,3):
        normall.append((normal1[i]+normal2[i]+normal3[i])/3)
        
    for i in range(0,4):
        nnormal.append((normal11[i]+normal22[i]+normal33[i])/3)


for i in range(0,3):
    print(normall[i])
for i in range(0,4):
    print(nnormal[i])

def text_cw(name, msg):
    desktop_path = "./images/"
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
    for i in range(0,7):
        file.write(msg[i][0])
        file.write('\n')
    #file.write(msg)
    file.close()
    

value=np.array([["完整音量平均:"+str(normall[0]).replace('[','').replace(']','')]
                ,["完整音量變異數:"+str(normall[1]).replace('[','').replace(']','')]
                ,["完整語速:"+str(normall[2]).replace('[','').replace(']','')]
                ,["斷句音量平均:"+str(nnormal[0]).replace('[','').replace(']','')]
                ,["斷句音量變異數:"+str(nnormal[1]).replace('[','').replace(']','')]
                ,["斷句語速:"+str(nnormal[2]).replace('[','').replace(']','')]
                ,["斷句沉默時間:"+str(nnormal[3]).replace('[','').replace(']','')]])

text_cw("a",value)
