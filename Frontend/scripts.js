function getUrl(path) {
    return 'https://magic-database.herokuapp.com/' + path.substr(0, 1).toLowerCase() + path.substr(1, path.length - 1);
}
function checkLocalLog(){
    console.log('Game_Size: ' + localStorage.getItem('gameSize') + ' || Base_Life: ' + localStorage.getItem('baseLife'));
    console.log('Game_Name: ' + localStorage.getItem('gameName') + ' || Game_Pass: ' + localStorage.getItem('gamePass'));
    console.log('Player_Name: ' + localStorage.getItem('playerName') + ' || Player_Email: ' + localStorage.getItem('playerEmail') + ' || Host_Toggle: ' + localStorage.getItem('hostToggle'));
    console.log('Commander: ' + localStorage.getItem('commanderName') + ' || Partner_Name: ' + localStorage.getItem('partnerName'));
}
function getAllPlayers(){ //Gets all players and returns a promise containing the response
    const requestBody={
        method: 'GET',
        headers:{
            "content-type": "application/json; charset=UTF-8",
            gameId: localStorage.getItem('gameName')
        }
    };
    return fetch(getUrl('players'),requestBody)
    .then(response => {return response;});
}
function setHost(host) {
    if(!localStorage.getItem('gameName')) {
        alert('Cannot set host without a valid game');
        return Error('Cannot set host without a valid game');
    }
    if(localStorage.getItem('hostToggle') !== 'true') {
        return Error('You are not the host')
    }
    const requestBody={
        method: 'PUT',
        headers:{
            "content-type": "application/json; charset=UTF-8",
            gameId: localStorage.getItem('gameName'),
            hostEmail: host
        }
    };
    return fetch(getUrl('host'), requestBody)
    .then(response => {
        if(response.status !== 200) {
            response.text().then(error => {alert(error)});
        }
    });
}