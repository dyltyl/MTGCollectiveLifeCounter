import psycopg2
import os


def connect_to_database(): #Todo: add null checks/etc
    parts = os.environ['DATABASE_URL'].split(":")
    username = parts[1][2:]
    password = parts[2][0:parts[2].index("@")]
    database = parts[3][parts[3].index("/")+1:]

    return psycopg2.connect("dbname="+database+" user="+username+" password="+password)


def check_table_exists(table):
    database = connect_to_database()
    cursor = database.cursor()
    cursor.execute("SELECT 1 FROM information_schema.tables WHERE table_name = '"+table+"';")
    result = cursor.fetchone()[0] == 1
    cursor.close()
    database.close()
    return result


def create_table(tablename, filename):
    if not check_table_exists(tablename):
        database = connect_to_database()
        sql_file = open(filename, "r")
        sql = sql_file.read()
        sql_file.close()
        cursor = database.cursor()
        cursor.execute(sql)
        cursor.close()
    else:
        print(tablename + ' needs updating')


def setup_tables():
    create_table('players', 'createPlayers.sql')
    create_table('games', 'createGames.sql')
    create_table('commanders', 'createCommanders.sql')
    create_table('commander_damage', 'createCommanderDamage.sql')
    create_table('life', 'createLife.sql')


setup_tables()
