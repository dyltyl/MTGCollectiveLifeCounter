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
/**
 * 
 * @param {function} addFunction Function to add a given player to the array
 * @param {function} updateFunction
 * @param {function} removeFunction Function to remove a given player from the array
 * @param {Player[]} players 
 * @param {Player[]} data 
 */
function compareArrays(addFunction, updateFunction, removeFunction, players, data) {
    //Comparing the 2 arrays
    let equal = players.length === data.length;
    for(let i = 0; i < data.length && equal; i++) {
        if(players[i].email !== data[i].email) {
            equal = false;
            break;
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
                addFunction(data[i]);
                players.push(data[i]);
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
            //player in players but not in data
            if(!found) {
                removeFunction(players[i]);
                players.splice(i, 1);
                i--;
            }
        }
    }
    //Updating stats
    for(let i = 0; i < players.length && updateFunction !== null; i++) {
        if(players[i].email === data[i].email) {
            if(players[i].life !== data[i].life) {
                updateFunction(data[i]);
            }
            else if(players[i].poison !== data[i].poison)
                updateFunction(data[i]);
            else if(players[i].experience !== data[i].experience)
                updateFunction(data[i]);
            else if(players[i].commanderDamage !== data[i].commanderDamage)
                updateFunction(data[i]);
            players[i] = data[i];
        }
    }
    return players;
}