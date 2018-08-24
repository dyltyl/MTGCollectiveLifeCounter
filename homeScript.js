
function addInput(){
    var root = document.getElementById('root'); //Root located within HTML body
    var input = document.createElement('input');
    var textNode = document.createTextNode('Game_ID ');

    var searchButton = document.createElement('button');
    
    root.appendChild(textNode);
    root.appendChild(input);
}