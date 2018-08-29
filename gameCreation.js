const url = 'https://magic-database.herokuapp.com/createGame';

function createLocalGameStorage(){
    localStorage.setItem('gameSize', document.getElementById('gameSize').value);
    localStorage.setItem('baseLife', document.getElementById('baseLife').value);
    console.log('Game_Size of ' + localStorage.getItem('gameSize') + ' locally.');
    console.log('Base_Life_Total of ' + localStorage.getItem('baseLife') + ' stored locally');
}
function checkLocalLog(){
    console.log('Game_Size of ' + localStorage.getItem('gameSize') + ' locally.');
    console.log('Base_Life_Total of ' + localStorage.getItem('baseLife') + ' stored locally');
}


const Data={
    gameId : "te262stID",
    gamePassword : "Hello",
    startingLife : 40
}
console.log(JSON.stringify(Data));
const otherParam={
    headers:{
        "content-type": "application/json; charset=UTF-8"
    },
    body: (Data),
    method: "POST"
};

/*
function dbCreateGame(){
    console.log('dbCreateGame() called');
    fetch(url, otherParam)
    // .then(data=>{return data.json()})
    .then(res=>{console.log(res)})
    .catch(error=>console.log(error))
}*/

function dbCreateGame(){
    fetch(url, {
        method: 'post',
        body: JSON.stringify(Data)
    }).then(function(response){
        return response.json();
    })
}

dbCreateGame();