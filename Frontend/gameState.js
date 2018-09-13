let life = 40; //get this from starting life in db
let poison = 0;
let experience = 0;
let commanderDamage = {};
let displayedStat = 'Life';
let displayedNumber = life;
let players = [];
let myIndex = -1;
/**
 * Sets the displayed number and the db for the specified stat
 * @param {number} amount The amount to set 
 * @param {string} url Either Life, Poison, or Experience depending on which you're setting
 */
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
/**
 * Refreshes the game from the database, updating lifetotals
 */
function gameRefresh(){
    getAllPlayers()
    .then(response => {
        if(response.status !== 200) {
            response.text().then(error => alert(error));
            return Error(response.text().then(error => {return error;}));
        }
        else {
            return response.json();
        }
    })
    .then(res => {
        players = compareArrays(addPlayer, updatePlayer, removePlayer, players, res);
        myIndex = getIndexOfMe();
        setTimeout(gameRefresh, 500);
    });
}
/**
 * Adds the player to the game and displays them
 * @param {Player} player The player being added
 */
function addPlayer(player) {
    if(player.email !== localStorage.getItem('playerEmail')) {
        let root = document.getElementById('enemyStats');
        let playerSlot = document.createElement('div');
        
        let nameDisplay = document.createElement('h3');
        nameDisplay.textContent = player.name;
        
        let lifeDisplay = document.createElement('h3');
        lifeDisplay.setAttribute('id', player.email+'Life');
        lifeDisplay.textContent = player.life;

        playerSlot.appendChild(nameDisplay);
        playerSlot.appendChild(lifeDisplay);
        root.appendChild(playerSlot);
    }
}
/**
 * Updates the player's life total
 * @param {Player} updatedPlayer The updated Player from the database
 */
function updatePlayer(updatedPlayer) {
    if(updatedPlayer.email !== localStorage.getItem('playerEmail')) {
        document.getElementById(updatedPlayer.email+'Life').textContent = updatedPlayer.life;
    }
}
/**
 * Removes the player from the game
 * @param {Player} player The player being removed
 */
function removePlayer(player) {
//TODO
}
/**
 * Initializes the page, setting the stats based off of the database
 */
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
                document.getElementById('lifeTotal').textContent = life;
            });
        }
        else {
            response.text().then(error => alert(error));
        }
    }).catch(err => {console.log(err)});
}
/**
 * Changes the currently displayed stat with the one passed in
 * @param {String} stat The stat to display, either Poison, Life, or Experience
 */
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
}
/**
 * Finds the index in the players array of the current player
 */
function getIndexOfMe() {
    for(let i = 0; i < players.length; i++) {
        if(players[i].email === localStorage.getItem('playerEmail')) {
            return i;
        }
    }
    return -1;
}
initialize();
gameRefresh();
