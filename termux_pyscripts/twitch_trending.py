from datetime import datetime
import pymysql
import os
from dotenv import load_dotenv
import command
import json

#get environmental variables
load_dotenv()
max_retrial=int(os.environ.get("max_retrial"))
#print("max retrial:",max_retrial)

#type classes
string_class=type('s')
int_class=type(4)

#fetch trending twitch streams
trending_data=''
for _ in range(max_retrial):
	res=command.run(["twitch","api","get","-q","language=ko","/streams"])
	data_string=res.output.decode("utf-8")
	data_string=data_string[data_string.find("{"):]
	data=json.loads(data_string)
	
	if 'data' not in data and 'status' in data and data['status']!=200:
		command.run(["twitch","token"])
		continue
	trending_data=data
	break
if trending_data=='':
	print('cannot fetch trending twitch streams')
	exit(1)

#print(trending_data)
#exit()

#process trending twitch streams data
processed_data=[]
trending_time=datetime.now().strftime('%Y-%m-%d %H:%M:%S')
twitch_url_base='https://www.twitch.tv/'
video_type=str(2)
for item in trending_data["data"]:
	channel_name=item["user_name"]
	url=twitch_url_base+item["user_login"]
	viewer_count=str(item["viewer_count"])
	title=item["title"]
	processed_data.append([channel_name,url,viewer_count,trending_time,title,video_type])
	#print(channel_name,url,viewer_count,trending_time,title,video_type)
#exit()

#make query statement
def quote_value(value):
	if type(value)==string_class:
		return "\'"+value+"\'"
	return value
query='insert into trending_videos(channel_name,url,viewer_count,trending_time,title,video_type) values '
query_args=[]
for pdidx,datum in enumerate(processed_data):
	#print(datum,pdidx)
	query+="("
	for eidx,element in enumerate(datum):
		#query+=quote_value(element)
		query+="%s"
		if eidx<len(datum)-1:
			query+=","
	query+=")"
	if pdidx<len(processed_data)-1:
		query+=","
	else:
		query+=";"
	query_args+=datum
#print(query)

#insert trending twitch streams to maraidb
host='localhost'
user='root'
password=''
database='termux_db0'
conn=pymysql.connect(host=host,user=user,password=password,database=database)
try:
	with conn.cursor() as cur:
		cur.execute(query,query_args)
		conn.commit()
finally:
	conn.close()
