let num = 0; //0 = log in, 1 = register
function changeRegister(){
  document.getElementById("current_status").innerHTML = "Register";
  document.getElementById("current_status_button").innerHTML = "Register";
  document.getElementById("switcher").innerHTML = `Already have an account? <a id="switcher_button" onclick="changeLogIn()">Log In</a>`;
  num = 1;
}//👍

function changeLogIn(){
  document.getElementById("current_status").innerHTML = "Log In";
  document.getElementById("current_status_button").innerHTML = "Log In";
  document.getElementById("switcher").innerHTML = `Don't have an account? <a id="switcher_button" onclick="changeRegister()">Create an Account</a>`;
  num = 0;
}

function logIn(){
  let username = document.getElementById("username_input").value.replace(/\s/g, '');
  if(username == ""){
    document.getElementById("error_place").innerHTML = "Please enter an Email";
    document.getElementById("error_place").style = `background-color:#f90006;padding:20px;`;
    return;
  }
  
  let password = document.getElementById("password_input").value;
  if(password == ""){
    document.getElementById("error_place").innerHTML = "Please enter a Password";//👍
    document.getElementById("error_place").style = `background-color:#f90006;padding:20px;`;
    return;
  }

  if(document.getElementById("error_place").innerHTML != ""){
    document.getElementById("error_place").innerHTML = "";
    document.getElementById("error_place").style = `background-color:#ffffff;padding:0px;`;
  }//👍

  $.ajaxSetup({async: false});
  $.post(("/log-in"),
    {"username": username,
     "password": password,
     "num": num
    },
    function(data, status){
      if(status == "success"){
        if(data == "Bad Parameters"){
         alert("Something went wrong \nDouble check everything is filled out");
        }
        else if(data == "Internal Error"){
          alert("We ran into an Internal Error. Refresh the page and Try Again");
        }//👍
        else if(data == "Password Invalid"){
          alert("The Password provided is incorrect. Please Try Again");
        }
        else if(data == "User Not Found"){
          alert("The Username provided is not registered. Check for typos or Create an Account");
        }
        else if(data == "User Already Registered"){
          alert("The Username provided is not available");
        }
        else if(data == "success"){
          alert("You have logged in. Welcome back");
          window.location.href = "/";
        }
        else if(data == "User created"){
          alert("You have successfully created an account");
          window.location.href = "/";
        }
      }
      else{//👍
       alert("Something went wrong \nPlease try again later...");
      }
    }
  );
  
  console.log(`${username} ${password} ${num}`);
}