from azure.cognitiveservices.language.textanalytics import TextAnalyticsClient
from msrest.authentication import CognitiveServicesCredentials
import os
import requests
from pprint import pprint
import json

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
    file.write("分數:"+msg)
    file.write("\n")
    file.close()

path="./images/3/all/mood.txt"

key_var_name = 'TEXT_ANALYTICS_SUBSCRIPTION_KEY'
if not 'key_var_name' in os.environ:
    raise Exception('Please set/export the environment variable: {}'.format(key_var_name))
subscription_key = os.environ['key_var_name']

endpoint_var_name = 'TEXT_ANALYTICS_ENDPOINT'
if not 'endpoint_var_name' in os.environ:
    raise Exception('Please set/export the environment variable: {}'.format(endpoint_var_name))
endpoint = os.environ['endpoint_var_name']

credentials = CognitiveServicesCredentials(subscription_key)
sentiment_url = endpoint + "/text/analytics/v2.1/sentiment"
text_analytics = TextAnalyticsClient(endpoint=sentiment_url, credentials=credentials)

f=open(path,'r',encoding='UTF-8')

#documents=json.dumps(dict)

ff=str(f.read())
documents = {"documents": [
    {"id": "1", 
    "language": "en",
    "text": ff
    }
    ]
}

headers = {"Ocp-Apim-Subscription-Key": subscription_key}
response = requests.post(sentiment_url, headers=headers, json=documents)
sentiments = response.json()

for i in sentiments['documents']:
    print("分數:","%.2f" %i['score'])
    score="%.2f" %i['score']
    text_cw("d",score)
