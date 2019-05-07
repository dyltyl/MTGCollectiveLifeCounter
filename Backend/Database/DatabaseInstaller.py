import psycopg2
import os


class DatabaseInstaller:
    def __init__(self):
        parts = os.environ['DATABASE_URL'].split(":")
        self.username = parts[1][2:]
        self.password = parts[2][0:parts[2].index("@")]
        self.database = parts[3][parts[3].index("/") + 1:]
        self.path = os.path.realpath(__file__)
        try:
            self.path = self.path[0:self.path.rindex('/')]
        except ValueError:
            self.path = self.path[0:self.path.rindex('\\')]
        print('username: ' + self.username)
        print('password: ' + self.password)
        print('database: ' + self.database)
        print('path: ' + self.path)

        try:
            database = self.connect_to_database()
            database.close()
        except psycopg2.OperationalError as e:
            print(e)
            raise Exception('Cannot connect to database\n'+str(e))

    def connect_to_database(self): #Todo: add null checks/etc
        return psycopg2.connect("dbname="+self.database+" user="+self.username+" password="+self.password)

    def check_table_exists(self, table):
        database = self.connect_to_database()
        cursor = database.cursor()
        cursor.execute("SELECT 1 FROM information_schema.tables WHERE table_name = '"+table+"';")
        result = cursor.fetchone()
        cursor.close()
        database.close()
        if result is None:
            return False
        return True

    def create_table(self, tablename, filename):
        if not self.check_table_exists(tablename):
            database = self.connect_to_database()
            sql_file = open(filename, "r")
            sql = sql_file.read()
            sql_file.close()
            cursor = database.cursor()
            cursor.execute(sql)
            cursor.close()
        else:
            print(tablename + ' needs updating')

    def setup_tables(self):
        self.create_table('players', self.path+'/createPlayers.sql')
        self.create_table('games', self.path+'/createGames.sql')
        self.create_table('commanders', self.path+'/createCommanders.sql')
        self.create_table('commander_damage', self.path+'/createCommanderDamage.sql')
        self.create_table('life', self.path+'/createLife.sql')
        print(self.path+'/createPlayers.sql')


installer = DatabaseInstaller()
installer.setup_tables()
