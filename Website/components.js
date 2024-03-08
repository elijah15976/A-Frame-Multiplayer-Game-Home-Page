let $_ = (selector)=> document.querySelector(selector);

//Modal
//Concept Source 👍 - https://www.w3schools.com/howto/howto_css_modals.asp
class Modal{
  constructor(text,content){
    this.text = text;
    this.content = content;
  }
  render(container){//👍
    //Creates a div that acts like a button
    this.button = document.createElement("div");
    this.button.innerHTML = this.text; // Text display from div

    // create div which will be our modal
    this.modal = document.createElement("div");
    this.modal.setAttribute("class","modal");

    // create a event listener that when click on
    // will make the model appear
    this.button.addEventListener("click",()=>{
      this.modal.style.display = "block";
    })

    //create a container for the content of the model
    this.modal_content = document.createElement("div");
    this.modal_content.setAttribute("class","modal-content");
    this.modal_content.innerHTML ="Content";

    //create a container for the header of the model
    this.modal_header = document.createElement("div");
    this.modal_header.setAttribute("class","modal-header");
    this.modal_content.innerHTML ="Header";

    this.modal_body = document.createElement("div");
    this.modal_body.setAttribute("class","modal-body");
    console.log(typeof(this.content) == "object")
    if(typeof(this.content) == "object"){
      this.modal_body.append(this.content);
    }else{
      this.modal_body.innerHTML = this.content;
    }

    this.closeButton = document.createElement("span");
    this.closeButton.setAttribute("class","close");
    this.closeButton.innerHTML = "&times";
    this.closeButton.addEventListener("click",()=>{
      this.modal.style.display = "none";
    })

    this.modal_header.append(this.closeButton);
    this.modal_content.append(this.modal_header);
    this.modal_content.append(this.modal_body);
    this.modal.append(this.modal_content);

    $_(`#${container}`).append(this.button)
    $_(`#${container}`).append(this.modal)
  }
}

//Gallery
//Concept Source - Custom made
class Gallery{
  constructor(images){
    this.index = 0;
    this.images = images;
  }
  render(container){
    this.obj = document.createElement("img");
    this.obj.src = this.images[0];
    this.obj.addEventListener("click",()=>{
      this.next();
    })
    if(container){
      $_(`#${container}`).append(this.obj)
    }else{
      return this.obj;
    }

  }
  next(){
    this.index++ ;
    this.obj.src = this.images[this.index % this.images.length];
  }
  play(){
    this.next();
    setTimeout(this.play.bind(this),2500);
    //https://coursesweb.net/javascript/settimeout-this-class-function-javascript_t#:~:text=setTimeout()%20can%20be%20used,with%20the%20special%20word%20this%20.
  }
}
//👍

//https://www.w3schools.com/howto/howto_css_flip_card.asp
class FlipCard{
  constructor(front,back){
    this.front = front;
    this.back = back;
  }
  render(container){
    this.obj = document.createElement("div");
    this.obj.setAttribute("class","flip-card");
    this.obj.addEventListener("click",()=>{
      console.log(this.obj);
      this.obj.classList.toggle("flip");
    })
    let build = "";
    build += `<div class="flip-front">${this.front}</div>`
    build += `<div class="flip-back">${this.back}</div>`

    this.obj.innerHTML = build
    $_(`#${container}`).append(this.obj)
  }
}


//https://www.w3schools.com/howto/howto_js_collapsible.asp
class Collapsible{
  constructor(text,content){
    this.text = text;
    this.content = content;
  }
  render(container){
    this.button = document.createElement("button");
    this.button.setAttribute("class","collapsible");
    this.button.innerHTML = this.text;
    //👍
    this.div = document.createElement("div");
    this.div.setAttribute("class","content");
    this.div.innerHTML = this.content;

    this.button.addEventListener("click",function(){
      this.classList.toggle("active");
      let content = this.nextElementSibling;
      if (content.style.display === "block") {
        content.style.display = "none";
      } else {
        content.style.display = "block";
      }
    })

    $_(`#${container}`).append(this.button)
    $_(`#${container}`).append(this.div)
  }
}

class LeaderBoard{
  constructor(dataLeft, dataRight){
    this.dataLeft = dataLeft;
    this.dataRight = dataRight;
  }

  render(container){
    this.leaderboardDiv = document.createElement("div");
    this.leaderboardDiv.classList.add("leaderboard");

    this.pacmanDiv = document.createElement("div");
    this.pacmanDiv.classList.add("thumbs");
    this.pacmanDiv.id = "pacman-leaders";
    this.pacmanDiv.innerHTML = "<h1>Pacman Wins</h1>";

    this.ghostDiv = document.createElement("div");
    this.ghostDiv.classList.add("thumbs");
    this.ghostDiv.id = "ghost-leaders";//👍
    this.ghostDiv.innerHTML = "<h1>Ghost Wins</h1>";

    for (let i = 0; i < this.dataLeft.length; i++) {
      let item = document.createElement("div");
      item.classList.add("array");
      if(this.dataLeft[i].PacmanWins != "0"){
        item.innerHTML = this.dataLeft[i].Username + " - " + this.dataLeft[i].PacmanWins;
      }
      else{
        item.innerHTML = this.dataLeft[i].Username + " - O";
      }
      this.pacmanDiv.append(item);
    }

    for (let i = 0; i < this.dataRight.length; i++) {
      let item = document.createElement("div");
      item.classList.add("array");
      if(this.dataRight[i].GhostWins != "0"){
        item.innerHTML = this.dataRight[i].Username + " - " + this.dataRight[i].GhostWins;
      }
      else{
        item.innerHTML = this.dataLeft[i].Username + " - O";
      }
      this.ghostDiv.append(item);
    }

    this.leaderboardDiv.append(this.pacmanDiv);
    this.leaderboardDiv.append(this.ghostDiv);
    //👍
    $_(`#${container}`).append(this.leaderboardDiv);
  }
}
