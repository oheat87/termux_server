import pymysql

#query
query='delete from trending_videos where timestampdiff(minute,trending_time,now())>60;'

#delete outdated trending videos
host='localhost'
user='root'
password=''
database='termux_db0'
conn=pymysql.connect(host=host,user=user,password=password,database=database)
try:
	with conn.cursor() as cur:
		cur.execute(query)
		conn.commit()
finally:
	conn.close()
