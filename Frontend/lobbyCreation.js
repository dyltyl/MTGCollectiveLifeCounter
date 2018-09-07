const getAllUrl = 'https://magic-database.herokuapp.com/players';
const root = document.getElementById('root');

var lSPlayerName = localStorage.getItem('playerName');
var lSCommanderName = localStorage.getItem('commanderName');
var lSCommanderTwo = localStorage.getItem('partnerName');
var gameSize = localStorage.getItem('gameSize');
var baseLife = localStorage.getItem('baseLife');
var players = [];

function checkLog(){
    console.log('Creator Name: ' + lSPlayerName);
    console.log('Creator Commander: ' + lSCommanderName);
    console.log('GameSize: ' + gameSize);
    console.log('BaseLife: ' + baseLife );
}
/**
 * Creates and emptySlot element within JS, intended to be replaced by a 
 * createPlayerSlot element
 */
function createEmptySlot(){ 
    var root = document.getElementById('root');
    var emptySlot = document.createElement('div');
    emptySlot.setAttribute('class','playerSlot');
    var txtNode = document.createTextNode('...Waiting for player...');
    emptySlot.appendChild(txtNode);

    //root.appendChild(emptySlot);
    return emptySlot;
}

function createPlayerSlot(name, commanderOne, commanderTwo){
    var root = document.getElementById('root');
    var playerSlot = document.createElement('div');
    playerSlot.setAttribute('class','playerSlot');

    if (lSCommanderTwo === undefined){
        var playerText = document.createTextNode(name + ' || ' + commanderOne);
    }else{
       var playerText = document.createTextNode(name + ' || ' + commanderOne + " || "  + commanderTwo);
    }
    playerSlot.appendChild(playerText);
    // playerSlot.appendChild(c1);
    // root.appendChild(playerSlot);
    return playerSlot;
}

function createNameSlot(name){
    var nameSlot = document.createElement('div');
    nameSlot.setAttribute('class','playerSlot');
    var nameText = document.createTextNode(name);
    nameSlot.appendChild(nameText);
    return nameSlot;
}

function createWaitingSlots(){ //TODO: Should load in player slots already in game before adding waiting slots
    for(i=0; i<gameSize; i++){
        root.appendChild(createEmptySlot());
    }
}

/**
 * fetches getAllPlayers() and for each inserts them into the empty waiting lobby slots
 */
function playerRefresh(){
    const requestBody={
        method: 'GET',
        headers:{
            "content-type": "application/json; charset=UTF-8",
            gameId: localStorage.getItem('gameName')
        }
    };
    fetch(getAllUrl,requestBody)
    .then(function(response){ return response.json();})
    .then(function(data){
        //Determine if the arrays are equal
        let equal = true;
        if(data.length !== players.length)
            equal = false;
        for(let i = 0; i < data.length && equal; i++) {
            if(data[i].email !== players[i].email) {
                equal = false;
            }
        }   
        //If not equal
        if(!equal) {
            for(let i = 0; i < data.length; i++) {
                let found = false;
                for(let j = 0; j < players.length; j++) {
                    if(data[i].email === players[j].email) {
                        found = true;
                        break;
                    }
                }
                //player in data but not players
                if(!found) {
                    let index = findEmptySlot();
                    if(index === -1) {
                        console.log('no more room :('); //TODO: Actual error message
                    }
                    else {
                        document.getElementById('root').children[index].textContent = data[i].email;
                        players.push(data[i]);
                    }

                }
            }

            for(let i = 0; i < players.length; i++) {
                let found = false;
                for(let j = 0; j < data.length; j++) {
                    if(data[j].email === players[i].email) {
                        found = true;
                        break;
                    }
                }
                //player in players but not data
                if(!found) {
                    let index = findSlot(players[i]);
                    if(index === -1) {
                        console.log('uhhhh.....that\'s not supposed to happen');
                    }
                    else {
                        document.getElementById('root').children[index].textContent = '...Waiting for player...';
                        players.splice(i, 1);
                        i--;
                    }
                }
            }
            
        }
    })
    .catch(error=>console.log(error))
    .then(_=>setTimeout(playerRefresh, 5000))
}
function findEmptySlot() {
    var slots = document.getElementById('root').children;
    for(let i = 0; i < slots.length; i++) {
        if(slots[i].textContent === '...Waiting for player...') {
            return i;
        }
    }
    return -1;
}
function findSlot(player) {
    var slots = document.getElementById('root').children;
    for(let i = 0; i < slots.length; i++) {
        if(slots[i].textContent === player.email) {
            return i;
        }
    }
    return -1;
}

// root.appendChild(createPlayerSlot(lSPlayerName,lSCommanderName,lSCommanderTwo));
createWaitingSlots();
playerRefresh();