//TODO: Store Partner Commanders locally
const url = 'https://magic-database.herokuapp.com/getAllPlayers';

function createInputField(){
    var root = document.getElementById('root'); //Root located within HTML body
    if(root.childElementCount <1){
        var input = document.createElement('input');
        input.setAttribute('id','partnerName');
        input.appendChild(document.createTextNode('Partner Name'));
        root.appendChild(input); 
    } else{
        root.removeChild(document.getElementById('partnerName'));
    }
}

function createEntranceStorage(){
    localStorage.setItem('playerName', document.getElementById('playerName').value);
    localStorage.setItem('commanderName', document.getElementById('commanderName').value);
    localStorage.setItem('playerEmail',  document.getElementById('playerEmail').value);
    localStorage.setItem('playerPass',  document.getElementById('playerPass').value);
    console.log('Player ' + localStorage.getItem('playerName') + ' locally stored.')
    console.log('Commander ' + localStorage.getItem('commanderName') + ' locally stored.');
}
function checkLocalLog(){
    console.log('Player ' + localStorage.getItem('playerName') + ' locally stored.')
    console.log('Commander ' + localStorage.getItem('commanderName') + ' locally stored.');
}

const otherParam={
    headers:{
       'content-type':'application/json; charset=UTF-8',
       'gameId': 'newGame'
    }
};
function dbCreatePlayer(){
    fetch(url,otherParam)
    .then(response => response.json())
    .then(data=>{
        console.log(data)
    })
}
dbCreatePlayer();