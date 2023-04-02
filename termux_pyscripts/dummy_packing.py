import pymysql
from datetime import datetime,timedelta;

url_base='https://youtube.com/watch?v='
packing_count=3000
minute_step=1
row_per_minute_step=10

str_class=type('a')
int_class=type(1)

def quote_value(value):
	if type(value)==str_class:
		return "\'"+value+"\'"
	return value
#make dummy dataset
processed_data=[]
minute_diff=0
datetime_now=datetime.now()
dummy_url_id=0
for i in range(packing_count//row_per_minute_step):
	datetime_cur=datetime_now-timedelta(minutes=minute_diff)
	datetime_cur_str=datetime_cur.strftime("%Y-%m-%d %H:%M:%S")
	for j in range(row_per_minute_step):
		url=url_base+str(dummy_url_id)
		processed_data.append([url,datetime_cur_str])
		dummy_url_id+=1
	minute_diff+=minute_step
processed_data.sort(key=lambda a:a[1])

#make query
query='insert into dummy(url,time) values '
for pdidx,datum in enumerate(processed_data):
	query+="("
	for eidx,element in enumerate(datum):
		query+=quote_value(element)
		if eidx<len(datum)-1:
			query+=","
	query+=")"
	if pdidx<len(processed_data)-1:
		query+=","
	else:
		query+=";"

#print(query)

#insert dummy data to maria db
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
