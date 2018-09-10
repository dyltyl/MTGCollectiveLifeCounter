var urlEnum = Object.freeze({
    'life':'https://magic-database.herokuapp.com/life',
    'poison':'https://magic-database.herokuapp.com/poison',
    'experience':'https://magic-database.herokuapp.com/experience',
    'commander':'https://magic-database.herokuapp.com/commander'
});
const getAllUrl = 'https://magic-database.herokuapp.com/players';
var life = 40; //get this from starting life in db
var poison = 0;
var experience = 0;
function set(amount, url) { //url = life, poison, experience, commander
    if(!url in urlEnum) {
        console.log('bad');
        return;
    }
    let path = urlEnum[url];
    console.log(path+'/'+amount);
    const requestBody={
        method: 'PUT',
        body: '',
        headers:{
            "content-type": "application/json; charset=UTF-8",
            gameId: localStorage.getItem('gameName'),
            gamePassword: localStorage.getItem('gamePass'),
            email: localStorage.getItem('playerEmail'),
            password: localStorage.getItem('playerPass')
        }
    };
    life = amount;
    document.getElementById('lifeTotal').textContent = amount;
    fetch(path+'/'+amount, requestBody)
    .then(res => {
        if(res.status === 200) {
            if(url === 'life') {
                console.log(life)
            }
        }
        else {
            res.text().then(error => alert(error));
        }
    })
}
function getAllPlayers(){
    const requestBody={
        method: 'GET',
        headers:{
            "content-type": "application/json; charset=UTF-8",
            gameId: localStorage.getItem('gameName')
        }
    };
    fetch(getAllUrl,requestBody)
    .then(response => {return response.json()})
    .then(res => {
        console.log(res);
        console.log(getIndexOfMe(res));
    })
}
function getIndexOfMe(players) {
    for(let i = 0; i < players.length; i++) {
        if(players[i].email === localStorage.getItem('playerEmail')) {
            return i;
        }
    }
    return -1;
}
getAllPlayers();
