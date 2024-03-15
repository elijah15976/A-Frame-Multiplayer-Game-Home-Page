 
let accountStyle = `#account:hover ~ #account-dropdown{
               display:block;
             }
             #account-dropdown:hover{
               display:block;
               cursor:pointer;
             }`;
let accountStyle2 = `#account:hover{
               cursor:pointer;
             }`;

window.onload = function(){
  if(window.location.pathname == "/"){
    let user = getUser().username;
    console.log(user);
    let styleSheet = document.createElement("style");
    if(user === undefined || user.length <= 0){
      styleSheet.innerText = accountStyle2;
      document.head.appendChild(styleSheet);
      document.getElementById("account").addEventListener('click', function(event){window.location.href = "/login";});
    }
    else{
      document.getElementById("account").innerHTML = user;
      styleSheet.innerText = accountStyle;
      document.head.appendChild(styleSheet);
      document.getElementById("mainButton").innerHTML = "Play Now";
      document.getElementById("mainButton").setAttribute("onclick", "window.location.href='https://48b09dc7-e9b7-4a1c-b3f7-f27f96ed5e29-00-2lap5e65rigp1.kirk.repl.co'");
    }
  }

  galleryImages()
  getLeaderBoard()//👍
}

function getUser(){
  $.ajaxSetup({async: false});
  data = $.getJSON("/get-user").responseJSON;
  console.log(data);
  if(data != "success"){
    return data;
  }
  else if(data == "{}"){
    return "";
  }
}

function logOut(){
  document.cookie = "logOut=1;Max-Age=1;SameSite=Lax;Path=/;Domain=f2166e48-2864-40b5-b38a-00a461eb243a-00-j15vqjkvw7bk.riker.replit.dev";
  $.ajaxSetup({async: false});
  data = $.getJSON("/get-user").responseText;
  if(data == "success"){
    document.cookie = "token=;Max-Age=1;Domain=f2166e48-2864-40b5-b38a-00a461eb243a-00-j15vqjkvw7bk.riker.replit.dev;SameSite=Lax;Path=/";
    alert("You have successfully signed out");
    window.location.href = "/";//👍
  }
  else{
    alert("Something went wrong trying to sign you out\nRefresh the page and try again");
  }
}

function galleryImages(){
    let images = [];//👍
    images.push(`/maze.png`);
    images.push(`/josh.jpg`);
    images.push(`/maze2.png`);
    // images.push(`/maze3.png`);
    images.push(`/lobby.png`);
    images.push(`/lobby2.png`);

  let gallery = new Gallery(images);
  gallery.render("screenshot");

  gallery.play();
}

function getLeaderBoard(){
  $.ajaxSetup({async: false});
  data = $.getJSON("/get-leaderboard").responseJSON;
  $_("#leaderboard").innerHTML = "";

  pacmanLeaders = [...data].sort((b, a) => a.PacmanWins - b.PacmanWins);
  ghostLeaders = [...data].sort((b, a) => a.GhostWins - b.GhostWins);


  let leaderboard = new LeaderBoard(pacmanLeaders, ghostLeaders);
  leaderboard.render("leaderboard");//👍

  setTimeout(getLeaderBoard, 30000);
}
