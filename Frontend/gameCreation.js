
function createLocalGameStorage(){
    localStorage.setItem('gameSize', document.getElementById('gameSize').value);
    localStorage.setItem('baseLife', document.getElementById('baseLife').value);
    localStorage.setItem('gameName', document.getElementById('gameName').value);
    localStorage.setItem('gamePass', document.getElementById('gamePass').value);
    console.log('Game_Size of ' + localStorage.getItem('gameSize') + ' locally.');
    console.log('Base_Life_Total of ' + localStorage.getItem('baseLife') + ' stored locally');
}

function createGame(){
    //TODO:Actual error messages
    if(document.getElementById('gameSize').value > 8 || document.getElementById('gameSize').value < 2) {
        alert('There must be between 2 and 8 players');
        return;
    }
    if(document.getElementById('baseLife').value < 1) {
        alert('Life must start at a value above 0');
        return;
    }
    if(!document.getElementById('gameName').value) {
        alert('Please name the game');
        return;
    }
    createLocalGameStorage();
    //Create Game
    const Data={
        gameId : document.getElementById('gameName').value,
        gamePassword : document.getElementById('gamePass').value,
        startingLife : document.getElementById('baseLife').value,
        maxSize: document.getElementById('gameSize').value,
        host: 'TODO' //TODO
    }
    console.log(JSON.stringify(Data));
    const otherParam={
        method: "POST",
        body: JSON.stringify(Data),
        headers:{
            "content-type": "application/json; charset=UTF-8"
        }
    };

    fetch(getUrl('game'), otherParam)
    .then(res=>{
        console.log(res); 
        if(res.status == 200) { //Game created successfully
            window.location.href= 'playerCreation.html';
        }
        else {
            res.text().then(error => alert(error));
        }
    })
    .catch(error=>alert(error)) //Not sure if this is ever called
}

function handleErrors(response) { //What this?
    if (!response.ok) {
        throw Error(response.statusText);
    }
    return response;
}