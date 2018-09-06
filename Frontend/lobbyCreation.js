const getAllUrl = 'https://magic-database.herokuapp.com/players';
const root = document.getElementById('root');

var lSPlayerName = localStorage.getItem('playerName');
var lSCommanderName = localStorage.getItem('commanderName');
var lSCommanderTwo = localStorage.getItem('partnerName');
var gameSize = localStorage.getItem('gameSize');
var baseLife = localStorage.getItem('baseLife');
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
        for(var i = 0; i<data.length; i++){
            console.log(data);
            console.log(JSON.stringify(data));
            dbGAR.push(data[i]);
            console.log('dbGAR size inside function: ' + dbGAR.length);
        }
        for(var i = 0; i<dbGAR.length;i++){
            console.log('hello?');
            console.log(dbGAR[i].name);
            root.appendChild(createNameSlot(dbGAR[i].name));
        }
        if(dbGAR.length<localStorage.getItem('gameSize')){
            for(var i =0; i<localStorage.getItem('gameSize')-dbGAR.length;i++){
                root.appendChild(createEmptySlot());
            }
        }
    })
    .then(res=>{console.log(res)})
    .catch(error=>console.log(error))
    .then(_=>setInterval(playerRefresh(), 1000))
}

// root.appendChild(createPlayerSlot(lSPlayerName,lSCommanderName,lSCommanderTwo));
// createWaitingSlots();
playerRefresh();