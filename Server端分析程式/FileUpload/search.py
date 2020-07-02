import jieba
import os
#import matplotlib.pyplot as plt #有需要繪製長條圖再開

#切換自訂詞庫
jieba.set_dictionary('dict1.txt')
#jieba.add_word('增加') 手動增加詞庫





#===資料夾全域變數================
#在cutword()跟searchkey()內
#把整篇文章斷詞後的結果 EX.我/很/高興
outtxt='./斷詞輸出/斷詞輸出.txt'

#================================
#在init()內
#語音轉文字，沒有做過任何處理的版本
recording="./images/2/all/mood.txt"

#================================
#在similar_score()內
#詞彙語料庫情緒分類(同義詞跟分數的部分)
similar = "./similar/"

#================================
#在 readfile()內，現在沒用到
#speaktxt = "./speaktxt/"
#================================






'''
文章斷詞
'''
def cutword(filename):
    cutin=filename #放進去斷詞的文章
    #outtxt='./斷詞輸出/斷詞輸出.txt'

    f=open(cutin,'r',encoding='UTF-8')
    out=open(outtxt,'w',encoding='UTF-8')

    w=f.read()

    seg_list = jieba.cut(w)  #精準模式
    cuttext="/".join(seg_list)
    out.write(cuttext)
    #print(cuttext)
    f.close()
    out.close()
'''
跟詞庫比較，並返回加總分數
'''
def searchkey(filesimilar):
    #outtxt='./斷詞輸出/斷詞輸出.txt'
    keytxt=filesimilar

    fl=open(outtxt,'r+',encoding='UTF-8')
    f=open(keytxt,'r')
    w=fl.read()
    lines = f.readlines()
    keyword = []  ## 空列表, 将第i行数据存入list中
    for i in range(0,lines.__len__(),1):
        keyword.append([])
        for word in lines[i].split('，'): #切割逗號
            if word != '':
                keyword[i].append(word)
                #print(word)
# =============================================================================
#                 global K
#                 k=0
#                 for x in range(0,word.__len__(),1):
#                     asc=ord(word[x])
#                     if asc>127:
#                         k+=asc
#                 #print(hash(k),id(k))
#                 print(hash(50))
# =============================================================================
    f.close()
# =============================================================================
#     for i in range(0,len(keyword),1):
#         for j in range(1,len(keyword[i]),1):
#             print(keyword[i][j])
# =============================================================================
    cutarray=[]
    for word in w.split('/'):
        if word != '':
            cutarray.append(word) 
            #print(word)

    #cutarray.sort()
    setcut=set(cutarray)
    #print("想死" in setcut)

    sum=0
    allkey=[]
    
# =============================================================================
#     a=0
#     for i in range(0,len(keyword),1):
#         for j in range(1,len(keyword[i]),1):
#             while True:
#             #從a的位置開始找keyword[i][j] 
#                 idkey=w.find(keyword[i][j],a)
#                 if idkey==-1:  #-1等於沒找到退出迴圈
#                     break
#                 else:
#                     a=idkey+1
#                     sum+=int(keyword[i][0])
#                     allkey.append(keyword[i][j])
# =============================================================================
                    
    for i in range(0,len(keyword),1):
        for j in range(1,len(keyword[i]),1):
                if keyword[i][j] in setcut:
                    sum+=int(keyword[i][0])*cutarray.count(keyword[i][j])
                    allkey.append(keyword[i][j])
# =============================================================================
#     if allkey==[]:
#         print("沒有關鍵字")
# =============================================================================
# =============================================================================
#     else:
#         allkey.sort()
#         #set() 函数创建一个无序不重复元素集
#         #就是把同樣關鍵字砍到只剩下1個
#         allkeyset=set(allkey)
#         for item in allkeyset: #顯示關鍵字幾次
#             print (item,":",cutarray.count(item),"次")
#         print("總共",sum,"分")
# =============================================================================
    fl.close()
    #print(sum)
    return sum
'''
選擇哪個文字檔
#現沒用到#
'''
'''def readfile():
    #speaktxt = "./speaktxt/" #存放文字檔的路徑
    filename= os.listdir(speaktxt) #資料夾下的所有檔案 
    num=0
    for file in range(len(speaktxt)):
        print(num+1, end=".")
        num+=1
        print(filename[file])
    name = input('請輸入檔名：')
    return speaktxt+filename[int(name)-1]
'''
    

def similar_score():
    #similar = "./similar/" #存放文字檔的路徑
    filename= os.listdir(similar) #資料夾下的所有檔案 
    allsimilar=[]
    for file in range(len(filename)):
        #print(filepath+filename[file])
        score=searchkey(similar+filename[file])
        allsimilar.append(score)
    #for i in range(0,score.__len__(),1):
    return allsimilar
    
def init():
    #recording="./speaktxt/recording.txt" #選擇錄音轉換成文字的檔案
    #filesimilar=keysimilar()
    cutword(recording) #斷詞
    score=similar_score()
    if(score[0]==score[1]==score[3]==score[4]==0):
        score[0]=1
        score[1]=1
        score[3]=1
        score[4]=1
    #bar_graph(score)
    #print(score)
    return score
    
    
if __name__ == '__main__':
    init()