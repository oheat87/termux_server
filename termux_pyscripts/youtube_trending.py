from datetime import datetime
import requests
import pymysql

#type classes
string_class=type('s')
int_class=type(4)

#fetch trending youtube videos
get_url='https://www.googleapis.com/youtube/v3/videos?part=id,snippet,contentDetails,statistics&chart=mostPopular&regionCode=KR&key=AIzaSyBqv8P3bHu7HOnoNP9gtnZmAhjPRu_2nUs&maxResults=20'
res=requests.get(get_url)
trending_data=res.json()
#print(trending_data)

#process trending youtube videos data
processed_data=[]
trending_time=datetime.now().strftime('%Y-%m-%d %H:%M:%S')
youtube_url_base='https://www.youtube.com/watch?v='
video_type=str(1)
for item in trending_data["items"]:
	channel_name=item["snippet"]["channelTitle"]
	url=youtube_url_base+item["id"]
	view_count=str(item["statistics"]["viewCount"])
	title=''
	if "localized" in item["snippet"] and "title" in item["snippet"]["localized"]:
		title=item["snippet"]["localized"]["title"]
	else:
		title=item["snippet"]["title"]
	processed_data.append([channel_name,url,view_count,trending_time,title,video_type])
	#print(channel_name,url,view_count,trending_time,title,video_type)
#make query statement
def quote_value(value):
	if type(value)==string_class:
		return "\'"+value+"\'"
	return value
query='insert into trending_videos(channel_name,url,view_count,trending_time,title,video_type) values '
query_args=[]
for pdidx,datum in enumerate(processed_data):
	#print(datum,pdidx)
	query+="("
	for eidx,element in enumerate(datum):
		#if type(element)==int_class:
		#	print('view_count is int class')
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
#exit()
#insert trending youtube videos to maraidb
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
