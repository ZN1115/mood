import os


#===資料夾全域變數================
#在 similar_score()內
#要比較最短編輯的句子(情緒句子跟分數)
sentence = "./sentence/"

#================================
#在init()內
#放語音轉文字切割而成的句子，每一句分成一個txt檔(不限檔名，txt檔就收)
brokensent = "./images/2/cut/"

#================================





'''
比較兩個句子的最短編輯距離
'''
def editDistance(s1, s2):
    if len(s1) > len(s2):#長度比較
        s1, s2 = s2, s1

    distances = range(len(s1) + 1) #從[0,1,2,......,s1+1]
    for i2, c2 in enumerate(s2): #迭代index,item
        distances_ = [i2+1]
        #print(i2,c2)
        for i1, c1 in enumerate(s1):
            if c1 == c2:
                distances_.append(distances[i1])
            else:
                distances_.append(1 + min((distances[i1], distances[i1 + 1], distances_[-1])))
        distances = distances_
    #print(distances[-1])
    return distances[-1]
'''
切割逗點
'''
def sentence_cut(sent):
    f=open(sent,'r+',encoding='UTF-8')
    lines = f.readlines()
    keyword = []  ## 空列表, 将第i行数据存入list中
    for i in range(0,lines.__len__(),1):
        keyword.append([])
        for word in lines[i].split('，'): #切割逗號
            if word != '':
                keyword[i].append(word)            
    f.close()
    return keyword
'''
查詢跟每一句的最短編輯距離，
並取最短的(如果最短的距離存在複數，就全都放進去)
返回差距最短句子分數，情緒
'''
def similar_score(s1):
    #sentence = "./sentence/" #存放文字檔的路徑
    filename= os.listdir(sentence) #資料夾下的所有檔案 
    bignum=100000
    distancearray=[]
    text=[]
    osfile=[]
    for file in range(len(filename)):
        #print(sentence+filename[file])
        s2=sentence_cut(sentence+filename[file])#切割
        for i in range(0,s2.__len__(),1):
            distance=editDistance(s1, s2[i][1])#最短編輯距離
            #print(s2[i][1])
            if distance == bignum: #如果有一樣短的距離
                distancearray.append(distance)
                text.append(s2[i])
                osfile.append(filename[file].replace(".txt",""))
                #print(s2[i][1])
            elif distance < bignum: #有更短的編輯距離就清空原本陣列
                bignum=distance
                distancearray.clear()
                distancearray.append(distance)
                text.clear()
                text.append(s2[i])
                osfile.clear()
                osfile.append(filename[file].replace(".txt",""))
                #print(s2[i][1])
    #osfile=osfile.replace(".txt","")
    return bignum,text,osfile #差距最短距離，差距句子分數，情緒
'''
各項情緒分數
'''
def sentencescore(text,mood):
    scaredscore=0
    sadscore=0
    melancholyscore=0
    anxietyscore=0
    angryscore=0
    for i in range(len(text)):
        #print("跟",s1,"\n差距最短的是",text[i],"\n距離為",distance,"\n情緒歸類在",mood[i])
        #print("\n==========================")
        if(mood[i]=="害怕"):
            scaredscore+=int(text[i][0])
        elif(mood[i]=="悲傷"):
            sadscore+=int(text[i][0])
        elif(mood[i]=="憂鬱"):
            melancholyscore+=int(text[i][0])
        elif(mood[i]=="焦慮"):
            anxietyscore+=int(text[i][0])
        elif(mood[i]=="生氣"):
            angryscore+=int(text[i][0])
    score=[]
    score.append(scaredscore)
    score.append(sadscore)
    score.append(melancholyscore)
    score.append(anxietyscore)
    score.append(angryscore)
    return score
'''
取出情緒比例
'''
def sumnum(sumscore):
    onenum=0
    for i in range(len(sumscore)):
        onenum+=sumscore[i]
    onenum=onenum-sumscore[2]
    for i in range(len(sumscore)):
        if i!=2 and sumscore[i]!=0:
            sumscore[i]=sumscore[i]/onenum
    return sumscore
def init():
    #手動輸入
# =============================================================================
#     sumscore=[0,0,0,0,0]
#     for i in range(0,6,1):
#         s1=input("請輸入單字或句子:")
#         distance,text,mood=similar_score(s1)
#         score=sentencescore(text,mood)
#         #bar_graph(score)
#         print(i,".",score)
#         for j in range(len(score)):
#             sumscore[j]=sumscore[j]+score[j]
#     print(sumscore)
# =============================================================================
    #讀文字檔
    sumscore=[0,0,0,0,0]
    #brokensent = "./brokensent/" #存放文字檔的路徑
    filename= os.listdir(brokensent) #資料夾下的所有檔案 
    #i=0
    for file in range(len(filename)):
        f=open(brokensent+filename[file],'r',encoding='UTF-8')
        s1 = "".join(f.readlines())
        distance,text,mood=similar_score(s1)
        score=sentencescore(text,mood)
        #i=i+1
        #print(i,".",score)
        f.close()
        for j in range(len(score)):
            sumscore[j]=sumscore[j]+score[j]
    sumscore=sumnum(sumscore)
    #print(sumscore)
    return sumscore
if __name__=='__main__':
    init()
    
    
