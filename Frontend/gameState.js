var life = 40; //get this from starting life in db
var poison = 0;
var experience = 0;
var commanderDamage = {};
var displayedStat = 'Life';
var displayedNumber = life;
function set(amount, url) { //url = life, poison, experience
    if(url !== 'Life' && url !== 'Poison' && url !== 'Experience') {
        console.log('bad');
        return;
    }
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
    displayedNumber = amount;
    document.getElementById('lifeTotal').textContent = amount;
    fetch(getUrl(url)+'/'+amount, requestBody)
    .then(res => {
        if(res.status === 200) {
            if(url === 'Life') 
                life = amount;
            else if(url === 'Poison') 
                poison = amount;
            else if(url === 'Experience')
                experience = amount;
        }
        else {
            res.text().then(error => alert(error));
        }
    });
    
}
function refresh(){
    getAllPlayers()
    .then(response => {
        if(response.status !== 200) {
            response.text().then(error => alert(error));
        }
        else {
            return response.json();
        }
    })
    .then(res => {
        console.log(res);
        console.log(getIndexOfMe(res));
    })
}
function initialize() { //Probably should move some of this into startGame, save stats in local storage
    const requestBody={
        method: 'GET',
        headers:{
            "content-type": "application/json; charset=UTF-8",
            gameId: localStorage.getItem('gameName'),
            gamePassword: localStorage.getItem('gamePass'),
            email: localStorage.getItem('playerEmail'),
            password: localStorage.getItem('playerPass')
        }
    };
    fetch(getUrl('Player'), requestBody)
    .then(response => {
        if(response.status == 200) {
            response.json().then(result => {
                life = result.life;
                displayedNumber = life;
                poison = result.poison;
                experience = result.experience;
                commanderDamage = result.commanderDamage;
                console.log(life);
                console.log(experience);
                console.log(poison);
                console.log(commanderDamage); //TODO: this method doesn't get the commander damage, I might need to modify for that or just update it during refresh
                document.getElementById('lifeTotal').textContent = life;
            });
        }
        else {
            response.text().then(error => alert(error));
        }
    }).catch(err => {console.log(err)});
}
function switchTo(stat) {
    if(stat !== displayedStat) {
        document.getElementById(stat.toLowerCase()+'Button').setAttribute('onclick', 'switchTo("'+displayedStat+'")');
        document.getElementById(stat.toLowerCase()+'Button').textContent = displayedStat;
        document.getElementById(stat.toLowerCase()+'Button').id = displayedStat.toLowerCase()+'Button';
        displayedStat = stat;
        document.getElementById('name').textContent = displayedStat;
        if(stat === 'Poison') {
            document.getElementById('lifeTotal').textContent = poison;
            displayedNumber = poison;
        }   
        else if(stat === 'Life') {
            document.getElementById('lifeTotal').textContent = life;
            displayedNumber = life;
        }
        else if(stat === 'Experience') {
            document.getElementById('lifeTotal').textContent = experience;
            displayedNumber = experience;
        }
    }
    else
    console.log(stat);
}
function getIndexOfMe(players) {
    for(let i = 0; i < players.length; i++) {
        if(players[i].email === localStorage.getItem('playerEmail')) {
            return i;
        }
    }
    return -1;
}
initialize();
refresh();
