const url = 'https://magic-database.herokuapp.com/game';

function createLocalGameStorage(){
    localStorage.setItem('gameSize', document.getElementById('gameSize').value);
    localStorage.setItem('baseLife', document.getElementById('baseLife').value);
    localStorage.setItem('gameName', document.getElementById('gameName').value);
    localStorage.setItem('gamePass', document.getElementById('gamePass').value);
    console.log('Game_Size of ' + localStorage.getItem('gameSize') + ' locally.');
    console.log('Base_Life_Total of ' + localStorage.getItem('baseLife') + ' stored locally');
}
function checkLocalLog(){
    console.log('Game_Size of ' + localStorage.getItem('gameSize') + ' stored locally.');
    console.log('Base_Life_Total of ' + localStorage.getItem('baseLife') + ' stored locally');
    console.log('Game_Name of ' + localStorage.getItem('gameName') + ' stored locally.');
    console.log('Game_Pass of ' + localStorage.getItem('gamePass') + ' stored locally');
}

function createGame(){
    //TODO:Actual error messages
    if(document.getElementById('gameSize').value > 8 || document.getElementById('gameSize').value < 2) {
        console.log('There must be between 2 and 8 players');
        return;
    }
    if(document.getElementById('baseLife').value < 1) {
        console.log('Life must start at a value above 0');
        return;
    }
    if(!document.getElementById('gameName').value) {
        console.log('Please name the game');
        return;
    }
    createLocalGameStorage();
    //Create Game
    const Data={
        gameId : document.getElementById('gameName').value,
        gamePassword : document.getElementById('gamePass').value,
        startingLife : document.getElementById('baseLife').value
    }
    console.log(JSON.stringify(Data));
    const otherParam={
        method: "POST",
        body: JSON.stringify(Data),
        headers:{
            "content-type": "application/json; charset=UTF-8"
        }
    };

    fetch(url, otherParam)
    .then(res=>{
        console.log(res); 
        if(res.status == 200) { //Game created successfully
            window.location.href= 'playerCreation.html';
        }
        else {
            res.text().then(error => console.log(error));
        }
    })
    .catch(error=>console.log(error)) //Not sure if this is ever called
}

function handleErrors(response) { //What this?
    if (!response.ok) {
        throw Error(response.statusText);
    }
    return response;
}