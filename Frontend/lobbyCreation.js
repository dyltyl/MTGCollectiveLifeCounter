const getAllUrl = 'https://magic-database.herokuapp.com/players';
const root = document.getElementById('root');

var lSPlayerName = localStorage.getItem('playerName');
var lSCommanderName = localStorage.getItem('commanderName');
var lSCommanderTwo = localStorage.getItem('partnerName');
var gameSize = localStorage.getItem('gameSize');
var baseLife = localStorage.getItem('baseLife');
var players = [];
// var root = document.getElementById('root');

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

function createWaitingSlots(){
    for(i=0; i<gameSize-1; i++){
        root.appendChild(createEmptySlot());
    }
}

/**
 * fetches getAllPlayers() and for each inserts them into the empty waiting lobby slots
 */
function playerRefresh(){
    var dbGAR = new Array();
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
        console.log(data);
        console.log(players);
        //Determine if the arrays are equal
        var equal = true;
        if(data.length !== players.length)
            equal = false;
        for(let i = 0; i < data.length && equal; i++) {
            if(data[i].email !== players[i].email) {
                equal = false;
            }
        }   
        //If not equal
        if(!equal) {
            players = data;

            for(let i = 0; i < data.length; i++) {
                var found = false;
                for(let j = 0; j < players.length; j++) {
                    if(data[i].email === players[j].email) {
                        found = true;
                        break;
                    }
                }
                //player in data but not players
                if(!found) {
                    //TODO: fill player slot
                }
            }

            for(let i = 0; i < players.length; i++) {
                var found = false;
                for(let j = 0; j < data.length; j++) {
                    if(data[i].email === players[j].email) {
                        found = true;
                        break;
                    }
                }
                //player in players but not data
                if(!found) {
                    //TODO: remove player slot
                }
            }
            
        }
    })
    //.then(res=>{console.log(res)})
    .catch(error=>console.log(error))
    //.then(_=>setInterval(playerRefresh(), 100000))
}

// root.appendChild(createPlayerSlot(lSPlayerName,lSCommanderName,lSCommanderTwo));
// createWaitingSlots();
playerRefresh();