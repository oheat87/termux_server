import pymysql

#query
query='delete from dummy where timestampdiff(minute,time,now())>60;'

#delete rows from maria db
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
